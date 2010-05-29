package com.google.gwt.dist.compiler.agent.impl;

import com.google.gwt.dev.CompilePerms.CompilePermsOptions;
import com.google.gwt.dist.util.options.CompileTaskOptionsImpl;

/**
 * Concrete class to implement compiler perm options.
 */
@SuppressWarnings("serial")
public class CompilePermsOptionsImpl extends CompileTaskOptionsImpl implements
    CompilePermsOptions {

  private int localWorkers;
  private int[] permsToCompile;

  public CompilePermsOptionsImpl() {
  }

  public CompilePermsOptionsImpl(CompilePermsOptions other) {
    copyFrom(other);
  }

  public void copyFrom(CompilePermsOptions other) {
    super.copyFrom(other);
    setPermsToCompile(other.getPermsToCompile());
    setLocalWorkers(other.getLocalWorkers());
  }

  public int getLocalWorkers() {
    return localWorkers;
  }

  public int[] getPermsToCompile() {
    return (permsToCompile == null) ? null : permsToCompile.clone();
  }

  public void setLocalWorkers(int localWorkers) {
    this.localWorkers = localWorkers;
  }

  public void setPermsToCompile(int[] permsToCompile) {
    this.permsToCompile = (permsToCompile == null) ? null
        : permsToCompile.clone();
  }
}
