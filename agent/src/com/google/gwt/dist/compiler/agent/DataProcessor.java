package com.google.gwt.dist.compiler.agent;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;

import com.google.gwt.core.ext.UnableToCompleteException;
import com.google.gwt.dist.ProcessingState;
import com.google.gwt.dist.compiler.agent.events.DataReceivedListener;

/**
 * Definition of objects that will handle data processing.
 */
public interface DataProcessor extends Runnable, DataReceivedListener {

	/**
	 * This method is fired whenever CompilePerms operation is finished.
	 */
	void compilePermsFinished();
	
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
	void storeInputStreamOnDisk(byte[] receivedData)
			throws FileNotFoundException, IOException; 

	/**
	 * This method starts CompilePerms operation.
	 * 
	 * @throws UnableToCompleteException
	 *             Thrown if there was an internal problem with CompilePerms.
	 * @throws MalformedURLException
	 */
	void startCompilePerms() throws UnableToCompleteException,
			MalformedURLException;
}
