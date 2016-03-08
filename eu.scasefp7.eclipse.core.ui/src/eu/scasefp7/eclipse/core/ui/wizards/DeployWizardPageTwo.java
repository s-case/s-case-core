package eu.scasefp7.eclipse.core.ui.wizards;

import java.net.MalformedURLException;
import java.net.URL;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
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

import eu.scasefp7.eclipse.core.ui.Activator;
import org.eclipse.swt.widgets.Text;

/**
 * Wizard intended to help users deploy generated services.
 * 
 * @author Leonora Gaspar
 * @author Marin Orlic
 */
public class DeployWizardPageTwo extends WizardPage {

	protected DeployWizardPageTwo(String pageName) {
		super(pageName);
		setTitle(pageName);
	}

	@Override
	public void createControl(Composite parent) {
		final Composite composite = new Composite(parent, SWT.NONE);
		initializeDialogUnits(parent);
		composite.setFont(parent.getFont());
		
		GridLayout layout = new GridLayout();
		layout.marginWidth = 0;
		layout.marginHeight = 0;
		composite.setLayout(layout);
        composite.setLayoutData(new GridData(GridData.FILL_BOTH));
		
		new Label(composite, SWT.NONE);
		
		StyledText styledText = new StyledText (composite,  SWT.MULTI);
		styledText.setEditable(false);
		//styledText.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		styledText.setText("To start the generated web service, create a WAR file using Maven with the goal 'package'.\n "
				+ "The created WAR file is ready to be deployed on a web server that supports Java JAX-RS web service.\n"
				+ "Your web service will connect to the database using credentials configured in the project.\n");
		
		String url = "http://s-case.github.io/webbook.html";
		this.setControl(composite);
		new Label(composite, SWT.NONE);
		
		Label lblMoreInformation = new Label(composite, SWT.NONE);
		lblMoreInformation.setText("More information:");
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
					Activator.log("Unable to open browser.", e1);
				}
				
		  }      
		});
		setPageComplete(true);
		
	}
}
