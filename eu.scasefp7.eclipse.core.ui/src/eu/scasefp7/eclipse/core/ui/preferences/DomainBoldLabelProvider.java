package eu.scasefp7.eclipse.core.ui.preferences;

import org.eclipse.jface.viewers.IFontProvider;
import org.eclipse.swt.graphics.Font;
import org.eclipse.ui.dialogs.FilteredTree;
import org.eclipse.ui.dialogs.PatternFilter;

/**
 * @author Marin Orlic
 */
public class DomainBoldLabelProvider extends DomainLabelProvider implements IFontProvider {
	private FilteredTree filterTree;
	private PatternFilter filterForBoldElements;

	DomainBoldLabelProvider(FilteredTree filterTree) {
		this.filterTree = filterTree;
		filterForBoldElements = filterTree.getPatternFilter();
	}

	public Font getFont(Object element) {
		return FilteredTree.getBoldFont(element, filterTree,
				filterForBoldElements);
	}
}