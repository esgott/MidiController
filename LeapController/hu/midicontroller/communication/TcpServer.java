package hu.midicontroller.communication;

import hu.midicontroller.Config;
import hu.midicontroller.protocol.LeapController.FingersPosition;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Logger;

public class TcpServer implements Runnable {

	private final LinkedBlockingQueue<FingerData> queue;
	private final ServerSocket serverSocket;
	private Socket socket;
	private boolean stopped = false;
	private final static Logger logger = Logger.getLogger(TcpServer.class
			.getName());

	public TcpServer(LinkedBlockingQueue<FingerData> queue,
			ServerSocket serverSocket) {
		this.queue = queue;
		this.serverSocket = serverSocket;
	}

	@Override
	public void run() {
		InetSocketAddress address = new InetSocketAddress(Config.PORT);
		try {
			serverSocket.bind(address);
			logger.info("Server started");
			while (!stopped()) {
				try {
					logger.info("Waiting for client to connect");
					socket = serverSocket.accept();
					handleConnection();
				} catch (IOException e) {
					logger.info("Client disconnected: " + e.getMessage());
				}
			}
		} catch (IOException e1) {
			logger.severe("Failed to bind: " + e1.getMessage());
		}
	}

	private synchronized boolean stopped() {
		return stopped;
	}

	private synchronized void setStopped(boolean state) {
		stopped = state;
	}

	private void handleConnection() throws IOException {
		logger.info("Client connected");
		try {
			while (true) {
				FingerData fingerData = queue.take();
				FingersPosition message = createMessage(fingerData);
				OutputStream outputStrem = socket.getOutputStream();
				logger.fine("Sending message: " + message);
				message.writeDelimitedTo(outputStrem);
			}
		} catch (InterruptedException e) {
			logger.warning("Queue waiting interrupted: " + e.getMessage());
		}
	}

	private FingersPosition createMessage(FingerData fingerData) {
		FingersPosition.Builder message = FingersPosition.newBuilder();
		for (int i = 0; i < Config.NUM_OF_FINGERS; i++) {
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
		setStopped(true);
		serverSocket.close();
	}

}
