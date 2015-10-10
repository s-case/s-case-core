package eu.scasefp7.eclipse.services.nlp.internal.provider;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.ecf.core.ContainerConnectException;
import org.eclipse.ecf.core.ContainerCreateException;
import org.eclipse.ecf.core.ContainerTypeDescription;
import org.eclipse.ecf.core.IContainer;
import org.eclipse.ecf.core.identity.ID;
import org.eclipse.ecf.core.identity.Namespace;
import org.eclipse.ecf.core.security.IConnectContext;
import org.eclipse.ecf.remoteservice.IRemoteService;
import org.eclipse.ecf.remoteservice.IRemoteServiceRegistration;
import org.eclipse.ecf.remoteservice.client.IRemoteCallParameter;
import org.eclipse.ecf.remoteservice.client.IRemoteCallable;
import org.eclipse.ecf.remoteservice.client.RemoteCallParameter;
import org.eclipse.ecf.remoteservice.client.RemoteCallable;
import org.eclipse.ecf.remoteservice.client.RemoteServiceClientRegistration;
import org.eclipse.ecf.remoteservice.rest.client.HttpPostRequestType;
import org.eclipse.ecf.remoteservice.rest.client.RestClientContainer;
import org.eclipse.ecf.remoteservice.rest.client.RestClientContainerInstantiator;

import eu.scasefp7.eclipse.services.nlp.INLPService;

public class NLPServiceClientContainer extends RestClientContainer {

    public static final String CONTAINER_TYPE_NAME = "eu.scasefp7.eclipse.services.nlp";

    private IRemoteServiceRegistration serviceRegistration;

    public static class Instantiator extends RestClientContainerInstantiator {

        /**
         * 1. This method is called by the ECF RSA implementation when a remote
         * service is to be imported. The exporterSupportedConfigs parameter
         * contains the exported config types associated with the remote
         * service. The implementation of this method decides whether we are
         * interested in this remote service config type. If we are
         * (exporterSupportedConfigs contains CONTAINER_TYPE_NAME, then we
         * return an array of strings containing our CONTAINER_TYPE_NAME
         */
        @Override
        public String[] getImportedConfigs(ContainerTypeDescription description, String[] exporterSupportedConfigs) {
            /**
             * If the exporterSupportedConfigs contains CONTAINER_TYPE_NAME,
             * then return that CONTAINER_TYPE_NAME to trigger RSA usage of this
             * container instantiator
             */
            if (Arrays.asList(exporterSupportedConfigs).contains(CONTAINER_TYPE_NAME))
                return new String[] { CONTAINER_TYPE_NAME };
            return null;
        }

        @Override
        /**
         * 2. This method is called by the ECF RSA to create a new instance of
         * the appropriate
         * container type (aka OSGi config type)
         */
        public IContainer createInstance(ContainerTypeDescription description, Object[] parameters)
                throws ContainerCreateException {
            return new NLPServiceClientContainer();
        }
    }
    
    

    /**
     * 3. This method is called by the ECF RSA implementation to 'connect' to
     * the targetID. targetID (of appropriate type, in this case TimezoneID) is
     * created by the RSA implementation and then passed to this method. For the
     * geonames service, this targetID has value: http://api.geonames.org/ as
     * given in the ecf.endpoint.id remote service property. See the <a href=
     * "https://github.com/ECF/Geonames/blob/master/bundles/org.eclipse.ecf.geonames.timezone.consumer.edef/timezoneserviceendpointdescription.xml"
     * >example EDEF</a>
     */
    @Override
    public void connect(ID targetID, IConnectContext connectContext) throws ContainerConnectException {
        // Set the connectTargetID in the RestClientContainer super class
        super.connect(targetID, connectContext);

        // we will use default parameters (for username see parameters below)
        setAlwaysSendDefaultParameters(false);

        IRemoteCallable annotatePhrase = createCallableAnnotatePhrase();
        IRemoteCallable annotateSentence = createCallableAnnotateSentence();
        IRemoteCallable annotateProject = createCallableAnnotateProject();
        IRemoteCallable extractQueryTerms = createCallableExtractQueryTerms();

        /**
         * Now we register the remote callables. This associates the
         * INLPService proxy with a correct dynamically constructed URL.
         * When RSA requests the remote service proxy from this container, the
         * proxy will have all the necessary code to construct the appropriate
         * URL and make the appropriate REST request.
         */
        serviceRegistration = registerCallables(INLPService.class, 
                new IRemoteCallable[] { 
                    annotatePhrase,
                    annotateSentence,
                    annotateProject,
                    extractQueryTerms
                }, null);

//        setParameterSerializer(new StringParameterSerializer() {
//            
//            @Override
//            public IRemoteCallParameter[] serializeParameter(String endpoint, IRemoteCall call, IRemoteCallable callable,
//                    IRemoteCallParameter[] currentParameters, Object[] paramToSerialize) {
//                System.out.println("Param serializer");
//                return super.serializeParameter(endpoint, call, callable, currentParameters, paramToSerialize);
//            }
//            
//        });
        
        /**
         * In order for the proxy to handle the response from the NLP
         * service, it's necessary to define a response deserializer to
         * convert the data from the JSON response (or failure/exception
         * information) to an instance of Timezone for the proxy to return to
         * the remote service consumer. This is done by defining an
         * IRemoteResponseDeserializer. When the HTTP response is received, it
         * is passed to the remote response deserializer to convert from the
         * response JSON to a Annotation instance that will be returned by the
         * proxy.
         * 
         */
        setResponseDeserializer(new NLPServiceResponseDeserializer());
    }

    /**
     * @return
     */
    private IRemoteCallable createCallableAnnotatePhrase() {
        /**
         * Setup the association between the INLPService class and the
         * NLP REST remote service. Here is the specification of the <a
         * href="https://github.com/s-case/nlp-server/blob/master/scase-wp3-nlp-server/NLPServer.md">
         * NLP service</a>.
         * <p>
         * The association is setup by creating and then registering an instance of IRemoteCallable. The IRemoteCallable
         * specifies both the association between the automatically constructed INLPService proxy's method name, as well
         * as the association between the proxy's method parameters and the remote service's parameters.
         * <p>
         */
        RemoteCallParameter.Builder parameterBuilder = new RemoteCallParameter.Builder()
            .addParameter("phrase")
            .addParameter("language")
            .addParameter("annotation_format");

        /**
         * Then we create a callableBuilder instance to associate the
         * annotatePhrase method to the path for this service. We also set the
         * default parameters to the ones we've specified via the
         * parameterBuilder above, and we define the HTTP request type as 'POST'.
         */
        RemoteCallable.Builder callableBuilder = new RemoteCallable.Builder("annotatePhrase", "/nlpserver/phrase")
            .setDefaultParameters(parameterBuilder.build())
            .setRequestType(new HttpPostRequestType(HttpPostRequestType.STRING_REQUEST_ENTITY, "application/json"));
        return callableBuilder.build();
    }

    /**
     * @return
     */
    private IRemoteCallable createCallableAnnotateSentence() {
        /**
         * Setup the association between the INLPService class and the
         * NLP REST remote service. Here is the specification of the <a
         * href="https://github.com/s-case/nlp-server/blob/master/scase-wp3-nlp-server/NLPServer.md">
         * NLP service</a>.
         * <p>
         * The association is setup by creating and then registering an instance of IRemoteCallable. The IRemoteCallable
         * specifies both the association between the automatically constructed INLPService proxy's method name, as well
         * as the association between the proxy's method parameters and the remote service's parameters.
         * <p>
         */
        RemoteCallParameter.Builder parameterBuilder = new RemoteCallParameter.Builder()
            .addParameter("sentence")
            .addParameter("language")
            .addParameter("annotation_format");

        /**
         * Then we create a callableBuilder instance to associate the
         * annotatePhrase method to the path for this service. We also set the
         * default parameters to the ones we've specified via the
         * parameterBuilder above, and we define the HTTP request type as 'POST'.
         */
        RemoteCallable.Builder callableBuilder = new RemoteCallable.Builder("annotateSentence", "/nlpserver/sentence")
            .setDefaultParameters(parameterBuilder.build())
            .setRequestType(new HttpPostRequestType(HttpPostRequestType.STRING_REQUEST_ENTITY, "application/json"));
        
        return callableBuilder.build();
    }

    /**
     * @return
     */
    private IRemoteCallable createCallableAnnotateProject() {
        /**
         * Setup the association between the INLPService class and the
         * NLP REST remote service. Here is the specification of the <a
         * href="https://github.com/s-case/nlp-server/blob/master/scase-wp3-nlp-server/NLPServer.md">
         * NLP service</a>.
         * <p>
         * The association is setup by creating and then registering an instance of IRemoteCallable. The IRemoteCallable
         * specifies both the association between the automatically constructed INLPService proxy's method name, as well
         * as the association between the proxy's method parameters and the remote service's parameters.
         * <p>
         */

        List<IRemoteCallParameter> params = new ArrayList<IRemoteCallParameter>();
        params.add(new RemoteCallParameter("project_name"));
        params.add(new RemoteCallParameter("project_requirements"));
        params.add(new RemoteCallParameter("language"));
        params.add(new RemoteCallParameter("annotation_format"));
        
        /**
         * Then we create a callableBuilder instance to associate the
         * annotatePhrase method to the path for this service. We also set the
         * default parameters to the ones we've specified via the
         * parameterBuilder above, and we define the HTTP request type as 'POST'.
         */
        RemoteCallable.Builder callableBuilder = new RemoteCallable.Builder("annotateProject", "/nlpserver/project")
            .setDefaultParameters(params.toArray(new IRemoteCallParameter[params.size()]))
            .setRequestType(new HttpPostRequestType(HttpPostRequestType.STRING_REQUEST_ENTITY, "application/json"));
        
        return callableBuilder.build();
    }

    /**
     * @return
     */
    private IRemoteCallable createCallableExtractQueryTerms() {
        /**
         * Setup the association between the INLPService class and the
         * NLP REST remote service. Here is the specification of the <a
         * href="https://github.com/s-case/nlp-server/blob/master/scase-wp3-nlp-server/NLPServer.md">
         * NLP service</a>.
         * <p>
         * The association is setup by creating and then registering an instance of IRemoteCallable. The IRemoteCallable
         * specifies both the association between the automatically constructed INLPService proxy's method name, as well
         * as the association between the proxy's method parameters and the remote service's parameters.
         * <p>
         */
       
        List<IRemoteCallParameter> params = new ArrayList<IRemoteCallParameter>();
        params.add(new RemoteCallParameter("question"));
        params.add(new RemoteCallParameter("language"));        
        
        /**
         * Then we create a callableBuilder instance to associate the
         * annotatePhrase method to the path for this service. We also set the
         * default parameters to the ones we've specified via the
         * parameterBuilder above, and we define the HTTP request type as 'POST'.
         */
        RemoteCallable.Builder callableBuilder = new RemoteCallable.Builder("extractQueryTerms", "/nlpserver/question")
            .setDefaultParameters(params.toArray(new IRemoteCallParameter[params.size()]))
            .setRequestType(new HttpPostRequestType(HttpPostRequestType.STRING_REQUEST_ENTITY, "application/json"));
        
        return callableBuilder.build();
    }

    @Override
    public void disconnect() {
        super.disconnect();
        // Unregister the nlpServiceRegistration upon 'disconnect'
        if (serviceRegistration != null) {
            serviceRegistration.unregister();
            serviceRegistration = null;
        }
    }

    NLPServiceClientContainer() {
        // Set this container's ID to a ranmdom UUID
        super(NLPServiceNamespace.createUUID());
    }

    @Override
    public Namespace getConnectNamespace() {
        // The required Namespace for this container
        // is the NLPServiceNamespace
        return NLPServiceNamespace.INSTANCE;
    }

    @Override
    protected IRemoteService createRemoteService(RemoteServiceClientRegistration registration) {
        return new NLPServiceRESTClientService(this, registration);
    }

}
