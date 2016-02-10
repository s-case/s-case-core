package eu.scasefp7.eclipse.core.ui.wizards;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;

import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;

import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.dialogs.ContainerSelectionDialog;

import eu.scasefp7.eclipse.core.ui.ScaseUiConstants;

import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.QualifiedName;


public class ScaseProjectWizardPageTwo extends WizardPage implements IScaseWizardPage{
	String modelsPath = "";
	String outputPath = "";
	String reqPath = "";
	String comPath = "";
	
	protected ScaseProjectWizardPageTwo(String pageName) {
		super(pageName);
		// TODO Auto-generated constructor stub
	}

	
	@Override
	public void createControl(Composite parent) {
		initializeDialogUnits(parent);

		final Composite composite= new Composite(parent, SWT.NULL);
		composite.setFont(parent.getFont());
		GridLayout layout = new GridLayout();
		layout.numColumns = 3;
		composite.setLayout(layout);
        composite.setLayoutData(new GridData(SWT.BEGINNING));
        
        Label a = new Label(composite, SWT.NULL);
        a.setText ("The models will be stored in folder:");
        GridData gridData1 = new GridData();
        gridData1.horizontalAlignment = GridData.FILL;
        gridData1.horizontalSpan = 3;
        a.setLayoutData(gridData1);
        
        Label label = new Label(composite, SWT.NULL);
        Text models = new Text(composite, SWT.BORDER);
        GridData gridData = new GridData();
		gridData.horizontalAlignment = SWT.FILL;
		gridData.grabExcessHorizontalSpace = true;
       
        final Button button = new Button(composite, SWT.PUSH);
        
        label.setText("Models folder path: ");
        models.setText("/models");
        button.setText("Browse...");
        models.setLayoutData(gridData);


        IContainer root = ResourcesPlugin.getWorkspace().getRoot();
        button.addListener(SWT.Selection, new Listener() {
        	@Override
            public void handleEvent(Event e) {
              Path resPath;
			switch (e.type) {
              case SWT.Selection:

            	  ContainerSelectionDialog  dlg = new ContainerSelectionDialog (parent.getShell(), root, true, null);
            	  dlg.setMessage("Select a directory");
            	   dlg.open();
            	   Object[] res = dlg.getResult();
            	   if(res != null)
            	   {
            		   resPath = (Path ) res[0];
            		   models.setText(resPath.toString()); 
            		   modelsPath = resPath.toString();
            	   }

                break;
              }
            }

          });
        
        //Empty row
        Label e1 = new Label(composite, SWT.NULL);
        GridData gridData11 = new GridData();
        gridData11.horizontalAlignment = GridData.FILL;
        gridData11.horizontalSpan = 3;
        e1.setLayoutData(gridData11);
        
        Label a2 = new Label(composite, SWT.NULL);
        a2.setText ("The generated code will be stored in folder:");
        GridData gridData2 = new GridData();
        gridData2.horizontalAlignment = GridData.FILL;
        gridData2.horizontalSpan = 3;
        a2.setLayoutData(gridData2);
        
        Label label2 = new Label(composite, SWT.NULL);
        Text output = new Text(composite,  SWT.BORDER);
        final Button button2 = new Button(composite, SWT.PUSH);
        
        label2.setText("Output folder path: ");
        output.setText("/output");
        button2.setText("Browse...");
        output.setLayoutData(gridData);
        
        button2.addListener(SWT.Selection, new Listener() {
        	@Override
            public void handleEvent(Event e) {
              Path resPath;
			switch (e.type) {
              case SWT.Selection:

            	  ContainerSelectionDialog  dlg = new ContainerSelectionDialog (parent.getShell(), root, true, null);
            	  dlg.setMessage("Select a directory");
            	   dlg.open();
            	   Object[] res = dlg.getResult();
            	   if(res != null)
            	   {
            		   resPath = (Path ) res[0];
            		   output.setText(resPath.toString()); 
            		   outputPath = resPath.toString();
            	   }

                break;
              }
            }

          });
        
        //Empty row
        Label e2 = new Label(composite, SWT.NULL);
        GridData gridData21 = new GridData();
        gridData21.horizontalAlignment = GridData.FILL;
        gridData21.horizontalSpan = 3;
        e2.setLayoutData(gridData21);
        
        Label a3 = new Label(composite, SWT.NULL);
        a3.setText ("The requirements will be stored in folder:");
        GridData gridData3 = new GridData();
        gridData3.horizontalAlignment = GridData.FILL;
        gridData3.horizontalSpan = 3;
        a3.setLayoutData(gridData3);
        
        Label label3 = new Label(composite, SWT.NULL);
        Text requirements = new Text(composite, SWT.BORDER);
        final Button button3 = new Button(composite, SWT.PUSH);
        
        label3.setText("Requirements folder path: ");
        requirements.setText("/requirements");
        button3.setText("Browse...");
        requirements.setLayoutData(gridData);
        
        button3.addListener(SWT.Selection, new Listener() {
        	@Override
            public void handleEvent(Event e) {
              Path resPath;
			switch (e.type) {
              case SWT.Selection:

            	  ContainerSelectionDialog  dlg = new ContainerSelectionDialog (parent.getShell(), root, true, null);
            	  dlg.setMessage("Select a directory");
            	   dlg.open();
            	   Object[] res = dlg.getResult();
            	   if(res != null)
            	   {
            		   resPath = (Path ) res[0];
            		   requirements.setText(resPath.toString()); 
            		   reqPath = resPath.toString();
            	   }

                break;
              }
            }

          });
        
        //Empty row
        Label e3 = new Label(composite, SWT.NULL);
        GridData gridData31 = new GridData();
        gridData31.horizontalAlignment = GridData.FILL;
        gridData31.horizontalSpan = 3;
        e3.setLayoutData(gridData31);
        
        Label a4 = new Label(composite, SWT.NULL);
        a4.setText ("The storyboards used for composition will be in folder:");
        GridData gridData4 = new GridData();
        gridData4.horizontalAlignment = GridData.FILL;
        gridData4.horizontalSpan = 3;
        a4.setLayoutData(gridData4);
        
        Label label4 = new Label(composite, SWT.NULL);
        Text compositions = new Text(composite, SWT.BORDER);
        final Button button4 = new Button(composite, SWT.PUSH);
        
        label4.setText("Compositions folder path: ");
        compositions.setText("/compositions");
        button4.setText("Browse...");
        compositions.setLayoutData(gridData);
        
        button4.addListener(SWT.Selection, new Listener() {
        	@Override
            public void handleEvent(Event e) {
              Path resPath;
			switch (e.type) {
              case SWT.Selection:

            	  ContainerSelectionDialog  dlg = new ContainerSelectionDialog (parent.getShell(), root, true, null);
            	  dlg.setMessage("Select a directory");
            	   dlg.open();
            	   Object[] res = dlg.getResult();
            	   if(res != null)
            	   {
            		   resPath = (Path ) res[0];
            		   compositions.setText(resPath.toString()); 
            		   comPath = resPath.toString();
            	   }

                break;
              }
            }

          });
        


		setControl(composite);
	}
	

	@Override
	public boolean performFinish(IResource resource) {
		IProject project = (IProject) resource;
		IFolder models   = project.getFolder("models");
		IFolder output   = project.getFolder("output");
		IFolder requirements = project.getFolder("requirements");
		IFolder compositions = project.getFolder("compositions");
		
		IProgressMonitor monitor = new NullProgressMonitor();
		try {
			models.create(true, true, monitor);
			output.create(true, true, monitor);
			requirements.create(true, true, monitor);
			compositions.create(true, true, monitor);
			
		} catch (CoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String projectName = project.getName();
		if(modelsPath.equals(""))
			modelsPath = "/" + projectName + "/models";
		if(outputPath.equals(""))
			outputPath = "/" + projectName + "/output";
		if(reqPath.equals(""))
			reqPath = "/" + projectName + "/requirements";
		if(comPath.equals(""))
			comPath = "/" + projectName + "/compositions";
		
		try {
			project.setPersistentProperty(new QualifiedName("", ScaseUiConstants.MODELS_FOLDER), modelsPath);
			project.setPersistentProperty(new QualifiedName("", ScaseUiConstants.OUTPUT_FOLDER), outputPath);
			project.setPersistentProperty(new QualifiedName("", ScaseUiConstants.REQUIREMENTS_FOLDER), reqPath);
			project.setPersistentProperty(new QualifiedName("", ScaseUiConstants.COMPOSITIONS_FOLDER), comPath);
		} catch (CoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

}
