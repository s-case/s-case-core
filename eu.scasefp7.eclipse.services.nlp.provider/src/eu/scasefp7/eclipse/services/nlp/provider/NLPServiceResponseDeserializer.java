package eu.scasefp7.eclipse.services.nlp.provider;

import java.io.NotSerializableException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.ecf.remoteservice.IRemoteCall;
import org.eclipse.ecf.remoteservice.client.IRemoteCallable;
import org.eclipse.ecf.remoteservice.client.IRemoteResponseDeserializer;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import eu.scasefp7.eclipse.services.nlp.Annotation;
import eu.scasefp7.eclipse.services.nlp.AnnotationFormat;
import eu.scasefp7.eclipse.services.nlp.Terms;

/**
 * Response deserializer, parses JSON service response into POJOs.
 * 
 * @author Marin Orlić
 */
public class NLPServiceResponseDeserializer implements IRemoteResponseDeserializer {
   
    /**
     * Constructs the deserializer
     */
    NLPServiceResponseDeserializer() {
    }

    @Override
    public Object deserializeResponse(String endpoint, IRemoteCall call, IRemoteCallable callable,
            @SuppressWarnings("rawtypes") Map responseHeaders, byte[] responseBody)
            throws NotSerializableException {
        try {
            // Convert responseBody to String and parse using org.json
            JSONObject jo = new JSONObject(new String(responseBody));

            System.out.println("Response> " + jo.toString(4));       
            
            if(callable.getMethod().equals("annotateSentence")) {
                return parseSentenceAnnotations(jo);
            }
             
            if(callable.getMethod().equals("annotatePhrase")) {
                return parsePhraseAnnotations(jo);
            }
           
            if(callable.getMethod().equals("extractQueryTerms")) {  
                return parseQueryTerms(jo);
            }
                
            if(callable.getMethod().equals("annotateProject")) {    
                return parseProjectAnnotations(jo);
            }

            throw new NotSerializableException("Unknown service method");
        } catch (Exception e) {
            NotSerializableException ex = new NotSerializableException(
                    "Problem in response from NLP service endpoint=" + endpoint + " status message: "
                            + e.getMessage());
            ex.setStackTrace(e.getStackTrace());
            throw ex;
        }
    }

    /**
     * Extracts the annotations for a single sentence from server response.
     * 
     * @param jo JSONObject with response
     * @return Annotation object containing parsed annotations
     * @throws NotSerializableException
     * @see Annotation
     */
    private Annotation parseSentenceAnnotations(JSONObject jo) throws NotSerializableException {
        /* Sentence
         * {
            "annotation_format": "ann",
            "annotations": [
                "R1 ActsOn Arg1:T2 Arg2:T3",
                "R2 HasProperty Arg1:T3 Arg2:T4",
                "T1 Action 6 9 let",
                "T2 Action 13 16 try",
                "T3 Theme 30 40 invocation",
                "T4 Property 22 29 service"
            ],
            "created_at": "2015-10-06T12:19Z",
            "sentence": "First let me try some service invocation."
        }
         */
        Annotation result = new Annotation();
        
        try {
            result.setFormat(getAnnotationFormat(jo));
            result.setText(jo.getString("sentence")); //$NON-NLS-1$
            
            JSONArray arr = jo.getJSONArray("annotations"); //$NON-NLS-1$
            result.setAnnotations(jsonArrayToStringArray(arr));
        } catch(JSONException je) {
            NotSerializableException ex = new NotSerializableException(
                    "Problem parsing response from NLP service annotateSentence(), status message: " 
                            + je.getMessage());
            ex.setStackTrace(je.getStackTrace());
            throw ex;
        }  
        
        return result;
    }

    /**
     * Extracts the annotations for a single phrase from server response.
     * 
     * @param jo JSONObject with response
     * @return Annotation object containing parsed annotations
     * @throws NotSerializableException
     * @see Annotation
     */
    private Annotation parsePhraseAnnotations(JSONObject jo) throws NotSerializableException {
        Annotation result = new Annotation();
        
        try {
            result.setFormat(getAnnotationFormat(jo));
            result.setText(jo.getString("phrase")); //$NON-NLS-1$
             
            JSONArray arr = jo.getJSONArray("annotations"); //$NON-NLS-1$
            result.setAnnotations(jsonArrayToStringArray(arr));
        } catch(JSONException je) {
            NotSerializableException ex = new NotSerializableException(
                    "Problem parsing response from NLP service annotatePhrase(), status message: " 
                            + je.getMessage());
            ex.setStackTrace(je.getStackTrace());
            throw ex;
        }
        
        return result;
    }

    /**
     * Extracts the annotations for a single sentence from server response.
     * 
     * @param jo JSONObject with response
     * @return Annotation object containing parsed annotations
     * @throws NotSerializableException
     * @see Terms
     */
    private Terms parseQueryTerms(JSONObject jo) throws NotSerializableException {
        /* Query
        {
            created_at: "2015-10-09T21:29Z"
            question: "Find systems which provide a search for computer products"
            query_terms: [5]
            0:  "find"
            1:  "system"
            2:  "search"
            3:  "computer"
            4:  "product"
            -
        }
        */
        Terms result = new Terms();
        
        try {
            result.setText(jo.getString("question")); //$NON-NLS-1$
        
            JSONArray arr = jo.getJSONArray("query_terms"); //$NON-NLS-1$
            result.setTerms(jsonArrayToStringArray(arr));
        } catch(JSONException je) {
            NotSerializableException ex = new NotSerializableException(
                    "Problem parsing response from NLP service extractQueryTerms(), status message: " 
                            + je.getMessage());
            ex.setStackTrace(je.getStackTrace());
            throw ex;
        }  
        
        return result;
    }

    /**
     * Extracts the annotations for a project from server response. Project name is discarded.
     * 
     * @param jo JSONObject with response
     * @return Annotation array containing parsed annotations
     * @throws NotSerializableException
     * @see Annotations
     */
    private Annotation[] parseProjectAnnotations(JSONObject jo) throws NotSerializableException {
        /* Project
         {
             "created_at":"2015-10-09T21:34Z",
             "project_name":"pr",
             "project_requirements":[
                 {"id":"FR1","text":"Find systems which provide a search for computer products"},
                 {"id":"FR2","text":"Explain something to me"}
             ],
             "annotations":[
                 {
                     "id":"FR1","annotation":[
                         "R1 HasProperty Arg1:T1 Arg2:T2",
                         "R2 HasProperty Arg1:T1 Arg2:T3",
                         "R3 HasProperty Arg1:T4 Arg2:T5",
                         "R4 HasProperty Arg1:T6 Arg2:T7",
                         "T1 Object 5 12 systems",
                         "T2 Property 13 18 which",
                         "T3 Property 0 4 Find",
                         "T4 Object 29 35 search",
                         "T5 Property 36 39 for",
                         "T6 Object 49 57 products",
                         "T7 Property 40 48 computer"
                     ]
                 },
                 {
                     "id":"FR2","annotation":[
                         "R1 HasProperty Arg1:T1 Arg2:T2",
                         "T1 Object 0 7 Explain",
                         "T2 Property 8 17 something"
                     ]
                 }
            ],
            "annotation_format":"ann"
         }
         */
            
        
        try {
            ArrayList<Annotation> result = new ArrayList<Annotation>();
            
            // Get format
            AnnotationFormat format = getAnnotationFormat(jo); //$NON-NLS-1$
    
            // Get inputs
            JSONArray reqs = jo.getJSONArray("project_requirements"); //$NON-NLS-1$
            
            // Get annotations
            JSONArray anns = jo.getJSONArray("annotations"); //$NON-NLS-1$
    
            HashMap<String, String[]> map = new HashMap<String, String[]>();
            
            for(int ix = 0; ix < anns.length(); ix++) {
                JSONObject item = anns.getJSONObject(ix);
                
                String id = item.getString("id"); //$NON-NLS-1$
                JSONArray arr = item.getJSONArray("annotation"); //$NON-NLS-1$
                String[] ann = jsonArrayToStringArray(arr);
                
                map.put(id, ann);
            }
        
            // Link annotations
            for(int ix = 0; ix < reqs.length(); ix++) {
                JSONObject item = reqs.getJSONObject(ix);
                
                String id = item.getString("id"); //$NON-NLS-1$
                String text = item.getString("text"); //$NON-NLS-1$
                String[] ann = map.get(id);
                
                if(ann == null) {
                    throw new NotSerializableException("Annotation not found for requirement " + id);
                }
                
                Annotation r = new Annotation(id, text, ann, format);
                result.add(r);
            }
            
             return result.toArray(new Annotation[result.size()]);
            
        } catch(Exception e) {
            NotSerializableException ex = new NotSerializableException(
                    "Problem parsing response from NLP service annotateProject(), status message: " 
                            + e.getMessage());
            ex.setStackTrace(e.getStackTrace());
            throw ex;
        }
    }

    /**
     * Reads the annotation format from response.
     * 
     * @param jo JSONObject with response
     * @return annotation format read from "annotation_format"
     * @throws JSONException if key is missing
     */
    private AnnotationFormat getAnnotationFormat(JSONObject jo) throws JSONException {
        return AnnotationFormat.fromString(jo.getString("annotation_format")); //$NON-NLS-1$
    }

    /**
     * Converts JSON array of strings to String array.
     * 
     * @param arr JSONArray to convert
     * @return String array containing the items from arr
     * @throws JSONException
     */
    private String[] jsonArrayToStringArray(JSONArray arr) throws JSONException {
        String[] anns = new String[arr.length()];
        
        for(int ix = 0; ix < arr.length(); ix++) {
            anns[ix] = arr.getString(ix);
        }
        return anns;
    }
}