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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.IRegistryEventListener;
import org.eclipse.core.runtime.InvalidRegistryObjectException;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.QualifiedName;
import org.eclipse.core.runtime.RegistryFactory;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IViewSite;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.commands.ICommandService;
import org.eclipse.ui.handlers.IHandlerService;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.ui.services.IServiceLocator;

import eu.scasefp7.eclipse.core.ui.ScaseUiConstants;
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
	public static final String ID = "eu.scasefp7.eclipse.core.ui.views.Dashboard";

	/**
	 * The element names defining the contribution.
	 */
    private static final String CONTRIBUTION_GROUP = "group";
    private static final String CONTRIBUTION_GROUP_NAME = "name";
    private static final String CONTRIBUTION_COMMAND = "command";
    private static final String CONTRIBUTION_COMMAND_ID = "commandId";
    private static final String CONTRIBUTION_COMMAND_LABEL = "label";
    private static final String CONTRIBUTION_COMMAND_TOOLTIP = "tooltip";
    private static final String CONTRIBUTION_COMMAND_PARAM = "parameter";
    private static final String CONTRIBUTION_COMMAND_PARAM_NAME = "name";
    private static final String CONTRIBUTION_COMMAND_PARAM_VALUE = "value";
    private static final String CONTRIBUTION_COMMAND_NOTIFICATION_SUCCESS = "notification";
    private static final String CONTRIBUTION_COMMAND_NOTIFICATION_FAIL = "error";

    protected HashMap<ICommandListener, String> registeredCommandListeners = new HashMap<ICommandListener, String>();
      
    /**
     * The currently selected project
     */
    private IProject currentProject;

    /**
     * The current selection
     */
    private ISelection currentSelection;
    
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
        }
        site.getPage().addPostSelectionListener(this);
        super.init(site);
    }

    /**
	 * This is a callback that will allow us
	 * to create the viewer and initialize it.
	 */
	public void createPartControl(Composite parent) {
	    // Set the main layout
		RowLayout rl_parent = new RowLayout(SWT.HORIZONTAL);
		rl_parent.pack = false;
		rl_parent.fill = true;
		rl_parent.marginWidth = 10;
		rl_parent.marginHeight = 10;
		rl_parent.spacing = 10;
		parent.setLayout(rl_parent);
		parent.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		
		// Read the configuration of the dashboard
		IExtensionRegistry registry = Platform.getExtensionRegistry();
        IConfigurationElement[] contributions = registry.getConfigurationElementsFor(ScaseUiConstants.DASHBOARD_EXTENSION);
        
        // Create the configured items
        for (IConfigurationElement elem : contributions) {
            if(elem.getName().equals(CONTRIBUTION_GROUP)) {
                handleGroup(parent, elem);
            }
            if(elem.getName().equals(CONTRIBUTION_COMMAND)) {
                handleButton(parent, elem);   
            }
        }
		
        parent.addControlListener(new ControlAdapter() {
            @Override
            public void controlResized(ControlEvent e) {
                parent.getShell().layout();
                System.out.println("RESIZED");
            }
        });
        
        
		// Project setup
        /*int startR = 95,
        stopR = 0,
        startG = 197,
        stopG = 61,
        startB = 186,
        stopB = 92;
        int steps = 4;
        int step = 0;
         */
		//groupProject.setBackground(SWTResourceManager.getColor(startR-(step*(startR-stopR)/steps), startG-(step*(startG-stopG)/steps), startB-(step*(startB-stopB)/steps)));
		// Increment gradient step
		//step++;
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
    	ICommandService commandService = (ICommandService) getSite().getService(ICommandService.class);
    	
    	// Clear out the listeners
    	for(Map.Entry<ICommandListener, String> entry : this.registeredCommandListeners.entrySet()) {
    		Command command = commandService.getCommand(entry.getValue());
    		command.removeCommandListener(entry.getKey());
    	}

        // Remove selection and registry listener registrations
        getSite().getPage().removePostSelectionListener(this);
        RegistryFactory.getRegistry().removeListener(this);
        
        currentProject = null;
        currentSelection = null;
    }

    /**
     * Sets the focus.
     * 
     * @see org.eclipse.ui.part.WorkbenchPart#setFocus()
     */
    @Override
    public void setFocus() {
   
    }

    /**
     * Create a group and it's children based on the configuration element.
     * 
     * @param parent control
     * @param elem configuration element describing the button
     * @throws InvalidRegistryObjectException
     */
    private void handleGroup(Composite parent, IConfigurationElement elem) throws InvalidRegistryObjectException {
        Group group = createGroup(parent, elem.getAttribute(CONTRIBUTION_GROUP_NAME));
        
        for (IConfigurationElement child : elem.getChildren()) {
            if(child.getName().equals(CONTRIBUTION_GROUP)) {
                handleGroup(group, child);
            }
            if(child.getName().equals(CONTRIBUTION_COMMAND)) {
                handleButton(group, child);    
            }
        }
    }

    /**
     * Create a button and attach the command to it based on the configuration element.
     * 
     * @param parent control
     * @param elem configuration element describing the button
     * @throws InvalidRegistryObjectException
     */
    private void handleButton(Composite parent, IConfigurationElement elem) throws InvalidRegistryObjectException {       

        String name = elem.getAttribute(CONTRIBUTION_COMMAND_LABEL);
        String tooltip = elem.getAttribute(CONTRIBUTION_COMMAND_TOOLTIP);
        final String commandId = elem.getAttribute(CONTRIBUTION_COMMAND_ID);
        final String notificationSuccess = elem.getAttribute(CONTRIBUTION_COMMAND_NOTIFICATION_SUCCESS);
        final String notificationFail = elem.getAttribute(CONTRIBUTION_COMMAND_NOTIFICATION_FAIL);
        
        Button btn = new Button(parent, SWT.NONE);
        
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
            
            if(params.isEmpty()) {
                // No parameters
                btn.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseDown(MouseEvent e) {
                        try {
                            executeCommand(commandId);
                            notifyUser(commandId, notificationSuccess);     
                        } catch (CommandException ex) {
                            notifyUser(commandId, notificationFail, ex);     
                            ex.printStackTrace();
                        }
                    }
                });
            } else {
                // Copy parameters
                btn.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseDown(MouseEvent e) {
                        try {
                            executeCommand(commandId, params);
                            notifyUser(commandId, notificationSuccess);     
                        } catch (CommandException ex) {
                            notifyUser(commandId, notificationFail, ex);     
                            ex.printStackTrace();
                        }
                    }
                });   
            }
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
        //group.setBackground(SWTResourceManager.getColor(startR-(step*(startR-stopR)/steps), startG-(step*(startG-stopG)/steps), startB-(step*(startB-stopB)/steps)));
        group.setText(name);
        
        // Configure layout
        FillLayout fl_group = new FillLayout(SWT.VERTICAL);
        fl_group.spacing = 10;
        fl_group.marginWidth = 10;
        fl_group.marginHeight = 10;
        group.setLayout(fl_group);
        
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
		ICommandService commandService = (ICommandService) serviceLocator.getService(ICommandService.class);
		
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
				if(command.getId().equals("eu.scasefp7.eclipse.core.commands.compileToOntology")) {
				    System.out.println("cmdEvent def " + cmdEvent.isDefinedChanged() + " en " + cmdEvent.isEnabledChanged() + " hand " + cmdEvent.isHandledChanged());
				    System.out.println("command " + command.getId() + " def " + command.isDefined() + " en " + command.isEnabled() + " hand " + command.isHandled());
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

		ICommandService commandService = (ICommandService) serviceLocator.getService(ICommandService.class);
		IHandlerService handlerService = (IHandlerService) serviceLocator.getService(IHandlerService.class);
		
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
		            System.out.println("Cannot find parameter: " + entry.getKey() + " of command " + commandId);
		        }
		    }
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

		IHandlerService handlerService = (IHandlerService) serviceLocator.getService(IHandlerService.class);
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
        
        List<AbstractNotification> notifications = new ArrayList<AbstractNotification>();
        
        NotificationPopup popup = new NotificationPopup(this.getViewSite().getShell());
        
        notifications.add(new DashboardNotification(commandId, getTitle(), message, PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_OBJS_INFO_TSK))); 
                
        popup.setContents(notifications);
        popup.open();
    }

    protected void notifyUser(String commandId, String message, CommandException ex) {
        List<AbstractNotification> notifications = new ArrayList<AbstractNotification>();
        
        NotificationPopup popup = new NotificationPopup(this.getViewSite().getShell());
        
        notifications.add(new DashboardNotification(commandId, getTitle(), message, PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_OBJS_ERROR_TSK))); 
        notifications.add(new DashboardNotification(commandId, getTitle(), ex.getLocalizedMessage(), PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_OBJS_ERROR_TSK))); 
                
        popup.setContents(notifications);
        popup.open();
    }

    @Override
    public void selectionChanged(IWorkbenchPart part, ISelection sel) {
     // we ignore null selection, or if we are pinned, or our own selection or same selection
        if (sel == null || sel.equals(currentSelection)) {
            return;
        }
        
        // we ignore selection if we are hidden OR selection is coming from another source as the last one
//        if(part == null || !part.equals(currentPart)){
//            return;
//        }
        
        updateSelection(sel);
        updateContentDescription();
    }

    protected IProject getProjectOfSelectionList(List<Object> selectionList) {
        IProject project = null;
        for (Object object : selectionList) {
            IFile file = (IFile) Platform.getAdapterManager().getAdapter(object, IFile.class);
            IProject theproject = null;
            if (file != null) {
                theproject = file.getProject();
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
    
    private void updateSelection(ISelection selection) {
        if(selection instanceof IStructuredSelection) {
            IStructuredSelection sel = (IStructuredSelection) selection;
        
            IProject project = getProjectOfSelectionList(sel.toList());
            if (project != null) {
                this.currentProject = project;
            }    
        }
    }
    
    private void updateContentDescription() {
        if (this.currentProject != null) {
            int projectDomain = getProjectDomainId(this.currentProject);
            DomainEntry de = findDomainById(IProjectDomains.PROJECT_DOMAINS, projectDomain);
            
            if(de != null) {
                setContentDescription("Active project: " + this.currentProject.getName() + " (" + de.getName() + ")");
            } else {
                setContentDescription("Active project: " + this.currentProject.getName() + " (domain unset)");                
            }
        } else {
            setContentDescription("Please select a project");
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
            
        }
        return ScaseUiConstants.PROP_PROJECT_DOMAIN_DEFAULT;
    }
    
    @Override
    public void added(IExtension[] extensions) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void removed(IExtension[] extensions) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void added(IExtensionPoint[] extensionPoints) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void removed(IExtensionPoint[] extensionPoints) {
        // TODO Auto-generated method stub
        
    }

}