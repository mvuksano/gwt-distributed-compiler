package com.google.gwt.dist.linker;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.ext.TreeLogger;
import com.google.gwt.core.ext.UnableToCompleteException;
import com.google.gwt.core.ext.TreeLogger.Type;
import com.google.gwt.dev.Link.LinkOptions;

/**
 * Linker implementation. Uses default gwt Linker which uses data which was
 * delivered by CompilePerms.
 */
public class Link {
	
	private LinkOptions options;
	
	public Link(LinkOptions options) {
		this.options = options;
	}
	
	public void run(TreeLogger logger) {
		List<String> moduleNames = new ArrayList<String>();
		moduleNames.add("com.hypersimple.HyperSimple");
		File workDir = new File("work");
		File warDir = new File("www");
		File extraDir = new File("extra");

		options.setModuleNames(moduleNames);
		options.setWorkDir(workDir);
		options.setWarDir(warDir);
		options.setExtraDir(extraDir);
		
		try {
			new com.google.gwt.dev.Link(options).run(logger);
		} catch (UnableToCompleteException e) {
			logger.log(Type.ERROR, e.getMessage());
		}
	}

}
