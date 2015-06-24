package eu.scasefp7.eclipse.core.ontology;

import java.util.ArrayList;

import eu.scasefp7.eclipse.core.ontology.OntologySource.OntologyType;
import eu.scasefp7.eclipse.core.ontology.OntologyJenaAPI;

/**
 * Provides an API for the static ontology in OWL format. Allows adding/deleting instances and properties.
 * 
 * @author themis
 */
public class StaticOntologyAPI {

	/** The API object for the static ontology. */
	private OntologyJenaAPI staticOntology;

	/** The project to connect to in the static ontology. */
	private String projectName;

	/**
	 * Initializes the connection of this API with the ontology. Upon calling this function, the ontology is loaded in
	 * memory. <u><b>NOTE</b></u> that you have to call {@link #close()} in order to save your changes to disk.
	 * 
	 * @param projectName the project to connect to in the static ontology.
	 * @param forceDelete boolean denoting whether any existing ontology file should be deleted.
	 */
	public StaticOntologyAPI(String projectName, boolean forceDelete) {
		staticOntology = new OntologyJenaAPI(OntologyType.STATIC,
				"http://www.owl-ontologies.com/Ontology1273059028.owl", forceDelete);
		this.projectName = projectName;
		staticOntology.addIndividual("Project", projectName);
	}

	/**
	 * Initializes the connection of this API with the ontology. Upon calling this function, the ontology is loaded in
	 * memory. <u><b>NOTE</b></u> that you have to call {@link #close()} in order to save your changes to disk.
	 * 
	 * @param projectName the project to connect to in the static ontology.
	 */
	public StaticOntologyAPI(String projectName) {
		staticOntology = new OntologyJenaAPI(OntologyType.STATIC,
				"http://www.owl-ontologies.com/Ontology1273059028.owl");
		this.projectName = projectName;
		staticOntology.addIndividual("Project", projectName);
	}

	/**
	 * Adds a requirement in the ontology and connects it to the project.
	 * 
	 * @param requirementName the requirement to be added.
	 */
	public void addRequirement(String requirementName) {
		staticOntology.addIndividual("Requirement", requirementName);
		staticOntology.addPropertyAndReverseBetweenIndividuals(projectName, "project_has_requirement", requirementName);
	}

	/**
	 * Connects a requirement to a concept of the ontology.
	 * 
	 * @param requirementName the requirement to be connected.
	 * @param conceptName the concept to be connected to the requirement.
	 */
	public void connectRequirementToConcept(String requirementName, String conceptName) {
		staticOntology.addPropertyAndReverseBetweenIndividuals(requirementName, "requirement_has_concept", conceptName);
	}

	/**
	 * Connects a requirement to an operation of the ontology.
	 * 
	 * @param requirementName the requirement to be connected.
	 * @param operationName the operation to be connected to the requirement.
	 */
	public void connectRequirementToOperation(String requirementName, String operationName) {
		staticOntology.addPropertyAndReverseBetweenIndividuals(requirementName, "requirement_has_operation",
				operationName);
	}

	/**
	 * Closes the connection of the ontology and saves it to disk. <u><b>NOTE</b></u> that if this function is not
	 * called, then the ontology is not saved.
	 */
	public void close() {
		staticOntology.close();
	}

	/**
	 * Returns the objects of the ontology for the current project.
	 * 
	 * @return an {@link ArrayList} containing the names of the objects.
	 */
	public ArrayList<String> getObjects() {
		return staticOntology.getIndividualsOfClass("object");
	}

	/**
	 * Returns the properties of a specific object.
	 * 
	 * @param object the name of the object of which the properties are returned.
	 * @return an {@link ArrayList} containing the name of the properties.
	 */
	public ArrayList<String> getPropertiesOfObject(String object) {
		return staticOntology.getIndividualNamesGivenIndividualAndProperty(object, "has_property");
	}

	/**
	 * Returns the actions of a specific object.
	 * 
	 * @param object the name of the object of which the actions are returned.
	 * @return an {@link ArrayList} containing the names of the actions.
	 */
	public ArrayList<String> getActionsOfObject(String object) {
		return staticOntology.getIndividualNamesGivenIndividualAndProperty(object, "receives_action");
	}

	/**
	 * Returns the requirements of a specific concept.
	 * 
	 * @param concept the name of the concept of which the requirements are returned.
	 * @return an {@link ArrayList} containing the names of the requirements.
	 */
	public ArrayList<String> getRequirementsOfConcept(String concept) {
		return staticOntology.getIndividualNamesGivenIndividualAndProperty(concept, "is_concept_of_requirement");
	}

	/**
	 * Returns the requirements of a specific operation.
	 * 
	 * @param operation the name of the operation of which the requirements are returned.
	 * @return an {@link ArrayList} containing the names of the requirements.
	 */
	public ArrayList<String> getRequirementsOfOperation(String operation) {
		return staticOntology.getIndividualNamesGivenIndividualAndProperty(operation, "is_operation_of_requirement");
	}

	/**
	 * Adds an actor to the ontology.
	 * 
	 * @param actor the name of the actor to be added.
	 */
	public void addActor(String actor) {
		staticOntology.addIndividual("actor", actor);
	}

	/**
	 * Adds an object to the ontology.
	 * 
	 * @param object the name of the object to be added.
	 */
	public void addObject(String object) {
		staticOntology.addIndividual("object", object);
	}

	/**
	 * Adds an action to the ontology.
	 * 
	 * @param action the name of the action to be added.
	 */
	public void addAction(String action) {
		staticOntology.addIndividual("action", action);
	}

	/**
	 * Connects an actor to an action.
	 * 
	 * @param actor the actor to be connected.
	 * @param action the action to be connected to the actor.
	 */
	public void connectActorToAction(String actor, String action) {
		staticOntology.addPropertyAndReverseBetweenIndividuals(actor, "is_actor_of", action);
	}

	/**
	 * Connects an action to an object.
	 * 
	 * @param action the action to be connected to the object.
	 * @param object the object to be connected.
	 */
	public void connectActionToObject(String action, String object) {
		staticOntology.addPropertyAndReverseBetweenIndividuals(action, "acts_on", object);
	}

	/**
	 * Adds a property to the ontology.
	 * 
	 * @param property the name of the property to be added.
	 */
	public void addProperty(String property) {
		staticOntology.addIndividual("property", property);
	}

	/**
	 * Connects an element to a property.
	 * 
	 * @param element the element to be connected to the property.
	 * @param property the property to be connected.
	 */
	public void connectElementToProperty(String element, String property) {
		staticOntology.addPropertyAndReverseBetweenIndividuals(element, "has_property", property);
	}
}
