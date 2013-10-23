package hu.midicontroller.leap;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

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
		List<Finger> fingers = orderFingers(fingerList);
		for (Finger finger : fingers) {
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

	private List<Finger> orderFingers(FingerList originalList) {
		List<Finger> result = new ArrayList<Finger>(Config.NUM_OF_FINGERS);
		for (Finger finger : originalList) {
			result.add(finger);
		}
		Collections.sort(result, new Comparator<Finger>() {
			@Override
			public int compare(Finger finger1, Finger finger2) {
				Vector position1 = finger1.tipPosition();
				Vector position2 = finger2.tipPosition();
				float diff = position1.getX() - position2.getX();
				if (diff < 0) {
					return -1;
				} else if (diff > 0) {
					return 1;
				} else {
					return 0;
				}
			}
		});
		return result;
	}

	private int percentage(int start, int end, int position) {
		float actual = position - start;
		float max = end - start;
		if (max == 0) {
			return 0;
		}
		return Math.round((actual / max) * 100);
	}

}
