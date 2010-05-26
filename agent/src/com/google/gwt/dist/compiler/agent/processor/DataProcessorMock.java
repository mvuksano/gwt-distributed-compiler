package com.google.gwt.dist.compiler.agent.processor;

import java.io.FileNotFoundException;
import java.io.IOException;

import com.google.gwt.dist.ProcessingState;
import com.google.gwt.dist.comm.SendDataPayload;

/**
 * 
 * Mock implementation of DataProcessor. This mock changes its state every n
 * seconds.
 * 
 */
public class DataProcessorMock implements DataProcessor, Runnable {

	private int interval = 15000;
	private ProcessingState mockProcessingState;

	public DataProcessorMock(int intervalInMiliSeconds) {
		mockProcessingState = ProcessingState.READY;
		interval = intervalInMiliSeconds;
	}

	public void compilePermsFinished() {
		this.mockProcessingState = ProcessingState.COMPLETED;
	}

	public void compilePermsStarted() {
		this.mockProcessingState = ProcessingState.INPROGRESS;
	}
	
	public ProcessingState getCurrentState() {
		return this.mockProcessingState;
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
	public void storeInputStreamOnDisk(SendDataPayload receivedData)
			throws FileNotFoundException, IOException {
	}

	@Override
	public void onDataReceived(SendDataPayload receivedData) {
	}
	
	public void reset(){
		mockProcessingState = ProcessingState.READY;
	}
}
