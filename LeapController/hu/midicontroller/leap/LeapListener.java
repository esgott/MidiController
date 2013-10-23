package hu.midicontroller.leap;

import hu.midicontroller.communication.FingerData;
import hu.midicontroller.communication.TcpServer;

import java.util.logging.Logger;

import com.leapmotion.leap.Controller;
import com.leapmotion.leap.FingerList;
import com.leapmotion.leap.Frame;
import com.leapmotion.leap.Listener;

public class LeapListener extends Listener {

	private final TcpServer tcpServer;
	private final FingerDataProcessor fingerDataProcessor;
	private final static Logger logger = Logger.getLogger(LeapListener.class
			.getName());

	public LeapListener(TcpServer server, FingerDataProcessor processor) {
		tcpServer = server;
		fingerDataProcessor = processor;
	}

	@Override
	public void onFrame(Controller controller) {
		Frame frame = controller.frame();
		logger.fine("New frame arrived with id " + frame.id());
		FingerList fingers = frame.fingers();
		FingerData fingerData = fingerDataProcessor.processData(fingers);
		tcpServer.sendData(fingerData);
	}

}
