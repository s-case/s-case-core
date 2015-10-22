package eu.scasefp7.eclipse.core.ui.views;

import org.eclipse.swt.graphics.Image;

/**
 * A notification with UI specific extensions.
 * 
 * @author Steffen Pingel
 */
public abstract class AbstractUiNotification extends AbstractNotification {

	public AbstractUiNotification(String eventId) {
		super(eventId);
	}

	public abstract Image getNotificationImage();

	public abstract Image getNotificationKindImage();

	/**
	 * Executes the default action for opening the notification.
	 */
	public abstract void open();

}
