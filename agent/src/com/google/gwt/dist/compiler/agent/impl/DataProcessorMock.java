package com.google.gwt.dist.compiler.agent.impl;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;

import com.google.gwt.core.ext.UnableToCompleteException;
import com.google.gwt.dist.compiler.agent.DataProcessor;
import com.google.gwt.dist.compiler.agent.SessionManager;
import com.google.gwt.dist.compiler.agent.events.CompilePermsListener;

/**
 * 
 * Mock implementation of DataProcessor. This mock changes its state every n
 * seconds.
 * 
 */
public class DataProcessorMock extends Thread implements DataProcessor {

	private List<CompilePermsListener> compilePermsListeners;

	public DataProcessorMock(SessionManager sm) {
		compilePermsListeners.add(sm);
		try {
			for (int i = 0; i < 10; i++) {
				Thread.sleep(30000);
				compilePermsFinished();
			}
		} catch (InterruptedException e) {

		}
	}

	@Override
	public void addListener(CompilePermsListener listener) {
		compilePermsListeners.add(listener);
	}

	@Override
	public void compilePermsFinished() {
		for (CompilePermsListener l : compilePermsListeners) {
			l.onCompilePermsFinished();
		}
	}

	@Override
	public void removeListener(CompilePermsListener listener) {
		compilePermsListeners.add(listener);
	}

	@Override
	public void startCompilePerms() throws UnableToCompleteException,
			MalformedURLException {

	}

	@Override
	public void storeInputStreamOnDisk(ByteArrayOutputStream receivedData)
			throws FileNotFoundException, IOException {
	}
}
