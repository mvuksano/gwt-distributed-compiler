package com.google.gwt.dist.linker;

import java.io.File;

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
		try {
			options.setWarDir(new File("www")); // TODO: remove this hardcorded
												// stuff.
			new com.google.gwt.dev.Link(options).run(logger);
		} catch (UnableToCompleteException e) {
			e.printStackTrace();
			logger.log(Type.ERROR, e.getMessage());
		}
	}

}
