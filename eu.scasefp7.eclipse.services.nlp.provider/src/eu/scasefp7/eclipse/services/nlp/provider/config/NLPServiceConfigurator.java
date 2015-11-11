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
package eu.scasefp7.eclipse.services.nlp.provider.config;

import org.eclipse.core.runtime.preferences.IEclipsePreferences.IPreferenceChangeListener;
import org.eclipse.core.runtime.preferences.IEclipsePreferences.PreferenceChangeEvent;

/**
 * @author emaorli
 *
 */
public class NLPServiceConfigurator implements IPreferenceChangeListener {

    /** Endpoint for the NLP service */
    public static final String P_NLP_ENDPOINT = "nlpServiceURI"; //$NON-NLS-1$
   
    /**
     * 
     */
    public NLPServiceConfigurator() {
        // TODO Auto-generated constructor stub
    }

    @Override
    public void preferenceChange(PreferenceChangeEvent event) {
        System.out.println(event);
        if(event.getKey().equals(P_NLP_ENDPOINT)) {
            
        }
        
    }

}
