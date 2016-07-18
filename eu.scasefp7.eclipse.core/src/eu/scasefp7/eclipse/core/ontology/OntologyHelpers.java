package eu.scasefp7.eclipse.core.ontology;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.QualifiedName;

import eu.scasefp7.eclipse.core.Activator;
import eu.scasefp7.eclipse.core.ontology.OntologySource.OntologyType;

/**
 * Contains helper functions that check whether an ontology file exists.
 * 
 * @author themis
 */
public class OntologyHelpers {

    /** This class should not be instantiated. */
    private OntologyHelpers()
    {   
    }
    
	/**
	 * Checks whether the given project contains the ontology in the models folder location.
	 * 
	 * @param project the project to check for the ontology file.
	 * @param ontologyFilename the filename of the ontology to check if it exists.
	 * @return {@code true} if the ontology exists, or {@code false} otherwise.
	 */
	private static boolean projectHasOntology(IProject project, String ontologyFilename) {
		String modelsFolderLocation = null;
		try {
			modelsFolderLocation = project.getPersistentProperty(new QualifiedName("",
					"eu.scasefp7.eclipse.core.ui.modelsFolder"));
		} catch (CoreException e) {
			Activator.log("Error retrieving project property (models folder location)", e);
		}
		IContainer container = project;
		if (modelsFolderLocation != null) {
		    IResource modelsFolder = project.findMember(new Path(modelsFolderLocation)); 
			if (modelsFolder != null && modelsFolder.exists())
				container = (IContainer) modelsFolder;
		}
		return container.getFile(new Path(ontologyFilename)).exists();
	}

	/**
	 * Checks whether the given project contains the static ontology in the models folder location.
	 * 
	 * @param project the project to check for the ontology file.
	 * @return {@code true} if the ontology exists, or {@code false} otherwise.
	 */
	public static boolean projectHasStaticOntology(IProject project) {
		return projectHasOntology(project, OntologyJenaAPI.getFilenameForOntologyType(OntologyType.STATIC));
	}

	/**
	 * Checks whether the given project contains the dynamic ontology in the models folder location.
	 * 
	 * @param project the project to check for the ontology file.
	 * @return {@code true} if the ontology exists, or {@code false} otherwise.
	 */
	public static boolean projectHasDynamicOntology(IProject project) {
		return projectHasOntology(project, OntologyJenaAPI.getFilenameForOntologyType(OntologyType.DYNAMIC));
	}

	/**
	 * Checks whether the given project contains the linked ontology in the models folder location.
	 * 
	 * @param project the project to check for the ontology file.
	 * @return {@code true} if the ontology exists, or {@code false} otherwise.
	 */
	public static boolean projectHasLinkedOntology(IProject project) {
		return projectHasOntology(project, OntologyJenaAPI.getFilenameForOntologyType(OntologyType.LINKED));
	}
}
