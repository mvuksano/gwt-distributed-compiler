package com.google.gwt.dist.comm;

import java.util.List;

import com.google.gwt.dev.CompilePerms.CompilePermsOptions;

public class SendDataPayload implements CommMessageResponse {

	private CompilePermsOptions options;
	private List<String> moduleNames;
	private byte[] data;

	private static final long serialVersionUID = -7026608948600915110L;

	public byte[] getPayload() {
		return this.data;
	}

	public List<String> getModuleNames() {
		return this.moduleNames;
	}

	public CompilePermsOptions getCompilerOptions() {
		return this.options;
	}

	public void setCompilerOptions(CompilePermsOptions options) {
		this.options = options;
	}

	public void setPayload(byte[] data) {
		this.data = data;
	}

	public void setModuleNames(List<String> moduleNames) {
		this.moduleNames = moduleNames;
	}

}
