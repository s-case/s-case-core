package eu.scasefp7.eclipse.core.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.ModelFactory;

public class TestOntologyHandler extends AbstractHandler {

	public Object execute(ExecutionEvent event) throws ExecutionException {
		OntModel base = ModelFactory.createOntologyModel();
		return null;
	}
}
