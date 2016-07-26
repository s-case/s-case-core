package eu.scasefp7.eclipse.core.ui.sourceprovider;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.ui.AbstractSourceProvider;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.ISources;
import org.eclipse.ui.IWindowListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.services.IServiceLocator;

import eu.scasefp7.eclipse.core.builder.ProjectUtils;
import eu.scasefp7.eclipse.core.ui.ScaseUiConstants;

/**
 * Resolves the compositions folder for the currently selected project.
 * 
 * @author Leonora Gaspar
 */
public class CompositionsFolderActive extends AbstractSourceProvider implements ISelectionListener, IWindowListener   {
    
	private final static String MY_STATE = ScaseUiConstants.COMPOSITIONS_FOLDER_SOURCE;
	private boolean enabled = true;

	/**
     * Constructs the source provider.
     */
	public CompositionsFolderActive() {
	}
	
	@Override
	public void initialize(IServiceLocator locator) {
		super.initialize(locator);
		PlatformUI.getWorkbench().addWindowListener(this);
	}
	
	@Override
	public void dispose() {
		PlatformUI.getWorkbench().removeWindowListener(this);

	}

	@Override
	public Map<String, Object> getCurrentState() {
		 Map<String, Object> map = new HashMap<String, Object>(1);
		 map.put(MY_STATE, enabled);
		 return map;
	}

	@Override
	public String[] getProvidedSourceNames() {
		return new String[] { MY_STATE };
	}
	

	@Override
	public void selectionChanged(IWorkbenchPart part, ISelection selection) {
		if(selection instanceof TreeSelection) {
    		TreeSelection treeSelection = (TreeSelection) selection;
    		Object element = treeSelection.getFirstElement();
    		if(element != null && element instanceof IFolder) {
    		    IFolder folder = (IFolder)element;
    		    IProject project = folder.getProject();
    			String compositionsPath = ProjectUtils.getProjectCompositionsPath(project);
    			
    			enabled = (!folder.getFullPath().toPortableString().equals(compositionsPath));
    		} else {
    			enabled = true;
    		}
    		fireSourceChanged(ISources.WORKBENCH, MY_STATE, enabled);
		}
	}

	@Override
	public void windowActivated(IWorkbenchWindow window) {
		window.getSelectionService().addSelectionListener(this);
	}

	@Override
	public void windowDeactivated(IWorkbenchWindow window) {
		window.getSelectionService().removeSelectionListener(this);
	}

	@Override
	public void windowClosed(IWorkbenchWindow window) {
		// Nothing to do
	}

	@Override
	public void windowOpened(IWorkbenchWindow window) {
		// Nothing to do
	}
}