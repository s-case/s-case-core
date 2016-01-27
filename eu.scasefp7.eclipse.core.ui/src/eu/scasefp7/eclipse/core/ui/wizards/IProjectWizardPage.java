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
package eu.scasefp7.eclipse.core.ui.wizards;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jface.wizard.IWizardPage;

/**
 * @author Marin Orlic
 *
 */
public interface IProjectWizardPage extends IWizardPage {
    /**
     * Performs any actions appropriate for the page in response to the user having pressed the Finish button in the wizard, 
     * or refuse if finishing now is not permitted.
     * 
     * Called by the {@link NewScaseProjectWizard#performFinish} for each page in the new project wizard.
     * @param resource representing the newly created project
     * 
     * @return true to indicate the finish request was accepted, and false to indicate that the finish request was refused
     * @see org.eclipse.jface.wizard.IWizard#performFinish()
     * @see NewScaseProjectWizard#performFinish() 
     */
    boolean performFinish(IAdaptable resource);
}
