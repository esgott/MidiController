package hu.midicontroller.leap;

import hu.midicontroller.Constants;
import hu.midicontroller.communication.FingerData;

import com.leapmotion.leap.Finger;
import com.leapmotion.leap.FingerList;
import com.leapmotion.leap.Vector;

public class FingerDataProcessor {

	public FingerData processData(FingerList fingerList) {
		int fingerId = 0;
		int[] fingerPositions = new int[Constants.NUM_OF_FINGERS];
		boolean[] taps = new boolean[Constants.NUM_OF_FINGERS];
		for (Finger finger : fingerList) {
			if (fingerId < Constants.NUM_OF_FINGERS) {
				Vector fingerTipPosition = finger.tipPosition();
				int fingerPosition = Math.round(fingerTipPosition.getY());
				fingerPositions[fingerId] = fingerPosition;
				taps[fingerId] = false;
			}
			fingerId++;
		}
		return new FingerData(fingerPositions, taps);
	}

}
