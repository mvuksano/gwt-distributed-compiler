package com.google.gwt.dist.compiler;

import java.io.File;

import com.google.gwt.dev.CompilerOptions;
import com.google.gwt.dist.link.LinkOptionsImpl;
import com.google.gwt.dist.precompile.PrecompileOptionsImpl;

public class CompilerOptionsImpl extends PrecompileOptionsImpl implements
		CompilerOptions {

	private LinkOptionsImpl linkOptions = new LinkOptionsImpl();
	private int localWorkers;

	private static final long serialVersionUID = -8157211518167034157L;
	
	public CompilerOptionsImpl() {
	}

	public CompilerOptionsImpl(CompilerOptions other) {
		copyFrom(other);
	}

	public void copyFrom(CompilerOptions other) {
		super.copyFrom(other);
		linkOptions.copyFrom(other);
		localWorkers = other.getLocalWorkers();
	}

	public File getExtraDir() {
		return linkOptions.getExtraDir();
	}

	public int getLocalWorkers() {
		return localWorkers;
	}

	@Deprecated
	public File getOutDir() {
		return linkOptions.getOutDir();
	}

	public File getWarDir() {
		return linkOptions.getWarDir();
	}

	public void setExtraDir(File extraDir) {
		linkOptions.setExtraDir(extraDir);
	}

	public void setLocalWorkers(int localWorkers) {
		this.localWorkers = localWorkers;
	}

	@Deprecated
	public void setOutDir(File outDir) {
		linkOptions.setOutDir(outDir);
	}

	public void setWarDir(File outDir) {
		linkOptions.setWarDir(outDir);
	}

}
