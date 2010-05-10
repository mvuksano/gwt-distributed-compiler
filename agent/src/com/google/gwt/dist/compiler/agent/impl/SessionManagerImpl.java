package com.google.gwt.dist.compiler.agent.impl;

import com.google.gwt.dist.ProcessingState;
import com.google.gwt.dist.compiler.agent.SessionManager;
import com.google.gwt.dist.compiler.agent.communicator.Communicator;
import com.google.gwt.dist.compiler.agent.events.CompilePermsListener;

/**
 * SessionManager handles sessions towards a node, for this agent.
 */
public class SessionManagerImpl implements SessionManager, CompilePermsListener {

	private Communicator communicator;
	private ProcessingState processingState;

	public SessionManagerImpl() {
		processingState = ProcessingState.READY;
	}

	public Communicator getCommunicator() {
		return this.communicator;
	}

	public ProcessingState getProcessingState() {
		return this.processingState;
	}

	public void onCompilePermsFinished() {
		synchronized (this) {
			this.processingState = ProcessingState.COMPLETED;
		}
	}

	public void setProcessingState(ProcessingState processingState) {
		this.processingState = processingState;
	}

	public void setCommunicator(Communicator communicator) {
		this.communicator = communicator;
	}

	public void startListening() {
		communicator.startServer();
	}

	public void stopListening() {
		communicator.stopServer();
	}
}