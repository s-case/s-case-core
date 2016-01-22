package eu.scasefp7.eclipse.core.ui.wizards;


import java.lang.reflect.Array;
import java.net.URI;
import java.util.ArrayList;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExecutableExtension;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.IRegistryEventListener;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.QualifiedName;
import org.eclipse.core.runtime.RegistryFactory;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.dialogs.WizardNewProjectCreationPage;

import eu.scasefp7.eclipse.core.ui.ScaseUiConstants;
import eu.scasefp7.eclipse.core.ui.preferences.PropertyWizardPage;
import eu.scasefp7.eclipse.core.ui.preferences.internal.DomainEntry;




public class NewScaseProjectWizard2 extends Wizard implements INewWizard, IExecutableExtension, IRegistryEventListener {
	
	private WizardNewProjectCreationPage _pageOne;
	private PropertyWizardPage _propertyPage;
	private boolean propertyPageSet = false;
	
	private static final String PAGE_NAME = "Project name";
	private static final String WIZARD_NAME = "New S-CASE Project"; 
	private static final String CONTRIBUTION_PAGE = "page";
   

	/**
	 * 
	 */
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
		
		 // Create the configured items
        for (IConfigurationElement elem : contributions) {
            if(elem.getName().equals(CONTRIBUTION_PAGE)) {
            	String classAttr = elem.getAttribute("class");
            	String description = elem.getAttribute("description");
                String title = elem.getAttribute("name");
                
                if(classAttr.equals("org.eclipse.ui.dialogs.WizardNewProjectCreationPage")){
                	_pageOne = new WizardNewProjectCreationPage(PAGE_NAME);
                	_pageOne.setTitle(title);
                	_pageOne.setDescription(description);
        	    }
                if(classAttr.equals("eu.scasefp7.eclipse.core.ui.preferences.PropertyWizardPage")){
                	_propertyPage = new PropertyWizardPage(title);
            	    _propertyPage.setTitle(description);
            	    propertyPageSet = true;
        	    }
                	
            }

        }
	    
	    addPage(_pageOne);
	    
	    if(propertyPageSet)
	    	addPage(_propertyPage);
	}
	
	@Override
	public boolean performFinish() {
		
		String name = _pageOne.getProjectName();
	    URI location = null;
	    if (!_pageOne.useDefaults()) {
	        location = _pageOne.getLocationURI();
	    } // else location == null
	 
	    IResource res = ScaseProjectSupport.createProject(name, location);
	    if(propertyPageSet){
	    	int k;
		    org.eclipse.swt.widgets.Label domainLabel = _propertyPage.getDomainLabel();
		    DomainEntry de = (DomainEntry) domainLabel.getData();
		    if (de == null)
		    	k = -1;
		    else
		    	k =  de.getId();
		    try {
				res.setPersistentProperty(new QualifiedName("", ScaseUiConstants.PROP_PROJECT_DOMAIN), Integer.toString(k));
			} catch (CoreException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    }
	      
	    
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