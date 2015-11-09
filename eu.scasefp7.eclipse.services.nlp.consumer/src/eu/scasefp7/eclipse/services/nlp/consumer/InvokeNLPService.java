package eu.scasefp7.eclipse.services.nlp.consumer;
import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.e4.core.contexts.ContextInjectionFactory;
import org.eclipse.e4.core.contexts.EclipseContextFactory;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.osgi.framework.BundleContext;
import org.osgi.util.tracker.ServiceTracker;

import eu.scasefp7.eclipse.services.nlp.AnnotationFormat;
import eu.scasefp7.eclipse.services.nlp.INLPServiceAsync;

/**
 * Copyright 2015 S-CASE Consortium
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/**
 * This class shows an example of invoking NLP service using explicit service tracking.
 * OSGi declarative services are a preferred method, since no manual housekeeping is needed.
 * 
 * @see NLPConsumer
 * @author Marin Orlic
 */
public class InvokeNLPService extends AbstractHandler {

    private ServiceTracker<INLPServiceAsync, INLPServiceAsync> tracker = null;
    private boolean serviceExists = true;
    
    /**
     * Constructor
     */
    public InvokeNLPService() {
        BundleContext context = Activator.getContext();

        tracker = new ServiceTracker<INLPServiceAsync, INLPServiceAsync>(context, INLPServiceAsync.class, null);
        tracker.open();
    }

    @Override
    public void dispose() {
        if (tracker != null) {
            tracker.close();
            tracker = null;
        }
        this.serviceExists = false;
    }

    
    @Override
    public Object execute(ExecutionEvent event) throws ExecutionException {
        IEclipseContext ctx = EclipseContextFactory.getServiceContext(Activator.getContext());
        InjectionTest test = ContextInjectionFactory.make(InjectionTest.class, ctx);
        test = test;
        
        INLPServiceAsync service = tracker.getService();
        
        if(service == null) {
            return null;
        }
        
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
        return null;
    }

    @Override
    public boolean isEnabled() {
        return this.serviceExists; // not refreshed correctly
    }

    @Override
    public boolean isHandled() {
        return this.serviceExists; // not refreshed correctly
    }
}
