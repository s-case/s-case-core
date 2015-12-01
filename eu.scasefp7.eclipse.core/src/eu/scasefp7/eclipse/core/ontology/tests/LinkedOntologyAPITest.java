package eu.scasefp7.eclipse.core.ontology.tests;

import eu.scasefp7.eclipse.core.handlers.LinkOntologiesHandler;
import eu.scasefp7.eclipse.core.ontology.DynamicOntologyAPI;
import eu.scasefp7.eclipse.core.ontology.LinkedOntologyAPI;
import eu.scasefp7.eclipse.core.ontology.StaticOntologyAPI;

/**
 * Links the static and the dynamic ontologies into one linked ontology.
 * 
 * @author themis
 */
public class LinkedOntologyAPITest {

	/**
	 * Links the static and the dynamic ontologies. The ontologies are read and combined in a new ontology.
	 * 
	 * @param args unused parameter.
	 */
	public static void main(String[] args) {
		String projectName = "Restmarks";

		// Load the two ontologies
		StaticOntologyAPI staticOntology = new StaticOntologyAPI(projectName, false);
		DynamicOntologyAPI dynamicOntology = new DynamicOntologyAPI(projectName, false);

		// Create a new file for the linked ontology and instantiate it
		LinkedOntologyAPI linkedOntology = new LinkedOntologyAPI(projectName);

		// Link the ontologies
		new LinkOntologiesHandler().linkOntologies(staticOntology, dynamicOntology, linkedOntology);

		// Close the linked ontology. The other two ontologies are not closed since they do not need to be saved.
		linkedOntology.close();
	}

}
