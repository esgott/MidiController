package hu.midicontroller.communication;

import hu.midicontroller.protocol.LeapController.FingersPosition;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.LinkedBlockingQueue;

public class TcpServer implements Runnable {

	private LinkedBlockingQueue<FingerData> queue;
	private ServerSocket serverSocket;
	private Socket socket;

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
				socket = serverSocket.accept();
				handleConnection();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void handleConnection() throws IOException {
		try {
			while (true) {
				FingerData fingerData = queue.take();
				FingersPosition message = createMessage(fingerData);
				OutputStream outputStrem = socket.getOutputStream();
				message.writeTo(outputStrem);
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private FingersPosition createMessage(FingerData fingerData) {
		FingersPosition.Builder message = FingersPosition.newBuilder();
		for (int i = 0; i < 10; i++) {
			FingersPosition.Finger.Builder finger = message.addFingersBuilder();
			finger.setFingerPosition(fingerData.getPosition(i));
			finger.setTapHappened(fingerData.tapHappened(i));
		}
		return message.build();
	}

	public void sendData(FingerData data) {
		try {
			queue.put(data);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
