package hu.midicontroller.communication;

import hu.midicontroller.Constants;
import hu.midicontroller.protocol.LeapController.FingersPosition;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class TcpServer implements Runnable {

	private final LinkedBlockingQueue<FingerData> queue;
	private final ServerSocket serverSocket;
	private Socket socket;
	private final static Logger logger = LogManager.getLogManager().getLogger(
			TcpServer.class.getName());

	public TcpServer(LinkedBlockingQueue<FingerData> queue,
			ServerSocket serverSocket) {
		this.queue = queue;
		this.serverSocket = serverSocket;
	}

	@Override
	public void run() {
		try {
			InetSocketAddress address = new InetSocketAddress(35678);
			serverSocket.bind(address);
			while (true) {
				logger.info("Server started");
				socket = serverSocket.accept();
				handleConnection();
			}
		} catch (IOException e) {
			logger.info("Disconnected: " + e.getMessage());
		}
	}

	private void handleConnection() throws IOException {
		try {
			while (true) {
				FingerData fingerData = queue.take();
				FingersPosition message = createMessage(fingerData);
				OutputStream outputStrem = socket.getOutputStream();
				logger.finest("Sending message: " + message);
				message.writeTo(outputStrem);
			}
		} catch (InterruptedException e) {
			logger.warning("Queue waiting interrupted: " + e.getMessage());
		}
	}

	private FingersPosition createMessage(FingerData fingerData) {
		FingersPosition.Builder message = FingersPosition.newBuilder();
		for (int i = 0; i < Constants.NUM_OF_FINGERS; i++) {
			FingersPosition.Finger.Builder finger = message.addFingersBuilder();
			finger.setFingerPosition(fingerData.getPosition(i));
			finger.setTapHappened(fingerData.tapHappened(i));
		}
		return message.build();
	}

	public void sendData(FingerData data) {
		logger.finer("New fingerData to queue: " + data);
		try {
			queue.put(data);
		} catch (InterruptedException e) {
			logger.warning("Queue waiting interrupted: " + e.getMessage());
		}
	}

	public void disconnect() throws IOException, InterruptedException {
		serverSocket.close();
	}

}
