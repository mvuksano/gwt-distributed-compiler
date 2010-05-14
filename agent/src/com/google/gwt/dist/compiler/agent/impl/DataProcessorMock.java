package com.google.gwt.dist.compiler.agent.impl;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.ext.UnableToCompleteException;
import com.google.gwt.dist.ProcessingState;
import com.google.gwt.dist.compiler.agent.DataProcessor;
import com.google.gwt.dist.compiler.agent.events.CompilePermsListener;

/**
 * 
 * Mock implementation of DataProcessor. This mock changes its state every n
 * seconds.
 * 
 */
public class DataProcessorMock implements DataProcessor, Runnable {

	private List<CompilePermsListener> compilePermsListeners;

	private int interval = 15000;
        private ProcessingState mockProcessingState;

	public DataProcessorMock(int intervalInMiliSeconds) {
		mockProcessingState = ProcessingState.READY;
		compilePermsListeners = new ArrayList<CompilePermsListener>();
		interval = intervalInMiliSeconds;
	}

	@Override
	public void addListener(CompilePermsListener listener) {
		compilePermsListeners.add(listener);
	}

	public void compilePermsFinished() {
		for (CompilePermsListener l : compilePermsListeners) {
			l.onCompilePermsFinished();
		}
	}

	public void compilePermsStarted() {
		for (CompilePermsListener l : compilePermsListeners) {
			l.onCompilePermsStarted();
		}
	}

	@Override
	public void removeListener(CompilePermsListener listener) {
		compilePermsListeners.remove(listener);
	}

	@Override
	public void run() {
		while (true) {
			try {
				Thread.sleep(interval);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			switch (mockProcessingState) {
			case READY:
				mockProcessingState = ProcessingState.INPROGRESS;
				compilePermsStarted();
				break;
			case INPROGRESS:
				mockProcessingState = ProcessingState.COMPLETED;
				compilePermsFinished();
				break;
			default:
				mockProcessingState = ProcessingState.READY;
				break;
			}
		}
	}

	@Override
	public void startCompilePerms() throws UnableToCompleteException,
			MalformedURLException {

	}

	@Override
	public void storeInputStreamOnDisk(byte[] receivedData)
			throws FileNotFoundException, IOException {
	}

	@Override
	public void onDataReceived(byte[] receivedData) {
		mockProcessingState = ProcessingState.INPROGRESS;
		
	}
}
