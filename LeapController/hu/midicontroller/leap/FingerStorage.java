package hu.midicontroller.leap;

import hu.midicontroller.Config;
import hu.midicontroller.communication.FingerData;

import java.util.ArrayList;
import java.util.List;

import com.leapmotion.leap.Finger;
import com.leapmotion.leap.Vector;

public class FingerStorage {

	private List<FingerHistory> fingers = new ArrayList<FingerHistory>();

	public FingerStorage(List<FingerHistory> fingerDataStorage)
			throws ConfigurationException {
		fingers = fingerDataStorage;

		for (int initialPosition : Config.INITIAL_FINGER_WIDTH_POSITIONS) {
			fingers.add(new FingerHistory(initialPosition));
		}

		if (fingers.size() != Config.NUM_OF_FINGERS) {
			throw new ConfigurationException();
		}
	}

	public void update(List<Finger> fingerList) {
		if (fingerList.size() == Config.NUM_OF_FINGERS) {
			updateEveryFingerWidthPosition(fingerList);
		} else {
			while (!fingerList.isEmpty()) {
				updateAndRemoveMostPuncutalItem(fingerList);
			}
		}
	}

	private void updateEveryFingerWidthPosition(List<Finger> allFingers) {
		for (int i = 0; i < allFingers.size(); i++) {
			Vector tipPosition = allFingers.get(i).tipPosition();
			FingerHistory finger = fingers.get(i);
			finger.setWidthPosition(tipPosition.getX());
			finger.setHeightPosition(tipPosition.getY());
		}
	}

	private void updateAndRemoveMostPuncutalItem(List<Finger> fingerList) {
		float minDiff = Float.MAX_VALUE;
		Finger punctualItem = null;
		FingerHistory punctualStoredItem = null;

		for (Finger finger : fingerList) {
			for (FingerHistory storedFinger : fingers) {
				float actualDiff = widthPositionDiff(finger, storedFinger);
				if (actualDiff < minDiff) {
					minDiff = actualDiff;
					punctualItem = finger;
					punctualStoredItem = storedFinger;
				}
			}
		}

		updateItemOrCloseItem(punctualStoredItem, punctualItem);
		fingerList.remove(punctualItem);
	}

	private float widthPositionDiff(Finger finger1, FingerHistory finger2) {
		float position1 = finger1.tipPosition().getX();
		float position2 = finger2.getWidthPosition();
		return Math.abs(position1 - position2);
	}

	private void updateItemOrCloseItem(FingerHistory storedFinger, Finger finger) {
		if (!storedFinger.updated()) {
			updateItem(storedFinger, finger);
		} else {
			FingerNeighbours neighbours = getNeighbours(storedFinger, finger);
			FingerHistory closer = neighbours.closerNeighbour;
			if (!closer.updated()) {
				updateItem(closer, finger);
			}
		}
	}

	private void updateItem(FingerHistory storedFinger, Finger finger) {
		Vector position = finger.tipPosition();
		storedFinger.setWidthPosition(position.getX());
		storedFinger.setHeightPosition(position.getY());
	}

	private FingerNeighbours getNeighbours(FingerHistory element, Finger data) {
		int index = fingers.indexOf(element);
		if (index == 0) {
			return new FingerNeighbours(fingers.get(1), null);
		} else if (index == (Config.NUM_OF_FINGERS - 1)) {
			return new FingerNeighbours(fingers.get(Config.NUM_OF_FINGERS - 2),
					null);
		} else {
			FingerHistory leftNeihgbour = fingers.get(index - 1);
			FingerHistory rightNeighbour = fingers.get(index + 1);
			FingerHistory closerNeighbour = getCloserNeighbour(leftNeihgbour,
					rightNeighbour, data);
			FingerHistory fartherNeighbour = closerNeighbour == leftNeihgbour ? rightNeighbour
					: leftNeihgbour;
			return new FingerNeighbours(closerNeighbour, fartherNeighbour);
		}
	}

	private FingerHistory getCloserNeighbour(FingerHistory leftNeighbour,
			FingerHistory rightNeighbour, Finger data) {
		float leftDiff = widthPositionDiff(data, leftNeighbour);
		float rightDiff = widthPositionDiff(data, rightNeighbour);
		return leftDiff < rightDiff ? leftNeighbour : rightNeighbour;
	}

	public FingerData getFingerDataForCommunication() {
		int[] tipPositions = new int[Config.NUM_OF_FINGERS];
		boolean[] taps = new boolean[Config.NUM_OF_FINGERS];

		for (int i = 0; i < Config.NUM_OF_FINGERS; i++) {
			FingerHistory actualFinger = fingers.get(i);
			tipPositions[i] = actualFinger.getPercentageDown();
			taps[i] = false;
			actualFinger.clearUpdated();
		}

		return new FingerData(tipPositions, taps);
	}

}
