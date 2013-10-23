package hu.midicontroller.leap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import hu.midicontroller.Config;
import hu.midicontroller.communication.FingerData;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

import com.leapmotion.leap.Finger;
import com.leapmotion.leap.Vector;

public class FingerStorageTest {
	private List<FingerHistory> fingerDataList;
	private FingerHistory[] mockFingerHistories = new FingerHistory[Config.NUM_OF_FINGERS];

	private List<Finger> dummyFingerList;
	private Finger[] mockFingers = new Finger[Config.NUM_OF_FINGERS];
	private Vector[] mockVectors = new Vector[Config.NUM_OF_FINGERS];

	private FingerStorage fingerStorage;

	@Before
	public void setUp() throws ConfigurationException {
		MockitoAnnotations.initMocks(this);
		for (int i = 0; i < Config.NUM_OF_FINGERS; i++) {
			mockVectors[i] = mock(Vector.class);
			when(mockVectors[i].getX()).thenReturn((float) i);
			when(mockVectors[i].getY()).thenReturn((float) (i * 10));
			mockFingers[i] = mock(Finger.class);
		}
		dummyFingerList = new ArrayList<Finger>();

		fingerDataList = new ArrayList<FingerHistory>();
		fingerStorage = new FingerStorage(fingerDataList);
	}

	@Test
	public void fingerDataFilledWhenRequested() {
		mockFingerDataList();
		FingerData data = fingerStorage.getFingerDataForCommunication();

		for (int i = 0; i < Config.NUM_OF_FINGERS; i++) {
			assertEquals(i, data.getPosition(i));
			assertEquals(false, data.tapHappened(i));
		}
	}

	private void mockFingerDataList() {
		for (int i = 0; i < Config.NUM_OF_FINGERS; i++) {
			mockFingerHistories[i] = mock(FingerHistory.class);
			fingerDataList.set(i, mockFingerHistories[i]);
			when(mockFingerHistories[i].getPercentageDown()).thenReturn(i);
		}
	}

	@Test
	public void everyFingerUpdatedWhenAllFingersProvided() {
		mockFingerDataList();
		fillFingerList(Config.NUM_OF_FINGERS);

		fingerStorage.update(dummyFingerList);

		for (int i = 0; i < mockFingerHistories.length; i++) {
			verify(mockFingerHistories[i]).setWidthPosition(i);
			verify(mockFingerHistories[i]).setHeightPosition(i * 10);
		}
	}

	private void fillFingerList(int numberOfElements) {
		for (int i = 0; i < numberOfElements; i++) {
			dummyFingerList.add(mockFingers[i]);
			when(mockFingers[i].tipPosition()).thenReturn(mockVectors[i]);
		}
	}

	@Test
	public void oneFingerInTheMiddleUpdatesTheClosest() {
		fillFingerList(1);
		when(mockVectors[0].getX()).thenReturn(54f);
		when(mockVectors[0].getY()).thenReturn(20f);

		fingerStorage.update(dummyFingerList);

		assertEquals(54f, fingerDataList.get(4).getWidthPosition(), 0.001f);
		assertEquals(20f, fingerDataList.get(4).getHeightPosition(), 0.001f);
	}

	@Test
	public void twoFingersAtTheSamePlaceSecondUpdatesClosestRightNeighbour() {
		fillFingerList(2);
		when(mockVectors[0].getX()).thenReturn(49f);
		when(mockVectors[0].getY()).thenReturn(30f);
		when(mockVectors[1].getX()).thenReturn(52f);
		when(mockVectors[1].getY()).thenReturn(20f);

		fingerStorage.update(dummyFingerList);

		assertEquals(49f, fingerDataList.get(4).getWidthPosition(), 0.001f);
		assertEquals(30f, fingerDataList.get(4).getHeightPosition(), 0.001f);
		assertEquals(52f, fingerDataList.get(5).getWidthPosition(), 0.001f);
		assertEquals(20f, fingerDataList.get(5).getHeightPosition(), 0.001f);
	}

	@Test
	public void twoFingersAtTheLeftEdgeSecondUpdatesClosestNeighbour() {
		fillFingerList(2);
		when(mockVectors[0].getX()).thenReturn(11f);
		when(mockVectors[0].getY()).thenReturn(30f);
		when(mockVectors[1].getX()).thenReturn(13f);
		when(mockVectors[1].getY()).thenReturn(20f);

		fingerStorage.update(dummyFingerList);

		assertEquals(11f, fingerDataList.get(0).getWidthPosition(), 0.001f);
		assertEquals(30f, fingerDataList.get(0).getHeightPosition(), 0.001f);
		assertEquals(13f, fingerDataList.get(1).getWidthPosition(), 0.001f);
		assertEquals(20f, fingerDataList.get(1).getHeightPosition(), 0.001f);
	}

	@Test
	public void twoFingersAtTheRightEdgeSecondUpdatesClosestNeighbour() {
		fillFingerList(2);
		when(mockVectors[0].getX()).thenReturn(99f);
		when(mockVectors[0].getY()).thenReturn(30f);
		when(mockVectors[1].getX()).thenReturn(98f);
		when(mockVectors[1].getY()).thenReturn(20f);

		fingerStorage.update(dummyFingerList);

		assertEquals(99f, fingerDataList.get(9).getWidthPosition(), 0.001f);
		assertEquals(30f, fingerDataList.get(9).getHeightPosition(), 0.001f);
		assertEquals(98f, fingerDataList.get(8).getWidthPosition(), 0.001f);
		assertEquals(20f, fingerDataList.get(8).getHeightPosition(), 0.001f);
	}

	@Test
	public void twoFingersAtTheSamePlaceSecondUpdatesClosestLeftNeighbour() {
		fillFingerList(2);
		when(mockVectors[0].getX()).thenReturn(49f);
		when(mockVectors[0].getY()).thenReturn(30f);
		when(mockVectors[1].getX()).thenReturn(46f);
		when(mockVectors[1].getY()).thenReturn(20f);

		fingerStorage.update(dummyFingerList);

		assertEquals(49f, fingerDataList.get(4).getWidthPosition(), 0.001f);
		assertEquals(30f, fingerDataList.get(4).getHeightPosition(), 0.001f);
		assertEquals(46f, fingerDataList.get(3).getWidthPosition(), 0.001f);
		assertEquals(20f, fingerDataList.get(3).getHeightPosition(), 0.001f);
	}

	@Test
	public void threeFingersAtTwoPlacesThirdNotUpdatedEnvironmentNotTouched() {
		fillFingerList(3);
		when(mockVectors[0].getX()).thenReturn(51f);
		when(mockVectors[0].getY()).thenReturn(30f);
		when(mockVectors[1].getX()).thenReturn(59f);
		when(mockVectors[1].getY()).thenReturn(20f);
		when(mockVectors[2].getX()).thenReturn(54f);
		when(mockVectors[2].getY()).thenReturn(10f);

		fingerStorage.update(dummyFingerList);

		assertEquals(40f, fingerDataList.get(3).getWidthPosition(), 0.001f);
		assertEquals(51f, fingerDataList.get(4).getWidthPosition(), 0.001f);
		assertEquals(30f, fingerDataList.get(4).getHeightPosition(), 0.001f);
		assertEquals(59f, fingerDataList.get(5).getWidthPosition(), 0.001f);
		assertEquals(20f, fingerDataList.get(5).getHeightPosition(), 0.001f);
		assertEquals(70f, fingerDataList.get(6).getWidthPosition(), 0.001f);
	}

	@Test
	public void updatedFlagCleardFromFingerHistories() {
		fillFingerList(3);
		when(mockVectors[0].getX()).thenReturn(51f);
		when(mockVectors[1].getX()).thenReturn(59f);

		fingerStorage.update(dummyFingerList);

		assertFalse(fingerDataList.get(3).updated());
		assertTrue(fingerDataList.get(4).updated());
		assertTrue(fingerDataList.get(5).updated());
		assertFalse(fingerDataList.get(6).updated());

		fingerStorage.getFingerDataForCommunication();

		for (FingerHistory fingerData : fingerDataList) {
			assertFalse(fingerData.updated());
		}
	}

}
