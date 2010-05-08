package com.google.gwt.dist.compiler.communicator;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.google.gwt.dist.Node;
import com.google.gwt.dist.SessionState;
import com.google.gwt.dist.SessionState.State;

/**
 * Test Communicator.
 */
public class CommunicatorTest {

	private Node node;
	private Communicator communicator;

	@BeforeClass
	public void setUp() {
		node = mock(Node.class);
		communicator = mock(Communicator.class);
	}

	@Test
	public void getSessionState() {
		// mock
		when(communicator.getSessionState(node)).thenReturn(
				new SessionState(State.INPROGRESS));
		
		// verify
		Assert.assertEquals(communicator.getSessionState(node).getState(), State.INPROGRESS);
	}
}
