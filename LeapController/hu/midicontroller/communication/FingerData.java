package hu.midicontroller.communication;

public class FingerData {
	private final int[] positions;
	private final boolean[] taps;

	public FingerData(int[] positions, boolean[] taps) {
		this.positions = positions;
		this.taps = taps;
	}

	public int getPosition(int index) {
		return positions[index];
	}

	public boolean tapHappened(int index) {
		return taps[index];
	}

	@Override
	public String toString() {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("[ ");
		for (int position : positions) {
			stringBuilder.append(position);
			stringBuilder.append(' ');
		}
		stringBuilder.append("] [ ");
		for (boolean tap : taps) {
			stringBuilder.append(tap);
			stringBuilder.append(' ');
		}
		stringBuilder.append(']');
		return stringBuilder.toString();
	}

}
