package eu.scasefp7.eclipse.core.builder;

import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Platform;

import eu.scasefp7.eclipse.core.Activator;

/**
 * Utility methods for project handling.
 * 
 * @author Marin Orlic
 */
public class ProjectUtils 
{
    /**
	 * Returns the project that the selected resource(s) belong to.
	 * 
	 * @param selectionList the selected resource(s).
	 * @return the project that the selected resource(s) belong to.
	 */
	public static IProject getProjectOfSelectionList(List<Object> selectionList) {
		IProject project = null;
		for (Object object : selectionList) {
			IProject theproject = getProjectOfSelectionElement(object);
			if (theproject != null) {
				if (project == null) {
					project = theproject;
				} else {
					if (!project.equals(theproject)) {
						return null;
					}
				}
			}
		}
		return project;
	}

    /**
     * Returns the project that the selected resource belongs to.
     * 
     * @param element the selected resource.
     * @return the project that the selected resources belong to.
     */
    public static IProject getProjectOfSelectionElement(Object element) {
        IProject project = null;
        IResource resource = (IResource) Platform.getAdapterManager().getAdapter(element, IResource.class);
        if (resource != null) {
            project = resource.getProject();
        } else {
            project = (IProject) Platform.getAdapterManager().getAdapter(element, IProject.class);
        }
        return project;
    }
    
    /**
     * Adds the S-CASE nature to the project.
     * 
     * @param project to add the nature to
     * @param monitor progress monitor
     * @throws CoreException
     */
    public static void addNature(IProject project, IProgressMonitor monitor) throws CoreException {
        try {
            IProjectDescription description = project.getDescription();
            String[] natures = description.getNatureIds();
            String[] newNatures = new String[natures.length + 1];
            System.arraycopy(natures, 0, newNatures, 0, natures.length);
            newNatures[natures.length] = ScaseNature.NATURE_ID;
            description.setNatureIds(newNatures);
            project.setDescription(description, monitor);
        } catch (CoreException e) {
            Activator.log("Unable to add S-CASE project nature to project.", e);
        }
    }
}

