package eu.scasefp7.eclipse.core.ui.wizards;

import java.net.MalformedURLException;
import java.net.URL;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Link;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.browser.IWebBrowser;

public class DeployWizardPageThree extends WizardPage{

	protected DeployWizardPageThree(String pageName) {
		super(pageName);
		setTitle(pageName);
	}

	@Override
	public void createControl(Composite parent) {
		final Composite composite = new Composite(parent, SWT.NULL);
		composite.setFont(parent.getFont());
		GridLayout layout = new GridLayout();
		layout.numColumns = 3;
		composite.setLayout(layout);
        composite.setLayoutData(new GridData(SWT.BEGINNING));
        
		
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		
		Label lblLabel = new Label(composite, SWT.NONE);
		lblLabel.setText("label1");
		new Label(composite, SWT.NONE);
		
		String url = "http://s-case.github.io/";
		Link link = new Link(composite, SWT.NONE);
		link.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1));
		link.setText("<a href=\"#\">" + url + "</a>"); //$NON-NLS-1$ //$NON-NLS-2$
		  link.addSelectionListener(new SelectionAdapter() {
		    public void widgetSelected(SelectionEvent e) {
		    	IWebBrowser browser;
				try {
					browser = PlatformUI.getWorkbench().getBrowserSupport().createBrowser("browser");
					browser.openURL(new URL(url));
				} catch (PartInitException | MalformedURLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
		    }      
		  });
		
		this.setControl(composite);
		setPageComplete(false);
		
	}
	
	@Override
	public boolean canFlipToNextPage() {
		return false;
	}
	

}
