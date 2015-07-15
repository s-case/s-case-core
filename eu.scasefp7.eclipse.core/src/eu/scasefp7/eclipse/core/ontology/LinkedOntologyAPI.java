package eu.scasefp7.eclipse.core.ontology;

import java.util.ArrayList;

import org.eclipse.core.resources.IProject;

import eu.scasefp7.eclipse.core.ontology.OntologySource.OntologyType;
import eu.scasefp7.eclipse.core.ontology.OntologyJenaAPI;

/**
 * Provides an API for the linked ontology in OWL format. Allows adding/deleting instances and properties.
 * 
 * @author themis
 */
public class LinkedOntologyAPI {

	/** The API for the linked ontology. */
	private OntologyJenaAPI linkedOntology;

	/** The project to connect to in the linked ontology. */
	private String projectName;

	/**
	 * Initializes the connection of this API with the ontology. Upon calling this function, the ontology is loaded in
	 * memory. <u><b>NOTE</b></u> that you have to call {@link #close()} in order to save your changes to disk.
	 * 
	 * @param project the project to connect to in the linked ontology.
	 * @param forceDelete boolean denoting whether any existing ontology file should be deleted.
	 */
	public LinkedOntologyAPI(IProject project, boolean forceDelete) {
		linkedOntology = new OntologyJenaAPI(project, OntologyType.LINKED,
				"http://www.owl-ontologies.com/Ontology1273059028.owl", forceDelete);
		this.projectName = project.getName();
		linkedOntology.addIndividual("Project", projectName);
	}

	/**
	 * Initializes the connection of this API with the ontology. Upon calling this function, the ontology is loaded in
	 * memory. <u><b>NOTE</b></u> that you have to call {@link #close()} in order to save your changes to disk.
	 * 
	 * @param project the project to connect to in the linked ontology.
	 */
	public LinkedOntologyAPI(IProject project) {
		linkedOntology = new OntologyJenaAPI(project, OntologyType.LINKED,
				"http://www.owl-ontologies.com/Ontology1273059028.owl");
		this.projectName = project.getName();
		linkedOntology.addIndividual("Project", projectName);
	}

	/**
	 * Similar to the other constructors, used only for testing reasons.
	 * 
	 * @param projectName the name of the project.
	 */
	public LinkedOntologyAPI(String projectName) {
		linkedOntology = new OntologyJenaAPI(null, OntologyType.STATIC,
				"http://www.owl-ontologies.com/Ontology1273059028.owl", true);
		this.projectName = projectName;
		linkedOntology.addIndividual("Project", projectName);
	}

	/**
	 * Connects the project to an element of the ontology.
	 * 
	 * @param elementName the element to be connected to the project.
	 */
	public void connectProjectToElement(String elementName) {
		linkedOntology.addPropertyAndReverseBetweenIndividuals(projectName, "has_element", elementName);
	}

	/**
	 * Adds a requirement in the ontology and connects it to the project.
	 * 
	 * @param requirementName the requirement to be added.
	 */
	public void addRequirement(String requirementName) {
		linkedOntology.addIndividual("Requirement", requirementName);
		linkedOntology.addPropertyAndReverseBetweenIndividuals(projectName, "has_requirement", requirementName);
	}

	/**
	 * Connects a requirement to an element of the ontology.
	 * 
	 * @param requirementName the requirement to be connected.
	 * @param elementName the element to be connected to the requirement.
	 */
	public void connectRequirementToElement(String requirementName, String elementName) {
		linkedOntology.addPropertyAndReverseBetweenIndividuals(requirementName, "contains_element", elementName);
		linkedOntology.addPropertyAndReverseBetweenIndividuals(projectName, "has_element", elementName);
	}

	/**
	 * Adds an activity diagram in the ontology and connects it to the project.
	 * 
	 * @param activityDiagramName the requirement to be added.
	 */
	public void addActivityDiagram(String activityDiagramName) {
		linkedOntology.addIndividual("ActivityDiagram", activityDiagramName);
		linkedOntology
				.addPropertyAndReverseBetweenIndividuals(projectName, "has_activity_diagram", activityDiagramName);
	}

	/**
	 * Connects an activity diagram to an element of the ontology.
	 * 
	 * @param activityDiagramName the activity diagram to be connected.
	 * @param elementName the element to be connected to the activity diagram.
	 */
	public void connectActivityDiagramToElement(String activityDiagramName, String elementName) {
		linkedOntology.addPropertyAndReverseBetweenIndividuals(activityDiagramName, "contains_element", elementName);
		linkedOntology.addPropertyAndReverseBetweenIndividuals(projectName, "has_element", elementName);
	}

	/**
	 * Adds a resource to the ontology.
	 * 
	 * @param resourceName the name of the resource to be added.
	 */
	public void addResource(String resourceName) {
		linkedOntology.addIndividual("Resource", resourceName);
	}

	/**
	 * Adds a property to a specific resource of the ontology.
	 * 
	 * @param resourceName the name of the resource to connect the property to.
	 * @param propertyName the name of the property to be added.
	 */
	public void addPropertyToResource(String resourceName, String propertyName) {
		linkedOntology.addIndividual("Property", propertyName);
		linkedOntology.addPropertyAndReverseBetweenIndividuals(resourceName, "has_property", propertyName);
	}

	/**
	 * Adds an activity to a specific resource of the ontology. Sets the activity type of the activity to "Other".
	 * 
	 * @param resourceName the name of the resource to connect the activity to.
	 * @param activityName the name of the activity to be added.
	 */
	public void addActivityToResource(String resourceName, String activityName) {
		linkedOntology.addIndividual("Activity", activityName);
		linkedOntology.addPropertyAndReverseBetweenIndividuals(resourceName, "has_activity", activityName);
		linkedOntology.addPropertyToIndividual(activityName, "activitytype", "Other");
	}

	/**
	 * Adds an activity to a specific resource of the ontology. This function is overloaed in order to account for the
	 * type of the activity.
	 * 
	 * @param resourceName the name of the resource to connect the activity to.
	 * @param activityName the name of the activity to be added.
	 * @param activitytype the type of the newly added activity.
	 */
	public void addActivityToResource(String resourceName, String activityName, String activitytype) {
		linkedOntology.addIndividual("Activity", activityName);
		linkedOntology.addPropertyAndReverseBetweenIndividuals(resourceName, "has_activity", activityName);
		linkedOntology.addPropertyToIndividual(activityName, "activitytype", activitytype);
	}

	/**
	 * Adds an action to a specific activity of the ontology.
	 * 
	 * @param activityName the name of the activity to connect the action to.
	 * @param actionName the name of the action to be added.
	 */
	public void addActionToActivity(String activityName, String actionName) {
		linkedOntology.addIndividual("Action", actionName);
		linkedOntology.addPropertyAndReverseBetweenIndividuals(activityName, "has_action", actionName);
	}

	/**
	 * Adds a condition to a specific activity of the ontology.
	 * 
	 * @param activityName the name of the activity to connect the action to.
	 * @param conditionName the name of the condition to be added.
	 */
	public void addConditionToActivity(String activityName, String conditionName) {
		linkedOntology.addIndividual("Condition", conditionName);
		linkedOntology.addPropertyAndReverseBetweenIndividuals(activityName, "has_condition", conditionName);
	}

	/**
	 * Adds a forthcoming activity to a specific activity of the ontology.
	 * 
	 * @param activityName the name of the activity to connect the next activity to.
	 * @param nextActivityName the name of the next activity to be added.
	 */
	public void addNextActivityToActivity(String activityName, String nextActivityName) {
		linkedOntology.addPropertyAndReverseBetweenIndividuals(activityName, "has_next_activity", nextActivityName);
	}

	/**
	 * Returns the resources of the ontology for the current project.
	 * 
	 * @return an {@link ArrayList} containing the names of the resources.
	 */
	public ArrayList<String> getResources() {
		return linkedOntology.getIndividualsOfClass("Resource");
	}

	/**
	 * Returns the activities of a specific resource.
	 * 
	 * @param resourceName the name of the resource of which the activities are returned.
	 * @return an {@link ArrayList} containing the name of the activities.
	 */
	public ArrayList<String> getActivitiesOfResource(String resourceName) {
		return linkedOntology.getIndividualNamesGivenIndividualAndProperty(resourceName, "has_activity");
	}

	/**
	 * Returns the properties of a specific resource.
	 * 
	 * @param resourceName the name of the resource of which the properties are returned.
	 * @return an {@link ArrayList} containing the name of the properties.
	 */
	public ArrayList<String> getPropertiesOfResource(String resourceName) {
		return linkedOntology.getIndividualNamesGivenIndividualAndProperty(resourceName, "has_property");
	}

	/**
	 * Returns the resource of a specific activity.
	 * 
	 * @param activityName the name of the activity of which the resource is returned.
	 * @return the name of the resource.
	 */
	public String getResourceOfActivity(String activityName) {
		return linkedOntology.getIndividualNameGivenIndividualAndProperty(activityName, "is_activity_of");
	}

	/**
	 * Returns the forthcoming activities of a specific activity.
	 * 
	 * @param activityName the name of the activity of which the next activities are returned.
	 * @return an {@link ArrayList} containing the name of the next activities.
	 */
	public ArrayList<String> getNextActivitiesOfActivity(String activityName) {
		return linkedOntology.getIndividualNamesGivenIndividualAndProperty(activityName, "has_next_activity");
	}

	/**
	 * Returns the action of a specific activity.
	 * 
	 * @param activityName the name of the activity of which the action is returned.
	 * @return the name of the action.
	 */
	public String getActionOfActivity(String activityName) {
		return linkedOntology.getIndividualNameGivenIndividualAndProperty(activityName, "has_action");
	}

	/**
	 * Returns the activity type of a specific activity.
	 * 
	 * @param activityName the name of the activity of which the activity type is returned.
	 * @return the type of the activity.
	 */
	public String getActivityTypeOfActivity(String activityName) {
		return linkedOntology.getIndividualPropertyValue(activityName, "activitytype")/* .getString() */;
	}

	/**
	 * Adds an input representation to a specific resource.
	 * 
	 * @param resourceName the name of the resource to which the input representation is added.
	 * @param inputRepresentation the representation to be added.
	 */
	public void addInputRepresentationToResource(String resourceName, String inputRepresentation) {
		throw new UnsupportedOperationException();
	}

	/**
	 * Adds an output representation to a specific resource.
	 * 
	 * @param resourceName the name of the resource to which the output representation is added.
	 * @param outputRepresentation the representation to be added.
	 */
	public void addOutputRepresentationToResource(String resourceName, String outputRepresentation) {
		throw new UnsupportedOperationException();
	}

	/**
	 * Returns the input representation of a specific resource.
	 * 
	 * @param resourceName the name of the resource of which the input representation is returned.
	 * @return the input representation of this resource.
	 */
	public String getInputRepresentationOfResource(String resourceName) {
		throw new UnsupportedOperationException();
	}

	/**
	 * Returns the output representation of a specific resource.
	 * 
	 * @param resourceName the name of the resource of which the output representation is returned.
	 * @return the output representation of this resource.
	 */
	public String getOutputRepresentationOfResource(String resourceName) {
		throw new UnsupportedOperationException();
	}

	/**
	 * Closes the connection of the ontology and saves it to disk. <u><b>NOTE</b></u> that if this function is not
	 * called, then the ontology is not saved.
	 */
	public void close() {
		linkedOntology.close();
	}
}
