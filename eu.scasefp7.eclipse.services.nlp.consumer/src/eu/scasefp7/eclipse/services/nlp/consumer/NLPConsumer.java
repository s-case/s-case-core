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
package eu.scasefp7.eclipse.services.nlp.consumer;

import eu.scasefp7.eclipse.services.nlp.AnnotationFormat;
import eu.scasefp7.eclipse.services.nlp.INLPService;
import eu.scasefp7.eclipse.services.nlp.NLPException;

/**
 * @author emaorli
 *
 */
public class NLPConsumer {

    private INLPService service;
    
    // Method will be used by DS to set the quote service
    public synchronized void setNLPService(INLPService service) {
        System.out.println("Service was set. Thank you DS!");
        this.service = service;
        // I know I should not use the service here but just for demonstration
        try {
            System.out.println(service.annotateSentence("First let me try some service invocation.", "", AnnotationFormat.ANN));
        } catch (NLPException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    // Method will be used by DS to unset the quote service
    public synchronized void unsetNLPService(INLPService service) {
        System.out.println("Service was unset. Why did you do this to me?");
        if (this.service == service) {
            this.service = null;
        }
    } 
    
}
