package com.google.gwt.dist.comm;

import java.io.Serializable;

/**
 * The definition for object that is used by client and agent for
 * intercommunication.
 */
public interface CommMessage<T extends CommMessagePayload> extends
		Serializable {

	/**
	 * CommMessage Types. ECHO - Message that should be returned as is.
	 * DELIVER_DATA is a message which contains data to be processed. QUERY -
	 * Request current progress. RETURN_RESULT - Request result of the
	 * processing to be returned in the response.
	 */
	public enum CommMessageType {
		ECHO, DELIVERY_DATA, QUERY, RETURN_RESULT
	}

	public CommMessageType getCommMessageType();

	public T getResponse();

	public void setResponse(T response);
}
