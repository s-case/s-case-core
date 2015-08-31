package eu.scasefp7.eclipse.core.handlers;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
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
import eu.scasefp7.eclipse.core.ontologytoyamltools.Property;
import eu.scasefp7.eclipse.core.ontologytoyamltools.Resource;
import eu.scasefp7.eclipse.core.ontologytoyamltools.Stemmer;
import eu.scasefp7.eclipse.core.ontologytoyamltools.VerbTypeFinder;

/**
 * Class used to read data from the linked ontology and create a CIM in YAML format.
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

			// Verb type finder determining whether a verb is CRUD
			VerbTypeFinder verbTypeFinder = new VerbTypeFinder();

			// Iterate over all resources
			ArrayList<Resource> resources = new ArrayList<Resource>();
			for (String resourceName : linkedOntology.getResources()) {

				Resource resource = new Resource(Stemmer.stem(resourceName));
				if (!resources.contains(resource)) {

					// Iterate over each activity of this resource
					for (String activity : linkedOntology.getActivitiesOfResource(resourceName)) {
						String action = linkedOntology.getActionOfActivity(activity);
						String actiontype = linkedOntology.getActivityTypeOfActivity(activity);
						if (actiontype == null || actiontype.equals("Other")) {
							// Use automatic verb type finder
							String verbtype = verbTypeFinder.getVerbType(action);
							if (verbTypeFinder.getVerbType(action).equals("Other")) {
								// Verb is of type Other
								Resource algoresource = new Resource(Stemmer.stem(resourceName) + "_"
										+ Stemmer.stem(action), true);
								if (!resources.contains(algoresource)) {
									resource.addRelatedResource(Stemmer.stem(resourceName) + "_" + Stemmer.stem(action));
									resources.add(algoresource);
								}
							} else
								// Verb is CRUD
								resource.addCRUDActivity(verbtype);
						} else
							// Verb is CRUD
							resource.addCRUDActivity(actiontype);

						// Iterate over next activities
						for (String next_activity : linkedOntology.getNextActivitiesOfActivity(activity)) {
							String relatedResource = linkedOntology.getResourceOfActivity(next_activity);
							resource.addRelatedResource(Stemmer.stem(relatedResource));
						}
					}

					// Iterate over each property of this resource
					for (String property : linkedOntology.getPropertiesOfResource(resourceName)) {
						resource.addProperty(new Property(Stemmer.stem(property)));
					}

					resources.add(resource);
				}
			}

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
		return null;
	}

}
