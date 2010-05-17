package com.google.gwt.dist.compiler;

import com.google.gwt.core.ext.TreeLogger;
import com.google.gwt.core.ext.UnableToCompleteException;
import com.google.gwt.dev.Precompile.PrecompileOptions;
import com.google.gwt.dev.jjs.JsOutputOption;
import com.google.gwt.dev.util.log.PrintWriterTreeLogger;

/**
 * Compiler that will initiate GWT Java to JavaScript compilation process.
 * {@link http://code.google.com/p/google-web-toolkit/wiki/DistributedBuilds}
 */
public class Precompile {
	
	private PrecompileOptions options;
	
	public Precompile(PrecompileOptions options) {
		this.options = options;
	}
	
	public PrecompileOptions getPrecompileOptions() {
		return this.options;
	}
	
	public void run(TreeLogger logger) {

		try {
			((PrintWriterTreeLogger) logger).setMaxDetail(options.getLogLevel());
			options.setOptimizePrecompile(false);
			options.setOutput(JsOutputOption.OBFUSCATED);
			com.google.gwt.dev.Precompile precompile = new com.google.gwt.dev.Precompile(options);
			precompile.run(logger);
		} catch (UnableToCompleteException e) {
			logger.log(TreeLogger.ERROR, e.getMessage());
		}
	}
	
	public void setPrecompileOptions(PrecompileOptions options) {
		this.options = options;
	}
}
