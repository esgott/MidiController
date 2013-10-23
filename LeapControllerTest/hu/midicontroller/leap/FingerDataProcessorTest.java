package hu.midicontroller.leap;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import hu.midicontroller.communication.FingerData;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.leapmotion.leap.Finger;
import com.leapmotion.leap.FingerList;

public class FingerDataProcessorTest {

	@Mock
	private FingerStorage mockFingerStorage;
	@Mock
	private FingerData mockFingerData;
	@Mock
	private Finger mockFinger;
	@Mock
	private FingerList mockFingerList;
	private List<Finger> emptyFingerList = new ArrayList<>(0);

	private FingerDataProcessor fingerDataProcessor;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		fingerDataProcessor = new FingerDataProcessor(mockFingerStorage);
	}

	@Test
	public void fingerDataConvertedAndSentForProcessingWhenEmpty() {
		when(mockFingerList.count()).thenReturn(0);
		when(mockFingerStorage.getFingerDataForCommunication()).thenReturn(
				mockFingerData);

		FingerData result = fingerDataProcessor.processData(mockFingerList);

		verify(mockFingerStorage).update(emptyFingerList);
		verify(mockFingerStorage).getFingerDataForCommunication();
		assertEquals(result, mockFingerData);
	}

}
