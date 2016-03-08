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

/**
 * @author "Marin Orlic"
 *
 */
public class TransitionArtefact extends BaseArtefact {

    private String condition;
    private String source;
    private String target;
    
    /**
     * Constructs a transition artifact.
     * 
     * @param name of the transition
     * @param type of the artifact
     * @param parent group
     * @param condition of the transition
     * @param source activity
     * @param target activity
     */
    public TransitionArtefact(String name, String type, ArtefactGroup parent, String condition, String source, String target) {
        super(name, type, parent);
        this.condition = condition;
        this.source = source;
        this.target = target;
    }

    /**
     * Get the condition associated with the transition.
     * @return the condition
     */
    public String getCondition() {
        return condition;
    }

    /**
     * Get the source activity of the transition.
     * @return the source
     */
    public String getSource() {
        return source;
    }

    /**
     * Get the target activity of the transition.
     * @return the target
     * 
     */
    public String getTarget() {
        return target;
    }

    /**
     * Checks if transition has condition set.
     * @return true if transition has condition
     */
    public boolean hasCondition() {
        return this.condition != null;
    }
}
