package eu.scasefp7.eclipse.core.ui.navigator;

import org.eclipse.core.resources.IProject;

public class ArtefactGroup implements IArtefactGroup {
	
	protected String name;
	protected IProject parent;
	protected Object[] children;
	
	ArtefactGroup(String name, IProject parent){
		this.name = name;
		this.parent = parent;
		this.children = null;
	}
	
	ArtefactGroup(String name, IProject parent, Object[] children){
		this.name = name;
		this.parent = parent;
		this.children = children;
	}

	@Override
	public IProject getParent() {
		return parent;
	}

	@Override
	public Object[] getChildren() {
		return children;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void setChildren(Object[] children) {
		this.children = children;
	}

}
