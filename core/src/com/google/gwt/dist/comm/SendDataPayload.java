package com.google.gwt.dist.comm;

import java.util.List;

public class SendDataPayload implements CommMessageResponse {

	private List<String> moduleNames;
	private byte[] data;
	
	private static final long serialVersionUID = -7026608948600915110L;
	
	public byte[] getPayload() {
		return this.data;
	}
	public List<String> getModuleNames() {
		return this.moduleNames;
	}
	
	public void setPayload(byte[] data) {
		this.data = data;
	}
	
	public void setModuleNames(List<String> moduleNames) {
		this.moduleNames = moduleNames;
	}
	
}
