package hu.midicontroller.gui;

import javax.swing.SwingUtilities;

import hu.midicontroller.Config;
import hu.midicontroller.protocol.LeapController.FingersPosition;
import hu.midicontroller.protocol.LeapController.FingersPosition.Finger;

public class FingerDataDispatcher {

	private Gui gui;

	public FingerDataDispatcher(Gui gui) {
		this.gui = gui;
	}

	public void dispatch(FingersPosition message) {
		for (int fingerId = 0; fingerId < Config.NUM_OF_FINGERS; fingerId++) {
			Finger finger = message.getFingers(fingerId);
			int position = finger.getFingerPosition();
			boolean tapHappened = finger.getTapHappened();
			FingerPanel panel = gui.getFingerPanel(fingerId);
			SwingUtilities.invokeLater(new GuiUpdater(panel, position,
					tapHappened));
		}
	}

}
