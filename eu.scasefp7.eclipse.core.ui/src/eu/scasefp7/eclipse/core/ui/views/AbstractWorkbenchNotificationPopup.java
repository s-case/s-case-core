package eu.scasefp7.eclipse.core.ui.views;

import org.eclipse.mylyn.commons.ui.dialogs.AbstractNotificationPopup;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

/**
 * A popup window that uses the workbench shell image in the title.
 * 
 * @author Steffen Pingel
 */
public class AbstractWorkbenchNotificationPopup extends AbstractNotificationPopup {

	/**
	 * TODO
	 * @param display
	 * @param style
	 */
	public AbstractWorkbenchNotificationPopup(Display display, int style) {
		super(display, style);
	}

	/**
	 * TODO
	 * @param display
	 */
	public AbstractWorkbenchNotificationPopup(Display display) {
		super(display);
	}

	/**
     * Utility method to get the best parenting possible for a dialog. If there is a modal shell create it so as to
     * avoid two modal dialogs. If not then return the shell of the active workbench window. If neither can be found
     * return null.
     * <p>
     * <b>Note: Applied from patch on bug 99472.</b>
     * 
     * @return Shell or <code>null</code>
     */
    public static Shell getBestShell() {
        if (!PlatformUI.isWorkbenchRunning() || PlatformUI.getWorkbench().isClosing()) {
            return null;
        }
        Shell modal = getModalShellExcluding(null);
        if (modal != null) {
            return modal;
        }
        return getNonModalShell();
    }

    /**
     * Return the modal shell that is currently open. If there isn't one then return null.
     * <p>
     * <b>Note: Applied from patch on bug 99472.</b>
     * 
     * @param shell
     *            A shell to exclude from the search. May be <code>null</code>.
     * @return Shell or <code>null</code>.
     */
    private static Shell getModalShellExcluding(Shell shell) {
        IWorkbench workbench = PlatformUI.getWorkbench();
        Shell[] shells = workbench.getDisplay().getShells();
        int modal = SWT.APPLICATION_MODAL | SWT.SYSTEM_MODAL | SWT.PRIMARY_MODAL;
        for (Shell shell2 : shells) {
            if (shell2.equals(shell)) {
                break;
            }
            // Do not worry about shells that will not block the user.
            if (shell2.isVisible()) {
                int style = shell2.getStyle();
                if ((style & modal) != 0) {
                    return shell2;
                }
            }
        }
        return null;
    }    
    
    /**
     * Get the active non modal shell. If there isn't one return null.
     * <p>
     * <b>Note: Applied from patch on bug 99472.</b>
     * 
     * @return Shell
     */
    private static Shell getNonModalShell() {
        IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
        if (window == null) {
            IWorkbenchWindow[] windows = PlatformUI.getWorkbench().getWorkbenchWindows();
            if (windows.length > 0) {
                return windows[0].getShell();
            }
        } else {
            return window.getShell();
        }

        return null;
    }

    
	/**
	 * @param maximumHeight
	 * @return image
	 */
	public static Image getWorkbenchShellImage(int maximumHeight) {
        // always use the launching workbench window
        IWorkbenchWindow[] windows = PlatformUI.getWorkbench().getWorkbenchWindows();
        if (windows != null && windows.length > 0) {
            IWorkbenchWindow workbenchWindow = windows[0];
            if (workbenchWindow != null && !workbenchWindow.getShell().isDisposed()) {
                Image image = getBestShell().getImage();
                int diff = Integer.MAX_VALUE;
                if (image != null && image.getBounds().height <= maximumHeight) {
                    diff = maximumHeight - image.getBounds().height;
                } else {
                    image = null;
                }

                Image[] images = getBestShell().getImages();
                if (images != null && images.length > 0) {
                    // find the icon that is closest in size, but not larger than maximumHeight 
                    for (Image image2 : images) {
                        int newDiff = maximumHeight - image2.getBounds().height;
                        if (newDiff >= 0 && newDiff <= diff) {
                            diff = newDiff;
                            image = image2;
                        }
                    }
                }

                return image;
            }
        }
        return null;
    }

}
