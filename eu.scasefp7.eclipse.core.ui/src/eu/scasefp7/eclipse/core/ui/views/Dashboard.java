/**
 * Copyright 2015 S-CASE Consortium
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package eu.scasefp7.eclipse.core.ui.views;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.core.commands.Command;
import org.eclipse.core.commands.CommandEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.ICommandListener;
import org.eclipse.core.commands.IParameter;
import org.eclipse.core.commands.NotEnabledException;
import org.eclipse.core.commands.NotHandledException;
import org.eclipse.core.commands.Parameterization;
import org.eclipse.core.commands.ParameterizedCommand;
import org.eclipse.core.commands.common.CommandException;
import org.eclipse.core.commands.common.NotDefinedException;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.IRegistryEventListener;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.QualifiedName;
import org.eclipse.core.runtime.RegistryFactory;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Link;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IViewSite;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.commands.ICommandService;
import org.eclipse.ui.handlers.IHandlerService;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.ui.services.IServiceLocator;

import eu.scasefp7.eclipse.core.builder.ProjectUtils;
import eu.scasefp7.eclipse.core.ui.Activator;
import eu.scasefp7.eclipse.core.ui.ScaseUiConstants;
import eu.scasefp7.eclipse.core.ui.SharedImages;
import eu.scasefp7.eclipse.core.ui.preferences.internal.DomainEntry;
import eu.scasefp7.eclipse.core.ui.preferences.internal.IProjectDomains;

/**
 * Creates a dashboard viewpart composed of multiple groups and buttons with commands attached.
 * Configuration data is read from contributions to an extension point.
 * 
 * @see ScaseUiConstants#DASHBOARD_EXTENSION
 * @author Marin Orlic
 */

public class Dashboard extends ViewPart implements ISelectionListener, IRegistryEventListener {

	/**
	 * The ID of the view as specified by the extension.
	 */
	public static final String ID = ScaseUiConstants.DASHBOARD_VIEW;

	/**
	 * The element names defining the contribution.
	 */
    private static final String CONTRIBUTION_GROUP = "group";
    private static final String CONTRIBUTION_GROUP_NAME = "name";
    private static final String CONTRIBUTION_GROUP_ID = "id";
    private static final String CONTRIBUTION_GROUP_BEFORE = "appearsBefore";
    private static final String CONTRIBUTION_COMMAND = "command";
    private static final String CONTRIBUTION_COMMAND_ID = "commandId";
    private static final String CONTRIBUTION_COMMAND_BUTTON_ID = "buttonId";
    private static final String CONTRIBUTION_COMMAND_GROUP = "groupId";
    private static final String CONTRIBUTION_COMMAND_LABEL = "label";
    private static final String CONTRIBUTION_COMMAND_TOOLTIP = "tooltip";
    private static final String CONTRIBUTION_COMMAND_BEFORE = "appearsBefore";
    private static final String CONTRIBUTION_COMMAND_PARAM = "parameter";
    private static final String CONTRIBUTION_COMMAND_PARAM_NAME = "name";
    private static final String CONTRIBUTION_COMMAND_PARAM_VALUE = "value";
    private static final String CONTRIBUTION_COMMAND_NOTIFICATION_SUCCESS = "notification";
    private static final String CONTRIBUTION_COMMAND_NOTIFICATION_FAIL = "error";

    protected HashMap<ICommandListener, String> registeredCommandListeners = new HashMap<ICommandListener, String>();
    
    /**
     * Composite containing the dashboard view.
     */
    private Composite dashboardComposite;
    
    /**
     * Composite containing the 'please select a project' message.
     */
    private Composite noProjectSelectedMessage;
    
    /**
     * Composite containing the 'configure as an S-CASE project' message.
     */   
    private Composite noScaseProjectMessage;
    
    /**
     * Main composite of the view.
     */
    private Composite viewComposite;
    
    /**
     * Layout of the main view.
     */
    private StackLayout viewLayout;
    
    /**
     * Name of the group and group of buttons.
     */
    private HashMap<String, Group> groups = new HashMap<String, Group>();
    
    /**
     * Button ID and buttons.
     */
    private HashMap<String, Button> buttons = new HashMap<String, Button>();
    
    /**
     * First button is above the button with given string ID.
     */
    private List<Entry<Control,String>> ordering = new ArrayList<Entry<Control,String>>();
    
    /**
     * The currently selected project.
     */
    private IProject currentProject;
    
    /**
	 * The constructor.
	 */
	public Dashboard() {
	    RegistryFactory.getRegistry().addListener(this, ScaseUiConstants.DASHBOARD_EXTENSION);
	}

	@Override
	public void init(IViewSite site) throws PartInitException {
	    IWorkbenchPage page = site.getPage();
        if (page != null) {
            updateSelection(page.getSelection());
            updateContentDescription();
			updateButtons();
        }
        site.getPage().addPostSelectionListener(this);
        super.init(site);
    }

    /**
	 * This is a callback that will allow us
	 * to create the viewer and initialize it.
	 */
	public void createPartControl(Composite parent) {
	    this.viewComposite = parent;
        this.viewLayout = new StackLayout();
        this.viewComposite.setLayout(this.viewLayout);
	    
	    this.dashboardComposite = createDashboard(this.viewComposite);
	    this.noProjectSelectedMessage = createNoProjectSelectedMessage(this.viewComposite);
	    this.noScaseProjectMessage = createProjectConfigMessage(this.viewComposite);
	    
        this.viewLayout.topControl = this.dashboardComposite;
        this.viewComposite.layout();	    
	}
	
	/**
	 * Creates the dashboard composite.
	 * 
	 * @param composite to contain the dashboard
	 * @return dashboard composite
	 */
	private Composite createDashboard(Composite parent) {
	    Composite composite = new Composite(parent, SWT.NONE);
	    
	    // Set the main layout
		RowLayout layout = new RowLayout(SWT.HORIZONTAL);
		layout.pack = false;
		layout.fill = true;
		layout.marginWidth = 10;
		layout.marginHeight = 10;
		layout.spacing = 10;
		composite.setLayout(layout);
		composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		
		// Add menu and toolbar
		hookContextMenu();
        contributeToActionBars();
		
		// Read the configuration of the dashboard
		IExtensionRegistry registry = Platform.getExtensionRegistry();
        IConfigurationElement[] contributions = registry.getConfigurationElementsFor(ScaseUiConstants.DASHBOARD_EXTENSION);
        
        // Create the configured items
        for (IConfigurationElement elem : contributions) {
            if(elem.getName().equals(CONTRIBUTION_GROUP)) {
                handleGroup(composite, elem);
            }
            if(elem.getName().equals(CONTRIBUTION_COMMAND)) {
                handleButton(composite, elem);   
            }
        }
		
        // Sort the contributions
        if(!ordering.isEmpty()){
            for(Entry<Control, String> e : ordering){
                String id = e.getValue();
                Control first = e.getKey();
                Control second = null;
                if (buttons.get(id) != null) {
                    second = buttons.get(id);
                } else if (groups.get(id) != null) {
                    second = groups.get(id);
                }
                
                first.moveAbove(second);
            }
        }
        
        composite.addControlListener(new ControlAdapter() {
            @Override
            public void controlResized(ControlEvent e) {
                composite.getShell().layout();
            }
        });
        
        updateContentDescription();
        updateButtons();
        
        return composite;
	}

    /**
     * Disposes the view. Takes care to registered remove command listeners (on buttons).
     * 
     * @see org.eclipse.ui.part.WorkbenchPart#dispose()
     */
    @Override
    public void dispose() {
    	super.dispose();
    	
    	if(this.registeredCommandListeners.isEmpty()) {
    		return;
    	}
    	
    	// Get the command service
    	ICommandService commandService = (ICommandService)getSite().getService(ICommandService.class);
    	
    	// Clear out the listeners
    	for(Map.Entry<ICommandListener, String> entry : this.registeredCommandListeners.entrySet()) {
    		Command command = commandService.getCommand(entry.getValue());
    		command.removeCommandListener(entry.getKey());
    	}

        // Remove selection and registry listener registrations
        getSite().getPage().removePostSelectionListener(this);
        RegistryFactory.getRegistry().removeListener(this);
        
        currentProject = null;
    }

    /**
     * Sets the focus.
     * 
     * @see org.eclipse.ui.part.WorkbenchPart#setFocus()
     */
    @Override
    public void setFocus() {
   
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
//      manager.add(action1);
        manager.add(new Separator());
//      manager.add(action2);
    }

    private void fillContextMenu(IMenuManager manager) {
//      manager.add(action1);
//      manager.add(action2);
        // Other plug-ins can contribute there actions here
        manager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
    }
    
    private void fillLocalToolBar(IToolBarManager manager) {
//      manager.add(action1);
//      manager.add(action2);
//        CommandContributionItemParameter param = new CommandContributionItemParameter(getSite(), "eu.scasefp7.eclipse.core.ui.dashboard.menu1", 
//            "eu", new HashMap<Object, Object>(), null, null, null, "MENUUU", "C", 
//            "Shows the project properties pages", 
//            CommandContributionItem.STYLE_PUSH, getContentDescription(), false);
//        manager.add(new CommandContributionItem(param)); 
    }

    
    /**
     * Create a group and it's children based on the configuration element.
     * 
     * @param parent control
     * @param elem configuration element describing the button
     */
    private void handleGroup(Composite parent, IConfigurationElement elem) {
        String groupId = elem.getAttribute(CONTRIBUTION_GROUP_ID); 
        String appearsBefore = elem.getAttribute(CONTRIBUTION_GROUP_BEFORE);

        Group group = groups.get(groupId);
        if(group == null) {
            group = createGroup(parent, elem.getAttribute(CONTRIBUTION_GROUP_NAME));
            groups.put(groupId, group);
        }
        
        for (IConfigurationElement child : elem.getChildren()) {
            if(child.getName().equals(CONTRIBUTION_GROUP)) {
                handleGroup(group, child);
            }
            if(child.getName().equals(CONTRIBUTION_COMMAND)) {
                handleButton(group, child);    
            }
        }
        
        if(appearsBefore!= null && !appearsBefore.isEmpty()){
            Entry<Control, String> entry = new AbstractMap.SimpleEntry<Control, String>(group, appearsBefore);
            ordering.add(entry);
        }
    }

    /**
     * Create a button and attach the command to it based on the configuration element.
     * 
     * @param parent control
     * @param elem configuration element describing the button
     */
    private void handleButton(Composite parent, IConfigurationElement elem) {       

        Composite buttonParent = parent;
        String name = elem.getAttribute(CONTRIBUTION_COMMAND_LABEL);
        String tooltip = elem.getAttribute(CONTRIBUTION_COMMAND_TOOLTIP);
        String group = elem.getAttribute(CONTRIBUTION_COMMAND_GROUP);
        
        if(group != null && !group.isEmpty()) {
            buttonParent = groups.get(group);
        }
        
        String appearsBefore = elem.getAttribute(CONTRIBUTION_COMMAND_BEFORE);
        String buttonId = elem.getAttribute(CONTRIBUTION_COMMAND_BUTTON_ID);
        
        final String commandId = elem.getAttribute(CONTRIBUTION_COMMAND_ID);
        final String notificationSuccess = elem.getAttribute(CONTRIBUTION_COMMAND_NOTIFICATION_SUCCESS);
        final String notificationFail = elem.getAttribute(CONTRIBUTION_COMMAND_NOTIFICATION_FAIL);
        
        Button btn = new Button(buttonParent, SWT.NONE);
        
        if(name != null) {
            btn.setText(name);
        }
        if(tooltip != null) {
            btn.setToolTipText(tooltip);
        }
          
        if(commandId != null) {
            // Add command listener
            setupCommandListener(btn, commandId);
            
            // Setup execution
            final Map<String,String> params = getParameters(elem);
            
            // Setup listener
            btn.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseDown(MouseEvent e) {
                    try {
                        // Trace user action
                        Activator.TRACE.trace("/dashboard/userActions", "Button pressed: " + name);
                        
                        if (params == null || params.isEmpty()) {
                            executeCommand(commandId);
                        } else {
                            executeCommand(commandId, params);
                        }
                        notifyUser(commandId, notificationSuccess);     
                    } catch (CommandException ex) {
                        Activator.log("Unable to execute command " + commandId, ex);
                        notifyUser(commandId, notificationFail, ex);     
                    }
                }
            });
        }
        
        buttons.put(buttonId, btn);
        
        if(appearsBefore!= null && !appearsBefore.isEmpty()){
            Entry<Control, String> entry = new AbstractMap.SimpleEntry<Control, String>(btn, appearsBefore);
            ordering.add(entry);
        }
    }

    /**
     * Creates a group with fill layout and configured margins.
     * 
     * @param parent control
     * @param name for the group, displayed as label
     * @return created group control
     */
    private static Group createGroup(Composite parent, String name) {
        Group group;
        group = new Group(parent, SWT.NONE);
        group.setText(name);
        
        // Configure layout
        FillLayout layout = new FillLayout(SWT.VERTICAL);
        layout.spacing = 10;
        layout.marginWidth = 10;
        layout.marginHeight = 10;
        group.setLayout(layout);
        
        return group;
    }

	/**
	 * Convenience method to register a command listener and enable/disable control based on command configuration.
	 * 
	 * @param control
	 * @param commandId ID of the command to execute
	 */
	private void setupCommandListener(final Control control, String commandId) {

		// Obtain IServiceLocator implementer, e.g. from PlatformUI.getWorkbench():
		IServiceLocator serviceLocator = getSite();
		// or a site from within a editor or view:
		// IServiceLocator serviceLocator = getSite();

		// Get CommandService
		ICommandService commandService = (ICommandService)serviceLocator.getService(ICommandService.class);
		
	    // Lookup commmand with its ID
	    final Command command = commandService.getCommand(commandId);
	    
	    // Update UI initially
	    control.setEnabled(command.isDefined() && command.isEnabled());
	    
	    // Register state listener
	    ICommandListener listener = new ICommandListener() {
			@Override
			public void commandChanged(CommandEvent cmdEvent) {
				if(cmdEvent.isDefinedChanged() || cmdEvent.isEnabledChanged() || cmdEvent.isHandledChanged()) {
					control.setEnabled(command.isDefined() && command.isEnabled() && command.isHandled());
				}
			}
		}; 
	    
	    command.addCommandListener(listener);
	    registeredCommandListeners.put(listener, commandId);
	}

	/**
	 * Convenience method to call a command with parameters.
	 * 
	 * @param commandId ID of the command to execute
	 * @param parameters map of command parameters in form (parameterId, value)
	 * @throws CommandException if the command execution fails
	 */
	protected void executeCommand(String commandId, Map<String, String> parameters) throws CommandException {
		// Obtain IServiceLocator implementer, e.g. from PlatformUI.getWorkbench():
		IServiceLocator serviceLocator = getSite();
		// or a site from within a editor or view:
		// IServiceLocator serviceLocator = getSite();

		ICommandService commandService = (ICommandService)serviceLocator.getService(ICommandService.class);
		IHandlerService handlerService = (IHandlerService)serviceLocator.getService(IHandlerService.class);
		
		try  { 
		    // Lookup command with its ID
		    Command command = commandService.getCommand(commandId);

		    ArrayList<Parameterization> params = new ArrayList<Parameterization>();
		    for(Map.Entry<String, String> entry : parameters.entrySet()) {
		        IParameter p = command.getParameter(entry.getKey());
		        if(p != null) {
    				Parameterization param = new Parameterization(p, entry.getValue());
    				params.add(param);
		        } else {
		            Activator.TRACE.trace("/dashboard/executeCommand", "Cannot find parameter: " + entry.getKey() + " of command " + commandId);
		        }
		    }
		    
		    Activator.TRACE.trace("/dashboard/userActions", "Command called: " + commandId + ", parameters: " + params); 
	        
			ParameterizedCommand parametrizedCommand = new ParameterizedCommand(command, params.toArray(new Parameterization[params.size()]));
		    handlerService.executeCommand(parametrizedCommand, null);
		    
		} catch (ExecutionException | NotDefinedException |
		        NotEnabledException | NotHandledException ex) {
		    
		   throw ex;
		}
	}

	
	/**
	 * Convenience method to call a command with no parameters.
	 * 
	 * @param commandId ID of the command to execute
	 * @throws CommandException if the command execution fails
	 */
	protected void executeCommand(String commandId) throws CommandException {
		// Obtain IServiceLocator implementer, e.g. from PlatformUI.getWorkbench():
		IServiceLocator serviceLocator = getSite();
		// or a site from within a editor or view:
		// IServiceLocator serviceLocator = getSite();

		Activator.TRACE.trace("/dashboard/executeCommand", "Executing: " + commandId);
		
		IHandlerService handlerService = (IHandlerService)serviceLocator.getService(IHandlerService.class);
		try  { 
		    // Execute command via its ID
			handlerService.executeCommand(commandId, null);

		} catch (ExecutionException | NotDefinedException |
		        NotEnabledException | NotHandledException ex) {
		    
		    throw ex;
		}
	}

    /**
     * Reads command parameters as children of configuration element for the command.
     * Borrowed from org.eclipse.ui.internal.menus.MenuHelper.
     * 
     * @param element
     * @return map with parameter names and values
     * @see org.eclipse.ui.internal.menus.MenuHelper#getParameters(IConfigurationElement)
     */
    @SuppressWarnings("restriction")
    private static Map<String, String> getParameters(IConfigurationElement element) {
        HashMap<String, String> map = new HashMap<String, String>();
        IConfigurationElement[] parameters = element.getChildren(CONTRIBUTION_COMMAND_PARAM);
        for (int i = 0; i < parameters.length; i++) {
            String name = parameters[i].getAttribute(CONTRIBUTION_COMMAND_PARAM_NAME);
            String value = parameters[i].getAttribute(CONTRIBUTION_COMMAND_PARAM_VALUE);
            if (name != null && value != null) {
                map.put(name, value);
            }
        }
        return map;
    }

    protected void notifyUser(String commandId, String message) {
        if(message != null) {
            List<AbstractNotification> notifications = new ArrayList<AbstractNotification>();
            
            NotificationPopup popup = new NotificationPopup(this.getViewSite().getShell());
            
            notifications.add(new DashboardNotification(commandId, getPartName(), message, PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_OBJS_INFO_TSK))); 
                    
            popup.setContents(notifications);
            popup.open();
        }
    }

    protected void notifyUser(String commandId, String message, CommandException ex) {
        List<AbstractNotification> notifications = new ArrayList<AbstractNotification>();
        
        NotificationPopup popup = new NotificationPopup(this.getViewSite().getShell());
        
        notifications.add(new DashboardNotification(commandId, getPartName(), ((message != null) ? message + ": " : "") + ex.getLocalizedMessage(), PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_OBJS_ERROR_TSK))); 
                
        popup.setContents(notifications);
        popup.open();
    }

    @Override
    public void selectionChanged(IWorkbenchPart part, ISelection sel) {
        updateSelection(sel);
        updateContentDescription();
		updateButtons();
    }

    protected IProject getProjectOfSelectionList(List<Object> selectionList) {
		IProject project = null;
		for (Object object : selectionList) {
			IResource resource = (IResource) Platform.getAdapterManager().getAdapter(object, IResource.class);
			IProject theproject = null;
			if (resource != null) {
				theproject = resource.getProject();
			} else {
				theproject = (IProject) Platform.getAdapterManager().getAdapter(object, IProject.class);
			}
			if (theproject != null) {
				if (project == null) {
					project = theproject;
				} else {
					if (!project.equals(theproject)) {
						return null;
					}
				}
			}
		}
		return project;
    }

	private boolean isSCASEProject(IProject project) {
		boolean isSCASEProject = false;
		try {
			isSCASEProject = currentProject.hasNature(ScaseUiConstants.PROJECT_NATURE);
		} catch (CoreException e) {
			Activator.log("Error setting the current project in the dashboard", e);
		}
		return isSCASEProject;
	}
    
	@SuppressWarnings("unchecked")
	private void updateSelection(ISelection selection) {
		if (selection instanceof IStructuredSelection) {
			IProject project = getProjectOfSelectionList(((IStructuredSelection) selection).toList());
			if (project != null) {
				Activator.TRACE.trace("/dashboard/selectedProjectChanged", "Selected project: " + project.getName());
				
				currentProject = project;
				
                if(isSCASEProject(project)) {
                    this.viewLayout.topControl = this.dashboardComposite;
                } else {
                    this.viewLayout.topControl = this.noScaseProjectMessage;
                }
                this.viewComposite.layout();
			} else {
				// This condition is experimental and can be used to retain selection when selecting other views.
				// if (currentProject == null || !currentProject.exists())
			    Activator.TRACE.trace("/dashboard/selectedProjectChanged", "No project selected.");
                
			    currentProject = null;
			    if(this.viewLayout != null && this.viewComposite != null) {
			        this.viewLayout.topControl = this.noProjectSelectedMessage;
			        this.viewComposite.layout();
			    }
			}
		}
	}

    private void updateContentDescription() {
		if (currentProject != null) {
			if (isSCASEProject(currentProject)) {
				DomainEntry de = findDomainById(IProjectDomains.PROJECT_DOMAINS, getProjectDomainId(currentProject));
				setContentDescription("Active project: " + currentProject.getName() + " ("
						+ (de == null ? "domain unset" : de.getName()) + ")");
			} else
				setContentDescription("Active project: " + currentProject.getName() + " (non-S-CASE project)");
		} else
			setContentDescription("Please select a project");
    }

    private void updateButtons() {
		if (buttons != null) {
			if (currentProject != null && currentProject.exists() && isSCASEProject(currentProject)) {
				for (Button button : buttons.values())
					button.setEnabled(true);
			} else {
				for (Button button : buttons.values())
					button.setEnabled(false);
			}
		}
    }
   
    private DomainEntry findDomainById(DomainEntry[] domains, int domainId) {
        for (DomainEntry de : domains) {
            if (de.getId() == domainId) {
                return de;
            }
            if(de.hasChildren()) {
                for (DomainEntry child : de.getChildren()) {
                    if(child.getId() == domainId) {
                        return child;
                    }
                }
            }
        }
        return null;
    }
    
    private static int getProjectDomainId(IProject project) {
        try {
            String domain = project.getPersistentProperty(new QualifiedName("", ScaseUiConstants.PROP_PROJECT_DOMAIN));
            if(domain != null) {
                return Integer.parseInt(domain);
            }
        } catch (CoreException | NumberFormatException e) {
            Activator.log("Unable to load project domain", e);
        }
        return ScaseUiConstants.PROP_PROJECT_DOMAIN_DEFAULT;
    }
    
    @Override
    public void added(IExtension[] extensions) {
        dashboardComposite.update();
    }

    @Override
    public void removed(IExtension[] extensions) {
        dashboardComposite.update();
    }

    @Override
    public void added(IExtensionPoint[] extensionPoints) {
        dashboardComposite.update();
    }

    @Override
    public void removed(IExtensionPoint[] extensionPoints) {
        dashboardComposite.update();
    }

    /**
     * Creates a 'Please select a project' message.
     * 
     * @param parent to contain the message
     * @return message composite
     */
    private Composite createNoProjectSelectedMessage(Composite parent) {
        Composite composite = new Composite(parent, SWT.NONE);
        composite.setLayout(new GridLayout(1, false));
        
        Composite c1 = new Composite(composite, SWT.NONE);
        c1.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, true, 1, 1));
        GridLayout gl_composite1 = new GridLayout(2, false);
        gl_composite1.horizontalSpacing = 20;
        gl_composite1.marginWidth = 20;
        gl_composite1.marginHeight = 20;
        c1.setLayout(gl_composite1);
        
        Label lblNewLabel = new Label(c1, SWT.NONE);
        lblNewLabel.setImage(Activator.getImages().getImage(SharedImages.Images.VIEW_SCASE32));
        lblNewLabel.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1));
        lblNewLabel.setImage(Activator.getImages().getImage(SharedImages.Images.VIEW_SCASE32));
        
        Label lblProjectNotScase = new Label(c1, SWT.NONE);
        lblProjectNotScase.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1));
        lblProjectNotScase.setText("Please select a project to start.");
        
        return composite;
    }

    /**
     * Creates a 'Configure as an S-CASE project' message.
     * 
     * @param parent to contain the message
     * @return message composite
     */
    private Composite createProjectConfigMessage(Composite parent) {
        Composite composite = new Composite(parent, SWT.NONE);
        composite.setLayout(new GridLayout(1, false));
        
        Composite c1 = new Composite(composite, SWT.NONE);
        c1.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, true, 1, 1));
        GridLayout gl_composite1 = new GridLayout(2, false);
        gl_composite1.horizontalSpacing = 20;
        gl_composite1.marginWidth = 20;
        gl_composite1.marginHeight = 20;
        c1.setLayout(gl_composite1);
        
        Label lblNewLabel = new Label(c1, SWT.NONE);
        lblNewLabel.setImage(Activator.getImages().getImage(SharedImages.Images.VIEW_SCASE32));
        lblNewLabel.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 2));
        lblNewLabel.setImage(Activator.getImages().getImage(SharedImages.Images.VIEW_SCASE32));
        
        Label lblProjectNotScase = new Label(c1, SWT.NONE);
        lblProjectNotScase.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1));
        lblProjectNotScase.setText("The selected project is not configured for S-CASE.");
        
        Link link = new Link(c1, SWT.NONE);
        link.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
        link.setText("<a>Configure as an S-CASE project</a>");
        new Label(c1, SWT.NONE);
        new Label(c1, SWT.NONE);
        link.addSelectionListener(new SelectionListener() {
            
            private void configureProject() {
                try {
                    if(currentProject != null) {
                        ProjectUtils.addNature(currentProject, null);
                        selectionChanged(Dashboard.this, new StructuredSelection(currentProject));
                    }
                } catch (CoreException ex) {
                    Activator.log("Error when configuring project.", ex);                        
                }
            }
            
            public void widgetSelected(SelectionEvent e) {
                configureProject();
            }
    
            public void widgetDefaultSelected(SelectionEvent e) {
                configureProject();
            }
        });
        
        return composite;
    }

}
