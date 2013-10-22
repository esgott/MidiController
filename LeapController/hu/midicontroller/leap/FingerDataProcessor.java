package hu.midicontroller.leap;

import hu.midicontroller.Config;
import hu.midicontroller.communication.FingerData;

import com.leapmotion.leap.Finger;
import com.leapmotion.leap.FingerList;
import com.leapmotion.leap.Vector;

public class FingerDataProcessor {

	private int[] minPosition = new int[Config.NUM_OF_FINGERS];
	private int[] maxPosition = new int[Config.NUM_OF_FINGERS];

	public FingerData processData(FingerList fingerList) {
		int fingerId = 0;
		int[] fingerPositions = new int[Config.NUM_OF_FINGERS];
		boolean[] taps = new boolean[Config.NUM_OF_FINGERS];
		for (Finger finger : fingerList) {
			if (fingerId < Config.NUM_OF_FINGERS) {
				Vector fingerTipPosition = finger.tipPosition();
				int fingerPosition = Math.round(fingerTipPosition.getY());
				if (minPosition[fingerId] == 0) {
					minPosition[fingerId] = fingerPosition;
				} else {
					minPosition[fingerId] = Math.min(minPosition[fingerId],
							fingerPosition);
				}
				maxPosition[fingerId] = Math.max(maxPosition[fingerId],
						fingerPosition);
				taps[fingerId] = fingerPosition == minPosition[fingerId];
				fingerPositions[fingerId] = percentage(minPosition[fingerId],
						maxPosition[fingerId], fingerPosition);
			}
			fingerId++;
		}
		return new FingerData(fingerPositions, taps);
	}

	private int percentage(int start, int end, int position) {
		float actual = position - start;
		float max = end - start;
		if (max == 0) {
			return 0;
		}
		return Math.round((actual/max) * 100);
	}

}
