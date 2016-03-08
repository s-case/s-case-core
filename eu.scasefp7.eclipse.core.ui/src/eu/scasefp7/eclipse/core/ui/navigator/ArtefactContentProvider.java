package eu.scasefp7.eclipse.core.ui.navigator;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.widgets.Display;

import eu.scasefp7.eclipse.core.ontology.DynamicOntologyAPI;
import eu.scasefp7.eclipse.core.ontology.LinkedOntologyAPI;
import eu.scasefp7.eclipse.core.ontology.StaticOntologyAPI;

/**
 * Adds ontology contents to navigation.
 * 
 * @author Leonora Gaspar
 * @author Marin Orlic
 */
public class ArtefactContentProvider implements ITreeContentProvider, IResourceChangeListener {
    private static final Object[] EMPTY = new Object[0];
    private Viewer viewer;
    private Object[] parents = null; // parents of artefacts
    ArtefactGroup[] rootGroup = new ArtefactGroup[1];
    private String[] groupNames = { "Requirements", "Objects", "Activities", "Transitions" };

    /**
     * Constructs the content provider, registers as IResourceChangeListener to detect changes in workspace.
     */
    public ArtefactContentProvider() {
        ResourcesPlugin.getWorkspace().addResourceChangeListener(this, IResourceChangeEvent.POST_CHANGE);

    }

    public void dispose() {

        ResourcesPlugin.getWorkspace().removeResourceChangeListener(this);
    }

    public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
        this.viewer = viewer;
    }

    public Object[] getElements(Object inputElement) {
        System.out.println("getElements: " + inputElement);

        return getChildren(inputElement);
    }

    public Object[] getChildren(Object parentElement) {

        if ((parentElement instanceof IProject)) {
            initializeParents((IProject) parentElement);

            IPath projectPath = ((IProject) parentElement).getLocation();
            File file = new File(projectPath.toString() + "/StaticOntology.owl");
            if (file.exists())
                initializeStatic((IProject) parentElement);

            file = new File(projectPath.toString() + "/DynamicOntology.owl");
            if (file.exists())
                initializeDynamic((IProject) parentElement);

            initializeLinked((IProject) parentElement);
            
            return rootGroup;
        }

        if (parentElement instanceof IArtefact)
            return ((IArtefact) parentElement).getChildren();

        if ((parentElement instanceof IArtefactGroup))
            return ((IArtefactGroup) parentElement).getChildren();

        return EMPTY;
    }

    public Object getParent(Object element) {
        System.out.println("getParent: " + element);

        if ((element instanceof IArtefact))
            return ((IArtefact) element).getParent();

        return EMPTY;
    }

    public boolean hasChildren(Object element) {

        if ((element instanceof IFolder) && findParent(element) != null)
            return true;

        if (element instanceof IArtefactGroup && ((IArtefactGroup) element).getChildren() != null
                && ((IArtefactGroup) element).getChildren().length != 0)
            return true;

        return false;
    }

    private Object findParent(Object element) {
        if (parents == null)
            return null;

        Object[] arrayOfObject;
        int j = (arrayOfObject = parents).length;

        for (int i = 0; i < j; i++) {
            Object p = arrayOfObject[i];
            if (element == p)
                return p;
        }
        return null;
    }

    // initializes the root and groups of artifacts
    private void initializeParents(IProject parent) {
        parents = new Object[groupNames.length];
        int ix = 0;
        String[] arrayOfString;
        int j = (arrayOfString = groupNames).length;

        for (int i = 0; i < j; i++) {
            String name = arrayOfString[i];
            ArtefactGroup artGroup = new ArtefactGroup(name, name, parent);

            parents[(ix++)] = artGroup;
        }
        rootGroup[0] = new ArtefactGroup("Design", "Root", parent, parents); // the children of root are the parents of artefacts
    }

    // initializes artifacts found in static ontology
    private void initializeStatic(IProject parent) {
        Set<BaseArtefact> Requirements = new HashSet<BaseArtefact>();
        ArrayList<BaseArtefact> Objects = new ArrayList<BaseArtefact>();
        StaticOntologyAPI staticOntology = new StaticOntologyAPI(parent);

        for (String object : staticOntology.getObjects()) {
            ArrayList<String> actions = staticOntology.getActionsOfObject(object);
            ArrayList<String> properties = staticOntology.getPropertiesOfObject(object);

            IArtefact art = new ObjectArtefact(object, "Object", (ArtefactGroup) parents[1], actions, properties);
            Objects.add((BaseArtefact) art);

            for (String requirement : staticOntology.getRequirementsOfConcept(object)) {
                String text = staticOntology.getTextOfRequirement(requirement);
                IArtefact art2 = new RequirementArtefact(text, "Requirement", (ArtefactGroup) parents[0], object, requirement);
                Requirements.add((BaseArtefact) art2);
            }
        }

        BaseArtefact[] requirements = Requirements.stream().toArray(BaseArtefact[]::new);
        BaseArtefact[] objects = Objects.stream().toArray(BaseArtefact[]::new);

        ((ArtefactGroup) parents[0]).setChildren(requirements);
        ((ArtefactGroup) parents[1]).setChildren(objects);
    }

    // initializes artifacts found in dynamic ontology
    private void initializeDynamic(IProject parent) {
        ArrayList<BaseArtefact> Activities = new ArrayList<BaseArtefact>();
        ArrayList<BaseArtefact> Transitions = new ArrayList<BaseArtefact>();

        DynamicOntologyAPI dynamicOntology = new DynamicOntologyAPI(parent);

        for (String activity : dynamicOntology.getActivities()) {
            String action = dynamicOntology.getActionOfActivity(activity);
            String kind = dynamicOntology.getActivityTypeOfActivity(activity);
            ArrayList<String> properties = dynamicOntology.getPropertiesOfActivity(activity);
                        
            IArtefact art = new ActivityArtefact(action, "Activity", (ArtefactGroup) parents[2], kind, properties);
            Activities.add((BaseArtefact) art);
        }

        for (String transition : dynamicOntology.getTransitions()) {
            String condition = dynamicOntology.getConditionOfTransition(transition);
            String sourceActivity = dynamicOntology.getSourceActivityOfTransition(transition);
            String targetActivity = dynamicOntology.getTargetActivityOfTransition(transition);

            IArtefact art = new TransitionArtefact(transition, "Transition", (ArtefactGroup) parents[3], condition, sourceActivity, targetActivity);
            Transitions.add((BaseArtefact) art);
        }
   
        BaseArtefact[] activities = Activities.stream().toArray(BaseArtefact[]::new);
        BaseArtefact[] transitions = Transitions.stream().toArray(BaseArtefact[]::new);

        ((ArtefactGroup) parents[2]).setChildren(activities);
        ((ArtefactGroup) parents[3]).setChildren(transitions);
    }

    private void initializeLinked(IProject parent) {
     /*   ArrayList<BaseArtefact> Objects = new ArrayList<BaseArtefact>();
        LinkedOntologyAPI linkedOntology = new LinkedOntologyAPI(parent);
        
        for(String resource : linkedOntology.getResources()) {
            ArrayList<String> actions = linkedOntology.getActivitiesOfResource(resource);
            ArrayList<String> properties = linkedOntology.getPropertiesOfResource(resource);

            IArtefact art = new ObjectArtefact(resource, "Object", (ArtefactGroup) parents[1], actions, properties);
            Objects.add((BaseArtefact) art);
        }
        
        BaseArtefact[] objects = Objects.stream().toArray(BaseArtefact[]::new);
        ((ArtefactGroup) parents[3]).setChildren(objects);
        */
    }

    @Override
    public void resourceChanged(IResourceChangeEvent event) {
        Display.getDefault().asyncExec(new Runnable() {
            public void run() {
                viewer.refresh();
            }
        });
    }
}