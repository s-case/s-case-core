package eu.scasefp7.eclipse.core.handlers;

import java.util.List;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.Platform;

public abstract class ProjectAwareHandler extends AbstractHandler {

	/**
	 * Returns the project that the selected file(s) belong to.
	 * 
	 * @param selectionList the selected file(s).
	 * @return the project that the selected file(s) belong to.
	 */
	protected IProject getProjectOfSelectionList(List<Object> selectionList) {
		IProject project = null;
		for (Object object : selectionList) {
			IFile file = (IFile) Platform.getAdapterManager().getAdapter(object, IFile.class);
			IProject theproject = null;
			if (file != null) {
				theproject = file.getProject();
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
