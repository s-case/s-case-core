package eu.scasefp7.eclipse.core.connect.handlers;

import java.util.List;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.handlers.HandlerUtil;

import eu.scasefp7.eclipse.core.connect.uploader.ProjectAwareHandler;
import eu.scasefp7.eclipse.core.connect.uploader.ProjectUploader;

/**
 * A command handler for uploading a project to the assets registry.
 * 
 * @author themis
 */
public class UploadProject extends ProjectAwareHandler {

	@Override
	@SuppressWarnings("unchecked")
	public Object execute(ExecutionEvent event) throws ExecutionException {
		ISelection selection = HandlerUtil.getCurrentSelection(event);
		if (selection instanceof IStructuredSelection) {
			IStructuredSelection structuredSelection = (IStructuredSelection) selection;
			List<Object> selectionList = structuredSelection.toList();
			IProject project = getProjectOfSelectionList(selectionList);
			Job job = new Job("Uploading S-CASE project") {
				@Override
				protected IStatus run(IProgressMonitor monitor) {
					ProjectUploader.uploadProject(project, monitor);
					return Status.OK_STATUS;
				}
			};
			job.setUser(true);
			job.schedule();
		}
		return null;
	}
}
