package eu.scasefp7.eclipse.core.ui.handlers;

import java.util.List;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.QualifiedName;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.handlers.HandlerUtil;

import eu.scasefp7.eclipse.core.builder.ProjectUtils;
import eu.scasefp7.eclipse.core.ui.Activator;
import eu.scasefp7.eclipse.core.ui.ScaseUiConstants;

public class ConvertToProjectFolderHandler extends AbstractHandler {
	
	String PARAM_TYPE = "eu.scasefp7.eclipse.core.ui.commands.convertToFolder.type";
	
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		String type = event.getParameter(PARAM_TYPE);
		ISelection selection = HandlerUtil.getCurrentSelection(event);
		
		if (selection instanceof IStructuredSelection) {
			IStructuredSelection structuredSelection = (IStructuredSelection) selection;
			@SuppressWarnings("unchecked")
            List<Object> selectionList = structuredSelection.toList();
	
			for (Object object : selectionList) {
				IFolder folder = (IFolder) Platform.getAdapterManager().getAdapter(object, IFolder.class);
				String path = folder.getFullPath().toPortableString();
				IProject project = folder.getProject();
				
				if(type.equals("output")) {
				    ProjectUtils.setProjectOutputPath(project, path);
				} else if(type.equals("requirements")) {
				    ProjectUtils.setProjectRequirementsPath(project, path);
				} else if(type.equals("compositions")) {
				    ProjectUtils.setProjectCompositionsPath(project, path);
				} else if(type.equals("models")) {
				    ProjectUtils.setProjectModelsPath(project, path);
				}
			}
		}
		return null;
	}

	

}
