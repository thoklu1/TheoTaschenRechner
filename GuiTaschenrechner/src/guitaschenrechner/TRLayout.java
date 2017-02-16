package guitaschenrechner;
import java.awt.*;

public class TRLayout implements LayoutManager
{
    public void layoutContainer(Container c)
    {
        Component[] comps = c.getComponents();
        if(comps.length >= 2){
            comps[0].setBounds(0, 0, c.getWidth(), 30);
            comps[1].setBounds(0, 30, c.getWidth(), 30);
            
            for(int i=2; i<comps.length; i++){
                int x = ((i-2)%4)*c.getWidth()/4;
                int y = (i-2)/4*30 + 60;
                int w = c.getWidth()/4;
                int h = 30;
                comps[i].setBounds(x,y,w,h);
            }
        }
    }
    
    public Dimension minimumLayoutSize(Container c)
    {
        return new Dimension(100,30);
    }
    
    public Dimension preferredLayoutSize(Container c)
    {
        return new Dimension(240,(c.getComponents().length-1)/4*30+60);
    }
    
    public void removeLayoutComponent(Component c)
    {
    }
    
    public void addLayoutComponent(String name, Component c)
    {
    }
}