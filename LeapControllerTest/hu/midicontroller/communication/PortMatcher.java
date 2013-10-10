package hu.midicontroller.communication;

import java.net.InetSocketAddress;

import org.mockito.ArgumentMatcher;

public class PortMatcher extends ArgumentMatcher<InetSocketAddress> {
	
	private int portToExpect;
	
	public PortMatcher(int portToExpect) {
		this.portToExpect = portToExpect;
	}

	@Override
	public boolean matches(Object argument) {
		if (argument instanceof InetSocketAddress) {
			InetSocketAddress address = (InetSocketAddress) argument;
			return address.getPort() == portToExpect;
		}
		return false;
	}

}
