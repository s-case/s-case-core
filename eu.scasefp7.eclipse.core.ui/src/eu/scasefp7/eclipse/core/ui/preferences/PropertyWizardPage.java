package eu.scasefp7.eclipse.core.ui.preferences;


import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.QualifiedName;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

import eu.scasefp7.eclipse.core.ui.ScaseUiConstants;
import eu.scasefp7.eclipse.core.ui.preferences.internal.DomainEntry;
import eu.scasefp7.eclipse.core.ui.wizards.IScaseWizardPage;


/**
 * @author emaorli
 *
 */
public class PropertyWizardPage extends WizardPage implements IScaseWizardPage  {
	ProjectDomainPropertyPage propPage;
	Composite composite;
	private static final int DOMAIN_DEFAULT = -1;
	
	
	/**
	 * @param pageName
	 */
	public PropertyWizardPage(String pageName) {
		super(pageName);
		propPage = new ProjectDomainPropertyPage();
	}


@Override
	public void createControl(Composite parent) {
		composite = new Composite(parent, SWT.NONE);
		GridLayout gl_composite = new GridLayout();
		gl_composite.marginWidth = 0;
		gl_composite.marginHeight = 0;
		composite.setLayout(gl_composite);
		GridData data = new GridData(GridData.FILL);
		data.grabExcessHorizontalSpace = true;
		composite.setLayoutData(data);
	
		int domainId = DOMAIN_DEFAULT;
		
		// Add the domain label
		propPage.createDomainLabel(composite, (domainId != DOMAIN_DEFAULT) ? domainId : null);
		
		// Add tree
		propPage.createTreeViewer(composite);
		
		setControl(composite);
	}


	/**
	 * @return
	 */
	public Label getDomainLabel(){
		return propPage.getDomainLabel();
	}


	@Override
	public boolean performFinish(IResource resource) {
		int k;
	    Label domainLabel = propPage.getDomainLabel();
	    DomainEntry de = (DomainEntry) domainLabel.getData();
	    if (de == null)
	    	k = -1;
	    else
	    	k =  de.getId();
	    try {
	    	resource.setPersistentProperty(new QualifiedName("", ScaseUiConstants.PROP_PROJECT_DOMAIN), Integer.toString(k));
		} catch (CoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true;
	}
	
	
}
