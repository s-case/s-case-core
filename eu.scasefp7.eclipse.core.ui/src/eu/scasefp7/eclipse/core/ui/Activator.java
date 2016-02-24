package eu.scasefp7.eclipse.core.ui;

import java.util.Dictionary;

import java.util.Hashtable;

import org.eclipse.osgi.service.debug.DebugOptions;
import org.eclipse.osgi.service.debug.DebugOptionsListener;
import org.eclipse.osgi.service.debug.DebugTrace;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;


/**
 * The activator class controls the plug-in life cycle
 */
public class Activator extends AbstractUIPlugin implements DebugOptionsListener {

	// The plug-in ID
	public static final String PLUGIN_ID = "eu.scasefp7.eclipse.core.ui"; //$NON-NLS-1$
	
	// The shared instance
	private static Activator plugin;
	
	// Images
	private static SharedImages images = null;
	
	// fields to cache the debug flags
	public static boolean DEBUG = false;
	public static DebugTrace trace = null;


	/**
	 * The constructor
	 */
	public Activator() {
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
		Dictionary<String, String> props = new Hashtable<String,String>(4);
		props.put(DebugOptions.LISTENER_SYMBOLICNAME, PLUGIN_ID); 
		context.registerService(DebugOptionsListener.class.getName(), this, props);
		plugin = this;

	    
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception { 
		plugin = null;
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

	@Override
	public void optionsChanged(DebugOptions options) {
		DEBUG = options.getBooleanOption(PLUGIN_ID + "/debug", false);
		trace = options.newDebugTrace(PLUGIN_ID);
		
	}
	
}
