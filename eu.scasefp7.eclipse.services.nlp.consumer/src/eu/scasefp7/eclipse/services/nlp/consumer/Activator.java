package eu.scasefp7.eclipse.services.nlp.consumer;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.util.tracker.ServiceTracker;
import org.osgi.util.tracker.ServiceTrackerCustomizer;

import eu.scasefp7.eclipse.services.nlp.AnnotationFormat;
import eu.scasefp7.eclipse.services.nlp.INLPServiceAsync;

public class Activator implements BundleActivator, ServiceTrackerCustomizer<INLPServiceAsync, INLPServiceAsync>  {

	private static BundleContext context;

    private ServiceTracker<INLPServiceAsync, INLPServiceAsync> tracker;

	static BundleContext getContext() {
		return context;
	}

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext bundleContext) throws Exception {
		Activator.context = bundleContext;
		tracker = new ServiceTracker<INLPServiceAsync, INLPServiceAsync>(context, INLPServiceAsync.class, this);
        tracker.open();
	}

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext bundleContext) throws Exception {
	    if (tracker != null) {
            tracker.close();
            tracker = null;
        }
		Activator.context = null;
	}


    @Override
    public INLPServiceAsync addingService(ServiceReference<INLPServiceAsync> reference) {
        INLPServiceAsync service = getContext().getService(reference);
        System.out.println("Got INLPServiceAsync");
        // Get completable future and when complete
        service.annotateSentenceAsync("Find systems which provide a search for computer products", "", AnnotationFormat.ANN).whenComplete(
                (result, exception) -> {
                    // Check for exception and print out
                    if (exception != null) {
                        System.out.println(exception.getMessage());
                        exception.printStackTrace();
                    } else
                        // Success!
                        System.out.println("Received response:  posts=" + result);
                });
        // Report
        System.out.println("Returning INLPServiceAsync");
        return service;
    }

    @Override
    public void modifiedService(ServiceReference<INLPServiceAsync> reference, INLPServiceAsync service) {
        // TODO Auto-generated method stub

    }

    @Override
    public void removedService(ServiceReference<INLPServiceAsync> reference, INLPServiceAsync service) {
        // TODO Auto-generated method stub

    }

}

