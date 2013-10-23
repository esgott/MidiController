package hu.midicontroller.leap;

import hu.midicontroller.Config;
import hu.midicontroller.communication.FingerData;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.leapmotion.leap.Finger;
import com.leapmotion.leap.FingerList;
import com.leapmotion.leap.Vector;

public class FingerDataProcessor {

	private FingerStorage fingerStorage;

	public FingerDataProcessor(FingerStorage fingerStorage) {
		this.fingerStorage = fingerStorage;
	}

	public FingerData processData(FingerList fingers) {
		List<Finger> fingerList = convertToStandardList(fingers);
		orderFingers(fingerList);
		fingerStorage.update(fingerList);
		return fingerStorage.getFingerDataForCommunication();
	}

	private List<Finger> convertToStandardList(FingerList originalList) {
		List<Finger> result = new ArrayList<Finger>(Config.NUM_OF_FINGERS);
		for (int i = 0; i < originalList.count(); i++) {
			result.add(originalList.get(i));
		}
		return result;
	}

	private void orderFingers(List<Finger> list) {
		Collections.sort(list, new Comparator<Finger>() {
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
	}

}
