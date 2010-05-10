package com.google.gwt.dist.impl;

import com.google.gwt.dist.comm.CommMessage;
import com.google.gwt.dist.comm.CommMessageResponse;

public class CommMessageImpl implements CommMessage {

	private static final long serialVersionUID = -6282502951222110241L;
	
	private CommMessageType commMessageType;
	private CommMessageResponse response;
	
	public CommMessageImpl() {
		commMessageType = CommMessageType.ECHO;
	}
	
	public CommMessageResponse getResponse() {
		return this.response;
	}
	
	public CommMessageType getCommMessageType() {
		return this.commMessageType;
	}
	
	public void setResponse(CommMessageResponse response) {
		this.response = response;
	}
}
