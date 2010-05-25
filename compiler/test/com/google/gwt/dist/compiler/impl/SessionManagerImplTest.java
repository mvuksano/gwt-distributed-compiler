package com.google.gwt.dist.compiler.impl;

import static org.mockito.Mockito.mock;

import org.testng.annotations.BeforeClass;

import com.google.gwt.dev.CompilePerms.CompilePermsOptions;
import com.google.gwt.dist.Application;
import com.google.gwt.dist.Node;
import com.google.gwt.dist.SessionManager;
import com.google.gwt.dist.compiler.communicator.Communicator;
import com.google.gwt.dist.util.ZipCompressor;
import com.google.gwt.dist.util.ZipDecompressor;

public class SessionManagerImplTest {

	Application application;
	Communicator communicator;
	CompilePermsOptions compilePermsOptions;
	ZipCompressor compressor;
	ZipDecompressor decompressor;
	SessionManager sessionManager;
	Node node;

	@BeforeClass
	public void init() {
		application = mock(Application.class);
		communicator = mock(Communicator.class);
		compilePermsOptions = mock(CompilePermsOptions.class);
		compressor = mock(ZipCompressor.class);
		decompressor = mock(ZipDecompressor.class);
		node = mock(Node.class);
		sessionManager = new SessionManagerImpl(application, communicator,
				node, compilePermsOptions, compressor, decompressor);
	}
}
