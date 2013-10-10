package hu.midicontroller.communication;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.LinkedBlockingQueue;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class TcpServerTest {

	@Mock
	private LinkedBlockingQueue<FingerData> mockQueue;
	@Mock
	private ServerSocket mockServerSocket;
	@Mock
	private Socket mockSocket;
	@Mock
	private OutputStream mockOutputStream;

	private TcpServer tcpServer;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		tcpServer = new TcpServer(mockQueue, mockServerSocket);
	}

	@Test
	public void afterConnectionNewDataHandledThenServerStoppedWithExceptions()
			throws IOException, InterruptedException {
		when(mockServerSocket.accept()).thenReturn(mockSocket).thenThrow(
				new IOException());
		when(mockQueue.take()).thenReturn(createFingerData()).thenThrow(
				new InterruptedException());
		when(mockSocket.getOutputStream()).thenReturn(mockOutputStream);

		tcpServer.run();

		verify(mockServerSocket).bind(argThat(new PortMatcher(35678)));
		verify(mockServerSocket, times(2)).accept();
		verify(mockQueue, times(2)).take();
		verify(mockOutputStream, atLeastOnce()).write(any(byte[].class),
				anyInt(), anyInt());
		;
	}

	private FingerData createFingerData() {
		int[] positions = new int[10];
		boolean[] taps = new boolean[10];
		for (int i = 1; i < 10; i++) {
			positions[i] = i;
			taps[i] = (i % 2) == 0;
		}
		FingerData fingerData = new FingerData(positions, taps);
		return fingerData;
	}

}
