package hu.midicontroller.gui;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;

import javax.swing.SwingUtilities;

public class Main extends WindowAdapter {

	private static Gui gui = new Gui();
	private static TcpClient tcpClient;

	public static void main(String[] args) {
		FingerDataDispatcher dispatcher = new FingerDataDispatcher(gui);
		tcpClient = new TcpClient(dispatcher);
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				gui.createAndShowGUI();
			}
		});
		Thread tcpThread = new Thread(tcpClient);
		tcpThread.start();
	}

	@Override
	public void windowClosed(WindowEvent e) {
		try {
			tcpClient.disconnect();
		} catch (IOException e1) {
			e1.printStackTrace();
		}

	}

}
