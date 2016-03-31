package eu.scasefp7.eclipse.core.ui.wizards;


import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExecutableExtension;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.IRegistryEventListener;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.RegistryFactory;
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
public class NewScaseProjectWizard2 extends Wizard implements INewWizard, IExecutableExtension, IRegistryEventListener {
	
	private static final String CONTRIBUTION_CLASS = "class";
    private static final String CONTRIBUTION_TITLE = "title";
    private static final String CONTRIBUTION_DESCRIPTION = "description";
    private static final String CONTRIBUTION_PAGE = "page";

    private static final String PAGE_NAME = "Project name";
    private static final String WIZARD_NAME = "New S-CASE Project";     
    
    private WizardNewProjectCreationPage _pageOne;
	
	
    private List<IScaseWizardPage> _pages = new ArrayList<IScaseWizardPage>();
	

	public NewScaseProjectWizard2() {
		setWindowTitle(WIZARD_NAME);
		//RegistryFactory.getRegistry().addListener(this, ScaseUiConstants.NEWPROJECT_EXTENSION);
	}
	

	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {

	}
	
	@Override
	public void addPages() {
	    super.addPages();
	    IExtensionRegistry registry = Platform.getExtensionRegistry();
		IConfigurationElement[] contributions = registry.getConfigurationElementsFor(ScaseUiConstants.NEWPROJECTPAGES_EXTENSION);
		
		// Add first page to be able to create a project
		_pageOne = new WizardNewProjectCreationPage(PAGE_NAME);
		_pageOne.setTitle("Create a new S-CASE Project");
	    _pageOne.setDescription("Enter project name.");
	    addPage(_pageOne);
		
	    // Add contributions
        if (contributions.length > 0) {
        	// Create the configured items
			for (IConfigurationElement elem : contributions) {
				if (elem.getName().equals(CONTRIBUTION_PAGE)) {

					String className = elem.getAttribute(CONTRIBUTION_CLASS);
                    String description = elem.getAttribute(CONTRIBUTION_DESCRIPTION);
                    String title = elem.getAttribute(CONTRIBUTION_TITLE);

					try {
						Class<? extends IScaseWizardPage> c = Class.forName(className).asSubclass(IScaseWizardPage.class);
						IScaseWizardPage p = (IScaseWizardPage) c.getDeclaredConstructor(String.class).newInstance(title);
						
						p.setTitle(title);
						p.setDescription(description);
						
						_pages.add(p);
						addPage(p);

					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}

			} 
		}
        
	    
	    
	}
	
	@Override
	public boolean performFinish() {
		
		String name = _pageOne.getProjectName();
	    URI location = null;
	    if (!_pageOne.useDefaults()) {
	        location = _pageOne.getLocationURI();
	    } 
	 
	    IResource res = ScaseProjectSupport.createProject(name, location);
	 
	    for (IScaseWizardPage page : _pages) 
			page.performFinish(res);
	    
		return true;
	}
	

	@Override
	public void setInitializationData(IConfigurationElement config,
			String propertyName, Object data) throws CoreException {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void added(IExtension[] extensions) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void removed(IExtension[] extensions) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void added(IExtensionPoint[] extensionPoints) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void removed(IExtensionPoint[] extensionPoints) {
		// TODO Auto-generated method stub
		
	}

}