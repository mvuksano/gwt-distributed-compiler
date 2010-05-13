package com.google.gwt.dist.compiler.agent.impl;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.ext.UnableToCompleteException;
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

	public DataProcessorMock(int intervalInMiliSeconds) {
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

	@Override
	public void removeListener(CompilePermsListener listener) {
		compilePermsListeners.add(listener);
	}
	
	@Override
	public void run() {
		try {
			Thread.sleep(interval);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		compilePermsFinished();
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
		// TODO Auto-generated method stub
		
	}
}
