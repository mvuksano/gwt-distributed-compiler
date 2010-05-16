package com.google.gwt.dist.compiler;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

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
	
	public void run(TreeLogger logger) {
		PrecompileOptions precompileOptions = new PrecompileOptionsImpl();

		try {
			((PrintWriterTreeLogger) logger).setMaxDetail(TreeLogger.WARN);
			List<String> moduleNames = new ArrayList<String>();
			moduleNames.add("com.hypersimple.HyperSimple");
			File workDir = new File("work");

			precompileOptions.setModuleNames(moduleNames);
			precompileOptions.setWorkDir(workDir);
			precompileOptions.setOptimizePrecompile(false);
			precompileOptions.setOutput(JsOutputOption.OBFUSCATED);

			com.google.gwt.dev.Precompile precompile = new com.google.gwt.dev.Precompile(precompileOptions);
			precompile.run(logger);

		} catch (UnableToCompleteException e) {
			logger.log(TreeLogger.ERROR, e.getMessage());
		}
	}
}
