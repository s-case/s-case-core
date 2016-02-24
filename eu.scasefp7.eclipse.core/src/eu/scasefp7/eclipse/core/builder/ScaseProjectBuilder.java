package eu.scasefp7.eclipse.core.builder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.core.commands.Command;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.NotEnabledException;
import org.eclipse.core.commands.NotHandledException;
import org.eclipse.core.commands.Parameterization;
import org.eclipse.core.commands.ParameterizedCommand;
import org.eclipse.core.commands.common.NotDefinedException;
import java.io.File;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;
import org.eclipse.core.resources.IResourceVisitor;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.resources.ProjectScope;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IRegistryEventListener;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.variables.IDynamicVariable;
import org.eclipse.core.variables.IDynamicVariableResolver;
import org.eclipse.core.variables.IStringVariable;
import org.eclipse.core.variables.VariablesPlugin;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.commands.ICommandService;
import org.eclipse.ui.handlers.IHandlerService;
import org.eclipse.ui.preferences.ScopedPreferenceStore;
import org.eclipse.ui.services.IServiceLocator;

/**
 * Builder for S-CASE projects.
 * 
 * @author Leonora Gašpar
 * @author Marin Orlić
 *
 */
public class ScaseProjectBuilder extends IncrementalProjectBuilder implements ISelectionListener, IRegistryEventListener {

    /**
     * Builder ID
     */
    public static final String BUILDER_ID = "eu.scasefp7.eclipse.core.scaseBuilder";

    /**
     * Marker ID
     */
    private static final String MARKER_TYPE = "eu.scasefp7.eclipse.core.problemMarker";
    
    private static final String CONTRIBUTION_STRING = "string";
    private static final String CONTRIBUTION_VARIABLE = "variable";
    private static final String CONTRIBUTION_PREFERENCE = "preference";
    
    
    class BuildDeltaVisitor implements IResourceDeltaVisitor {

        public boolean visit(IResourceDelta delta) throws CoreException {
            switch (delta.getKind()) {
            case IResourceDelta.ADDED:
            case IResourceDelta.CHANGED:
            	IResource resource = delta.getResource();
            	if (resource instanceof IFile) {
            		IExtensionRegistry registry = Platform.getExtensionRegistry();
            		IConfigurationElement[] contributions = registry.getConfigurationElementsFor("eu.scasefp7.eclipse.core.builder");
            		 if (contributions.length > 0) 
            			 for (IConfigurationElement elem : contributions) 
            				 if (elem.getName().equals("buildStep")) {
            					 String commandId = elem.getAttribute("commandId");
            					 String regex = elem.getAttribute("condition");
            					
            					 IConfigurationElement[] elements = elem.getChildren("commandParameters");
            					 boolean hasParameters = elements.length > 0;
            					 HashMap<String,String> parameters = new HashMap<String,String>();    					 
            					 
            					 if(hasParameters)
                						 getParameters(resource, parameters, elements[0]);
            					 
            	                 if(resource.getName().matches(regex)) {
            	                	 if(!hasParameters)
            	                		 executeCommand(commandId, resource.getFullPath().toString());
            	                	 else
            	                		 executeCommand(commandId, resource.getFullPath().toString(), parameters);
            	                	 System.out.println("Command: " + commandId+" on file: "+resource.getFullPath().toString()+" (d)");
            	                 }
         					}
            	}

                break;
            case IResourceDelta.REMOVED:
            case IResourceDelta.NO_CHANGE:
                break;
            }

            return true;
        }
    }

    class BuildResourceVisitor implements IResourceVisitor {
        public boolean visit(IResource resource) {
        	
        	if (resource instanceof IFile) {
        		IExtensionRegistry registry = Platform.getExtensionRegistry();
        		IConfigurationElement[] contributions = registry.getConfigurationElementsFor("eu.scasefp7.eclipse.core.builder");
        		 if (contributions.length > 0) 
        			 for (IConfigurationElement elem : contributions) 
        				 if (elem.getName().equals("buildStep")) {
        					 String commandId = elem.getAttribute("commandId");
        					 String regex = elem.getAttribute("condition");
        					 IConfigurationElement[] elements = elem.getChildren("commandParameters");
        					 boolean hasParameters = elements.length > 0;
        					 HashMap<String,String> parameters = new HashMap<String,String>();
        					 
        					 if(hasParameters)
        						 getParameters(resource, parameters, elements[0]);
        					 
        	                 if(resource.getName().matches(regex)) {
        	                	 if(!hasParameters)
        	                		 executeCommand(commandId, resource.getFullPath().toString());
        	                	 else
        	                		 executeCommand(commandId, resource.getFullPath().toString(), parameters);
        	                	 System.out.println("Command: " + commandId+" on file: "+resource.getFullPath().toString());
        	                 }
     					}
        	}
            // return true to continue visiting children.
            return true;
        }
    }
    class CleanResourceVisitor implements IResourceVisitor {
        public boolean visit(IResource resource) throws CoreException {
        	
        	if (resource instanceof IFile) {
        		IExtensionRegistry registry = Platform.getExtensionRegistry();
        		IConfigurationElement[] contributions = registry.getConfigurationElementsFor("eu.scasefp7.eclipse.core.builder");
        		 if (contributions.length > 0) 
        			 for (IConfigurationElement elem : contributions) 
        				 if (elem.getName().equals("cleanStep")) {
        					
        					 IConfigurationElement[] removals = elem.getChildren("removal");
        					 if(removals.length>0)
	        					 for (IConfigurationElement removal : removals) {
	        						 String regex = removal.getAttribute("condition");
	        						  if(resource.getName().matches(regex)) {
	        							 IPath path = resource.getRawLocation();
	        							 File file = path.toFile();
	        							 file.delete();
	        							 String name = resource.getName();
										 resource.delete(false, null);
										System.out.println("Removal: "+name + " removed!");
	        							  
	        						  }
	        						
	        					 }
        					 IConfigurationElement[] commands = elem.getChildren("command");
        					 
        					 if(commands.length>0)
	        					 for (IConfigurationElement command : commands) {
	        						 String regex = command.getAttribute("condition");
	        						 String commandId = command.getAttribute("commandId");
	        						  if(resource.getName().matches(regex)) {
	        							  IConfigurationElement[] params = command.getChildren("commandParameters");
	        	        				  boolean hasParameters = params.length > 0;
	        	        				  HashMap<String,String> parameters = new HashMap<String,String>();	 
	        	        				  if(hasParameters)
	        	        					  getParameters(resource, parameters, params[0]);
	        	        				  
	        	        	               if(!hasParameters)
	        	        	                	executeCommand(commandId, resource.getFullPath().toString());
	        	        	               else
	        	        	                	executeCommand(commandId, resource.getFullPath().toString(), parameters);
	        	        	               System.out.println("Cleaning command...: " + commandId);  
	        						  }
	        						
	        					 }
     					}
        	}
            // return true to continue visiting children.
            return true;
        }
    }
    
    private void getParameters(IResource resource, HashMap<String,String> parameters, IConfigurationElement elements) {
    	for (IConfigurationElement element : elements.getChildren()) {
			 if (element.getName().equals(CONTRIBUTION_STRING)) {
				 String paramName = element.getAttribute("name");
				 String value = element.getAttribute("value");
				 parameters.put(paramName, value);
			 }
			 if (element.getName().equals(CONTRIBUTION_VARIABLE)) {
				 String className = element.getAttribute("variableResolver");
				 String variableId = element.getAttribute("variableId");
				 String paramName = element.getAttribute("name");
				 IStringVariable[] variables = VariablesPlugin.getDefault().getStringVariableManager().getVariables();
				 
				 Class<? extends IDynamicVariableResolver> clazz;
				try {
					clazz = Class.forName(className).asSubclass(IDynamicVariableResolver.class);
					IDynamicVariableResolver resolver = (IDynamicVariableResolver) clazz.newInstance();
					 for (int i = 0; i < variables.length; i++) {
							IDynamicVariable currVar = ((IDynamicVariable)variables[i]);
							String name = currVar.getName();
							String value = "";
							if(name.equals(variableId ) ){
								value = resolver.resolveValue(currVar, null);
								parameters.put(paramName, value );
							}	
					 }
				} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | IllegalArgumentException  | SecurityException | CoreException e) {
					e.printStackTrace();
				}

			 }
			 if (element.getName().equals(CONTRIBUTION_PREFERENCE)) {
				 String preferenceId = element.getAttribute("preferenceId");
				 String pluginId = element.getAttribute("pluginId");
				 String paramName = element.getAttribute("name");
				 
				 ProjectScope ps = new ProjectScope(resource.getProject());
				 ScopedPreferenceStore scoped = new ScopedPreferenceStore(ps, pluginId);
				 String value = scoped.getString(preferenceId);
				 parameters.put(paramName, value );
			 }
	 }
    }


    protected void executeCommand(String commandId) {
        IServiceLocator serviceLocator = PlatformUI.getWorkbench();
        IHandlerService handlerService = (IHandlerService) serviceLocator.getService(IHandlerService.class);
        try {
            handlerService.executeCommand(commandId, null);
        } catch (ExecutionException | NotDefinedException | NotEnabledException | NotHandledException ex) {
            ex.printStackTrace();
        }
    }
    
    /**
     * Execution of command without parameters on resource with given name.
     * @param commandId
     * @param name
     */

    protected void executeCommand(String commandId, String name) {
        IServiceLocator serviceLocator = PlatformUI.getWorkbench();

        ICommandService commandService = (ICommandService) serviceLocator.getService(ICommandService.class);
        IHandlerService handlerService = (IHandlerService) serviceLocator.getService(IHandlerService.class);
        Command command = commandService.getCommand(commandId);

        Parameterization[] params;
		try {
			params = new Parameterization[] { new Parameterization(command.getParameter("fileName"), (String) name) };
			ParameterizedCommand parametrizedCommand = new ParameterizedCommand(command, params);

	          handlerService.executeCommand(parametrizedCommand, null);
		} catch (NotDefinedException | ExecutionException | NotEnabledException | NotHandledException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
    }
    /**
     * Execution of command with parameters on file with given name.
     * @param commandId
     * @param name
     * @param parameters
     */
    protected void executeCommand(String commandId, String name, HashMap<String,String> parameters) {
        IServiceLocator serviceLocator = PlatformUI.getWorkbench();

        ICommandService commandService = (ICommandService) serviceLocator.getService(ICommandService.class);
        IHandlerService handlerService = (IHandlerService) serviceLocator.getService(IHandlerService.class);
        Command command = commandService.getCommand(commandId);
        try {
	        List<Parameterization> par = new ArrayList<Parameterization>();
	        par.add(new Parameterization(command.getParameter("fileName"), (String) name));
	        
	        for(Entry<String, String> e : parameters.entrySet()) 
	            par.add(new Parameterization(command.getParameter(e.getKey()), e.getValue() ));
	      
	        Parameterization[] params = (Parameterization[]) par.toArray(new Parameterization[par.size()]);
	        ParameterizedCommand parametrizedCommand = new ParameterizedCommand(command, params);
	        handlerService.executeCommand(parametrizedCommand, null);
		} catch (NotDefinedException | ExecutionException | NotEnabledException | NotHandledException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
    }


    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.core.internal.events.InternalBuilder#build(int,
     * java.util.Map, org.eclipse.core.runtime.IProgressMonitor)
     */
    protected IProject[] build(int kind, @SuppressWarnings("rawtypes") Map args, IProgressMonitor monitor) throws CoreException {

        if (kind == FULL_BUILD) 
            fullBuild(monitor);
        else {
            IResourceDelta delta = getDelta(getProject());
            if (delta == null) {
                fullBuild(monitor);
            } else {
                incrementalBuild(delta, monitor);
            }
        }
        return null;
    }

    

	protected void clean(IProgressMonitor monitor) throws CoreException {
        // delete markers set and files created
        getProject().deleteMarkers(MARKER_TYPE, true, IResource.DEPTH_INFINITE);
        cleanBuild(monitor);
    }

    protected void deleteMarkers(IFile file) {
        try {
            file.deleteMarkers(MARKER_TYPE, false, IResource.DEPTH_ZERO);
        } catch (CoreException ce) {
        	ce.printStackTrace();
        }
    }
    
    private void cleanBuild(IProgressMonitor monitor) {
    	try {
            //clean(monitor);
            getProject().accept(new CleanResourceVisitor());
        } catch (CoreException e) {
            e.printStackTrace();
        }
	}

    protected void fullBuild(final IProgressMonitor monitor) throws CoreException {
        try {
            //clean(monitor);
            getProject().accept(new BuildResourceVisitor());
        } catch (CoreException e) {
        	e.printStackTrace();
        }
    }

    protected void incrementalBuild(IResourceDelta delta, IProgressMonitor monitor) throws CoreException {
        try {
            //clean(monitor);
             delta.accept(new BuildDeltaVisitor());
        } catch (CoreException e) {
            e.printStackTrace();
        }
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

	@Override
	public void selectionChanged(IWorkbenchPart part, ISelection selection) {
		// TODO Auto-generated method stub
		
	}

	

	
	
}

