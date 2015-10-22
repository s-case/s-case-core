package eu.scasefp7.eclipse.core.ui.views;

import java.util.Date;

import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.IAdaptable;

/**
 * A notification. Each notification has an associated <code>eventId</code> that identifies the type of the
 * notification.
 * 
 * @author Rob Elves
 * @author Mik Kersten
 * @author Steffen Pingel
 */
public abstract class AbstractNotification implements Comparable<AbstractNotification>, IAdaptable {

	private final String eventId;

	public AbstractNotification(String eventId) {
		Assert.isNotNull(eventId);
		this.eventId = eventId;
	}

	public int compareTo(AbstractNotification o) {
		if (o == null) {
			return 1;
		}
		return compare(getDate(), o.getDate());
	}

	public String getEventId() {
		return eventId;
	}

	public abstract Date getDate();

	public abstract String getDescription();

	public abstract String getLabel();

	/**
	 * Returns a token that identifies correlated notifications, e.g. all notifications resulting from a refresh
	 * operation. Returns <code>null</code> by default.
	 * 
	 * @return any object; null, if no token is specified
	 */
	public Object getToken() {
		return null;
	}

	/**
     * Compares <code>o1</code> and <code>o2</code>.
     * 
     * @since 3.7
     * @return a negative integer, 0, or a positive, if o1 is less than o2, o1 equals o2 or o1 is more than o2; null is
     *         considered less than any value
     */
    public static <T> int compare(Comparable<T> o1, T o2) {
        if (o1 == null) {
            return (o2 != null) ? 1 : 0;
        } else if (o2 == null) {
            return -1;
        }
        return o1.compareTo(o2);
    }
}
