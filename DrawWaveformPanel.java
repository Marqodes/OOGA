import javax.swing.JPanel;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

public class DrawWaveformPanel extends JPanel
{
    private int numberOfChannels;
    private float[] monoSamples;
    private float[][] stereoSamples;

    public DrawWaveformPanel(float[] monoSamples, int numberOfChannels)
    {
        this.monoSamples = monoSamples;
        this.numberOfChannels = numberOfChannels;
        setBackground(new Color(127, 117, 121));
        setPreferredSize(new Dimension(getWidth(), getHeight() - 50));
    }
    
    public DrawWaveformPanel(float[][] stereoSamples, int numberOfChannels)
    {
        this.stereoSamples = stereoSamples;
        this.numberOfChannels = numberOfChannels;
        setBackground(new Color(127, 117, 121));
        setPreferredSize(new Dimension(getWidth(), getHeight() - 50));
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

        if(numberOfChannels == 1)
        {
            drawMonoWaveForm(g2, monoSamples);
        }
        else
        {
            drawStereoWaveForm(g2, stereoSamples);
        }
    }

    private void drawMonoWaveForm(Graphics2D g2, float[] singleChannelSamples)
    {
        int panelWidth = getWidth();
        int panelHeight = getHeight();
        int centerY = panelHeight / 2;

        float maxAmplitude = 1.0f;
        
        int scaledHeight = (int) (panelHeight / 2.0f * maxAmplitude);
        for(int i = 0; i < singleChannelSamples.length -1; i++)
        {
            int x1 = (int) ((i / (float) singleChannelSamples.length) * panelWidth);
            int y1 = centerY - (int) (singleChannelSamples[i] * scaledHeight);
            int x2 = (int) (((i + 1) / (float) singleChannelSamples.length) * panelWidth);
            int y2 = centerY - (int) (singleChannelSamples[i + 1] * scaledHeight);

            // draw line between two points
            g2.drawLine(x1, y1, x2, y2);
        }
    }

    private void drawStereoWaveForm(Graphics2D g2, float[][] dualChannelSamples)
    {
        int panelWidth = getWidth();
        int panelHeight = getHeight();
        int halfHeight = panelHeight / 2;

        // Scaling factors for vertical amplitude (centered at y midpoint of each half)
        int leftChannelYCenter = halfHeight / 2;
        int rightChannelYCenter = halfHeight + halfHeight / 2;
        float xScale = (float) panelWidth / dualChannelSamples[0].length;

        // Draw Left Channel
        for (int i = 0; i < dualChannelSamples[0].length - 1; i++) 
        {
            int x1 = (int) (i * xScale);
            int x2 = (int) ((i + 1) * xScale);

            int y1 = leftChannelYCenter - (int) (dualChannelSamples[0][i] * (halfHeight / 2));
            int y2 = leftChannelYCenter - (int) (dualChannelSamples[0][i + 1] * (halfHeight / 2));

            g2.drawLine(x1, y1, x2, y2);
        }

        // Draw Right Channel
        for (int i = 0; i < dualChannelSamples[1].length - 1; i++) 
        {
            int x1 = (int) (i * xScale);
            int x2 = (int) ((i + 1) * xScale);

            int y1 = rightChannelYCenter - (int) (dualChannelSamples[1][i] * (halfHeight / 2));
            int y2 = rightChannelYCenter - (int) (dualChannelSamples[1][i + 1] * (halfHeight / 2));

            g2.drawLine(x1, y1, x2, y2);
        }
    }
    
}
