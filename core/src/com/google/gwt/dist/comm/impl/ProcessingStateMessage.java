package com.google.gwt.dist.comm.impl;

import com.google.gwt.dist.comm.CommMessage;
import com.google.gwt.dist.comm.ProcessingStatePayload;

public class ProcessingStateMessage implements CommMessage<ProcessingStatePayload> {

	private static final long serialVersionUID = -6282502951222110241L;
	
	private CommMessageType commMessageType;
	private ProcessingStatePayload response;
	
	public ProcessingStateMessage() {
		commMessageType = CommMessageType.ECHO;
	}
	
	public ProcessingStateMessage(CommMessageType type) {
		commMessageType = type;
	}
	
	public ProcessingStatePayload getResponse() {
		return this.response;
	}
	
	public CommMessageType getCommMessageType() {
		return this.commMessageType;
	}
	
	public void setResponse(ProcessingStatePayload response) {
		this.response = response;
	}
}
