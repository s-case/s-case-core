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
import org.eclipse.wb.swt.SWTResourceManager;

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
		layout.numColumns = 2;
		layout.marginWidth = 0;
		layout.marginHeight = 0;
		composite.setLayout(layout);
        composite.setLayoutData(new GridData(GridData.FILL_BOTH));
        
        
		
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		
		
		StyledText styledText = new StyledText (composite,  SWT.MULTI);
		styledText.setEditable(false);
		styledText.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		styledText.setText("Lorem ipsum dolor sit amet, consectetur adipiscing elit.\n "
				+ "Sed at ante. Mauris eleifend, quam a vulputate dictum, massa quam dapibus leo,\n"
				+ "eget vulputate orci purus ut lorem. In fringilla mi in ligula.\n "
				+ "Pellentesque aliquam quam vel dolor. Nunc adipiscing.\n"
				+ "sagittis et, lacinia at, venenatis non, arcu. Nunc nec libero. In cursus dictum risus.\n"
				+ "Etiam tristique nisl a nulla. Ut a orci. Curabitur dolor nunc, egestas at, accumsan at,\n"
				+ "malesuada nec, magna.\n");
		
		new Label(composite, SWT.NONE);
		
		String url = "https://github.com/s-case/s-case-core";
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
	    new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		this.setControl(composite);
		setPageComplete(false);
		
	}

}
