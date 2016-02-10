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



public class NewScaseProjectWizard2 extends Wizard implements INewWizard, IExecutableExtension, IRegistryEventListener {
	
	private WizardNewProjectCreationPage _pageOne;
	private List<IScaseWizardPage> pages = new ArrayList<IScaseWizardPage>();
	
	private static final String PAGE_NAME = "Project name";
	private static final String WIZARD_NAME = "New S-CASE Project"; 
	private static final String CONTRIBUTION_PAGE = "page";
   


	public NewScaseProjectWizard2() {
		setWindowTitle(WIZARD_NAME);
		RegistryFactory.getRegistry().addListener(this, ScaseUiConstants.NEWPROJECT_EXTENSION);
	}
	

	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {

	}
	
	@Override
	public void addPages() {
	    super.addPages();
	    IExtensionRegistry registry = Platform.getExtensionRegistry();
		IConfigurationElement[] contributions = registry.getConfigurationElementsFor(ScaseUiConstants.NEWPROJECT_EXTENSION);
		
		_pageOne = new WizardNewProjectCreationPage(PAGE_NAME);
    	_pageOne.setTitle("Create a S-Case Project");
    	_pageOne.setDescription("Enter project name.");
	    addPage(_pageOne);
		
        for (IConfigurationElement elem : contributions) {
            if(elem.getName().equals(CONTRIBUTION_PAGE)) {
            	
            	String title = elem.getAttribute("name");
            	String description = elem.getAttribute("description");
            	String qualifiedName = elem.getAttribute("class");
            	
				try {
					 Class<? extends IScaseWizardPage> c = Class.forName(qualifiedName).asSubclass (IScaseWizardPage.class);
					 IScaseWizardPage p = (IScaseWizardPage) c.getDeclaredConstructor(String.class).newInstance(title);
		             
		             p.setDescription(description);
		             p.setTitle(title);
		             //add pages to the list so we can call performFinish on them later
		             pages.add(p);
		             //add page to the wizard
		             addPage(p);
		             
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
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
	 
	    for (IScaseWizardPage page : pages) 
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