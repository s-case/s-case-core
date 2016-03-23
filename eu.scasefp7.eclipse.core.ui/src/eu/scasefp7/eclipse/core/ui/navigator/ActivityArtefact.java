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
package eu.scasefp7.eclipse.core.ui.navigator;

import java.util.ArrayList;

/**
 * Represents the activity in the requirements.
 * 
 * @author Marin Orlic
 *
 */
public class ActivityArtefact extends BaseArtefact {

    private String kind;
    private ArrayList<String> properties = null;

    /**
     * Creates the activity artifact.
     * 
     * @param name
     * @param type
     * @param parent
     * @param kind
     * @param properties
     */
    public ActivityArtefact(String name, String type, ArtefactGroup parent, String kind, ArrayList<String> properties) {
        super(name, type, parent);
        this.kind = kind;
        this.properties = properties;
    }

    /**
     * @return the kind
     */
    public String getKind() {
        return kind;
    }

    /**
     * @return the properties
     */
    public ArrayList<String> getProperties() {
        return properties;
    }

    /**
     * Checks if activity has properties set.
     * @return true if activity has properties
     */
    public boolean hasProperties() {
        return this.properties != null && !this.properties.isEmpty();
    }

}
