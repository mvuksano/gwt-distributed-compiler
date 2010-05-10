package com.google.gwt.dist.compiler.communicator;

import static org.mockito.Mockito.mock;

import org.testng.annotations.BeforeClass;

import com.google.gwt.dist.Node;

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
}
