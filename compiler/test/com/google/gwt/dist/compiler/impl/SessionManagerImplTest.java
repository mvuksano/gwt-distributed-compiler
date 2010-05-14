package com.google.gwt.dist.compiler.impl;

import static org.mockito.Mockito.mock;

import org.testng.annotations.BeforeClass;

import com.google.gwt.dist.Node;
import com.google.gwt.dist.compiler.SessionManager;
import com.google.gwt.dist.compiler.communicator.Communicator;
import com.google.gwt.dist.util.ZipCompressor;

public class SessionManagerImplTest {

	ZipCompressor compressor;
	Communicator communicator;
	SessionManager sessionManager;
	Node node;

	@BeforeClass
	public void init() {
		compressor = mock(ZipCompressor.class);
		communicator = mock(Communicator.class);
		node = mock(Node.class);
		sessionManager = new SessionManagerImpl(communicator, node, compressor);
	}
}
