package hu.midicontroller;

import hu.midicontroller.communication.TcpServerTest;
import hu.midicontroller.leap.FingerDataProcessorTest;
import hu.midicontroller.leap.FingerHistoryTest;
import hu.midicontroller.leap.FingerStorageTest;
import hu.midicontroller.leap.LeapListenerTest;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ TcpServerTest.class, LeapListenerTest.class,
		FingerDataProcessorTest.class, FingerStorageTest.class,
		FingerHistoryTest.class })
public class AllTests {

}
