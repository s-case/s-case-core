package eu.scasefp7.eclipse.services.nlp.provider;

import java.io.NotSerializableException;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.Collection;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.eclipse.ecf.remoteservice.IRemoteCall;
import org.eclipse.ecf.remoteservice.client.IRemoteCallParameter;
import org.eclipse.ecf.remoteservice.client.IRemoteCallable;
import org.eclipse.ecf.remoteservice.client.RemoteServiceClientRegistration;
import org.eclipse.ecf.remoteservice.rest.client.HttpPostRequestType;
import org.eclipse.ecf.remoteservice.rest.client.RestClientContainer;
import org.eclipse.ecf.remoteservice.rest.client.RestClientService;
import org.json.JSONException;
import org.json.JSONStringer;

/**
 * Extension of the REST client service provided by the framework, supports sending multiple parameters with HTTP POST and JSON enconding 
 * (instead of application/x-www-form-urlencoded data).
 * 
 * @author Marin Orlić
 */
public class NLPServiceRESTClientService extends RestClientService 
{
        /**
         * Constructs the service in the container.
         * 
         * @param container to hold the service
         * @param registration in the container
         */
        public NLPServiceRESTClientService(RestClientContainer container, RemoteServiceClientRegistration registration) {
            super(container, registration);
        }

        /**
         * @throws UnsupportedEncodingException if the encoding is not supported
         * @throws NotSerializableException if the data cannot be serialized
         */
        protected HttpRequestBase preparePostMethod(String uri, IRemoteCall call, IRemoteCallable callable) throws NotSerializableException, UnsupportedEncodingException {
            HttpPost result = new HttpPost(uri);
            HttpPostRequestType postRequestType = (HttpPostRequestType) callable.getRequestType();

//                result.setHeader("Content-Language", language);
//                NameValuePair[] params = this.toNameValuePairs(uri, call, callable);
            
            IRemoteCallParameter[] defaultParameters = callable.getDefaultParameters();
            Object[] parameters = call.getParameters();
            if (postRequestType.useRequestEntity()) {
                String payload = "";
                JSONStringer json = new JSONStringer();
                
                try { 
                    json.object();
                    for(int ix = 0; ix < parameters.length; ix++) {
                        String key = defaultParameters[ix].getName();
                        Object value = parameters[ix];
                            
                        json.key(key);
                        if(value instanceof Collection) {
                            json.array();
                            
                            @SuppressWarnings("rawtypes")
                            Collection coll = (Collection) value;
                            int id = 1;
                            for(Object obj : coll) {
                                json.object();
                                json.key("id"); //$NON-NLS-1$
                                json.value("FR"+(id++));
                                json.key("text"); //$NON-NLS-1$
                                json.value(obj);
                                json.endObject();
                            }
                            
                            json.endArray();
                        } else {
                            json.value(value);
                        }
                    }
                    
                    json.endObject();
                    payload = json.toString();
                    
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }                
System.out.println("PAYLOAD>>>> " + payload);                    
                HttpEntity requestEntity = postRequestType.generateRequestEntity(uri, call, callable, defaultParameters[0], payload);
                result.setEntity(requestEntity);

//                    if (defaultParameters != null && defaultParameters.length > 0 && parameters != null && parameters.length > 0) {
                    
//                        result.setEntity(requestEntity);
//                    }
            } else {
                NameValuePair[] params = toNameValuePairs(uri, call, callable);
                if (params != null) {
                    result.setEntity(getUrlEncodedFormEntity(Arrays.asList(params), postRequestType));
                }
            }
            return result;
        }
    }