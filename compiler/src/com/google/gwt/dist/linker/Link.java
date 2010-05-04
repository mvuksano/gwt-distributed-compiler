package com.google.gwt.dist.linker;

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

}
