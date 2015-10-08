package eu.scasefp7.eclipse.services.nlp;

import org.eclipse.ecf.core.ContainerTypeDescription;
import org.eclipse.ecf.core.identity.Namespace;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

import eu.scasefp7.eclipse.services.nlp.internal.provider.NLPServiceClientContainer;
import eu.scasefp7.eclipse.services.nlp.internal.provider.NLPServiceNamespace;

public class Activator implements BundleActivator {

//    private ServiceRegistration<Namespace> namespaceRegistration = null;
//    private ServiceRegistration<ContainerTypeDescription> containerRegistration = null;
    
	@Override
	public void start(BundleContext bundleContext) throws Exception {
//		// Register an instance of NLPServiceNamespace
//	    this.namespaceRegistration = bundleContext.registerService(Namespace.class, new NLPServiceNamespace(), null);
//        // Register an instance of TimezoneContainerTypeDescription (see class below)
//	    this.containerRegistration = bundleContext.registerService(ContainerTypeDescription.class, new NLPServiceContainerTypeDescription(), null);
	}
	
	

	@Override
	public void stop(BundleContext bundleContext) throws Exception {
//	    this.namespaceRegistration.unregister();
//        this.containerRegistration.unregister();
	}
        
	
}
