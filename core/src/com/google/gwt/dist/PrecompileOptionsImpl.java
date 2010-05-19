package com.google.gwt.dist;

import java.io.File;
import java.io.Serializable;

import com.google.gwt.dev.Precompile.PrecompileOptions;
import com.google.gwt.dev.jjs.JJSOptionsImpl;
import com.google.gwt.dev.jjs.JsOutputOption;
import com.google.gwt.dist.compiler.impl.CompileTaskOptionsImpl;

@SuppressWarnings("serial")
public class PrecompileOptionsImpl extends CompileTaskOptionsImpl implements
		PrecompileOptions, Serializable {

	private boolean disableUpdateCheck;
	private File dumpFile;
	private boolean enableGeneratingOnShards = true;
	private File genDir;
	private final JJSOptionsImpl jjsOptions = new JJSOptionsImpl();
	private int maxPermsPerPrecompile;
	private boolean validateOnly;

	public PrecompileOptionsImpl() {
	}

	public PrecompileOptionsImpl(PrecompileOptions other) {
		copyFrom(other);
	}

	public void copyFrom(PrecompileOptions other) {
		super.copyFrom(other);

		jjsOptions.copyFrom(other);

		setDisableUpdateCheck(other.isUpdateCheckDisabled());
		setDumpSignatureFile(other.getDumpSignatureFile());
		setGenDir(other.getGenDir());
		setMaxPermsPerPrecompile(other.getMaxPermsPerPrecompile());
		setValidateOnly(other.isValidateOnly());
		setEnabledGeneratingOnShards(other.isEnabledGeneratingOnShards());
	}

	public File getDumpSignatureFile() {
		return dumpFile;
	}

	public File getGenDir() {
		return genDir;
	}

	public int getMaxPermsPerPrecompile() {
		return maxPermsPerPrecompile;
	}

	public JsOutputOption getOutput() {
		return jjsOptions.getOutput();
	}

	public boolean isAggressivelyOptimize() {
		return jjsOptions.isAggressivelyOptimize();
	}

	public boolean isCastCheckingDisabled() {
		return jjsOptions.isCastCheckingDisabled();
	}

	public boolean isClassMetadataDisabled() {
		return jjsOptions.isClassMetadataDisabled();
	}

	public boolean isDraftCompile() {
		return jjsOptions.isDraftCompile();
	}

	public boolean isEnableAssertions() {
		return jjsOptions.isEnableAssertions();
	}

	public boolean isEnabledGeneratingOnShards() {
		return enableGeneratingOnShards;
	}

	public boolean isOptimizePrecompile() {
		return jjsOptions.isOptimizePrecompile();
	}

	public boolean isRunAsyncEnabled() {
		return jjsOptions.isRunAsyncEnabled();
	}

	public boolean isSoycEnabled() {
		return jjsOptions.isSoycEnabled();
	}

	public boolean isSoycExtra() {
		return jjsOptions.isSoycExtra();
	}

	public boolean isUpdateCheckDisabled() {
		return disableUpdateCheck;
	}

	public boolean isValidateOnly() {
		return validateOnly;
	}

	public void setAggressivelyOptimize(boolean aggressivelyOptimize) {
		jjsOptions.setAggressivelyOptimize(aggressivelyOptimize);
	}

	public void setCastCheckingDisabled(boolean disabled) {
		jjsOptions.setCastCheckingDisabled(disabled);
	}

	public void setClassMetadataDisabled(boolean disabled) {
		jjsOptions.setClassMetadataDisabled(disabled);
	}

	public void setDisableUpdateCheck(boolean disabled) {
		disableUpdateCheck = disabled;
	}

	public void setDraftCompile(boolean draft) {
		jjsOptions.setDraftCompile(draft);
	}

	public void setDumpSignatureFile(File dumpFile) {
		this.dumpFile = dumpFile;
	}

	public void setEnableAssertions(boolean enableAssertions) {
		jjsOptions.setEnableAssertions(enableAssertions);
	}

	public void setEnabledGeneratingOnShards(boolean enabled) {
		enableGeneratingOnShards = enabled;
	}

	public void setGenDir(File genDir) {
		this.genDir = genDir;
	}

	public void setMaxPermsPerPrecompile(int maxPermsPerPrecompile) {
		this.maxPermsPerPrecompile = maxPermsPerPrecompile;
	}

	public void setOptimizePrecompile(boolean optimize) {
		jjsOptions.setOptimizePrecompile(optimize);
	}

	public void setOutput(JsOutputOption output) {
		jjsOptions.setOutput(output);
	}

	public void setRunAsyncEnabled(boolean enabled) {
		jjsOptions.setRunAsyncEnabled(enabled);
	}

	public void setSoycEnabled(boolean enabled) {
		jjsOptions.setSoycEnabled(enabled);
	}

	public void setSoycExtra(boolean soycExtra) {
		jjsOptions.setSoycExtra(soycExtra);
	}

	public void setValidateOnly(boolean validateOnly) {
		this.validateOnly = validateOnly;
	}

}

