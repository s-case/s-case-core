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
package eu.scasefp7.eclipse.services.nlp;

import java.util.Arrays;

/**
 * @author emaorli
 *
 */
public class Terms {
    String text;
    String[] terms;
    
    public Terms() {
        
    }
    
    public Terms(String text, String[] terms) {
        this.text = text;
        this.terms = terms;
    }

    /**
     * @return the text
     */
    public String getText() {
        return text;
    }

    /**
     * @param text the text to set
     */
    public void setText(String text) {
        this.text = text;
    }

    /**
     * @return the terms
     */
    public String[] getTerms() {
        return terms;
    }

    /**
     * @param terms the terms to set
     */
    public void setTerms(String[] terms) {
        this.terms = terms;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Annotation [text=").append(text).append(", terms=")
                .append(Arrays.toString(terms)).append("]");
        return builder.toString();
    }
    
}