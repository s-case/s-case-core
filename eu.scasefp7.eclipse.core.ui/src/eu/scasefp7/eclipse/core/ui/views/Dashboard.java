package eu.scasefp7.eclipse.core.ui.views;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.commands.Command;
import org.eclipse.core.commands.CommandEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.ICommandListener;
import org.eclipse.core.commands.NotEnabledException;
import org.eclipse.core.commands.NotHandledException;
import org.eclipse.core.commands.Parameterization;
import org.eclipse.core.commands.ParameterizedCommand;
import org.eclipse.core.commands.common.NotDefinedException;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.IWorkbenchCommandConstants;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.commands.ICommandService;
import org.eclipse.ui.handlers.IHandlerService;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.ui.services.IServiceLocator;

import eu.scasefp7.eclipse.core.ui.IScaseUiConstants;
import org.eclipse.wb.swt.SWTResourceManager;


/**
 * This sample class demonstrates how to plug-in a new
 * workbench view. The view shows data obtained from the
 * model. The sample creates a dummy model on the fly,
 * but a real implementation would connect to the model
 * available either in this or another plug-in (e.g. the workspace).
 * The view is connected to the model using a content provider.
 * <p>
 * The view uses a label provider to define how model
 * objects should be presented in the view. Each
 * view can present the same model objects using
 * different labels and icons, if needed. Alternatively,
 * a single label provider can be shared between views
 * in order to ensure that objects of the same type are
 * presented in the same way everywhere.
 * <p>
 */

public class Dashboard extends ViewPart {

	/**
	 * The ID of the view as specified by the extension.
	 */
	public static final String ID = "eu.scasefp7.eclipse.core.ui.views.Dashboard";

	protected HashMap<ICommandListener, String> registeredCommandListeners = new HashMap<ICommandListener, String>(); 
	
	/**
	 * The constructor.
	 */
	public Dashboard() {
	}

	/**
	 * This is a callback that will allow us
	 * to create the viewer and initialize it.
	 */
	public void createPartControl(Composite parent) {
		
		int startR = 95,
			stopR = 0,
			startG = 197,
			stopG = 61,
			startB = 186,
			stopB = 92;
		int steps = 4;
		int step = 0;
		
				//
//		Color color1 = new Color(device, r, g, b);
//        Color color2 = Color.BLUE;
//        int steps = 30;
//        int rectWidth = 10;
//        int rectHeight = 10;
//
//        for (int i = 0; i < steps; i++) {
//            float ratio = (float) i / (float) steps;
//            int red = (int) (color2.getRed() * ratio + color1.getRed() * (1 - ratio));
//            int green = (int) (color2.getGreen() * ratio + color1.getGreen() * (1 - ratio));
//            int blue = (int) (color2.getBlue() * ratio + color1.getBlue() * (1 - ratio));
//            Color stepColor = new Color(red, green, blue);
//            Rectangle2D rect2D = new Rectangle2D.Float(rectWidth * i, 0, rectWidth, rectHeight);
//            g2.setPaint(stepColor);
//            g2.fill(rect2D);
//        }
		
		RowLayout rl_parent = new RowLayout(SWT.HORIZONTAL);
		rl_parent.pack = false;
		rl_parent.fill = true;
		rl_parent.marginWidth = 10;
		rl_parent.marginHeight = 10;
		rl_parent.spacing = 10;
		parent.setLayout(rl_parent);
		
		// Project setup
		Group groupProject = new Group(parent, SWT.NONE);
		groupProject.setBackground(SWTResourceManager.getColor(startR-(step*(startR-stopR)/steps), startG-(step*(startG-stopG)/steps), startB-(step*(startB-stopB)/steps)));
		groupProject.setText("Project setup");
		FillLayout fl_groupProject = new FillLayout(SWT.VERTICAL);
		fl_groupProject.spacing = 10;
		fl_groupProject.marginWidth = 10;
		fl_groupProject.marginHeight = 10;
		groupProject.setLayout(fl_groupProject);
		
		Button btnNature = new Button(groupProject, SWT.NONE);
		btnNature.setText("Configure project");
		btnNature.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				executeCommand("eu.scasefp7.eclipse.core.commands.testSetup");
			}
		});
		
		// Add command listener
		setupCommandListener(btnNature, "eu.scasefp7.eclipse.core.commands.testSetup");
		
		// Increment gradient step
		step++;
		
		Group groupStatic = new Group(parent, SWT.NONE);
		groupStatic.setText("Static modeling");
		groupStatic.setBackground(SWTResourceManager.getColor(startR-(step*(startR-stopR)/steps), startG-(step*(startG-stopG)/steps), startB-(step*(startB-stopB)/steps)));
		FillLayout fl_groupStatic = new FillLayout(SWT.VERTICAL);
		fl_groupStatic.spacing = 10;
		fl_groupStatic.marginWidth = 10;
		fl_groupStatic.marginHeight = 10;
		groupStatic.setLayout(fl_groupStatic);
		
		Button btnRequirements = new Button(groupStatic, SWT.NONE);
		btnRequirements.setText("Requirements");
		btnRequirements.setToolTipText("Create new textual requirements collection");
		btnRequirements.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				HashMap<String, String> args = new HashMap<String, String>();
				args.put(IWorkbenchCommandConstants.FILE_NEW_PARM_WIZARDID, IScaseUiConstants.REQUIREMENTS_EDITOR_NEWWIZARDID);
				executeCommand(IWorkbenchCommandConstants.FILE_NEW, args);
			}
		});
		
//		// Add command listener
//		setupCommandListener(btnRequirements, IWorkbenchCommandConstants.FILE_NEW);
//		
		Button btnImportUmlDiagram = new Button(groupStatic, SWT.NONE);
		btnImportUmlDiagram.setText("Import UML diagram");
		btnImportUmlDiagram.setToolTipText("Import UML diagram from an image");
		btnImportUmlDiagram.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				HashMap<String, String> args = new HashMap<String, String>();
				args.put(IWorkbenchCommandConstants.FILE_IMPORT_PARM_WIZARDID, IScaseUiConstants.UML_RECOGNIZER_NEWWIZARDID);
				executeCommand(IWorkbenchCommandConstants.FILE_IMPORT, args);
			}
		});
		
//		// Add command listener
//		setupCommandListener(btnImportUmlDiagram, IWorkbenchCommandConstants.FILE_IMPORT);
//		
		Group groupDynamic = new Group(parent, SWT.NONE);
		groupDynamic.setText("Dynamic modeling");
		groupDynamic.setBackground(SWTResourceManager.getColor(startR-(step*(startR-stopR)/steps), startG-(step*(startG-stopG)/steps), startB-(step*(startB-stopB)/steps)));
		FillLayout fl_groupDynamic = new FillLayout(SWT.VERTICAL);
		fl_groupDynamic.spacing = 10;
		fl_groupDynamic.marginWidth = 10;
		fl_groupDynamic.marginHeight = 10;
		groupDynamic.setLayout(fl_groupDynamic);
		
		Button btnStoryboards = new Button(groupDynamic, SWT.NONE);
		btnStoryboards.setText("Storyboards");
		btnStoryboards.setToolTipText("Create new storyboard diagram");
		btnStoryboards.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				HashMap<String, String> args = new HashMap<String, String>();
				args.put(IWorkbenchCommandConstants.FILE_NEW_PARM_WIZARDID, IScaseUiConstants.STORYBOARD_EDITOR_NEWWIZARDID);
				executeCommand(IWorkbenchCommandConstants.FILE_NEW, args);
			}
		});
		
//		// Add command listener
//		setupCommandListener(btnStoryboards, IWorkbenchCommandConstants.FILE_NEW);
		
		Button btnImportUmlDiagram2 = new Button(groupDynamic, SWT.NONE);
		btnImportUmlDiagram2.setText("Import UML diagram");
		btnImportUmlDiagram2.setToolTipText("Import UML diagram from an image");
		btnImportUmlDiagram2.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				HashMap<String, String> args = new HashMap<String, String>();
				args.put(IWorkbenchCommandConstants.FILE_IMPORT_PARM_WIZARDID, IScaseUiConstants.UML_RECOGNIZER_NEWWIZARDID);
				executeCommand(IWorkbenchCommandConstants.FILE_IMPORT, args);
			}
		});
		
		step++;
		// END GROUP
		
		Group grpRequirementsCompilation = new Group(parent, SWT.NONE);
		grpRequirementsCompilation.setText("Requirements compilation");
		grpRequirementsCompilation.setBackground(SWTResourceManager.getColor(startR-(step*(startR-stopR)/steps), startG-(step*(startG-stopG)/steps), startB-(step*(startB-stopB)/steps)));
		FillLayout fl_grpRequirementsCompilation = new FillLayout(SWT.VERTICAL);
		fl_grpRequirementsCompilation.spacing = 10;
		fl_grpRequirementsCompilation.marginWidth = 10;
		fl_grpRequirementsCompilation.marginHeight = 10;
		grpRequirementsCompilation.setLayout(fl_grpRequirementsCompilation);
		
		// Compile ontologies - RQS
		final Button btnRQSCompile = new Button(grpRequirementsCompilation, SWT.NONE);
		btnRQSCompile.setText("Compile requirements");
		btnRQSCompile.setToolTipText("Select a textual requirements file to compile");
		btnRQSCompile.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				executeCommand(IScaseUiConstants.REQUIREMENTS_EDITOR_COMMAND_EXPORTONTOLOGY);
			}
		});
		// Add command listener
		setupCommandListener(btnRQSCompile, IScaseUiConstants.REQUIREMENTS_EDITOR_COMMAND_EXPORTONTOLOGY);
		
		// Compile ontologies - Storyboards
		final Button btnSBDCompile = new Button(grpRequirementsCompilation, SWT.NONE);
		btnSBDCompile.setText("Compile storyboards");
		btnSBDCompile.setToolTipText("Select a storyboard file to compile");
		btnSBDCompile.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				executeCommand(IScaseUiConstants.STORYBOARD_EDITOR_COMMAND_EXPORTONTOLOGY);
			}
		});
		// Add command listener
		setupCommandListener(btnSBDCompile, IScaseUiConstants.STORYBOARD_EDITOR_COMMAND_EXPORTONTOLOGY);
		
		// Link ontologies
		final Button btnRequirements_1 = new Button(grpRequirementsCompilation, SWT.NONE);
		btnRequirements_1.setText("Link ontologies");
		btnRequirements_1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				executeCommand(IScaseUiConstants.COMMAND_EXPORTONTOLOGY);
			}
		});
		// Add command listener
		setupCommandListener(btnRequirements_1, IScaseUiConstants.COMMAND_EXPORTONTOLOGY);
		
		Button btnWebServiceComposition = new Button(grpRequirementsCompilation, SWT.NONE);
		btnWebServiceComposition.setText("Web service composition");
		
		step++;
		
		Group grpModeldrivenEngineering = new Group(parent, SWT.NONE);
		grpModeldrivenEngineering.setText("Model-driven engineering");
		grpModeldrivenEngineering.setBackground(SWTResourceManager.getColor(startR-(step*(startR-stopR)/steps), startG-(step*(startG-stopG)/steps), startB-(step*(startB-stopB)/steps)));
		FillLayout fl_grpModeldrivenEngineering = new FillLayout(SWT.VERTICAL);
		fl_grpModeldrivenEngineering.spacing = 10;
		fl_grpModeldrivenEngineering.marginWidth = 10;
		fl_grpModeldrivenEngineering.marginHeight = 10;
		grpModeldrivenEngineering.setLayout(fl_grpModeldrivenEngineering);
		
		Button btnRefine = new Button(grpModeldrivenEngineering, SWT.NONE);
		btnRefine.setText("Refine");
		btnRefine.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				executeCommand("eu.scasefp7.eclipse.core.commands.testOntology");
			}
		});
		
		// Add command listener
		setupCommandListener(btnRefine, "eu.scasefp7.eclipse.core.commands.testOntology");
		
		Group grpGenerateCode = new Group(grpModeldrivenEngineering, SWT.NONE);
		grpGenerateCode.setText("Generate code");
		FillLayout fl_grpGenerateCode = new FillLayout(SWT.VERTICAL);
		fl_grpGenerateCode.marginWidth = 10;
		fl_grpGenerateCode.spacing = 10;
		fl_grpGenerateCode.marginHeight = 10;
		grpGenerateCode.setLayout(fl_grpGenerateCode);
		
		Button btnSimple = new Button(grpGenerateCode, SWT.NONE);
		btnSimple.setText("Simple");
		
		Button btnAdvanced = new Button(grpGenerateCode, SWT.NONE);
		btnAdvanced.setText("Advanced");
		
		step++;
		
		Group grpYourest = new Group(parent, SWT.NONE);
		grpYourest.setText("YouREST");
		grpYourest.setBackground(SWTResourceManager.getColor(startR-(step*(startR-stopR)/steps), startG-(step*(startG-stopG)/steps), startB-(step*(startB-stopB)/steps)));
		FillLayout fl_grpYourest = new FillLayout(SWT.VERTICAL);
		fl_grpYourest.spacing = 10;
		fl_grpYourest.marginWidth = 10;
		fl_grpYourest.marginHeight = 10;
		grpYourest.setLayout(fl_grpYourest);
		
		Button btnDeploy = new Button(grpYourest, SWT.NONE);
		btnDeploy.setText("Deploy");
			
		hookContextMenu();
		contributeToActionBars();
	}

	private void hookContextMenu() {
		MenuManager menuMgr = new MenuManager("#PopupMenu");
		menuMgr.setRemoveAllWhenShown(true);
		menuMgr.addMenuListener(new IMenuListener() {
			public void menuAboutToShow(IMenuManager manager) {
				Dashboard.this.fillContextMenu(manager);
			}
		});
	}

	private void contributeToActionBars() {
		IActionBars bars = getViewSite().getActionBars();
		fillLocalPullDown(bars.getMenuManager());
		fillLocalToolBar(bars.getToolBarManager());
	}

	private void fillLocalPullDown(IMenuManager manager) {
//		manager.add(action1);
		manager.add(new Separator());
//		manager.add(action2);
	}

	private void fillContextMenu(IMenuManager manager) {
//		manager.add(action1);
//		manager.add(action2);
		// Other plug-ins can contribute there actions here
		manager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
	}
	
	private void fillLocalToolBar(IToolBarManager manager) {
//		manager.add(action1);
//		manager.add(action2);
	}

	@Override
	public void setFocus() {
		
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.ui.part.WorkbenchPart#dispose()
	 */
	@Override
	public void dispose() {
		super.dispose();
		
		if(this.registeredCommandListeners.isEmpty()) {
			return;
		}
		
		// Get the command service
		ICommandService commandService = (ICommandService) getSite().getService(ICommandService.class);
		
		// Clear out the listeners
		for(Map.Entry<ICommandListener, String> entry : this.registeredCommandListeners.entrySet()) {
			Command command = commandService.getCommand(entry.getValue());
			command.removeCommandListener(entry.getKey());
		}
	}

	/**
	 * Convenience method to register a command listener and enable/disable control based on command enablement.
	 * 
	 * @param control
	 * @param commandId ID of the command to execute
	 */
	private void setupCommandListener(final Control control, String commandId) {
		{
			// Obtain IServiceLocator implementer, e.g. from PlatformUI.getWorkbench():
			IServiceLocator serviceLocator = getSite();
			// or a site from within a editor or view:
			// IServiceLocator serviceLocator = getSite();
	
			// Get CommandService
			ICommandService commandService = (ICommandService) serviceLocator.getService(ICommandService.class);
			
		    // Lookup commmand with its ID
		    final Command command = commandService.getCommand(commandId);
		    
		    // Update UI initially
		    control.setEnabled(command.isDefined() && command.isEnabled());
		    
		    // Register state listener
		    ICommandListener listener = new ICommandListener() {
				@Override
				public void commandChanged(CommandEvent cmdEvent) {
					if(cmdEvent.isEnabledChanged() || cmdEvent.isEnabledChanged()) {
						control.setEnabled(command.isDefined() && command.isEnabled());
					}
				}
			}; 
		    
		    command.addCommandListener(listener);
		    registeredCommandListeners.put(listener, commandId);
		}
	}

	/**
	 * Convenience method to call a command with parameters.
	 * 
	 * @param commandId ID of the command to execute
	 * @param parameters map of command parameters in form (parameterId, value)
	 */
	protected void executeCommand(String commandId, Map<String, String> parameters) {
		// Obtain IServiceLocator implementer, e.g. from PlatformUI.getWorkbench():
		IServiceLocator serviceLocator = getSite();
		// or a site from within a editor or view:
		// IServiceLocator serviceLocator = getSite();

		ICommandService commandService = (ICommandService) serviceLocator.getService(ICommandService.class);
		IHandlerService handlerService = (IHandlerService) serviceLocator.getService(IHandlerService.class);
		
		try  { 
		    // Lookup commmand with its ID
		    Command command = commandService.getCommand(commandId);

		    ArrayList<Parameterization> params = new ArrayList<Parameterization>();
		    for(Map.Entry<String, String> entry : parameters.entrySet()) {
				Parameterization param = new Parameterization(command.getParameter(entry.getKey()), entry.getValue());
				params.add(param);
		    }
			ParameterizedCommand parametrizedCommand = new ParameterizedCommand(command, params.toArray(new Parameterization[params.size()]));
		    handlerService.executeCommand(parametrizedCommand, null);
		        
		} catch (ExecutionException | NotDefinedException |
		        NotEnabledException | NotHandledException ex) {
		    
		    // Replace with real-world exception handling
		    ex.printStackTrace();
		}
	}

	
	/**
	 * Convenience method to call a command with no parameters.
	 * 
	 * @param commandId ID of the command to execute
	 */
	protected void executeCommand(String commandId) {
		// Obtain IServiceLocator implementer, e.g. from PlatformUI.getWorkbench():
		IServiceLocator serviceLocator = getSite();
		// or a site from within a editor or view:
		// IServiceLocator serviceLocator = getSite();

		IHandlerService handlerService = (IHandlerService) serviceLocator.getService(IHandlerService.class);
		try  { 
		    // Execute commmand via its ID
			handlerService.executeCommand(commandId, null);        
		} catch (ExecutionException | NotDefinedException |
		        NotEnabledException | NotHandledException ex) {
		    // Replace with real-world exception handling
		    ex.printStackTrace();
		}
	}

}