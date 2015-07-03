package eu.scasefp7.eclipse.core.ui.views;


import org.eclipse.core.commands.Command;
import org.eclipse.core.commands.ExecutionException;
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
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
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

	/*
	 * The content provider class is responsible for
	 * providing objects to the view. It can wrap
	 * existing objects in adapters or simply return
	 * objects as-is. These objects may be sensitive
	 * to the current input of the view, or ignore
	 * it and always show the same content 
	 * (like Task List, for example).
	 */
	 
	class ViewContentProvider implements IStructuredContentProvider {
		public void inputChanged(Viewer v, Object oldInput, Object newInput) {
		}
		public void dispose() {
		}
		public Object[] getElements(Object parent) {
			return new String[] { "One", "Two", "Three" };
		}
	}
	class ViewLabelProvider extends LabelProvider implements ITableLabelProvider {
		public String getColumnText(Object obj, int index) {
			return getText(obj);
		}
		public Image getColumnImage(Object obj, int index) {
			return getImage(obj);
		}
		public Image getImage(Object obj) {
			return PlatformUI.getWorkbench().
					getSharedImages().getImage(ISharedImages.IMG_OBJ_ELEMENT);
		}
	}
	class NameSorter extends ViewerSorter {
	}

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
		RowLayout rl_parent = new RowLayout(SWT.HORIZONTAL);
		rl_parent.marginWidth = 10;
		rl_parent.marginHeight = 10;
		rl_parent.spacing = 10;
		parent.setLayout(rl_parent);
		
		Group groupStatic = new Group(parent, SWT.NONE);
		groupStatic.setText("Static modeling");
		FillLayout fl_groupStatic = new FillLayout(SWT.VERTICAL);
		fl_groupStatic.spacing = 10;
		fl_groupStatic.marginWidth = 10;
		fl_groupStatic.marginHeight = 10;
		groupStatic.setLayout(fl_groupStatic);
		
		Button btnRequirements = new Button(groupStatic, SWT.NONE);
		btnRequirements.setText("Requirements");
		
		Button btnImportUmlDiagram = new Button(groupStatic, SWT.NONE);
		btnImportUmlDiagram.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				// Obtain IServiceLocator implementer, e.g. from PlatformUI.getWorkbench():
				IServiceLocator serviceLocator = getSite();
				// or a site from within a editor or view:
				// IServiceLocator serviceLocator = getSite();

				ICommandService commandService = (ICommandService) serviceLocator.getService(ICommandService.class);
				IHandlerService handlerService = (IHandlerService) serviceLocator.getService(IHandlerService.class);
				
				try  { 
				    // Lookup commmand with its ID
				    Command command = commandService.getCommand(IWorkbenchCommandConstants.FILE_IMPORT);

					Parameterization[] params = new Parameterization[] { new Parameterization(
		                    command.getParameter(IWorkbenchCommandConstants.FILE_IMPORT_PARM_WIZARDID), "eu.scasefp7.eclipse.umlrec.importWizard") };
					ParameterizedCommand parametrizedCommand = new ParameterizedCommand(command, params);
					
					handlerService.executeCommand(parametrizedCommand, null);
				        
				} catch (ExecutionException | NotDefinedException |
				        NotEnabledException | NotHandledException ex) {
				    
				    // Replace with real-world exception handling
				    ex.printStackTrace();
				}
			}
		});
		btnImportUmlDiagram.setText("Import UML diagram");
		
		Group groupDynamic = new Group(parent, SWT.NONE);
		groupDynamic.setText("Dynamic modeling");
		FillLayout fl_groupDynamic = new FillLayout(SWT.VERTICAL);
		fl_groupDynamic.spacing = 10;
		fl_groupDynamic.marginWidth = 10;
		fl_groupDynamic.marginHeight = 10;
		groupDynamic.setLayout(fl_groupDynamic);
		
		Button btnStoryboards = new Button(groupDynamic, SWT.NONE);
		btnStoryboards.setText("Storyboards");
		
		Button btnImportUmlDiagram2 = new Button(groupDynamic, SWT.NONE);
		btnImportUmlDiagram2.setText("Import UML diagram");
		
		Group grpRequirementsCompilation = new Group(parent, SWT.NONE);
		grpRequirementsCompilation.setText("Requirements compilation");
		FillLayout fl_grpRequirementsCompilation = new FillLayout(SWT.VERTICAL);
		fl_grpRequirementsCompilation.spacing = 10;
		fl_grpRequirementsCompilation.marginWidth = 10;
		fl_grpRequirementsCompilation.marginHeight = 10;
		grpRequirementsCompilation.setLayout(fl_grpRequirementsCompilation);
		
		Button btnRequirements_1 = new Button(grpRequirementsCompilation, SWT.NONE);
		btnRequirements_1.setText("Requirements");
		
		Button btnWebServiceComposition = new Button(grpRequirementsCompilation, SWT.NONE);
		btnWebServiceComposition.setText("Web service composition");
		
		Group grpModeldrivenEngineering = new Group(parent, SWT.NONE);
		grpModeldrivenEngineering.setText("Model-driven engineering");
		FillLayout fl_grpModeldrivenEngineering = new FillLayout(SWT.VERTICAL);
		fl_grpModeldrivenEngineering.spacing = 10;
		fl_grpModeldrivenEngineering.marginWidth = 10;
		fl_grpModeldrivenEngineering.marginHeight = 10;
		grpModeldrivenEngineering.setLayout(fl_grpModeldrivenEngineering);
		
		Button btnRefine = new Button(grpModeldrivenEngineering, SWT.NONE);
		btnRefine.setText("Refine");
		
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
		
		Group grpYourest = new Group(parent, SWT.NONE);
		grpYourest.setText("YouREST");
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


}