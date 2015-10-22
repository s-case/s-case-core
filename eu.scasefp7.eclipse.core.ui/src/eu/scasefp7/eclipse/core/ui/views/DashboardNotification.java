package eu.scasefp7.eclipse.core.ui.views;

import java.util.Date;

import org.eclipse.swt.graphics.Image;

class DashboardNotification extends AbstractUiNotification {
    
    private String label;
    private String description;
    private Image image;

    DashboardNotification(String eventId, String label, String description, Image image) {
        super(eventId);
        this.label = label;
        this.description = description;
        this.image = image;
    }

    @Override
    public Object getAdapter(Class adapter) {
        return null;
    }

    @Override
    public String getLabel() {
        return this.label;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public Date getDate() {
        return new Date();
    }

    @Override
    public Object getToken() {
        return getEventId();
    }

    @Override
    public Image getNotificationImage() {
        return null;
    }

    @Override
    public Image getNotificationKindImage() {
        return this.image;
    }

    @Override
    public void open() {

    }
}