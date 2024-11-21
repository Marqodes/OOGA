import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JPanel;

public class PlayheadPanel extends JPanel
{
    private int playHeadX = 0;

    public PlayheadPanel()
    {
        setOpaque(false);
        setPreferredSize(new Dimension(getWidth(), getHeight() - 50));
    }

    public void updatePlayheadPosition(float progress)
    {
        playHeadX = (int) (progress * getWidth());
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        g.setColor(Color.green);
        g.fillRect(playHeadX, 0, 5, getHeight());
    }
    
}
