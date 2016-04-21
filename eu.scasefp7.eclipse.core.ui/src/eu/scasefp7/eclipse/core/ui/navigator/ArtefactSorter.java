package eu.scasefp7.eclipse.core.ui.navigator;

import org.eclipse.core.resources.IFolder;
import org.eclipse.jface.viewers.ViewerSorter;

/**
 * Sorts the artifacts by category order ( "Requirements", "Objects", "Activities", "Transitions" ).
 * 
 * @author Leonora Gaspar
 * @author Marin Orlic
 *
 */
public class ArtefactSorter extends ViewerSorter
{
   public int category(Object element)
   {
     if ((element instanceof IFolder)) {
       String name = ((IFolder)element).getName();
       String[] folders = { "Requirements", "Objects", "Activities", "Transitions"};
       
       for (int ix = 0; ix < folders.length; ix++) {
         if (folders[ix].equals(name))
           return ix;
       }
       return 0;
     }
     
     return super.category(element);
   }
}
