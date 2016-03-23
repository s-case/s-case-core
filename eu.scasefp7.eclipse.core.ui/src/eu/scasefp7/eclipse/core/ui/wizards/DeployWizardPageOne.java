package eu.scasefp7.eclipse.core.ui.wizards;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;

/**
 * @author Marin Orlic
 */
public class DeployWizardPageOne extends WizardPage {
	private String nextPage = "two";
	protected DeployWizardPageOne(String pageName) {
		super(pageName);
		setTitle(pageName);
	}

	@Override
	public void createControl(Composite parent) {
		final Composite composite = new Composite(parent, SWT.NONE);
		composite.setFont(parent.getFont());
		GridLayout layout = new GridLayout(2, false);
		composite.setLayout(layout);
        composite.setLayoutData(new GridData(SWT.BEGINNING));
        
		
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		
		Label lblLabel = new Label(composite, SWT.NONE);
		lblLabel.setText("Label1");
		new Label(composite, SWT.NONE);
		
		Button btnCompile = new Button(composite, SWT.NONE);
		btnCompile.setText("Compile");
		btnCompile.addListener(SWT.Selection, new Listener() {
        	@Override
            public void handleEvent(Event e) {
			switch (e.type) {
              case SWT.Selection:
            	  //TODO define what happens here
            	  System.out.println("Compile button pressed");
                break;
              }
            }

          });
		new Label(composite, SWT.NONE);
		
		Button btnRadioButton = new Button(composite, SWT.RADIO);
		btnRadioButton.setText("Radio Button1");
		btnRadioButton.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				boolean isSelected = ((Button)e.getSource()).getSelection();
		          if(isSelected)   
		        	  nextPage = "two";
			}
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
			}
		});
		new Label(composite, SWT.NONE);
		
		Button btnRadioButton_1 = new Button(composite, SWT.RADIO);
		btnRadioButton_1.setText("Radio Button2");
		btnRadioButton_1.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				boolean isSelected = ((Button)e.getSource()).getSelection();
		          if(isSelected)   
			        	  nextPage = "three";

			}
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
			}
		});
		
		this.setControl(composite);
		setPageComplete(false);

	}
	public String getNextPageName() {
		return nextPage;
	}
	@Override
	public boolean canFlipToNextPage() {
		return true;
	}

}
