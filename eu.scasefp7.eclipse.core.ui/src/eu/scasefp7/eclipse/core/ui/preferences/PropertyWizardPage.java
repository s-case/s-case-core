package eu.scasefp7.eclipse.core.ui.preferences;


import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;


/**
 * @author emaorli
 *
 */
public class PropertyWizardPage extends WizardPage  {
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
	
	
}