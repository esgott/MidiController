package hu.midicontroller.leap;

public class FingerHistory {

	private float widthPosition;
	private float heightPosition;
	private float maxHeightPosition = Float.NEGATIVE_INFINITY;
	private float minHeightPosition = Float.POSITIVE_INFINITY;
	private boolean updated = false;

	public FingerHistory(float widthPosition) {
		this.widthPosition = widthPosition;
	}

	public void setHeightPosition(float position) {
		heightPosition = position;
		maxHeightPosition = Math.max(maxHeightPosition, position);
		minHeightPosition = Math.min(minHeightPosition, position);
		updated = true;
	}

	public void setWidthPosition(float position) {
		widthPosition = position;
		updated = true;
	}

	public int getPercentageDown() {
		float relativeHeight = heightPosition - minHeightPosition;
		float relativeMax = maxHeightPosition - minHeightPosition;
		if ((relativeMax - relativeHeight) == 0) {
			return 0;
		}
		return Math.round((relativeHeight / relativeMax) * 100);
	}

	public float getWidthPosition() {
		return widthPosition;
	}

	public float getHeightPosition() {
		return heightPosition;
	}

	public void clearUpdated() {
		updated = false;
	}

	public boolean updated() {
		return updated;
	}

}
