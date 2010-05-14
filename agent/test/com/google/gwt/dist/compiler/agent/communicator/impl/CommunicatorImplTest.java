package com.google.gwt.dist.compiler.agent.communicator.impl;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.google.gwt.dist.ProcessingState;
import com.google.gwt.dist.comm.CommMessage;
import com.google.gwt.dist.comm.ProcessingStateResponse;
import com.google.gwt.dist.compiler.agent.DataProcessor;
import com.google.gwt.dist.compiler.agent.SessionManager;
import com.google.gwt.dist.impl.ProcessingStateMessage;

/**
 * Tests CommunicatorImpl behavior.
 */
public class CommunicatorImplTest {

	CommunicatorImpl communicator;
	SessionManager sessionManager;
	DataProcessor dataProcessor;

	@BeforeClass
	public void setUp() {
		dataProcessor = mock(DataProcessor.class);
		communicator = new CommunicatorImpl();
		sessionManager = mock(SessionManager.class);

		((CommunicatorImpl) communicator).setSessionManager(sessionManager);
	}

	@Test
	public void testCommMessageProcessing() {
		when(sessionManager.getProcessingState()).thenReturn(
				ProcessingState.COMPLETED);
		CommMessage message = new ProcessingStateMessage();
		message = communicator.processCommMessage(message);

		ProcessingStateResponse expected = new ProcessingStateResponse();
		expected.setCurrentState(ProcessingState.COMPLETED);
		Assert.assertEquals(message.getResponse(), expected);
	}
}
