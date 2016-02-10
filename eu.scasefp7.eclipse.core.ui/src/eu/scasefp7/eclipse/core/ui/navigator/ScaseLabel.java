package eu.scasefp7.eclipse.core.ui.navigator;

import java.net.URL;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.QualifiedName;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.IDecoration;
import org.eclipse.jface.viewers.ILightweightLabelDecorator;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.ui.internal.util.BundleUtility;
import org.osgi.framework.Bundle;

import eu.scasefp7.eclipse.core.ui.ScaseUiConstants;

public class ScaseLabel extends LabelProvider implements ILightweightLabelDecorator {

	@Override
	public void decorate(Object resource, IDecoration decoration) {
		if(resource instanceof IFolder){      
			IProject project = ((IFolder)resource).getProject();
			String modelsPath = "";
			String outputPath = "";
			String reqPath = "";
			String comPath = "";
			String resourcePath = ((IFolder) resource).getFullPath().toString();
			
			Bundle bundle = Platform.getBundle("eu.scasefp7.eclipse.core.ui");
			URL fullPathString = BundleUtility.find(bundle, "icons/s-case_8x8.png");
			
			try {
				comPath = project.getPersistentProperty(new QualifiedName("", ScaseUiConstants.COMPOSITIONS_FOLDER));
				reqPath = project.getPersistentProperty(new QualifiedName("", ScaseUiConstants.REQUIREMENTS_FOLDER));
				modelsPath = project.getPersistentProperty(new QualifiedName("", ScaseUiConstants.MODELS_FOLDER));
				outputPath = project.getPersistentProperty(new QualifiedName("", ScaseUiConstants.OUTPUT_FOLDER));
			} catch (CoreException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if(resourcePath.equals(comPath) || resourcePath.equals(reqPath) || resourcePath.equals(modelsPath) || resourcePath.equals(outputPath) ){
				decoration.addOverlay(ImageDescriptor.createFromURL(fullPathString), IDecoration.TOP_RIGHT);
				
			}
		}
		
	}


}
