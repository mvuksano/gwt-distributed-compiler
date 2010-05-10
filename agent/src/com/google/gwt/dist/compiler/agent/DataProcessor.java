package com.google.gwt.dist.compiler.agent;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;

import com.google.gwt.core.ext.UnableToCompleteException;
import com.google.gwt.dist.ProcessingState;
import com.google.gwt.dist.compiler.agent.events.CompilePermsListener;

/**
 * Definition of objects that will handle data processing.
 */
public interface DataProcessor {

	/**
	 * Attach a CompilePermsListener.
	 * 
	 * @param listener
	 *            Listener that will be notified of CompilePerms operation
	 *            finished.
	 */
	void addListener(CompilePermsListener listener);

	/**
	 * This method is fired whenever CompilePerms operation is finished.
	 */
	void compilePermsFinished();

	/**
	 * Detach a CompilePermsListener.
	 * 
	 * @param listener
	 *            Listener that will be detached and will no longer be notified
	 *            of CompilePerms operations finishing.
	 */
	void removeListener(CompilePermsListener listener);

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
	void storeInputStreamOnDisk(ByteArrayOutputStream receivedData)
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
