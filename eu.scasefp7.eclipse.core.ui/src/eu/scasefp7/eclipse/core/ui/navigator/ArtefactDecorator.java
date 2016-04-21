package eu.scasefp7.eclipse.core.ui.navigator;

import org.eclipse.jface.viewers.IDecoration;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ILightweightLabelDecorator;

/**
 * Decorates the objects in the navigation.
 * 
 * @author Marin Orlic
 */
public class ArtefactDecorator implements ILightweightLabelDecorator {

    public void addListener(ILabelProviderListener listener) {
    }

    public void dispose() {
    }

    public boolean isLabelProperty(Object element, String property) {
        return false;
    }

    public void removeListener(ILabelProviderListener listener) {
    }

    public void decorate(Object element, IDecoration decoration) {
        if ((element instanceof IArtefact)) {
            if (element instanceof TransitionArtefact) {
                decorateTransition((TransitionArtefact) element, decoration);
            } else if (element instanceof RequirementArtefact) {
                decorateRequirement((RequirementArtefact) element, decoration);
            } else if (element instanceof ObjectArtefact) {
                decorateObject((ObjectArtefact) element, decoration);
            } else if (element instanceof ActivityArtefact) {
                decorateActivity((ActivityArtefact) element, decoration);
            } else {
                decorateArtefact((IArtefact) element, decoration);
            }
        } else if ((element instanceof IArtefactGroup)) {
            decorateArtefactGroup((IArtefactGroup) element, decoration);
        }
    }

    private void decorateActivity(ActivityArtefact act, IDecoration decoration) {
        StringBuilder sb = new StringBuilder();
        sb.append("[").append(act.getKind()).append("] ");
        decoration.addPrefix(sb.toString());
        if(act.hasProperties()) {
            decoration.addSuffix(" " + act.getProperties());
        }
    }

    private void decorateObject(ObjectArtefact obj, IDecoration decoration) {
        if(obj.hasActions()) {
            decoration.addPrefix(obj.getActions().toString() + " ");
        }
        if(obj.hasProperties()) {
            decoration.addSuffix(" " + obj.getProperties().toString());
        }
    }

    private void decorateRequirement(RequirementArtefact req, IDecoration decoration) {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        sb.append(req.getConcept());
        sb.append("] ");
        decoration.addPrefix(sb.toString());
        
        sb = new StringBuilder();
        sb.append(" [");
        sb.append(req.getId());
        sb.append("]");
        decoration.addSuffix(sb.toString());
    }

    private void decorateArtefact(IArtefact comp, IDecoration decoration) {
       
    }

    private void decorateTransition(TransitionArtefact tran, IDecoration decoration) {
        StringBuilder sb = new StringBuilder();
        if (tran.hasCondition()) {
            sb.append(" [").append(tran.getCondition()).append("]");
        }
        decoration.addSuffix(sb.toString());
    }

    private void decorateArtefactGroup(IArtefactGroup ref, IDecoration decoration) {
       
    }
}

    
