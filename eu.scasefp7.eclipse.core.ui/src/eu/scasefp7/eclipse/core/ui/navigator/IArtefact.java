package eu.scasefp7.eclipse.core.ui.navigator;

/**
 * Interface representing a project artifact.
 * 
 * @author Leonora Gaspar
 * @author Marin Orlic
 */
public abstract interface IArtefact {

    /**
     * Get artifact name.
     * 
     * @return name of the artifact
     */
    public abstract String getName();

    /**
     * Get artifact type.
     * 
     * @return type of the artifact
     */
    public abstract String getType();

    /**
     * Get the parent group of the artifact.
     * 
     * @return artifact group that contains the artifact
     */
    public abstract ArtefactGroup getParent();

    /**
     * Get artifact contents (children).
     * 
     * @return array of children
     */
    public abstract Object[] getChildren();
}
