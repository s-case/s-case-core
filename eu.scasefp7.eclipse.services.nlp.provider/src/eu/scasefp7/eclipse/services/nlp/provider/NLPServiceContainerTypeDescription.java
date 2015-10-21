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
package eu.scasefp7.eclipse.services.nlp.provider;

import org.eclipse.ecf.core.ContainerTypeDescription;

public class NLPServiceContainerTypeDescription extends ContainerTypeDescription {
    public NLPServiceContainerTypeDescription() {
        super(NLPServiceClientContainer.CONTAINER_TYPE_NAME, new NLPServiceClientContainer.Instantiator(),
                "S-CASE NLP service client container");
    }
}