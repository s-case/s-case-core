package eu.scasefp7.eclipse.core.builder;

import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.Platform;

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
}

