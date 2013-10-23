package hu.midicontroller.leap;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import hu.midicontroller.communication.FingerData;
import hu.midicontroller.communication.TcpServer;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.leapmotion.leap.Controller;
import com.leapmotion.leap.Finger;
import com.leapmotion.leap.FingerList;
import com.leapmotion.leap.Frame;

public class LeapListenerTest {

	@Mock
	private TcpServer mockTcpServer;
	@Mock
	private FingerDataProcessor mockFingerDataProcessor;
	@Mock
	private Controller mockController;
	@Mock
	private Frame mockFrame;
	@Mock
	private FingerList mockFingerList;
	@Mock
	private FingerData mockFingerData;
	private List<Finger> fingerList = new ArrayList<Finger>();

	private LeapListener leapListener;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		leapListener = new LeapListener(mockTcpServer, mockFingerDataProcessor);
	}


	@Test
	public void finderDataProcessedOnNewFrame() {
		when(mockController.frame()).thenReturn(mockFrame);
		when(mockFrame.fingers()).thenReturn(mockFingerList);
		mockConvertList();
		when(mockFingerDataProcessor.processData(fingerList)).thenReturn(
				mockFingerData);

		leapListener.onFrame(mockController);

		verify(mockTcpServer).sendData(mockFingerData);
	}


	private void mockConvertList() {
		final int listSize = 5;
		when(mockFingerList.count()).thenReturn(listSize);
		for (int i = 0; i < listSize; i++) {
			Finger finger = new Finger();
			when(mockFingerList.get(i)).thenReturn(finger);
			fingerList.add(finger);
		}
	}

}
