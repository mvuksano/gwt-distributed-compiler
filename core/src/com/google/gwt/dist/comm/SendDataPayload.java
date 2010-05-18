package com.google.gwt.dist.comm;

import com.google.gwt.dev.CompilePerms.CompilePermsOptions;

public class SendDataPayload implements CommMessageResponse {

	private CompilePermsOptions options;
	private byte[] data;
	
	private static final long serialVersionUID = -7026608948600915110L;
	
	public byte[] getPayload() {
		return this.data;
	}
	public CompilePermsOptions getOptions() {
		return this.options;
	}
	
	public void setPayload(byte[] data) {
		this.data = data;
	}
	
	public void setOptions(CompilePermsOptions moduleNames) {
		this.options = moduleNames;
	}
	
}
