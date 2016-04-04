package eu.scasefp7.eclipse.core.ui.sourceprovider;

import java.util.HashMap;
import java.util.Map;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.QualifiedName;
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

import eu.scasefp7.eclipse.core.ui.Activator;
import eu.scasefp7.eclipse.core.ui.ScaseUiConstants;

/**
 * Resolves the models folder for the currently selected project.
 * 
 * @author Leonora Gaspar
 */
public class ModelsFolderActive extends AbstractSourceProvider implements ISelectionListener, IWindowListener   {
	
    private final static String MY_STATE = ScaseUiConstants.MODELS_FOLDER_SOURCE;
	private boolean enabled = true;

	/**
     * Constructs the source provider.
     */
	public ModelsFolderActive() {
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
		if(!(selection instanceof TreeSelection))
			return;
		TreeSelection treeSelection = (TreeSelection)selection;
		Object element = treeSelection.getFirstElement();
		if(element != null && element instanceof IFolder) {
			 IFolder folder = (IFolder)element;
			 IProject project = folder.getProject();
			 String modelsPath = "";
			try {
				modelsPath = project.getPersistentProperty(new QualifiedName("",ScaseUiConstants.MODELS_FOLDER));
			} catch (CoreException e) {
				Activator.log("Unable to read models folder path", e);
			}

			 if(folder.getFullPath().toPortableString().equals(modelsPath))
				 enabled = Boolean.FALSE;
			 else
				 enabled = Boolean.TRUE;
		 }
		else
			enabled = Boolean.TRUE;
		
		fireSourceChanged(ISources.WORKBENCH, MY_STATE, enabled);
		
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
