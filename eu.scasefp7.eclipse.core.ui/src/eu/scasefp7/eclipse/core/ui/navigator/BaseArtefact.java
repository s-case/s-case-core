package eu.scasefp7.eclipse.core.ui.navigator;

/**
 * Basic project artifact.
 * 
 * @author Leonora Gaspar
 * @author Marin Orlic
 */
public class BaseArtefact implements IArtefact {
    
    protected String name;
    protected String type;
    protected ArtefactGroup parent;

    /**
     * Constructs the object with given properties.
     * 
     * @param name of the artifact
     * @param type of the artifact
     * @param parent group of the artifact
     * @param data artefact data
     */
    public BaseArtefact(String name, String type, ArtefactGroup parent) {
        this.name = name;
        this.type = type;
        this.parent = parent;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }
    
    public ArtefactGroup getParent() {
        return parent;
    }

    public Object[] getChildren() {
        return null;
    }

    public String toString() {
        return "BaseArtefact [name=" + name + ", parent=" + parent + "]";
    }
}
