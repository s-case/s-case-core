package eu.scasefp7.eclipse.core.ui.navigator;

import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.wb.swt.ResourceManager;

public class ArtefactLabelProvider implements ILabelProvider {
   public void addListener(ILabelProviderListener listener) {}
   
   public void dispose() {}
   
   public boolean isLabelProperty(Object element, String property) {
     return false;
   }

 
   public void removeListener(ILabelProviderListener listener) {}
   

   public Image getImage(Object element) {
	 String type = "";
	 
     if (element instanceof IArtefact) 
    	 type = ((IArtefact)element).getType();
     if (element instanceof IArtefactGroup) 
    	 type = ((IArtefactGroup)element).getType();
     
     if(type.startsWith("Activit"))
    		 return ResourceManager.getPluginImage("eu.scasefp7.eclipse.core.ui", "icons/navigatorAction.png");
     if(type.startsWith("Requirement"))
    		 return ResourceManager.getPluginImage("eu.scasefp7.eclipse.core.ui", "icons/navigatorRequirement.png");
     if(type.startsWith("Object"))
    		 return ResourceManager.getPluginImage("eu.scasefp7.eclipse.core.ui", "icons/navigatorObject.png");
     if(type.startsWith("Transition"))
    		 return ResourceManager.getPluginImage("eu.scasefp7.eclipse.core.ui", "icons/navigatorTransition.png");
     if(type.startsWith("Root"))
    		 return ResourceManager.getPluginImage("eu.scasefp7.eclipse.core.ui", "icons/navigatorRoot.png");
     if (element instanceof IArtefact)
    		 return ResourceManager.getPluginImage("eu.scasefp7.eclipse.core.ui", "icons/artefact-text.png");

     return null;
   }
   
 

   public String getText(Object element) {
     System.out.println("Label for: " + element);
     
     if ((element instanceof IArtefact)) 
       return ((IArtefact)element).getName();
     
     if ((element instanceof IArtefactGroup)) 
         return ((IArtefactGroup)element).getName();
       
     
     return null;
   }
 }