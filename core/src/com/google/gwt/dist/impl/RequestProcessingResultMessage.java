package com.google.gwt.dist.impl;

import com.google.gwt.dist.comm.CommMessage;
import com.google.gwt.dist.comm.ReturnResultPayload;

/**
 * This message is used to notify agent that it should return Processing result
 * to the client. This message does not use response. Methods getResponse and
 * setResponse are present only to fulfill the interface requirements.
 */
public class RequestProcessingResultMessage implements
		CommMessage<ReturnResultPayload> {

	ReturnResultPayload response;
	
	/**
	 * Generated serial version UID.
	 */
	private static final long serialVersionUID = -5992457996880343173L;
	private CommMessageType type;

	public RequestProcessingResultMessage() {
		this.type = CommMessageType.RETURN_RESULT;
	}

	@Override
	public CommMessageType getCommMessageType() {
		return this.type;
	}

	@Override
	public ReturnResultPayload getResponse() {
		return this.response;
	}

	@Override
	public void setResponse(ReturnResultPayload response) {
		this.response = response;
	}

}
