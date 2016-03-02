package eu.scasefp7.eclipse.core.ui.views;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

public class ImageButton extends Composite
{
    private Color   textColor;
    private Image   image;
    private String  text;
    private int width = 0;
    private int height = 0;
	private int imgWidth;
	private int imgHeight;

    public ImageButton(Composite parent, int style)
    {
        super(parent, style);

        textColor = Display.getDefault().getSystemColor(SWT.COLOR_BLACK);

        addListener(SWT.Dispose, new Listener()
        {
            @Override
            public void handleEvent(Event arg0)
            {
                if (image != null)
                    image.dispose();
            }
        });

 
        addListener(SWT.Paint, new Listener()
        {
            @Override
            public void handleEvent(Event e)
            {
                paintControl(e);
            }
        });

        addListener(SWT.MouseDown, new Listener()
        {
            @Override
            public void handleEvent(Event arg0)
            {
                System.out.println("Click");
            }
        });
    }

    private void paintControl(Event event)
    {
        GC gc = event.gc;

        if (image != null) {
            //draw image
			gc.drawImage(image, 1, 1, imgWidth-1, imgHeight-1, 1, 1, width, height);
				
            //draw border
            gc.setForeground(Display.getDefault().getSystemColor(SWT.COLOR_GRAY));
            gc.drawRoundRectangle(1, 1, width, height, 5, 5);
            
            //draw text
            gc.setForeground(textColor);
            Point textSize = gc.textExtent(text);
            gc.drawText(text, (width - textSize.x) / 2 + 1, (height - textSize.y) / 2 + 1, true);
        }
    }

    public void setImage(Image image)
    {
        this.image = new Image(Display.getDefault(), image, SWT.IMAGE_COPY);
        width = image.getBounds().width;
        height = image.getBounds().height;
        imgWidth = width;
        imgHeight = height;
        redraw();
    }

    public void setText(String text)
    {
        this.text = text;
        redraw();
    }

    @Override
    public Point computeSize(int wHint, int hHint, boolean changed)
    {
        int overallWidth = width;
        int overallHeight = height;

        if (wHint != SWT.DEFAULT && wHint < overallWidth)
            overallWidth = wHint;

        if (hHint != SWT.DEFAULT && hHint < overallHeight)
            overallHeight = hHint;

        return new Point(overallWidth + 2, overallHeight + 2);
    }
    @Override
    public void setSize(int w, int h) {
    	width = w;
    	height = h;
    	redraw();
    }
    @Override
    public void setSize(Point size) {
    	setSize(size.x,size.y);
    }
}