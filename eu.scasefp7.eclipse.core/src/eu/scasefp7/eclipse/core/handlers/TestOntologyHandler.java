package eu.scasefp7.eclipse.core.handlers;

import java.util.List;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.handlers.HandlerUtil;

import eu.scasefp7.eclipse.core.ontology.StaticOntologyAPI;

public class TestOntologyHandler extends AbstractHandler {

	@SuppressWarnings("unchecked")
	public Object execute(ExecutionEvent event) throws ExecutionException {
		ISelection selection = HandlerUtil.getCurrentSelection(event);
		if (selection instanceof IStructuredSelection) {
			IStructuredSelection structuredSelection = (IStructuredSelection) selection;
			List<Object> selectionList = structuredSelection.toList();
			// Iterate over the selected files
			for (Object object : selectionList) {
				IFile file = (IFile) Platform.getAdapterManager().getAdapter(object, IFile.class);
				if (file == null) {
					if (object instanceof IAdaptable) {
						file = (IFile) ((IAdaptable) object).getAdapter(IFile.class);
					}
				}
				if (file != null) {
					// Instantiate the static ontology
					StaticOntologyAPI ontology = new StaticOntologyAPI("Restmarks");

					// Add a new requirement
					ontology.addRequirement("FR1");

					// Add an actor
					ontology.addActor("user");
					ontology.connectRequirementToConcept("FR1", "user");

					// Add an object
					ontology.addObject("bookmark");
					ontology.connectRequirementToConcept("FR1", "bookmark");

					// Add an action
					ontology.addAction("create");
					ontology.connectRequirementToOperation("FR1", "create");
					ontology.connectActorToAction("user", "create");
					ontology.connectActionToObject("create","bookmark");

					// Add a property
					ontology.addProperty("tag");
					ontology.connectRequirementToConcept("FR1", "tag");
					ontology.connectElementToProperty("bookmark", "tag");

					// Close and save the ontology
					ontology.close();
				}
			}
		}

		return null;
	}
}
