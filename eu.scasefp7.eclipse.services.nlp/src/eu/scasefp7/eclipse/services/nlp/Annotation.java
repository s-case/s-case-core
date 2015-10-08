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
public class Annotation {
    String id;
    String text;
    String[] annotations;
    AnnotationFormat format;
    
    public Annotation() {
        
    }
    
    public Annotation(String id, String text, String[] annotations, AnnotationFormat format) {
        this.id = id;
        this.text = text;
        this.annotations = annotations;
        this.format = format;
    }

    /**
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(String id) {
        this.id = id;
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
     * @return the annotations
     */
    public String[] getAnnotations() {
        return annotations;
    }

    /**
     * @param annotations the annotations to set
     */
    public void setAnnotations(String[] annotations) {
        this.annotations = annotations;
    }

    /**
     * @return the format
     */
    public AnnotationFormat getFormat() {
        return format;
    }

    /**
     * @param format the format to set
     */
    public void setFormat(AnnotationFormat format) {
        this.format = format;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Annotation [id=").append(id).append(", text=").append(text).append(", annotations=")
                .append(Arrays.toString(annotations)).append(", format=").append(format).append("]");
        return builder.toString();
    }
    
}