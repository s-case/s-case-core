/**
 * Copyright 2015 S-CASE Consortium
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package eu.scasefp7.eclipse.core.ui.preferences;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

import eu.scasefp7.eclipse.core.ui.preferences.internal.DomainEntry;

/**
 * @author Marin Orlic
 */
public class DomainContentProvider implements ITreeContentProvider {

    private DomainEntry[] domains;

    @Override
    public void dispose() {

    }

    @Override
    public Object[] getChildren(Object parentElement) {
        return ((DomainEntry) parentElement).getChildren();
    }

    @Override
    public Object[] getElements(Object inputElement) {
        return domains;
    }

    @Override
    public Object getParent(Object element) {
        return ((DomainEntry) element).getParent();
    }

    @Override
    public boolean hasChildren(Object element) {
        DomainEntry de = (DomainEntry) element;
        return de.hasChildren() && ((DomainEntry) element).getChildren().length > 0;
    }

    @Override
    public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
        this.domains = (DomainEntry[]) newInput;
    }

}
