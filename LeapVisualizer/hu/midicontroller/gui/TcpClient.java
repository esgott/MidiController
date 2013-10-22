package hu.midicontroller.gui;

import hu.midicontroller.Config;
import hu.midicontroller.protocol.LeapController.FingersPosition;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class TcpClient implements Runnable {

	private Socket socket;
	private FingerDataDispatcher dataDispatcher;
	private boolean stopped = false;

	public TcpClient(FingerDataDispatcher dispatcher) {
		dataDispatcher = dispatcher;
	}

	@Override
	public void run() {
		while (!stopped()) {
			try {
				socket = new Socket("localhost", Config.PORT);
				InputStream inputStrem = socket.getInputStream();
				while (true) {
					FingersPosition message = FingersPosition.parseDelimitedFrom(inputStrem);
					dataDispatcher.dispatch(message);
				}
			} catch (UnknownHostException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			sleep(1);
		}
	}

	private synchronized boolean stopped() {
		return stopped;
	}

	private synchronized void setStopped(boolean state) {
		stopped = state;
	}

	private void sleep(int seconds) {
		try {
			Thread.sleep(seconds * 1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void disconnect() throws IOException {
		setStopped(true);
		socket.close();
	}

}
