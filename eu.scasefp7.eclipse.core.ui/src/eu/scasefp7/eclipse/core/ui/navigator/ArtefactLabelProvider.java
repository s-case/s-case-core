package eu.scasefp7.eclipse.core.ui.navigator;

import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.swt.graphics.Image;

import eu.scasefp7.eclipse.core.ui.Activator;
import eu.scasefp7.eclipse.core.ui.SharedImages;

/**
 * Label provider for artefacts in navigation.
 * 
 * @author Leonora Gaspar
 * @author Marin Orlic
 */
public class ArtefactLabelProvider implements ILabelProvider {

    private SharedImages images = Activator.getImages();

    public void addListener(ILabelProviderListener listener) {
    }

    public void dispose() {
    }

    public boolean isLabelProperty(Object element, String property) {
        return false;
    }

    public void removeListener(ILabelProviderListener listener) {
    }

    public Image getImage(Object element) {
        String type = "";

        if (element instanceof IArtefact)
            type = ((IArtefact) element).getType();
        if (element instanceof IArtefactGroup)
            type = ((IArtefactGroup) element).getType();

        if (type.startsWith("Activit"))
            return this.images.getImage(SharedImages.Images.OBJ_ACTIVITY);
        if (type.startsWith("Requirement"))
            return this.images.getImage(SharedImages.Images.OBJ_REQUIREMENT);
        if (type.startsWith("Object"))
            return this.images.getImage(SharedImages.Images.OBJ_OBJECT);
        if (type.startsWith("Transition"))
            return this.images.getImage(SharedImages.Images.OBJ_TRANSITION);
        if (type.startsWith("Root"))
            return this.images.getImage(SharedImages.Images.OBJ_ARTF_ROOT);
        if (element instanceof IArtefact)
            return this.images.getImage(SharedImages.Images.OBJ_ARTEFACT);
        
        // Default: no image
        return null;
    }

    public String getText(Object element) {
        System.out.println("Label for: " + element);

        if ((element instanceof IArtefact)) {
            if(element instanceof TransitionArtefact) {
                return getTransitionText((TransitionArtefact) element);
            } 
            return ((IArtefact) element).getName();
        }
        if ((element instanceof IArtefactGroup))
            return ((IArtefactGroup) element).getName();

        return null;
    }

    private String getTransitionText(TransitionArtefact tran) {
        StringBuilder sb = new StringBuilder();
        sb.append(tran.getSource());
        sb.append(" \u2192 "); // RIGHTWARDS ARROW
        sb.append(tran.getTarget());
        return sb.toString();
    }
}