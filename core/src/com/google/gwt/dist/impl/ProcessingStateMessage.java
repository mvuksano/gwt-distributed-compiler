package com.google.gwt.dist.impl;

import com.google.gwt.dist.comm.CommMessage;
import com.google.gwt.dist.comm.ProcessingStateResponse;

public class ProcessingStateMessage implements CommMessage<ProcessingStateResponse> {

	private static final long serialVersionUID = -6282502951222110241L;
	
	private CommMessageType commMessageType;
	private ProcessingStateResponse response;
	
	public ProcessingStateMessage() {
		commMessageType = CommMessageType.ECHO;
	}
	
	public ProcessingStateMessage(CommMessageType type) {
		commMessageType = type;
	}
	
	public ProcessingStateResponse getResponse() {
		return this.response;
	}
	
	public CommMessageType getCommMessageType() {
		return this.commMessageType;
	}
	
	public void setResponse(ProcessingStateResponse response) {
		this.response = response;
	}
}
