package com.google.gwt.dist.comm;

public class ReturnResultResponse implements CommMessageResponse {

	private ProcessingStateResponse currentState;
	
	private static final long serialVersionUID = -5877376949168668383L;

	public ProcessingStateResponse getResponseValue() {
		return currentState;
	}
	
	public void setResponseValue(ProcessingStateResponse response) {
		this.currentState = response;
	}
}
