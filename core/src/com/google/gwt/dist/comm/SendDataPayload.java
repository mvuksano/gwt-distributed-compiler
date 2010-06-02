package com.google.gwt.dist.comm;

import com.google.gwt.dist.util.options.DistCompilePermsOptions;

@SuppressWarnings("serial")
public class SendDataPayload implements CommMessagePayload {

	private byte[] data;
	private DistCompilePermsOptions options;
	private String uuid;

	public DistCompilePermsOptions getCompilePermsOptions() {
		return this.options;
	}

	public byte[] getPayload() {
		return this.data;
	}
	
	public String getUUID() {
		return this.uuid;
	}

	public void setCompilePermsOptions(DistCompilePermsOptions options) {
		this.options = options;
	}

	public void setPayload(byte[] data) {
		this.data = data;
	}	
	
	public void setUUID(String uuid) {
		this.uuid = uuid;
	}
}
