package eu.scasefp7.eclipse.core.ui.preferences;

import org.eclipse.jface.viewers.LabelProvider;

import eu.scasefp7.eclipse.core.ui.preferences.internal.DomainEntry;

/**
 * @author Marin Orlic
 */
public class DomainLabelProvider extends LabelProvider {
	public String getText(Object element) {
		return ((DomainEntry) element).getName();
	}
}