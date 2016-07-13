package eu.scasefp7.eclipse.core.connect.handlers;

import java.util.List;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IProject;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.handlers.HandlerUtil;

import eu.scasefp7.eclipse.core.connect.uploader.ProjectAwareHandler;
import eu.scasefp7.eclipse.core.connect.uploader.ProjectUploader;

public class UploadProject extends ProjectAwareHandler {

	@Override
	@SuppressWarnings("unchecked")
	public Object execute(ExecutionEvent event) throws ExecutionException {
		ISelection selection = HandlerUtil.getCurrentSelection(event);
		if (selection instanceof IStructuredSelection) {
			IStructuredSelection structuredSelection = (IStructuredSelection) selection;
			List<Object> selectionList = structuredSelection.toList();
			IProject project = getProjectOfSelectionList(selectionList);
			ProjectUploader.uploadProject(project);
		}
		return null;
	}
}
