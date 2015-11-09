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


public class OpenBrowserHandler extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		try {
			String url = event.getParameter("eu.scasefp7.eclipse.core.ui.url");
			if(url.equals(null))
				url = "http://www.google.com";
			
			final IWebBrowser browser = PlatformUI.getWorkbench().getBrowserSupport().createBrowser("browser");
			browser.openURL(new URL(url));
		} catch (PartInitException | MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
