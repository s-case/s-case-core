package eu.scasefp7.eclipse.core.ui;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.TimeZone;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.QualifiedName;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.osgi.service.debug.DebugOptions;
import org.eclipse.osgi.service.debug.DebugOptionsListener;
import org.eclipse.osgi.service.debug.DebugTrace;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;


/**
 * The activator class controls the plug-in life cycle
 */
public class Activator extends AbstractUIPlugin implements DebugOptionsListener {

	/** The plug-in ID. */
	public static final String PLUGIN_ID = "eu.scasefp7.eclipse.core.ui"; //$NON-NLS-1$
	
	/** The starting time of the current session for this plugin. */
    private static String STARTING_TIME;

    /** The current error ID for this session for this plugin. */
    private static int errorID;
	
	/** The shared instance. */
	private static Activator plugin;
	
	/** The shared images. */
	private static SharedImages images = null;
	
	/** Cached debug tracing flag. */
    public static boolean DEBUG = false;
    
    /** Cached debug trace output. */
    public static DebugTrace TRACE = null;
    
    IResourceChangeListener rcl;
	
	/**
	 * The constructor.
	 */
	public Activator() {
	}

	/**
	 * Called when plugin starts. Marks the start time for logging.
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
		STARTING_TIME = Activator.oDateFormatter.format(new Date());
        errorID = 0;
        
        // Register for debug trace
        Dictionary<String, String> props = new Hashtable<String,String>(4);
        props.put(DebugOptions.LISTENER_SYMBOLICNAME, PLUGIN_ID); 
        context.registerService(DebugOptionsListener.class.getName(), this, props);
        IWorkspace workspace = ResourcesPlugin.getWorkspace();
        rcl = new IResourceChangeListener() {
           public void resourceChanged(IResourceChangeEvent event) {
        	   IResourceDelta delta = event.getDelta();
        	   try {
				delta.accept(new BuildDeltaVisitor());
			} catch (CoreException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
           }
        };
        workspace.addResourceChangeListener(rcl);
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		ResourcesPlugin.getWorkspace().removeResourceChangeListener(rcl);
		super.stop(context);
	}

	/**
	 * Returns the shared instance
	 *
	 * @return the shared instance
	 */
	public static Activator getDefault() {
		return plugin;
	}

	/**
	 * Returns the shared image registry
	 * 
	 * @return the images
	 */
	public static SharedImages getImages() {
	    if(images == null) {
	        images = new SharedImages();
	    }
	    
	    return images;
	}
	
	/**
     * Returns an image descriptor for the image file at the given
     * plug-in relative path
     *
     * @param path the path
     * @return the image descriptor
     */
    public static ImageDescriptor getImageDescriptor(String path) {
        return imageDescriptorFromPlugin(PLUGIN_ID, path);
    }
	
	/**
     * Logs an exception to the Eclipse log file. This method detects the class and the method in which the exception
     * was caught automatically using the current stack trace. If required, the user can override these values by
     * calling {@link #log(String, String, String, Exception)} instead.
     * 
     * @param message a human-readable message about the exception.
     * @param exception the exception that will be logged.
     */
    public static void log(String message, Exception exception) {
        StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[2];
        log(stackTraceElement.getClassName(), stackTraceElement.getMethodName(), message, exception);
    }

    /**
     * Logs an exception to the Eclipse log file. Note that in most cases you can use the
     * {@link #log(String, Exception)} method which automatically detects the class and the method in which the
     * exception was caught, so it requires as parameters only a human-readable message and the exception.
     * 
     * @param className the name of the class in which the exception was caught.
     * @param methodName the name of the method in which the exception was caught.
     * @param message a human-readable message about the exception.
     * @param exception the exception that will be logged.
     */
    public static void log(String className, String methodName, String message, Exception exception) {
        StringBuilder msg = new StringBuilder(message);
        msg.append("\n!ERROR_ID t" + errorID);
        msg.append("\n!SERVICE_NAME MDE UI Plugin");
        msg.append("\n!SERVICE_VERSION 1.0.0-SNAPSHOT");
        msg.append("\n!STARTING_TIME ").append(STARTING_TIME);
        msg.append("\n!CLASS_NAME ").append(className);
        msg.append("\n!FUNCTION_NAME ").append(methodName);
        msg.append("\n!FAILURE_TIMESTAMP ").append(oDateFormatter.format(new Date()));
        errorID++;
        if (plugin != null)
            plugin.getLog().log(new Status(Status.ERROR, PLUGIN_ID, Status.OK, msg.toString(), exception));
        else
            exception.printStackTrace();
    }
	
	/**
     * A UTC ISO 8601 date formatter used to log the time of errors.
     */
    private static final DateFormat oDateFormatter;
    static {
        oDateFormatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm'Z'");
        oDateFormatter.setTimeZone(TimeZone.getTimeZone("UTC"));
    }
    
    /**
     * Refreshes the debug trace sink when debug options have changed.
     */
    @Override
    public void optionsChanged(DebugOptions options) {
        DEBUG = options.getBooleanOption(PLUGIN_ID + "/debug", false);
        TRACE = options.newDebugTrace(PLUGIN_ID);
    }
    class BuildDeltaVisitor implements IResourceDeltaVisitor {

        public boolean visit(IResourceDelta delta) throws CoreException {
            switch (delta.getKind()) {
            case IResourceDelta.REMOVED:
            	   IResource res = delta.getResource();
	         	   if(res instanceof IFolder) {
	         		   IProject project = ((IFolder)res).getProject();
	         		   String resPath = res.getFullPath().toString();
	         		   try {
	         			String projectPath = project.getFullPath().toPortableString(); //path of the project containing resource
	 					String modelsPath = project.getPersistentProperty(new QualifiedName("", ScaseUiConstants.MODELS_FOLDER));
	 					String outputPath = project.getPersistentProperty(new QualifiedName("", ScaseUiConstants.OUTPUT_FOLDER));
	 					String reqPath = project.getPersistentProperty(new QualifiedName("", ScaseUiConstants.REQUIREMENTS_FOLDER));
	 					String comPath = project.getPersistentProperty(new QualifiedName("", ScaseUiConstants.COMPOSITIONS_FOLDER));
	 					IPath newPath = delta.getMovedToPath(); //path of the new (renamed) project, or null
	 					
	 					if(resPath.equals(modelsPath)) 
	 							if ((delta.getFlags() & IResourceDelta.MOVED_TO) != 0) //folder renamed
	 							    project.setPersistentProperty(new QualifiedName("", ScaseUiConstants.MODELS_FOLDER), newPath.toPortableString());
	 							else //folder deleted
	 								project.setPersistentProperty(new QualifiedName("", ScaseUiConstants.MODELS_FOLDER), projectPath);
	 					
	 					else if(resPath.equals(outputPath))  
	 							if ((delta.getFlags() & IResourceDelta.MOVED_TO) != 0)
	 							    project.setPersistentProperty(new QualifiedName("", ScaseUiConstants.OUTPUT_FOLDER), newPath.toPortableString());	 							
	 							else
	 								project.setPersistentProperty(new QualifiedName("", ScaseUiConstants.OUTPUT_FOLDER), projectPath);
	 					
	 					else if(resPath.equals(reqPath)) 
	 							if ((delta.getFlags() & IResourceDelta.MOVED_TO) != 0)	
	 							    project.setPersistentProperty(new QualifiedName("", ScaseUiConstants.REQUIREMENTS_FOLDER), newPath.toPortableString());			
	 							else
	 								project.setPersistentProperty(new QualifiedName("", ScaseUiConstants.REQUIREMENTS_FOLDER), projectPath);
	 					
	 					else if(resPath.equals(comPath))  
	 							if ((delta.getFlags() & IResourceDelta.MOVED_TO) != 0)	
	 							    project.setPersistentProperty(new QualifiedName("", ScaseUiConstants.COMPOSITIONS_FOLDER), newPath.toPortableString());
	 							else
	 								project.setPersistentProperty(new QualifiedName("", ScaseUiConstants.COMPOSITIONS_FOLDER), projectPath);
	 					
	 				} catch (CoreException e) {
	 					// TODO Auto-generated catch block
	 					e.printStackTrace();
	 				}
	         	   }
         	   System.out.println("detected!");
            
            	break;
            case IResourceDelta.NO_CHANGE:
                break;
            }

            return true;
        }
    }
}