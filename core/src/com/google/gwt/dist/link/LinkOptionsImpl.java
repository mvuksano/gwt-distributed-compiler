package com.google.gwt.dist.link;

import java.io.File;
import java.io.Serializable;

import com.google.gwt.dev.Link.LinkOptions;
import com.google.gwt.dist.compiler.CompileTaskOptionsImpl;

/**
 * Options for Link.
 */
public class LinkOptionsImpl extends CompileTaskOptionsImpl implements
		LinkOptions, Serializable {

	private static final long serialVersionUID = -5855460043698548770L;
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
