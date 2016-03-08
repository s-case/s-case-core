package eu.scasefp7.eclipse.core.ui.navigator;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.QualifiedName;
import org.eclipse.jface.viewers.IDecoration;
import org.eclipse.jface.viewers.ILightweightLabelDecorator;
import org.eclipse.jface.viewers.LabelProvider;

import eu.scasefp7.eclipse.core.ui.Activator;
import eu.scasefp7.eclipse.core.ui.ScaseUiConstants;

/**
 * Decorates the project folders with S-CASE icon overlay.
 * 
 * @author Leonora Gaspar
 * @author Marin Orlic
 *
 */
public class ProjectFolderLabelProvider extends LabelProvider implements ILightweightLabelDecorator {
 
	@Override
	public void decorate(Object resource, IDecoration decoration) {
		if(resource instanceof IFolder){      
			IProject project = ((IFolder)resource).getProject();
			String modelsPath = "";
			String outputPath = "";
			String reqPath = "";
			String comPath = "";
			String resourcePath = ((IFolder) resource).getFullPath().toString();
			
			try {
				comPath = project.getPersistentProperty(new QualifiedName("", ScaseUiConstants.COMPOSITIONS_FOLDER));
				reqPath = project.getPersistentProperty(new QualifiedName("", ScaseUiConstants.REQUIREMENTS_FOLDER));
				modelsPath = project.getPersistentProperty(new QualifiedName("", ScaseUiConstants.MODELS_FOLDER));
				outputPath = project.getPersistentProperty(new QualifiedName("", ScaseUiConstants.OUTPUT_FOLDER));
			} catch (CoreException e) {
				Activator.log("Unable to get project properties.", e);
			}
			if(resourcePath.equals(comPath) || resourcePath.equals(reqPath) || resourcePath.equals(modelsPath) || resourcePath.equals(outputPath) ){
				decoration.addOverlay(Activator.getImageDescriptor("icons/obj16/s-case_8.png"), IDecoration.TOP_RIGHT);
			}
		}
		
	}

}