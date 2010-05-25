package com.google.gwt.dist.comm;

import com.google.gwt.dev.CompilePerms.CompilePermsOptions;

@SuppressWarnings("serial")
public class SendDataPayload implements CommMessagePayload {

	private byte[] data;
	private CompilePermsOptions options;
	private String uuid;

	public CompilePermsOptions getCompilePermsOptions() {
		return this.options;
	}

	public byte[] getPayload() {
		return this.data;
	}
	
	public String getUUID() {
		return this.uuid;
	}

	public void setCompilePermsOptions(CompilePermsOptions options) {
		this.options = options;
	}

	public void setPayload(byte[] data) {
		this.data = data;
	}	
	
	public void setUUID(String uuid) {
		this.uuid = uuid;
	}
}
