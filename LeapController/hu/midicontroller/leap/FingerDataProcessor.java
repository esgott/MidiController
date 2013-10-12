package hu.midicontroller.leap;

import hu.midicontroller.communication.FingerData;

import com.leapmotion.leap.Finger;
import com.leapmotion.leap.FingerList;
import com.leapmotion.leap.Vector;

public class FingerDataProcessor {

	private static final int MAX_FINGER_NUM = 10;

	public FingerData processData(FingerList fingerList) {
		int fingerId = 0;
		int[] fingerPositions = new int[MAX_FINGER_NUM];
		boolean[] taps = new boolean[MAX_FINGER_NUM];
		for (Finger finger : fingerList) {
			if (fingerId < MAX_FINGER_NUM) {
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
