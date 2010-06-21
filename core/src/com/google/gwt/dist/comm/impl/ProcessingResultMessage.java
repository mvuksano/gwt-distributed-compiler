package com.google.gwt.dist.comm.impl;

import com.google.gwt.dist.comm.CommMessage;
import com.google.gwt.dist.comm.ProcessingResultPayload;

/**
 * This message is used to notify agent that it should return Processing result
 * to the client. This message does not use response. Methods getResponse and
 * setResponse are present only to fulfill the interface requirements.
 */
public class ProcessingResultMessage implements
		CommMessage<ProcessingResultPayload> {

	ProcessingResultPayload response;
	
	/**
	 * Generated serial version UID.
	 */
	private static final long serialVersionUID = -5992457996880343173L;
	private CommMessageType type;

	public ProcessingResultMessage() {
		this.type = CommMessageType.RETURN_RESULT;
	}

	@Override
	public CommMessageType getCommMessageType() {
		return this.type;
	}

	@Override
	public ProcessingResultPayload getResponse() {
		return this.response;
	}

	@Override
	public void setResponse(ProcessingResultPayload response) {
		this.response = response;
	}

}
