package com.google.gwt.dist.impl;

import com.google.gwt.dist.comm.CommMessage;
import com.google.gwt.dist.comm.SendDataPayload;

public class SendDataMessage implements CommMessage<SendDataPayload> {

	private SendDataPayload payload;
	private CommMessageType type;
	
	private static final long serialVersionUID = -981797211083780720L;
	
	public SendDataMessage() {
		this.type = CommMessageType.DELIVERY_DATA;
	}
	
	@Override
	public CommMessageType getCommMessageType() {
		return this.type;
	}

	@Override
	public SendDataPayload getResponse() {
		return this.payload;
	}

	@Override
	public void setResponse(SendDataPayload response) {
		this.payload = response;
		
	}

}