package eu.scasefp7.eclipse.core.ui.preferences;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.QualifiedName;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.dialogs.ContainerSelectionDialog;
import org.eclipse.ui.dialogs.ISelectionValidator;
import org.eclipse.ui.dialogs.PropertyPage;

import eu.scasefp7.eclipse.core.ui.Activator;
import eu.scasefp7.eclipse.core.ui.ScaseUiConstants;

/**
 * Property page to configure project folders (output, models, requirements, compositions).
 * 
 * @author Leonora Gaspar
 *
 */
public class ProjectFoldersPreferencePage extends PropertyPage {
	String modelsPath = "";
	String outputPath = "";
	String reqPath = "";
	String comPath = "";

	Text models;
	Text output;
	Text requirements;
	Text compositions;

	@Override
	protected Control createContents(Composite parent) {
		IProject project = null;
		IAdaptable element = getElement();
		if (element instanceof IProject) {
			project = (IProject) element;
		} else {
			Object resource = element.getAdapter(IResource.class);
			if (resource instanceof IProject) {
				project = (IProject) resource;
			} else {
				Activator.log("Unable to read project properties.", null); //$NON-NLS-1$
			}
		}
		try {
			modelsPath = project.getPersistentProperty(new QualifiedName("", ScaseUiConstants.MODELS_FOLDER)); //$NON-NLS-1$
			outputPath = project.getPersistentProperty(new QualifiedName("", ScaseUiConstants.OUTPUT_FOLDER)); //$NON-NLS-1$
			reqPath = project.getPersistentProperty(new QualifiedName("", ScaseUiConstants.REQUIREMENTS_FOLDER)); //$NON-NLS-1$
			comPath = project.getPersistentProperty(new QualifiedName("", ScaseUiConstants.COMPOSITIONS_FOLDER)); //$NON-NLS-1$
		} catch (CoreException exc) {
			Activator.log("Unable to read project properties.", exc); //$NON-NLS-1$
		}
		try {
			if (modelsPath == null) {
				modelsPath = project.getFolder("models").exists() ? "models" : "";
				project.setPersistentProperty(new QualifiedName("", ScaseUiConstants.MODELS_FOLDER), modelsPath);
			}
			if (outputPath == null) {
				outputPath = project.getFolder("output").exists() ? "output" : "";
				project.setPersistentProperty(new QualifiedName("", ScaseUiConstants.OUTPUT_FOLDER), outputPath);
			}
			if (reqPath == null) {
				reqPath = project.getFolder("requirements").exists() ? "requirements" : "";
				project.setPersistentProperty(new QualifiedName("", ScaseUiConstants.REQUIREMENTS_FOLDER), reqPath);
			}
			if (comPath == null) {
				comPath = project.getFolder("compositions").exists() ? "compositions" : "";
				project.setPersistentProperty(new QualifiedName("", ScaseUiConstants.COMPOSITIONS_FOLDER), comPath);
			}
		} catch (CoreException e4) {
			e4.printStackTrace();
		}

		initializeDialogUnits(parent);

		final Composite composite = new Composite(parent, SWT.NULL);
		composite.setFont(parent.getFont());
		GridLayout layout = new GridLayout();
		layout.numColumns = 3;
		composite.setLayout(layout);
		composite.setLayoutData(new GridData(SWT.BEGINNING));

		Label a = new Label(composite, SWT.NULL);
		a.setText("The models are stored in folder:");
		GridData gridData1 = new GridData();
		gridData1.horizontalAlignment = GridData.FILL;
		gridData1.horizontalSpan = 3;
		a.setLayoutData(gridData1);

		Label label = new Label(composite, SWT.NULL);
		models = new Text(composite, SWT.BORDER);
		GridData gridData = new GridData();
		gridData.horizontalAlignment = SWT.FILL;
		gridData.grabExcessHorizontalSpace = true;

		final Button button = new Button(composite, SWT.PUSH);

		label.setText("Models folder path: ");
		models.setText(modelsPath);
		models.setEnabled(false);
		button.setText("Browse...");
		models.setLayoutData(gridData);

		IContainer root = ResourcesPlugin.getWorkspace().getRoot();
		final IProject currentProject = project;
		ISelectionValidator validator = new ISelectionValidator() {
			@Override
			public String isValid(Object selection) {
				try {
					IFolder folder = root.getFolder((IPath) selection);
					if (!folder.getProject().equals(currentProject))
						return "You must select a folder inside your project";
				} catch (IllegalArgumentException e) {
					if (!currentProject.getFullPath().equals(selection))
						return "You must select a folder inside your project";
				}
				return null;
			}
		};
		button.addListener(SWT.Selection, new Listener() {
			@Override
			public void handleEvent(Event e) {
				Path resPath;
				switch (e.type) {
				case SWT.Selection:

					ContainerSelectionDialog dlg;
					if (modelsPath.equals(""))
						dlg = new ContainerSelectionDialog(parent.getShell(), currentProject, false, null);
					else
						dlg = new ContainerSelectionDialog(parent.getShell(), currentProject.getFolder(new Path(
								modelsPath)), false, null);
					dlg.setValidator(validator);
					dlg.setMessage("Select a folder");
					dlg.open();
					Object[] res = dlg.getResult();
					if (res != null) {
						resPath = (Path) res[0];
						String resPathString = "";
						for (int i = 1; i < resPath.segmentCount(); i++) {
							resPathString += resPath.segment(i) + "/";
						}
						if (resPathString.length() > 0)
							resPathString = resPathString.substring(0, resPathString.length() - 1);
						models.setText(resPathString);
						modelsPath = resPathString;
					}

					break;
				}
			}

		});

		// Empty row
		Label e1 = new Label(composite, SWT.NULL);
		GridData gridData11 = new GridData();
		gridData11.horizontalAlignment = GridData.FILL;
		gridData11.horizontalSpan = 3;
		e1.setLayoutData(gridData11);

		Label a2 = new Label(composite, SWT.NULL);
		a2.setText("The generated code is stored in folder:");
		GridData gridData2 = new GridData();
		gridData2.horizontalAlignment = GridData.FILL;
		gridData2.horizontalSpan = 3;
		a2.setLayoutData(gridData2);

		GridData gridData3 = new GridData();
		gridData3.horizontalAlignment = SWT.FILL;
		gridData3.grabExcessHorizontalSpace = true;
		Label label2 = new Label(composite, SWT.NULL);
		output = new Text(composite, SWT.BORDER);
		final Button button2 = new Button(composite, SWT.PUSH);

		label2.setText("Output folder path: ");
		output.setText(outputPath);
		output.setEnabled(false);
		button2.setText("Browse...");
		output.setLayoutData(gridData3);

		button2.addListener(SWT.Selection, new Listener() {
			@Override
			public void handleEvent(Event e) {
				Path resPath;
				switch (e.type) {
				case SWT.Selection:

					ContainerSelectionDialog dlg;
					if (outputPath.equals(""))
						dlg = new ContainerSelectionDialog(parent.getShell(), currentProject, false, null);
					else
						dlg = new ContainerSelectionDialog(parent.getShell(), currentProject.getFolder(new Path(
								outputPath)), false, null);
					dlg.setValidator(validator);
					dlg.setMessage("Select a folder");
					dlg.open();
					Object[] res = dlg.getResult();
					if (res != null) {
						resPath = (Path) res[0];
						String resPathString = "";
						for (int i = 1; i < resPath.segmentCount(); i++) {
							resPathString += resPath.segment(i) + "/";
						}
						if (resPathString.length() > 0)
							resPathString = resPathString.substring(0, resPathString.length() - 1);
						output.setText(resPathString);
						outputPath = resPathString;
					}
					break;
				}
			}

		});

		// Empty row
		Label e2 = new Label(composite, SWT.NULL);
		GridData gridData21 = new GridData();
		gridData21.horizontalAlignment = GridData.FILL;
		gridData21.horizontalSpan = 3;
		e2.setLayoutData(gridData21);

		Label a3 = new Label(composite, SWT.NULL);
		a3.setText("The requirements are stored in folder:");

		GridData gridData31 = new GridData();
		gridData31.horizontalAlignment = GridData.FILL;
		gridData31.horizontalSpan = 3;
		a3.setLayoutData(gridData31);

		GridData gridData4 = new GridData();
		gridData4.horizontalAlignment = SWT.FILL;
		gridData4.grabExcessHorizontalSpace = true;
		Label label3 = new Label(composite, SWT.NULL);
		requirements = new Text(composite, SWT.BORDER);
		final Button button3 = new Button(composite, SWT.PUSH);

		label3.setText("Requirements folder path: ");
		requirements.setText(reqPath);
		requirements.setEnabled(false);
		button3.setText("Browse...");
		requirements.setLayoutData(gridData4);

		button3.addListener(SWT.Selection, new Listener() {
			@Override
			public void handleEvent(Event e) {
				Path resPath;
				switch (e.type) {
				case SWT.Selection:

					ContainerSelectionDialog dlg;
					if (reqPath.equals(""))
						dlg = new ContainerSelectionDialog(parent.getShell(), currentProject, false, null);
					else
						dlg = new ContainerSelectionDialog(parent.getShell(), currentProject
								.getFolder(new Path(reqPath)), false, null);
					dlg.setValidator(validator);
					dlg.setMessage("Select a folder");
					dlg.open();
					Object[] res = dlg.getResult();
					if (res != null) {
						resPath = (Path) res[0];
						String resPathString = "";
						for (int i = 1; i < resPath.segmentCount(); i++) {
							resPathString += resPath.segment(i) + "/";
						}
						if (resPathString.length() > 0)
							resPathString = resPathString.substring(0, resPathString.length() - 1);
						requirements.setText(resPathString);
						reqPath = resPathString;
					}

					break;
				}
			}

		});

		// Empty row
		Label e3 = new Label(composite, SWT.NULL);
		GridData gridData32 = new GridData();
		gridData32.horizontalAlignment = GridData.FILL;
		gridData32.horizontalSpan = 3;
		e3.setLayoutData(gridData32);

		Label a4 = new Label(composite, SWT.NULL);
		a4.setText("The storyboards used for composition are in folder:");
		GridData gridData41 = new GridData();
		gridData41.horizontalAlignment = GridData.FILL;
		gridData41.horizontalSpan = 3;
		a4.setLayoutData(gridData41);

		Label label4 = new Label(composite, SWT.NULL);
		compositions = new Text(composite, SWT.BORDER);
		final Button button4 = new Button(composite, SWT.PUSH);

		GridData gridData6 = new GridData();
		gridData6.horizontalAlignment = SWT.FILL;
		gridData6.grabExcessHorizontalSpace = true;

		label4.setText("Compositions folder path: ");
		compositions.setText(comPath);
		compositions.setEnabled(false);
		button4.setText("Browse...");
		compositions.setLayoutData(gridData6);

		button4.addListener(SWT.Selection, new Listener() {
			@Override
			public void handleEvent(Event e) {
				Path resPath;
				switch (e.type) {
				case SWT.Selection:

					ContainerSelectionDialog dlg;
					if (comPath.equals(""))
						dlg = new ContainerSelectionDialog(parent.getShell(), currentProject, false, null);
					else
						dlg = new ContainerSelectionDialog(parent.getShell(), currentProject
								.getFolder(new Path(comPath)), false, null);
					dlg.setValidator(validator);
					dlg.setMessage("Select a folder");
					dlg.open();
					Object[] res = dlg.getResult();
					if (res != null) {
						resPath = (Path) res[0];
						String resPathString = "";
						for (int i = 1; i < resPath.segmentCount(); i++) {
							resPathString += resPath.segment(i) + "/";
						}
						if (resPathString.length() > 0)
							resPathString = resPathString.substring(0, resPathString.length() - 1);
						compositions.setText(resPathString);
						comPath = resPathString;
					}

					break;
				}
			}

		});

		// setControl(composite);
		return composite;
	}

	@Override
	public boolean performOk() {
		IProject project = null;
		IAdaptable element = getElement();
		if (element instanceof IProject) {
			project = (IProject) element;
		} else {
			Object resource = element.getAdapter(IResource.class);
			if (resource instanceof IProject) {
				project = (IProject) resource;
			} else {
				Activator.log("Unable to set project properties.", null); //$NON-NLS-1$
			}
		}
		try {
			project.setPersistentProperty(new QualifiedName("", ScaseUiConstants.MODELS_FOLDER), modelsPath);
			project.setPersistentProperty(new QualifiedName("", ScaseUiConstants.OUTPUT_FOLDER), outputPath);
			project.setPersistentProperty(new QualifiedName("", ScaseUiConstants.REQUIREMENTS_FOLDER), reqPath);
			project.setPersistentProperty(new QualifiedName("", ScaseUiConstants.COMPOSITIONS_FOLDER), comPath);

		} catch (CoreException e) {
			Activator.log("Unable to set project properties.", e);
		}

		return super.performOk();
	}

	@Override
	protected void performDefaults() {
		IProject project = null;
		IAdaptable element = getElement();
		if (element instanceof IProject) {
			project = (IProject) element;
		} else {
			Object resource = element.getAdapter(IResource.class);
			if (resource instanceof IProject) {
				project = (IProject) resource;
			} else {
				Activator.log("Unable to set project properties.", null); //$NON-NLS-1$
			}
		}
		try {
			modelsPath = project.getFolder("models").exists() ? "models" : "";
			outputPath = project.getFolder("output").exists() ? "output" : "";
			reqPath = project.getFolder("requirements").exists() ? "requirements" : "";
			comPath = project.getFolder("compositions").exists() ? "compositions" : "";
			project.setPersistentProperty(new QualifiedName("", ScaseUiConstants.MODELS_FOLDER), modelsPath);
			project.setPersistentProperty(new QualifiedName("", ScaseUiConstants.OUTPUT_FOLDER), outputPath);
			project.setPersistentProperty(new QualifiedName("", ScaseUiConstants.REQUIREMENTS_FOLDER), reqPath);
			project.setPersistentProperty(new QualifiedName("", ScaseUiConstants.COMPOSITIONS_FOLDER), comPath);
			models.setText(modelsPath);
			output.setText(outputPath);
			requirements.setText(reqPath);
			compositions.setText(comPath);
		} catch (CoreException e) {
			Activator.log("Unable to set project properties.", null); //$NON-NLS-1$
		}
		super.performDefaults();
	}
}
