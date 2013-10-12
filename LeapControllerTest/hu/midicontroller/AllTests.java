package hu.midicontroller;

import hu.midicontroller.communication.TcpServerTest;
import hu.midicontroller.leap.LeapListenerTest;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ TcpServerTest.class, LeapListenerTest.class })
public class AllTests {

}
