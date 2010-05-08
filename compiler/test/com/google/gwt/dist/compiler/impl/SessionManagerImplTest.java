package com.google.gwt.dist.compiler.impl;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.google.gwt.dist.Node;
import com.google.gwt.dist.SessionState;
import com.google.gwt.dist.SessionState.State;
import com.google.gwt.dist.compiler.SessionManager;
import com.google.gwt.dist.compiler.communicator.Communicator;

public class SessionManagerImplTest {

	Communicator communicator;
	SessionManager sessionManager;
	Node node;

	@BeforeClass
	public void init() {
		communicator = mock(Communicator.class);
		node = mock(Node.class);
		sessionManager = new SessionManagerImpl(communicator);
	}

	/**
	 * Test if SessionManager's compilePermsCompleted method returns correct
	 * values when in specific state.
	 */
	@Test
	public void testCompilePermsCompleted() {
		when(communicator.getSessionState(node)).thenReturn(
				new SessionState(State.READY));
		Assert.assertEquals(sessionManager.compilePermsCompleted(node), false);

		when(communicator.getSessionState(node)).thenReturn(
				new SessionState(State.INPROGRESS));
		Assert.assertEquals(sessionManager.compilePermsCompleted(node), false);

		when(communicator.getSessionState(node)).thenReturn(
				new SessionState(State.COMPLETED));
		Assert.assertEquals(sessionManager.compilePermsCompleted(node), true);
	}
}
