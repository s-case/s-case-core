package eu.scasefp7.eclipse.core.ui.handlers;

import java.net.MalformedURLException;
import java.net.URL;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.browser.IWebBrowser;
//import org.eclipse.ui.browser.IWorkbenchBrowserSupport;
//import org.eclipse.ui.internal.browser.WorkbenchBrowserSupport;


import eu.scasefp7.eclipse.core.ui.Activator;
import eu.scasefp7.eclipse.core.ui.ScaseUiConstants;


/**
 * Opens a URL given as a command parameter in an internal browser.
 * 
 * @author Leonora Gašpar
 *
 */
public class OpenBrowserHandler extends AbstractHandler {

	/**
	 * Command parameter - URL to open
	 */
	public static final String PARAM_URL = "eu.scasefp7.eclipse.core.ui.commands.openBrowser.url";
	
    @Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
        String url = "";
		try {
			url = event.getParameter(PARAM_URL);
			if(url == null) {
				url = ScaseUiConstants.PROJECT_HOMEPAGE;
			}
			
			final IWebBrowser browser = PlatformUI.getWorkbench().getBrowserSupport().createBrowser("browser");
			browser.openURL(new URL(url));
		} catch (PartInitException | MalformedURLException e) {
		    Activator.log("Unable to open browser on " + url, e);
		}
		
//		ALTERNATIVE:
		
//		try {
//			int style = IWorkbenchBrowserSupport.AS_EDITOR | IWorkbenchBrowserSupport.LOCATION_BAR | IWorkbenchBrowserSupport.STATUS;
//			IWebBrowser browser = WorkbenchBrowserSupport.getInstance().createBrowser(style, "MyBrowserID", "MyBrowserName", "MyBrowser Tooltip");
//			browser.openURL(new URL("http://www.google.de"));
//		} catch (PartInitException | MalformedURLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
		return null;
	}



}