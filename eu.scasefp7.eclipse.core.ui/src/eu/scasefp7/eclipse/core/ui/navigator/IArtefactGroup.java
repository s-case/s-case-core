package eu.scasefp7.eclipse.core.ui.navigator;

import org.eclipse.core.resources.IProject;

/**
 * Interface representing a group of artifacts in project.
 * 
 * @author Leonora Gaspar
 * @author Marin Orlic
 */
public abstract interface IArtefactGroup {
    
    /** 
     * Get group name. 
     * @return name of the group 
     */
    public abstract String getName();
    
    /** 
     * Get group type. 
     * @return type of the group
     */
    public abstract String getType();
    
    /** 
     * Get the parent project of the group. 
     * @return project that contains the group
     */
    public abstract IProject getParent();
    
    /** 
     * Get group contents (children). 
     * @return array of children
     */
    public abstract Object[] getChildren();
      
    /** 
     * Set group contents (children). 
     * @param children array to set
     */
    public abstract void setChildren(Object[] children);
}
