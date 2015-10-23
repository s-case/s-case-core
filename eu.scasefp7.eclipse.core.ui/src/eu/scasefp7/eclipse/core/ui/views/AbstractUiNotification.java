package eu.scasefp7.eclipse.core.ui.views;

import org.eclipse.swt.graphics.Image;

/**
 * A notification with UI specific extensions.
 * 
 * @author Steffen Pingel
 */
public abstract class AbstractUiNotification extends AbstractNotification {

	/**
	 * @param eventId
	 */
	public AbstractUiNotification(String eventId) {
		super(eventId);
	}

	/**
	 * @return image
	 */
	public abstract Image getNotificationImage();

	/**
	 * @return image
	 */
	public abstract Image getNotificationKindImage();

	/**
	 * Executes the default action for opening the notification.
	 */
	public abstract void open();

}
