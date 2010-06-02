package com.google.gwt.dist.util.options;

import java.io.File;
import java.io.Serializable;


/**
 * Concrete class to implement compiler perm options.
 */
public class DistCompilePermsOptionsImpl extends CompileTaskOptionsImpl
		implements DistCompilePermsOptions, Serializable {

	private static final long serialVersionUID = -4757085237535925396L;
	private File gwtClassPath;
	private int localWorkers;
	private int[] permsToCompile;
	private String uuid;

	public DistCompilePermsOptionsImpl(DistCompilerOptions other) {
		super.copyFrom(other);
		setGwtClassPath(other.getGwtClassPath());
		setUUID(other.getUUID());
	}

	public DistCompilePermsOptionsImpl(DistCompilePermsOptions other) {
		copyFrom(other);
	}

	public void copyFrom(DistCompilePermsOptions other) {
		super.copyFrom(other);
		setPermsToCompile(other.getPermsToCompile());
		setLocalWorkers(other.getLocalWorkers());
		setUUID(other.getUUID());
	}

	public int getLocalWorkers() {
		return localWorkers;
	}

	public int[] getPermsToCompile() {
		return (permsToCompile == null) ? null : permsToCompile.clone();
	}

	public String getUUID() {
		return this.uuid;
	}

	public void setLocalWorkers(int localWorkers) {
		this.localWorkers = localWorkers;
	}

	public void setPermsToCompile(int[] permsToCompile) {
		this.permsToCompile = (permsToCompile == null) ? null : permsToCompile
				.clone();
	}

	public void setUUID(String uuid) {
		this.uuid = uuid;
	}

	@Override
	public File getGwtClassPath() {
		return this.gwtClassPath;
	}

	@Override
	public void setGwtClassPath(File classpath) {
		this.gwtClassPath = classpath;
	}
}
