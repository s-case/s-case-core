package eu.scasefp7.eclipse.core.ui.navigator;

import org.eclipse.core.resources.IProject;

public abstract interface IArtefactGroup {
	  public abstract String getName();

	  public abstract IProject getParent();
	  
	  public abstract Object[] getChildren();
	  
	  public abstract void setChildren(Object[] child);
}
