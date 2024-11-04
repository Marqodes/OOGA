import javax.swing.JPanel;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

public class TimeTicksPanel extends JPanel
{
    private int tick_count = 60;
    private int tick_spacing = 10;

    public TimeTicksPanel()
    {
        setOpaque(false);
        // repaint();
    }

    @Override
    protected void paintComponent(Graphics g)
    {
        int width = getWidth();

        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        for(int i = 0; i < width; i+=10)
        {
            g2.setColor(Color.RED);
            g2.drawLine(i, 0, i, getHeight() - 10);
        }
    }
}
