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

     if ((element instanceof IArtefact)) 
       return ResourceManager.getPluginImage("eu.scasefp7.eclipse.core.ui", "icons/artefact-text.png");
     
     if ((element instanceof IArtefactGroup)) 
         return ResourceManager.getPluginImage("eu.scasefp7.eclipse.core.ui", "icons/artefact.png");
       
     
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