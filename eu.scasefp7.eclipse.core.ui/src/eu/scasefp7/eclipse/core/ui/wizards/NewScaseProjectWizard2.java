package eu.scasefp7.eclipse.core.ui.wizards;


import java.net.URI;
import java.util.ArrayList;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.dialogs.WizardNewProjectCreationPage;

import eu.scasefp7.eclipse.core.ui.ScaseUiConstants;

/**
 * @author Leonora Gaspar
 * @author Marin Orlic
 */
public class NewScaseProjectWizard2 extends Wizard implements INewWizard {
	
	private static final String CONTRIBUTION_CLASS = "class";
    private static final String CONTRIBUTION_TITLE = "title";
    private static final String CONTRIBUTION_DESCRIPTION = "description";
    private static final String CONTRIBUTION_PAGE = "page";

    private static final String PAGE_NAME = "Project name";
    private static final String WIZARD_NAME = "New S-CASE Project";     
    
    private WizardNewProjectCreationPage _pageOne;
	//private PropertyWizardPage _pageTwo;
	private IProjectWizardPage[] _pages = null;
	
	/**
	 * 
	 */
	public NewScaseProjectWizard2() {
		setWindowTitle(WIZARD_NAME);
	}
	

	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {

	}
	
	@Override
	public void addPages() {
	    super.addPages();
	    IExtensionRegistry registry = Platform.getExtensionRegistry();
		IConfigurationElement[] contributions = registry.getConfigurationElementsFor(ScaseUiConstants.NEWPROJECTPAGES_EXTENSION);
		ArrayList<IProjectWizardPage> pages = new ArrayList<IProjectWizardPage>();
		
		// Add first page to be able to create a project
		_pageOne = new WizardNewProjectCreationPage(PAGE_NAME);
        _pageOne.setTitle("Create a new S-CASE Project");
        _pageOne.setDescription("Enter project name.");
        addPage(_pageOne);
        
        // Add contributions
        if(contributions.length > 0) {
            // Create the configured items
            for (IConfigurationElement elem : contributions) {
                if(elem.getName().equals(CONTRIBUTION_PAGE)) {
                    try {
                        String description = elem.getAttribute(CONTRIBUTION_DESCRIPTION);
                        String title = elem.getAttribute(CONTRIBUTION_TITLE);
        
                        IProjectWizardPage page;               
                        page = (IProjectWizardPage) elem.createExecutableExtension(CONTRIBUTION_CLASS);
                    
                        page.setTitle(title);
                        page.setDescription(description);
                        pages.add(page);
                        addPage(page);
                    } catch (CoreException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }                    
                }
            }
        }
        
        _pages = (IProjectWizardPage[]) pages.toArray();
	}
	
	@Override
	public boolean performFinish() {
		
		String name = _pageOne.getProjectName();
	    URI location = null;
	    if (!_pageOne.useDefaults()) {
	        location = _pageOne.getLocationURI();
	    } // else location == null
	 
	    IResource res = ScaseProjectSupport.createProject(name, location);
	    
	    for(IProjectWizardPage page : _pages) {
	        if(!page.performFinish(res)) {
	            return false;
	        }
	    }

		return true;
	}
}