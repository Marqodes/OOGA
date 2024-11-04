import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

public class TimePanel extends JPanel
{
    private float audioLengthSeconds;
    public TimePanel(float audioLengthSeconds)
    {
        this.audioLengthSeconds = audioLengthSeconds;
        setBackground(Color.gray);
        setPreferredSize(new Dimension(getWidth(), 70));
    }


    @Override
    protected void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        int width = getWidth();
        int timeDiv = (int) (width / audioLengthSeconds);

        Graphics2D g2 = (Graphics2D) g;

        for(int i = timeDiv; i < width; i+=timeDiv)
        {
            g2.setColor(Color.RED);
            g2.drawLine(i, 0, i, 50);

            int seconds = i / timeDiv;
            String timeText = seconds + "s";

            g2.setColor(Color.BLACK);
            g2.drawString(timeText, i - g2.getFontMetrics().stringWidth(timeText) / 2, 65);
        }
    }
}
