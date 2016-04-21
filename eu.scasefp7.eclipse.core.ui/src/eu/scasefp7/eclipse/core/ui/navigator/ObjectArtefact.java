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
 * Represents an object in the requirements.
 * 
 * @author Marin Orlic
 */
public class ObjectArtefact extends BaseArtefact implements IArtefact {

    private ArrayList<String> actions = null;
    private ArrayList<String> properties = null;

    
    /**
     * Create the object artifact.
     * 
     * @param name
     * @param type
     * @param parent
     * @param actions
     * @param properties
     */
    public ObjectArtefact(String name, String type, ArtefactGroup parent, ArrayList<String> actions, ArrayList<String> properties) {
        super(name, type, parent);
        this.actions = actions;
        this.properties = properties;
    }

    /**
     * Get the object actions.
     * 
     * @return the actions
     */
    public ArrayList<String> getActions() {
        return actions;
    }

    /**
     * Get the object properties.
     * 
     * @return the properties
     */
    public ArrayList<String> getProperties() {
        return properties;
    }

    /**
     * Checks if object has properties set.
     * @return true if object has properties
     */
    public boolean hasProperties() {
        return this.properties != null && !this.properties.isEmpty();
    }

    /**
     * Checks if object has any related actions.
     * @return true if object relates to actions
     */
    public boolean hasActions() {
        return this.actions != null && !this.actions.isEmpty();
    }

}
