package com.google.gwt.dist.linker;

import java.io.File;

import com.google.gwt.dev.Link.LinkOptions;
import com.google.gwt.dist.compiler.CompileTaskOptionsImpl;

/**
 * Options for Link.
 */
public class LinkOptionsImpl extends CompileTaskOptionsImpl implements
		LinkOptions {

	private File extraDir;
	private File outDir;
	private File warDir;

	public LinkOptionsImpl() {
	}

	public LinkOptionsImpl(LinkOptions other) {
		copyFrom(other);
	}

	public void copyFrom(LinkOptions other) {
		super.copyFrom(other);
		setExtraDir(other.getExtraDir());
		setWarDir(other.getWarDir());
		setOutDir(other.getOutDir());
	}

	public File getExtraDir() {
		return extraDir;
	}

	@Deprecated
	public File getOutDir() {
		return outDir;
	}

	public File getWarDir() {
		return warDir;
	}

	public void setExtraDir(File extraDir) {
		this.extraDir = extraDir;
	}

	@Deprecated
	public void setOutDir(File outDir) {
		this.outDir = outDir;
	}

	public void setWarDir(File warDir) {
		this.warDir = warDir;
	}
}
