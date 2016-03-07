package eu.scasefp7.eclipse.core.ui.navigator;


public class BaseArtefact implements IArtefact
{
  protected String name;
  protected String type;
  protected ArtefactGroup parent;
   
  public BaseArtefact(String name, String type, ArtefactGroup parent)
  {
    this.name = name;
    this.type = type;
    this.parent = parent;
  }
   
   public String getName()
   {
     return name;
   }
   
   public String getType()
   {
     return type;
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
