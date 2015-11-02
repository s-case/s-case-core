package eu.scasefp7.eclipse.core.ui.navigator;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

import eu.scasefp7.eclipse.core.ontology.DynamicOntologyAPI;
import eu.scasefp7.eclipse.core.ontology.StaticOntologyAPI;


public class ArtefactContentProvider  implements ITreeContentProvider {
   private static final Object[] EMPTY = new Object[0];
   
   private Object[] parents = null;
   ArtefactGroup[] rootGroup = new ArtefactGroup[1];
   
   private String[] group_names = { "Requirements", "Objects", "Activities", "Transitions"}; 

   public void dispose() {}
   
   public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {}

   public Object[] getElements(Object inputElement) {
     System.out.println("getElements: " + inputElement);
     
     return getChildren(inputElement);
   }
  

   public Object[] getChildren(Object parentElement) {
     System.out.println("getChildren: " + parentElement);
     
     if ((parentElement instanceof IProject)) { 
    
             initializeParents((IProject) parentElement);
             initializeArtefacts((IProject) parentElement);

             return rootGroup; 
       }
  
     if ((parentElement instanceof IArtefact))
       return ((IArtefact)parentElement).getChildren();
     
     if ((parentElement instanceof IArtefactGroup))
         return ((IArtefactGroup)parentElement).getChildren();
     
     return EMPTY;
   }
   

  

   public Object getParent(Object element) {
     System.out.print("getParent: " + element);
     
     if ((element instanceof IArtefact)) 
       return ((IArtefact)element).getParent();
     
     return EMPTY;
   }
   

   public boolean hasChildren(Object element) {
     System.out.println("hasChildren: " + element);
     
     
     if ((element instanceof IFolder)) {
    	  //.scase folders will be populated using initializeParent and initializeArtefacts
    	   if (((IFolder)element).getName().endsWith(".scase"))
    		   return true;
	       if (findParent(element) != null) 
	    	   return true;
       return false;
     }
     
     if ((element instanceof IArtefact)) 
       return false;
     
     if (element instanceof IArtefactGroup && ((IArtefactGroup) element).getChildren()!= null) 
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
   

  private void initializeParents(IProject parent) {
     parents = new Object[group_names.length];
     int ix = 0;
     String[] arrayOfString; 
     int j = (arrayOfString = group_names).length; 
     
     for (int i = 0; i < j; i++) { 
    	 String  name   = arrayOfString[i];
    	 ArtefactGroup artGroup = new ArtefactGroup(name, parent);

    	 parents[(ix++)] = artGroup;
     }
     rootGroup[0] = new ArtefactGroup("Root", parent, parents);
   }
  
  
  private void initializeArtefacts(IProject parent) { 
	   IProject project = parent; 
		 
	   Set<BaseArtefact> Requirements      = new HashSet<BaseArtefact>();   
	   ArrayList<BaseArtefact> Objects     = new ArrayList<BaseArtefact>(); 
	   ArrayList<BaseArtefact> Activities  = new ArrayList<BaseArtefact>();
	   ArrayList<BaseArtefact> Transitions = new ArrayList<BaseArtefact>();
	   
	   StaticOntologyAPI  staticOntology  = new StaticOntologyAPI(project);
	   DynamicOntologyAPI dynamicOntology = new DynamicOntologyAPI(project); 
	   
	   for (String object : staticOntology.getObjects()) {
		   ArrayList<String> obActions  = staticOntology.getActionsOfObject(object);
		   ArrayList<String> obProperty = staticOntology.getPropertiesOfObject(object);
		   String resultStr;
		   
		   if(obProperty.isEmpty())
			   resultStr = "Object: " + object + ", actions: " + obActions;
		   else
			   resultStr = "Object: " + object + ", actions: " + obActions + ", properties: " + obProperty;
		   
		   IArtefact art = new BaseArtefact(resultStr, (ArtefactGroup) parents[1]);
		   Objects.add((BaseArtefact) art);
		  
		   
		   for (String requirement : staticOntology.getRequirementsOfConcept(object)) {
			   resultStr = "Requirement: " + requirement + ", concept: " + object;
			   IArtefact art2 = new BaseArtefact(resultStr, (ArtefactGroup) parents[0]);
			   Requirements.add((BaseArtefact) art2);
		   }

	   }
	   
	   for (String activity : dynamicOntology.getActivities()) {
		    String action = dynamicOntology.getActionOfActivity(activity);
		    String acType = dynamicOntology.getActivityTypeOfActivity(activity);
		    ArrayList<String> acProp = dynamicOntology.getPropertiesOfActivity(activity);
		    String resultStr;
		    if(acProp.isEmpty())
		    	resultStr = "Activity: " + action + ", type: " + acType;
		    else
		    	resultStr = "Activity: " + action + ", type: " + acType + ", properties:" + acProp;
		    IArtefact art = new BaseArtefact(resultStr, (ArtefactGroup) parents[2]);
		    Activities.add((BaseArtefact) art);
	   }
	 
	   for (String transition : dynamicOntology.getTransitions()) {

			String condition 		 = dynamicOntology.getConditionOfTransition(transition);		
			String sourcedynactivity = dynamicOntology.getSourceActivityOfTransition(transition);
			String targetdynactivity = dynamicOntology.getTargetActivityOfTransition(transition);
			String resultStr;
			
			if(condition == null)
				 resultStr = "from: " + sourcedynactivity + ", to: " + targetdynactivity;
			else
				 resultStr = "from: " + sourcedynactivity + ", to: " + targetdynactivity +", condition: " + condition;
			
			IArtefact art = new BaseArtefact(resultStr, (ArtefactGroup) parents[3]);
			Transitions.add((BaseArtefact) art);

		}
	   
	   BaseArtefact[] requirements = Requirements.stream().toArray(BaseArtefact[]::new);
	   BaseArtefact[] objects = Objects.stream().toArray(BaseArtefact[]::new);
	   BaseArtefact[] activities = Activities.stream().toArray(BaseArtefact[]::new);
	   BaseArtefact[] transitions = Transitions.stream().toArray(BaseArtefact[]::new);
	   
	   ((ArtefactGroup) parents[0]).setChildren(requirements);
	   ((ArtefactGroup) parents[1]).setChildren(objects);
	   ((ArtefactGroup) parents[2]).setChildren(activities);
	   ((ArtefactGroup) parents[3]).setChildren(transitions);
	   
	   
	  
  }
 }