package eu.scasefp7.eclipse.core.ui.navigator;

import org.eclipse.core.resources.IProject;

public class ArtefactGroup implements IArtefactGroup {
	
	protected String name;
	protected String type;
	protected IProject parent;
	protected Object[] children;
	
	ArtefactGroup(String name, String type, IProject parent){
		this.name = name;
		this.type = type;
		this.parent = parent;
		this.children = null;
	}
	
	ArtefactGroup(String name, String type, IProject parent, Object[] children){
		this.name = name;
		this.type = type;
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
	public String getType() {
		return type;
	}

	@Override
	public void setChildren(Object[] children) {
		this.children = children;
	}

}
