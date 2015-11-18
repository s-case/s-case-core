package eu.scasefp7.eclipse.core.ontology.tests;

import eu.scasefp7.eclipse.core.ontology.LinkedOntologyAPI;

/**
 * Tests the instantation of the linked ontology using external service resources.
 * 
 * @author themis
 */
public class LinkedOntologyExternalServiceAPITest {

	/**
	 * Tests the instantation of the linked ontology using external service resources.
	 * 
	 * @param args unused parameter.
	 */
	public static void main(String[] args) {
		String projectName = "Restmarks";

		// Create a new file for the linked ontology and instantiate it
		LinkedOntologyAPI linkedOntology = new LinkedOntologyAPI(projectName);

		// Add a new resource in the linked ontology
		linkedOntology.addResource("live", true);
		linkedOntology.connectProjectToElement("live");

		// Add a new operation for the resource
		linkedOntology.addOperationToResource("live", "get_live", "http://apilayer.net/api", "/live", "RESTful", "GET",
				"JSON");

		// Add the input parameters of the operation
		linkedOntology.addInputParameter("from", "Primitive", false, false, "string");
		linkedOntology.addInputParameter("subject", "Primitive", false, true, "string");
		linkedOntology.addInputParameter("to", "Primitive", false, false, "string");
		linkedOntology.addInputParameter("text", "Primitive", false, false, "string");
		linkedOntology.addInputParametersToOperation("get_live", "from", "subject", "to", "text");

		// Add the output parameters of the operation
		linkedOntology.addOutputParameter("privacy", "Primitive", "string");
		linkedOntology.addOutputParameter("USDEUR", "Primitive", "double");
		linkedOntology.addOutputParameter("USDGBP", "Primitive", "double");
		linkedOntology.addOutputParameter("quotes", "Object", "USDEUR", "USDGBP");
		linkedOntology.addOutputParameter("source", "Primitive", "string");
		linkedOntology.addOutputParameter("terms", "Primitive", "string");
		linkedOntology.addOutputParameter("timestamp", "Primitive", "integer");
		linkedOntology.addOutputParametersToOperation("get_live", "privacy", "quotes", "source", "terms", "timestamp");

		// Add a new resource in the linked ontology
		linkedOntology.addResource("messages", true);
		linkedOntology.connectProjectToElement("messages");

		// Add a new operation for the resource
		linkedOntology.addOperationToResource("messages", "post_messages", "https://api:{apikey}@api.mailgun.net/v3",
				"/{domain}/messages", "RESTful", "POST", "JSON");

		// Add the URI parameters of the operation
		linkedOntology.addURIParametersToOperation("post_messages", "apikey", "domain");

		// Add the query parameters of the operation
		linkedOntology.addInputParameter("apiKey", "Primitive", false, false, "string");
		linkedOntology.addQueryParametersToOperation("post_messages", "apiKey");

		// Close the linked ontology. The other two ontologies are not closed since they do not need to be saved.
		linkedOntology.close();
	}

}
