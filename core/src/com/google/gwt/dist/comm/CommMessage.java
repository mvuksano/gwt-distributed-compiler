package com.google.gwt.dist.comm;

import java.io.Serializable;

/**
 * The definition for object that is used by client and agent for
 * intercommunication.
 */
public interface CommMessage extends Serializable {

	/**
	 * CommMessage Types. ECHO - Message that should be returned as is. QUERY -
	 * Request current progress. RETURN_RESULT - Request result of the
	 * processing to be returned in the response.
	 */
	public enum CommMessageType {
		ECHO, QUERY, RETURN_RESULT
	}

	public CommMessageType getCommMessageType();

	public CommMessageResponse getResponse();

	public void setResponse(CommMessageResponse response);
}
