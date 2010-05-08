package com.google.gwt.dist;

import java.io.Serializable;

/**
 * The definition for object that is used by client and agent for
 * intercommunication.
 */
public interface CommMessage extends Serializable {
	
	void setResponse(byte[] os);
	
	byte[] getResponse();

}
