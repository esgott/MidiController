package hu.midicontroller.leap;

public class FingerHistory {

	private static final float EROSION_SPEED = 0.2f;
	private static final float EROSION_TRESHOLD = 20f;
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
		maxMinErosion();
		maxHeightPosition = Math.max(maxHeightPosition, position);
		minHeightPosition = Math.min(minHeightPosition, position);
		updated = true;
	}

	private void maxMinErosion() {
		float spaceUntilMax = maxHeightPosition - heightPosition;
		if (spaceUntilMax > EROSION_TRESHOLD) {
			maxHeightPosition -= EROSION_SPEED;
		}
		float spaceUntilMin = heightPosition - maxHeightPosition;
		if (spaceUntilMin > EROSION_TRESHOLD) {
			minHeightPosition += EROSION_SPEED;
		}
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

	public boolean tapHappened() {
		return updated && getPercentageDown() < 10;
	}

}
