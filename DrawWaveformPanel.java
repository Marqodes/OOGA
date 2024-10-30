import javax.swing.JPanel;

import java.awt.Graphics;
import java.awt.Graphics2D;

public class DrawWaveformPanel extends JPanel
{
    private float[] samples;

    public DrawWaveformPanel(float[] samples)
    {
        this.samples = samples;
    }

    @Override
    protected void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        drawWaveform(g);
    }

    private void drawWaveform(Graphics g)
    {
        Graphics2D g2 = (Graphics2D) g;

        int panelWidth = getWidth();
        int panelHeight = getHeight();

        int centerY = panelHeight / 2;

        float maxAmplitude = 1.0f;
        int scaledHeight = (int) (panelHeight / 2.0f * maxAmplitude);

        for(int i = 0; i < samples.length -1; i ++)
        {
            int x1 = (int) ((i / (float) samples.length) * panelWidth);
            int y1 = centerY - (int) (samples[i] * scaledHeight);
            int x2 = (int) (((i + 1) / (float) samples.length) * panelWidth);
            int y2 = centerY - (int) (samples[i + 1] * scaledHeight);

            // draw line between two points
            g2.drawLine(x1, y1, x2, y2);
        }
    }
    
}
