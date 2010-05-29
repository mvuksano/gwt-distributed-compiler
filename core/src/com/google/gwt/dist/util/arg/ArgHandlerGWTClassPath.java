package com.google.gwt.dist.util.arg;

import java.io.File;

import com.google.gwt.dist.util.options.OptionClassPath;
import com.google.gwt.util.tools.ArgHandlerDir;

public class ArgHandlerGWTClassPath extends ArgHandlerDir {

	private final OptionClassPath option;
	
	public ArgHandlerGWTClassPath(OptionClassPath option) {
		this.option = option;
	}
	
	@Override
	public String getPurpose() {
		return "Folders that contain files being used by GWT client side.";
	}

	@Override
	public String getTag() {
		return "-gwtClassPath";
	}

	@Override
	public void setDir(File dir) {
		option.setGwtClassPath(dir);
	}
	
	  @Override
	  public boolean isRequired() {
	    return true;
	  }

}
