package com.google.gwt.dist.util.arg;

import com.google.gwt.dist.util.options.OptionUUID;
import com.google.gwt.util.tools.ArgHandler;

/**
 * Argument handler for uuid parameter.
 */
public class ArgHandlerUUIDFile extends ArgHandler {

	private final OptionUUID option;

	public ArgHandlerUUIDFile(OptionUUID option) {
		this.option = option;
	}

	@Override
	public String getPurpose() {
		return "Specify a unique identifier that will be used by the remote agents.";
	}

	@Override
	public String getTag() {
		return "-uuid";
	}

	@Override
	public String[] getTagArgs() {
		return new String[] { "uuid" };
	}

	@Override
	public int handle(String[] args, int startIndex) {
		option.setUUID(args[startIndex + 1]);
		return 1;
	}
	
	  @Override
	  public boolean isRequired() {
	    return true;
	  }

}
