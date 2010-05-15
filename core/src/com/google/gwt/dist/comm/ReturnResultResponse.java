package com.google.gwt.dist.comm;

public class ReturnResultResponse implements CommMessageResponse {

	private byte[] response;
	
	private static final long serialVersionUID = -5877376949168668383L;

	public byte[] getResponseValue() {
		return response;
	}
	
	public void setResponseValue(byte[] response) {
		this.response = response;
	}
}
