package eu.scasefp7.eclipse.core.ui.preferences;

import java.util.ArrayList;
import java.util.Arrays;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.QualifiedName;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.PropertyDescriptor;
import org.eclipse.ui.views.properties.ResourcePropertySource;

import eu.scasefp7.eclipse.core.ui.Activator;
import eu.scasefp7.eclipse.core.ui.ScaseUiConstants;
import eu.scasefp7.eclipse.core.ui.preferences.internal.DomainEntry;
import eu.scasefp7.eclipse.core.ui.preferences.internal.IProjectDomains;

/**
 * Provides project properties to properties view. 
 * 
 * @author Leonora Gaspar
 * @author Marin Orlic
 */
public class ProjectResourcePropertySource extends ResourcePropertySource {
    
    private IProject project = null;

    /**
     * Constructs the property source.
     * 
     * @param project to show the properties for.
     */
    public ProjectResourcePropertySource(IProject project) {
        super(project);
        this.project = project;
    }

    @Override
    public IPropertyDescriptor[] getPropertyDescriptors() {
        ArrayList<IPropertyDescriptor>  arrayList = new ArrayList<IPropertyDescriptor>();
        try {
    		if(project.hasNature(ScaseUiConstants.PROJECT_NATURE)) {
    			PropertyDescriptor pd1 =  new PropertyDescriptor(ScaseUiConstants.PROP_PROJECT_DOMAIN, "Project domain"); //$NON-NLS-1$
    			PropertyDescriptor pd2 =  new PropertyDescriptor(ScaseUiConstants.COMPOSITIONS_FOLDER, "Compositions folder"); //$NON-NLS-1$
    			PropertyDescriptor pd3 =  new PropertyDescriptor(ScaseUiConstants.MODELS_FOLDER, "Models folder"); //$NON-NLS-1$
    			PropertyDescriptor pd4 =  new PropertyDescriptor(ScaseUiConstants.OUTPUT_FOLDER, "Output folder"); //$NON-NLS-1$
    			PropertyDescriptor pd5 =  new PropertyDescriptor(ScaseUiConstants.REQUIREMENTS_FOLDER, "Requirements folder"); //$NON-NLS-1$
    			
    			pd1.setCategory("Domain"); //$NON-NLS-1$
    			pd2.setCategory("Folders"); //$NON-NLS-1$
    			pd3.setCategory("Folders"); //$NON-NLS-1$
    			pd4.setCategory("Folders"); //$NON-NLS-1$
    			pd5.setCategory("Folders"); //$NON-NLS-1$
    			IPropertyDescriptor[] array = {pd1,pd2,pd3,pd4,pd5};
    			arrayList.addAll(Arrays.asList(array));
    		}
    	} catch (CoreException e) {
    		Activator.log("Unable to create property descriptors.", e);
    	}
        
      return arrayList.toArray(new IPropertyDescriptor[0]);
    }

    @Override
    public Object getPropertyValue(Object id) {
    	if(project.isAccessible()) {
    	    try {
    	        String result = project.getPersistentProperty(new QualifiedName("",(String) id)); //$NON-NLS-1$
    	    
    	        if (id.equals(ScaseUiConstants.PROP_PROJECT_DOMAIN)) {
    				try {
    					if(result == null || result.equals("-1")) //$NON-NLS-1$
    						result = "(not set)";
    					else{
    						DomainEntry de = findDomainById(IProjectDomains.PROJECT_DOMAINS, Integer.parseInt(result));
    						result = de.getParent().getName() + "/" + de.getName();
    					}
    					
    				} catch (NumberFormatException e) {
    				    Activator.log("Unable to read project domain.", e);
    				}
    		    } 
    	        
    	        return result;
    		} catch (CoreException e) {
    		    Activator.log("Unable to read project properties.", e);
    		}   
    	 }

    	return super.getPropertyValue(id);	
    }
    
    DomainEntry findDomainById(DomainEntry[] domains, int domainId) {
        for (DomainEntry de : domains) {
            if (de.getId() == domainId) {
                return de;
            }
            if(de.hasChildren()) {
                for (DomainEntry child : de.getChildren()) {
                    if(child.getId() == domainId) {
                        return child;
                    }
                }
            }
        }
        return null;
    }
}