package eu.scasefp7.eclipse.services.nlp.provider;


//import org.eclipse.ecf.osgi.services.remoteserviceadmin.RemoteServiceAdmin;
//import org.eclipse.ecf.osgi.services.remoteserviceadmin.RemoteServiceAdmin.ExportRegistration;
import org.eclipse.core.runtime.preferences.IEclipsePreferences.IPreferenceChangeListener;
import org.eclipse.core.runtime.preferences.IPreferencesService;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.services.IServiceLocator;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

import eu.scasefp7.eclipse.services.nlp.INLPService;
import eu.scasefp7.eclipse.services.nlp.provider.config.NLPServiceConfigurator;

/**
 * @author emaorli
 *
 */
public class Activator implements BundleActivator {

    private ServiceRegistration<INLPService> nlpRegistration = null;
    private IPreferenceChangeListener listener = null;
//    private ServiceRegistration<ContainerTypeDescription> containerRegistration = null;
    
	@Override
	public void start(BundleContext bundleContext) throws Exception {
//		// Register an instance of NLPServiceNamespace
//	    this.namespaceRegistration = bundleContext.registerService(Namespace.class, new NLPServiceNamespace(), null);
//        // Register an instance of TimezoneContainerTypeDescription (see class below)
//	    this.containerRegistration = bundleContext.registerService(ContainerTypeDescription.class, new NLPServiceContainerTypeDescription(), null);
	    
	    
	    // this.nlpRegistration = bundleContext.registerService(INLPService.class.getName(), new INLPService(), properties)
//	    RemoteServiceAdmin rsa = new RemoteServiceAdmin(bundleContext.getBundle());
//	    List<ExportRegistration> srs = rsa.getExportedRegistrations();
	
	    
	    IServiceLocator serviceLocator = PlatformUI.getWorkbench();
	    IPreferencesService prefsService = (IPreferencesService) serviceLocator.getService(IPreferencesService.class);
        
	    this.listener = new NLPServiceConfigurator();
	    prefsService.getRootNode().addPreferenceChangeListener(this.listener);
	    
	}
	
	

	@Override
	public void stop(BundleContext bundleContext) throws Exception {
//	    this.namespaceRegistration.unregister();
//        this.containerRegistration.unregister();
	    
	    if(this.listener != null) {
	        IServiceLocator serviceLocator = PlatformUI.getWorkbench();
	        IPreferencesService prefsService = (IPreferencesService) serviceLocator.getService(IPreferencesService.class);
	        prefsService.getRootNode().removePreferenceChangeListener(this.listener);
	        this.listener = null;
	    }
	          
	}
        
	
}
