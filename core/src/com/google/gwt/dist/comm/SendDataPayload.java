package com.google.gwt.dist.comm;

import com.google.gwt.dev.CompilePerms.CompilePermsOptions;

@SuppressWarnings("serial")
public class SendDataPayload implements CommMessageResponse {

	private CompilePermsOptions options;
	private byte[] data;


	public byte[] getPayload() {
		return this.data;
	}

	public CompilePermsOptions getCompilePermsOptions() {
		return this.options;
	}

	public void setCompilePermsOptions(CompilePermsOptions options) {
		this.options = options;
	}

	public void setPayload(byte[] data) {
		this.data = data;
	}
	
	public void setOptions(CompilePermsOptions moduleNames) {
		this.options = moduleNames;
	}
	
}
