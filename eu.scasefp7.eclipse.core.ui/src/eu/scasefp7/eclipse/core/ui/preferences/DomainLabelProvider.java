package eu.scasefp7.eclipse.core.ui.preferences;

import org.eclipse.jface.viewers.LabelProvider;

import eu.scasefp7.eclipse.core.ui.preferences.internal.DomainEntry;

public class DomainLabelProvider extends LabelProvider {
	public String getText(Object element) {
		return ((DomainEntry) element).getName();
	}
}