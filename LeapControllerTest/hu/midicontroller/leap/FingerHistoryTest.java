package hu.midicontroller.leap;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

public class FingerHistoryTest {

	private FingerHistory fingerHistory;

	@Before
	public void setUp() {
		fingerHistory = new FingerHistory(15f);
	}

	@Test
	public void percentageCalculatedCorrectlyWithZeroMinimum() {
		fingerHistory.setHeightPosition(0f);
		fingerHistory.setHeightPosition(100f);
		fingerHistory.setHeightPosition(45f);

		int percentage = fingerHistory.getPercentageDown();

		assertEquals(45, percentage);
	}

	@Test
	public void percentageCalculatedCorrectlyWithNONZeroMinimum() {
		fingerHistory.setHeightPosition(10f);
		fingerHistory.setHeightPosition(110f);
		fingerHistory.setHeightPosition(65f);

		int percentage = fingerHistory.getPercentageDown();

		assertEquals(55, percentage);
	}

	@Test
	public void percentageCalculatedCorrectlyWhenDifferenceIsZero() {
		fingerHistory.setHeightPosition(10f);

		int percentage = fingerHistory.getPercentageDown();

		assertEquals(0, percentage);
	}

}
