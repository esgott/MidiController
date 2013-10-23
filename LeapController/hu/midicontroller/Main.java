package hu.midicontroller;

import hu.midicontroller.communication.FingerData;
import hu.midicontroller.communication.TcpServer;
import hu.midicontroller.leap.ConfigurationException;
import hu.midicontroller.leap.FingerDataProcessor;
import hu.midicontroller.leap.FingerHistory;
import hu.midicontroller.leap.FingerStorage;
import hu.midicontroller.leap.LeapListener;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import com.leapmotion.leap.Controller;
import com.leapmotion.leap.Listener;

public class Main {

	private static final Logger logger = LogManager.getLogManager().getLogger(
			"");
	private static FileHandler logFile;
	private static TcpServer tcpServer;

	private static void initLogger() {
		try {
			logFile = new FileHandler("log.txt");
			Formatter formatter = new SimpleFormatter();
			logFile.setFormatter(formatter);
			logger.addHandler(logFile);
			setLevel(Level.INFO);
			logger.info("Program started");
		} catch (Exception e) {
			System.out.println("Failed to init logging: " + e.getMessage());
		}
	}

	private static void setLevel(Level level) {
		logger.setLevel(level);
		Handler[] handlers = logger.getHandlers();
		for (Handler handler : handlers) {
			handler.setLevel(level);
		}
	}

	public static void main(String[] args) {
		initLogger();
		try {
			tcpServer = new TcpServer(new LinkedBlockingQueue<FingerData>(),
					new ServerSocket());
			Thread serverThread = new Thread(tcpServer);
			serverThread.start();
			Listener listener = createListener(tcpServer);
			Controller controller = new Controller();
			controller.addListener(listener);
			waitForKeypress();
		} catch (IOException e) {
			logger.severe("Failed to create TcpServer: " + e.getMessage());
		} catch (ConfigurationException e) {
			e.printStackTrace();
		}
	}

	private static Listener createListener(TcpServer tcpServer)
			throws ConfigurationException {
		List<FingerHistory> fingerDataList = new ArrayList<FingerHistory>();
		FingerStorage fingerStorage = new FingerStorage(fingerDataList);
		FingerDataProcessor fingerDataProcessor = new FingerDataProcessor(
				fingerStorage);
		return new LeapListener(tcpServer, fingerDataProcessor);
	}

	private static void waitForKeypress() {
		try {
			System.in.read();
			tcpServer.disconnect();
		} catch (IOException | InterruptedException e) {
			logger.severe("Error: " + e.getMessage());
		}
		logger.info("Exiting");
		logFile.close();
	}

}
