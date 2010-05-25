package com.google.gwt.dist.comm;

import com.google.gwt.dist.ProcessingState;

public class ProcessingStateResponse implements CommMessagePayload {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7207509640462004711L;
	private ProcessingState currentState;

	public ProcessingStateResponse() {
		this.currentState = ProcessingState.READY;
	}
	
	public ProcessingStateResponse(ProcessingState state) {
		this.currentState = state;
	}

	public ProcessingState getCurrentState() {
		return this.currentState;
	}

	public void setCurrentState(ProcessingState state) {
		this.currentState = state;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof ProcessingStateResponse) {
			if (((ProcessingStateResponse) obj).currentState == this.currentState)
				return true;
		}
		return false;
	}
}
