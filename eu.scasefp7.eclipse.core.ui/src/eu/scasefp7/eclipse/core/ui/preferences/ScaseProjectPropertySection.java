package eu.scasefp7.eclipse.core.ui.preferences;

import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.views.properties.tabbed.AdvancedPropertySection;

import eu.scasefp7.eclipse.core.builder.ProjectUtils;

/**
 * Provides the project property section in tabbed properties view.
 * 
 * @author Leonora Gaspar
 */
public class ScaseProjectPropertySection extends AdvancedPropertySection {
	@Override
    public void setInput(IWorkbenchPart part, ISelection selection) {
        
        if (selection instanceof IStructuredSelection) {
            @SuppressWarnings("unchecked")
            List<Object> list = ((StructuredSelection)selection).toList();
            IProject project = ProjectUtils.getProjectOfSelectionList(list);
                        
            if(project != null) {
                ISelection newSelection = new StructuredSelection(new ProjectResourcePropertySource(project));
                super.setInput(part, newSelection); 
            } else {
                super.setInput(part, selection);
            }
        }
    }
}