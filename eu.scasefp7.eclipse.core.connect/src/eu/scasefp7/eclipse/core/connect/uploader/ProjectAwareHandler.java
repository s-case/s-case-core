package eu.scasefp7.eclipse.core.connect.uploader;

import java.util.List;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.Platform;

/**
 * Project aware handler for receiving the current project.
 * 
 * @author themis
 */
public abstract class ProjectAwareHandler extends AbstractHandler {

	/**
	 * Returns the project that the selected resource(s) belong to.
	 * 
	 * @param selectionList the selected resource(s).
	 * @return the project that the selected resource(s) belong to.
	 */
	protected IProject getProjectOfSelectionList(List<Object> selectionList) {
		IProject project = null;
		for (Object object : selectionList) {
			IResource resource = (IResource) Platform.getAdapterManager().getAdapter(object, IResource.class);
			IProject theproject = null;
			if (resource != null) {
				theproject = resource.getProject();
			} else {
				theproject = (IProject) Platform.getAdapterManager().getAdapter(object, IProject.class);
			}
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
}
