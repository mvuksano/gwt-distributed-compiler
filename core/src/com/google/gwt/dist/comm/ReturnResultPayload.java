package com.google.gwt.dist.comm;

public class ReturnResultPayload implements CommMessagePayload {

	private byte[] response;
	private String uuid;
	
	private static final long serialVersionUID = -5877376949168668383L;

	public byte[] getResponseValue() {
		return response;
	}
	
	public String getUUID() {
		return this.uuid;
	}
	
	public void setResponseValue(byte[] response) {
		this.response = response;
	}
	
	public void setUUID(String uuid) {
		this.uuid = uuid;
	}
}
