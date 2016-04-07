package eu.scasefp7.eclipse.core.ui.views;


import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Display;
import org.mihalis.opal.obutton.AbstractButtonRenderer;
import org.mihalis.opal.utils.SWTGraphicUtil;

public class SCaseButtonRenderer
  extends AbstractButtonRenderer
{
  private static SCaseButtonRenderer instance;
  private static final Color FIRST_BACKGROUND_COLOR = SWTGraphicUtil.getColorSafely(80, 184, 170);
  private static final Color SECOND_BACKGROUND_COLOR = SWTGraphicUtil.getColorSafely(115, 220, 215);
  
  protected Color getFontColor()
  {
    return Display.getDefault().getSystemColor(1);
  }
  
  protected Color getFirstBackgroundColor()
  {
    return FIRST_BACKGROUND_COLOR;
  }
  
  protected Color getSecondBackgroundColor()
  {
    return SECOND_BACKGROUND_COLOR;
  }
  
  public static SCaseButtonRenderer getInstance()
  {
    if (instance == null) {
      instance = new SCaseButtonRenderer();
    }
    return instance;
  }
}

/* Location:           C:\scaseWorkspace\Opal
 * Qualified Name:     org.mihalis.opal.obutton.GreenButtonRenderer
 * Java Class Version: 6 (50.0)
 * JD-Core Version:    0.7.1
 */