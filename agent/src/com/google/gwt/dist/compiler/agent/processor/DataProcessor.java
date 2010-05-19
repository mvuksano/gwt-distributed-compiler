package com.google.gwt.dist.compiler.agent.processor;

import java.io.FileNotFoundException;
import java.io.IOException;

import com.google.gwt.dist.ProcessingState;
import com.google.gwt.dist.comm.SendDataPayload;
import com.google.gwt.dist.compiler.agent.events.DataReceivedListener;

/**
 * Definition of objects that will handle data processing.
 */
public interface DataProcessor extends Runnable, DataReceivedListener {
	
	/**
	 * 
	 * @return
	 */
	ProcessingState getCurrentState();

	/**
	 * Method should store input stream on disk so that it can be processed by
	 * CompilePerms.
	 * 
	 * @param receivedData
	 *            Data that was send from the client (compiler).
	 * @param location
	 *            Directory which will be used to store the data for processing
	 *            by CompilePerms.
	 */
	void storeInputStreamOnDisk(SendDataPayload receivedData)
			throws FileNotFoundException, IOException; 

}
