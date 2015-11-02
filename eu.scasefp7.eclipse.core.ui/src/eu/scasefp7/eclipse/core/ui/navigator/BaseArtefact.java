package eu.scasefp7.eclipse.core.ui.navigator;


public class BaseArtefact implements IArtefact
{
  protected String name;
  protected ArtefactGroup parent;
   
  public BaseArtefact(String name, ArtefactGroup parent)
  {
    this.name = name;
    this.parent = parent;
  }
   
   public String getName()
   {
     return name;
   }
   
   public ArtefactGroup getParent()
   {
     return parent;
   }
   
   public Object[] getChildren()
   {
     return null;
   }
   
   public String toString()
   {
     return "BaseArtefact [name=" + name + ", parent=" + parent + "]";
   }
}
