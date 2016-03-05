package eu.scasefp7.eclipse.core.ui.wizards;


import java.net.URI;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExecutableExtension;
import org.eclipse.core.runtime.QualifiedName;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.dialogs.WizardNewProjectCreationPage;

import eu.scasefp7.eclipse.core.ui.Activator;
import eu.scasefp7.eclipse.core.ui.ScaseUiConstants;
import eu.scasefp7.eclipse.core.ui.preferences.PropertyWizardPage;
import eu.scasefp7.eclipse.core.ui.preferences.internal.DomainEntry;



/**
 * @author emaorli
 *
 */
public class NewScaseProjectWizard extends Wizard implements INewWizard, IExecutableExtension {
	
	private WizardNewProjectCreationPage _pageOne;
	private PropertyWizardPage _pageTwo;
	private ProjectFoldersWizardPage _pageThree;
	private static final String PAGE_NAME = "Project name";
	private static final String WIZARD_NAME = "New S-CASE Project"; 

	/**
	 * 
	 */
	public NewScaseProjectWizard() {
		setWindowTitle(WIZARD_NAME);
	}
	

	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		// TODO Auto-generated method stub

	}
	@Override
	public void addPages() {
	    super.addPages();
	 
	    _pageOne = new WizardNewProjectCreationPage(PAGE_NAME);
	    _pageOne.setTitle("Create a S-Case Project");
	    _pageOne.setDescription("Enter project name.");
	    
	    _pageTwo = new PropertyWizardPage("S-Case project domain");
	    _pageTwo.setTitle("Select project domain");
	    _pageTwo.setDescription("Select the domain of the project from the list");
	    
	    _pageThree = new ProjectFoldersWizardPage("Project folders");
	    _pageThree.setTitle("Folders in the project");
	    _pageThree.setDescription("Configure folders that will be used to organize the project");
	    
	    addPage(_pageOne);
	    addPage(_pageTwo);
	    addPage(_pageThree);
	}
	
	@Override
	public boolean performFinish() {
		
		String name = _pageOne.getProjectName();
	    URI location = null;
	    if (!_pageOne.useDefaults()) {
	        location = _pageOne.getLocationURI();
	    } // else location == null
	 
	    IResource res = ScaseProjectSupport.createProject(name, location);
	    int k;
	    org.eclipse.swt.widgets.Label domainLabel = _pageTwo.getDomainLabel();
	    DomainEntry de = (DomainEntry) domainLabel.getData();
	    if (de == null)
	    	k = -1;
	    else
	    	k =  de.getId();
	    try {
			res.setPersistentProperty(new QualifiedName("", ScaseUiConstants.PROP_PROJECT_DOMAIN), Integer.toString(k));
		} catch (CoreException e) {
			Activator.log("Unable to set project property", e);
		}
	    
	    _pageThree.performFinish(res);
	    
		return true;
	}
	

	@Override
	public void setInitializationData(IConfigurationElement config,
			String propertyName, Object data) throws CoreException {
		// TODO Auto-generated method stub
		
	}

}