package eu.scasefp7.eclipse.core.handlers;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.handlers.HandlerUtil;

import eu.scasefp7.eclipse.core.ontology.LinkedOntologyAPI;
import eu.scasefp7.eclipse.core.ontologytoyamltools.Operation;
import eu.scasefp7.eclipse.core.ontologytoyamltools.Property;
import eu.scasefp7.eclipse.core.ontologytoyamltools.Resource;
import eu.scasefp7.eclipse.core.ontologytoyamltools.Resources;
import eu.scasefp7.eclipse.core.ontologytoyamltools.Stemmer;
import eu.scasefp7.eclipse.core.ontologytoyamltools.StringHelpers;
import eu.scasefp7.eclipse.core.ontologytoyamltools.VerbTypeFinder;

/**
 * Class used to read data from the linked ontology and create a CIM in YAML
 * format.
 * 
 * @author themis
 */
public class OntologyToYamlHandler extends ProjectAwareHandler {

	@SuppressWarnings("unchecked")
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		ISelection selection = HandlerUtil.getCurrentSelection(event);
		if (selection instanceof IStructuredSelection) {
			IStructuredSelection structuredSelection = (IStructuredSelection) selection;
			List<Object> selectionList = structuredSelection.toList();
			IProject project = getProjectOfSelectionList(selectionList);

			// Load the ontology
			LinkedOntologyAPI linkedOntology = new LinkedOntologyAPI(project);
			Resources resources = createResources(linkedOntology);
			writeYamlFile(project, resources);
		}
		return null;
	}

	/**
	 * Creates the resources for the YAML file given the linked ontology.
	 * 
	 * @param linkedOntology the linked ontology that contains all the elements.
	 * @return a list of resources.
	 */
	private Resources createResources(LinkedOntologyAPI linkedOntology) throws ExecutionException {
		// Verb type finder determining whether a verb is CRUD
		VerbTypeFinder verbTypeFinder = new VerbTypeFinder();

		// Iterate over all resources
		Resources resources = new Resources();
		for (String resourceName : linkedOntology.getResources()) {
			Resource resource = resources.getResourceByName(
					StringHelpers.underscoreToCamelCase(Stemmer.stemNounConstruct(resourceName)), false,
					linkedOntology.resourceIsExternalService(resourceName));

			// Iterate over each related resource of this resource
			for (String relatedResourceName : linkedOntology.getRelatedResourcesOfResource(resourceName)) {
				resource.addRelatedResource(StringHelpers.underscoreToCamelCase(Stemmer
						.stemNounConstruct(relatedResourceName)));
			}

			// Iterate over each activity of this resource
			for (String activity : linkedOntology.getActivitiesOfResource(resourceName)) {
				String action = linkedOntology.getActionOfActivity(activity);
				String actiontype = linkedOntology.getActivityTypeOfActivity(activity);
				if (actiontype == null || actiontype.equals("Other")) {
					// Use automatic verb type finder
					String verbtype = verbTypeFinder.getVerbType(action);
					if (verbTypeFinder.getVerbType(action).equals("Other")) {
						// Verb is of type Other
						String stemmedAction = Stemmer.stemVerb(action);
						String algorithmicResourceName = StringHelpers.underscoreToCamelCase(Stemmer
								.stemNounConstruct(resourceName))
								+ stemmedAction.substring(0, 1).toUpperCase()
								+ stemmedAction.substring(1);
						resources.addResourceIfItDoesNotExist(algorithmicResourceName, true);
						resource.addRelatedResource(algorithmicResourceName);
					} else
						// Verb is CRUD
						resource.addCRUDActivity(verbtype);
				} else
					// Verb is CRUD
					resource.addCRUDActivity(actiontype);

				// Iterate over next activities
				for (String next_activity : linkedOntology.getNextActivitiesOfActivity(activity)) {
					String relatedResource = linkedOntology.getResourceOfActivity(next_activity);
					resource.addRelatedResource(StringHelpers.underscoreToCamelCase(Stemmer
							.stemNounConstruct(relatedResource)));
				}
			}

			// Iterate over each property of this resource
			for (String property : linkedOntology.getPropertiesOfResource(resourceName)) {
				resource.addProperty(new Property(StringHelpers.underscoreToCamelCase(Stemmer
						.stemNounConstruct(property))));
			}

			// Add information about external web services
			if (linkedOntology.resourceIsExternalService(resourceName)) {

				// Get the operation of this resource
				String operationName = linkedOntology.getOperationOfResource(resourceName);
				Operation operation = new Operation(operationName, linkedOntology.getOperationElements(operationName));

				// Add query parameters
				for (String parameterName : linkedOntology.getQueryParametersOfOperation(operationName)) {
					LinkedList<String> nonPrimitiveProperties = operation.addQueryParameter(parameterName,
							linkedOntology.getQueryParameterElements(parameterName),
							linkedOntology.getParameterTypeElements(parameterName));
					while (!nonPrimitiveProperties.isEmpty()) {
						String nonPrimitiveProperty = nonPrimitiveProperties.remove();
						nonPrimitiveProperties.addAll(operation.addNonPrimitiveProperty(nonPrimitiveProperty,
								linkedOntology.getQueryParameterElements(nonPrimitiveProperty),
								linkedOntology.getParameterTypeElements(nonPrimitiveProperty)));
					}
				}

				// Add input parameters
				for (String parameterName : linkedOntology.getInputParametersOfOperation(operationName)) {
					LinkedList<String> nonPrimitiveProperties = operation.addInputProperty(parameterName,
							linkedOntology.getInputParameterElements(parameterName),
							linkedOntology.getParameterTypeElements(parameterName));
					while (!nonPrimitiveProperties.isEmpty()) {
						String nonPrimitiveProperty = nonPrimitiveProperties.remove();
						nonPrimitiveProperties.addAll(operation.addNonPrimitiveProperty(nonPrimitiveProperty,
								linkedOntology.getInputParameterElements(nonPrimitiveProperty),
								linkedOntology.getParameterTypeElements(nonPrimitiveProperty)));
					}
				}

				// Add output parameters
				for (String parameterName : linkedOntology.getOutputParametersOfOperation(operationName)) {
					LinkedList<String> nonPrimitiveProperties = operation.addOutputProperty(parameterName,
							linkedOntology.getOutputParameterElements(parameterName),
							linkedOntology.getParameterTypeElements(parameterName));
					while (!nonPrimitiveProperties.isEmpty()) {
						String nonPrimitiveProperty = nonPrimitiveProperties.remove();
						nonPrimitiveProperties.addAll(operation.addNonPrimitiveProperty(nonPrimitiveProperty,
								linkedOntology.getOutputParameterElements(nonPrimitiveProperty),
								linkedOntology.getParameterTypeElements(nonPrimitiveProperty)));
					}
				}

				// Add URI parameters
				for (String parameterName : linkedOntology.getURIParametersOfOperation(operationName)) {
					operation.addURIParameter(parameterName);
				}

				// Add the operation to the resource
				resource.addOperation(operation);
			}
		}
		return resources;
	}

	/**
	 * Writes the YAML file of the project given the list of its resources.
	 * 
	 * @param project the project in which the YAML file is written.
	 * @param resources the resources to be written in the YAML file.
	 */
	private void writeYamlFile(IProject project, Resources resources) throws ExecutionException {
		// Open a new YAML file in the project
		IFile file = project.getFile("service.yml");
		if (file.exists()) {
			try {
				file.delete(IResource.FORCE, null);
			} catch (CoreException e) {
				e.printStackTrace();
			}
		}

		// Write the resources to file
		String ymlContents = "";
		for (Resource resource : resources) {
			ymlContents += resource.toYAMLString() + "\n\n";
		}
		InputStream ymlStream = new ByteArrayInputStream(ymlContents.getBytes(StandardCharsets.UTF_8));
		try {
			file.create(ymlStream, IResource.FORCE, null);
		} catch (CoreException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Test function for creating the YAML file from the linked ontology.
	 * 
	 * @param args unused parameter.
	 */
	public static void main(String[] args) {
		LinkedOntologyAPI linkedOntology = new LinkedOntologyAPI("Restmarks", false);
		try {
			Resources resources = new OntologyToYamlHandler().createResources(linkedOntology);
			String ymlContents = "";
			for (Resource resource : resources) {
				ymlContents += resource.toYAMLString() + "\n\n";
			}
			PrintWriter out = new PrintWriter("Restmarks.yaml", StandardCharsets.UTF_8.name());
			out.print(ymlContents);
			out.close();
		} catch (ExecutionException | FileNotFoundException | UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

}
