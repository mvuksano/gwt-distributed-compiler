package com.google.gwt.dist.compiler.agent.communicator.impl;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.google.gwt.dist.SessionState;
import com.google.gwt.dist.SessionState.State;
import com.google.gwt.dist.compiler.agent.DataProcessor;
import com.google.gwt.dist.compiler.agent.SessionManager;
import com.google.gwt.dist.compiler.agent.communicator.Communicator;

/**
 * Tests CommunicatorImpl behavior.
 */
public class CommunicatorImplTest {

	Communicator communicator;
	SessionManager sessionManager;
	DataProcessor dataProcessor;

	@BeforeClass
	public void setUp() {
		dataProcessor = mock(DataProcessor.class);
		communicator = new CommunicatorImpl(dataProcessor);
		sessionManager = mock(SessionManager.class);

		((CommunicatorImpl) communicator).setSessionManager(sessionManager);
	}

	/**
	 * Test if correct information is passed by SessionManager to the
	 * communicator.
	 */
	@Test
	public void testCompilePermsCompleted() {
		when(sessionManager.getState()).thenReturn(
				new SessionState(State.READY));
		Assert.assertTrue(!communicator.workFinished());

		when(sessionManager.getState()).thenReturn(
				new SessionState(State.COMPLETED));
		Assert.assertTrue(communicator.workFinished());
	}
}
