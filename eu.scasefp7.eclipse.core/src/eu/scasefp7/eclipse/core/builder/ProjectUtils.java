package eu.scasefp7.eclipse.core.builder;

import java.util.List;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.QualifiedName;

import eu.scasefp7.eclipse.core.Activator;

/**
 * Utility methods for project handling.
 * 
 * @author Marin Orlic
 */
public class ProjectUtils 
{
    /** Path to project folders for requirements. */
    public static final String REQUIREMENTS_FOLDER = "eu.scasefp7.eclipse.core.ui.rqsFolder"; //NON-NLS-1$

    /** Path to project folders for requirements. */
    public static final String COMPOSITIONS_FOLDER = "eu.scasefp7.eclipse.core.ui.compFolder"; //$NON-NLS-1$

    /** Path to project folders for requirements. */
    public static final String MODELS_FOLDER = "eu.scasefp7.eclipse.core.ui.modelsFolder"; //$NON-NLS-1$
    
    /** Path to project folders for requirements. */
    public static final String OUTPUT_FOLDER = "eu.scasefp7.eclipse.core.ui.outputFolder"; //$NON-NLS-1$
    
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
     * Checks if the project exists, is opened and has the S-CASE nature configured.
     * 
     * @param project to check
     * @return true if both the project and the S-CASE nature are available
     */
    public static boolean hasNature(IProject project) {
        try {
            return project.exists() && project.isOpen() && project.hasNature(ScaseNature.NATURE_ID);
        } catch (CoreException e) {
            Activator.log("Unable to check if project nature is configured.", e);
        }
        return false;
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
    
    /**
     * Sets the project models folder property.
     * 
     * @param project to configure
     * @param path to set as models path
     */
    public static void setProjectModelsPath(IProject project, String path) {
        try {
            project.setPersistentProperty(new QualifiedName("", MODELS_FOLDER), path);
        } catch (CoreException e) {
            Activator.log("Unable to set project properties.", e);
        }
    }
    
    /**
     * Reads the project models folder property.
     * 
     * @param project to configure
     * @return path set as models path
     */
    public static String getProjectModelsPath(IProject project) {
        String path = "";
        try {
            path = project.getPersistentProperty(new QualifiedName("", MODELS_FOLDER));
        } catch (CoreException e) {
            Activator.log("Error retrieving project property", e);
        }
        return path;
    }
    
    /**
     * Gets the project models folder.
     * 
     * @param project
     * @return models folder
     */
    public static IContainer getProjectModelsFolder(IProject project) {
        String path = getProjectModelsPath(project);
        IContainer container = project;
        if (path != null) {
            IResource folder = project.findMember(new Path(path));
            if (folder != null && folder.exists())
                container = (IContainer) folder;
        }
        return container;
    }
    
    /**
     * Sets the project requirements folder property.
     * 
     * @param project to configure
     * @param path to set as requirements path
     */
    public static void setProjectRequirementsPath(IProject project, String path) {
        try {
            project.setPersistentProperty(new QualifiedName("", REQUIREMENTS_FOLDER), path);
        } catch (CoreException e) {
            Activator.log("Unable to set project properties.", e);
        }
    }
    
    /**
     * Reads the project requirements folder property.
     * 
     * @param project to configure
     * @return path set as requirements path
     */
    public static String getProjectRequirementsPath(IProject project) {
        String path = "";
        try {
            path = project.getPersistentProperty(new QualifiedName("", REQUIREMENTS_FOLDER));
        } catch (CoreException e) {
            Activator.log("Error retrieving project property", e);
        }
        return path;
    }
    
    /**
     * Gets the project requirements folder.
     * 
     * @param project
     * @return models folder
     */
    public static IContainer getProjectRequirementsFolder(IProject project) {
        String path = getProjectRequirementsPath(project);
        IContainer container = project;
        if (path != null) {
            IResource folder = project.findMember(new Path(path));
            if (folder != null && folder.exists())
                container = (IContainer) folder;
        }
        return container;
    }
    
    /**
     * Sets the project compositions folder property.
     * 
     * @param project to configure
     * @param path to set as compositions path
     */
    public static void setProjectCompositionsPath(IProject project, String path) {
        try {
            project.setPersistentProperty(new QualifiedName("", COMPOSITIONS_FOLDER), path);
        } catch (CoreException e) {
            Activator.log("Unable to set project properties.", e);
        }
    }
    
    /**
     * Reads the project compositions folder property.
     * 
     * @param project to configure
     * @return path set as compositions path
     */
    public static String getProjectCompositionsPath(IProject project) {
        String path = "";
        try {
            path = project.getPersistentProperty(new QualifiedName("", COMPOSITIONS_FOLDER));
        } catch (CoreException e) {
            Activator.log("Error retrieving project property", e);
        }
        return path;
    }
    
    /**
     * Sets the project output folder property.
     * 
     * @param project to configure
     * @param path to set as output path
     */
    public static void setProjectOutputPath(IProject project, String path) {
        try {
            project.setPersistentProperty(new QualifiedName("", OUTPUT_FOLDER), path);
        } catch (CoreException e) {
            Activator.log("Unable to set project properties.", e);
        }
    }
    
    /**
     * Reads the project output folder property.
     * 
     * @param project to configure
     * @return path set as ouput path
     */
    public static String getProjectOutputPath(IProject project) {
        String path = "";
        try {
            path = project.getPersistentProperty(new QualifiedName("", OUTPUT_FOLDER));
        } catch (CoreException e) {
            Activator.log("Error retrieving project property", e);
        }
        return path;
    }
}

