package eu.scasefp7.eclipse.core.ui.navigator;

public abstract interface IArtefact {
	public abstract String getName();

	public abstract String getType();

	public abstract ArtefactGroup getParent();

	public abstract Object[] getChildren();
}
