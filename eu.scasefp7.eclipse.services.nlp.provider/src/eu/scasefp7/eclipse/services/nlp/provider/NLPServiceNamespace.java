package eu.scasefp7.eclipse.services.nlp.provider;

import java.net.URI;

import org.eclipse.ecf.core.identity.ID;
import org.eclipse.ecf.core.identity.IDCreateException;
import org.eclipse.ecf.core.identity.IDFactory;
import org.eclipse.ecf.core.identity.Namespace;
import org.eclipse.ecf.remoteservice.rest.identity.RestID;

/**
 * Service namespace.
 * @author Marin Orlić
 */
public class NLPServiceNamespace extends Namespace {

	/** ID of the service namespace */
	public static final String NAME = "eu.scasefp7.eclipse.services.nlp.namespace";
	
	/** Instance */
	public static NLPServiceNamespace INSTANCE;

	private static final long serialVersionUID = -428290464908414596L;

	/**
	 * Construct the namespace.
	 */
	public NLPServiceNamespace() {
		super(NAME, "S-CASE NLP service namespace");
		INSTANCE = this;
	}

	/**
	 * Service ID object.
	 */
	public class NLPServiceID extends RestID {
		private static final long serialVersionUID = 7975775175834482062L;

		NLPServiceID(URI uri) {
			super(NLPServiceNamespace.this, uri);
		}
	}

	@Override
	public ID createInstance(Object[] parameters) throws IDCreateException {
		try {
			return (ID) new NLPServiceID(URI.create((String) parameters[0]));
		} catch (Exception e) {
			throw new IDCreateException("Could not create REST ID", e); //$NON-NLS-1$
		}
	}

	/**
	 * Create the unique ID of the service.
	 * 
	 * @return ID of the service
	 * @throws IDCreateException if IDFactory fails
	 */
	public static NLPServiceID createUUID() throws IDCreateException {
		return (NLPServiceID) IDFactory.getDefault().createID(INSTANCE,
				"uuid:" + java.util.UUID.randomUUID().toString());
	}

	@Override
	public String getScheme() {
		return "eu.scasefp7.eclipse.services.nlp";
	}

}
