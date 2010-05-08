package com.google.gwt.dist.impl;

import com.google.gwt.dist.CommMessage;

public class CommMessageImpl implements CommMessage {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6282502951222110241L;
	private byte[] contents;
	
	@Override
	public byte[] getResponse() {
		return contents;
	}

	@Override
	public void setResponse(byte[] os) {
		contents = os;
		
	}

}
