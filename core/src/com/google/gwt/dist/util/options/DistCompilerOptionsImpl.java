package com.google.gwt.dist.util.options;

import java.io.File;
import java.util.List;

import com.google.gwt.dist.Node;

/**
 * Concrete class to implement Distributed compiler options.
 */
public class DistCompilerOptionsImpl extends CompilerOptionsImpl implements DistCompilerOptions, OptionClassPath, OptionNodes,
		OptionUUID {
	
	private File gwtClassPath;
	private List<Node> nodes;
	private String uuid;
	
	private static final long serialVersionUID = 1L;
	
	public DistCompilerOptionsImpl() {
	}
	
	public DistCompilerOptionsImpl(DistCompilerOptions other) {
		super.copyFrom(other);
	}

	@Override
	public List<Node> getNodes() {
		return this.nodes;
	}

	@Override
	public void setNodes(List<Node> nodes) {
		this.nodes = nodes;
	}

	@Override
	public String getUUID() {
		return this.uuid;
	}

	@Override
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
