package eu.scasefp7.eclipse.core.ui.navigator;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.QualifiedName;
import org.eclipse.jface.viewers.IDecoration;
import org.eclipse.jface.viewers.ILightweightLabelDecorator;
import org.eclipse.jface.viewers.LabelProvider;

import eu.scasefp7.eclipse.core.builder.ProjectUtils;
import eu.scasefp7.eclipse.core.ui.Activator;
import eu.scasefp7.eclipse.core.ui.ScaseUiConstants;

/**
 * Decorates the project folders with S-CASE icon overlay.
 * 
 * @author Leonora Gaspar
 * @author Marin Orlic
 *
 */
public class ProjectFolderLabelDecorator extends LabelProvider implements ILightweightLabelDecorator {
 
	@Override
	public void decorate(Object resource, IDecoration decoration) {
		if(resource instanceof IFolder){      
			IProject project = ((IFolder)resource).getProject();
			String modelsPath = "";
			String outputPath = "";
			String reqPath = "";
			String comPath = "";
			String resourcePath = ((IFolder) resource).getProjectRelativePath().toString();
			
			comPath = ProjectUtils.getProjectCompositionsPath(project);
			reqPath = ProjectUtils.getProjectRequirementsPath(project); 
			modelsPath = ProjectUtils.getProjectModelsPath(project); 
			outputPath = ProjectUtils.getProjectOutputPath(project); 

			if(resourcePath.equals(comPath) || resourcePath.equals(reqPath) || resourcePath.equals(modelsPath) || resourcePath.equals(outputPath) ){
				decoration.addOverlay(Activator.getImageDescriptor("icons/obj16/s-case_8.png"), IDecoration.TOP_RIGHT);
			}
		}
	}
}