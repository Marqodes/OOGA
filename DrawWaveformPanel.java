import javax.swing.JPanel;

import java.awt.Graphics;
import java.awt.Graphics2D;

public class DrawWaveformPanel extends JPanel
{
    private float[] samples;
    private int numberOfChannels;
    private float[] leftChannel;
    private float[] rightChannel;

    public DrawWaveformPanel(float[] samples, int numberOfChannels)
    {
        this.samples = samples;
        this.numberOfChannels = numberOfChannels;
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
        
        if(numberOfChannels == 1)
        {
            int scaledHeight = (int) (panelHeight / 2.0f * maxAmplitude);
            for(int i = 0; i < samples.length -1; i++)
            {
                int x1 = (int) ((i / (float) samples.length) * panelWidth);
                int y1 = centerY - (int) (samples[i] * scaledHeight);
                int x2 = (int) (((i + 1) / (float) samples.length) * panelWidth);
                int y2 = centerY - (int) (samples[i + 1] * scaledHeight);
    
                // draw line between two points
                g2.drawLine(x1, y1, x2, y2);
            }
        }
        else
        {
            spiltStereoSamples();
            drawStereoWaveForm(g2, leftChannel, 0, centerY);
            drawStereoWaveForm(g2, rightChannel, centerY + 1, panelHeight);
        }
    }

    private void spiltStereoSamples()
    { 
        leftChannel = new float[samples.length / 2];
        rightChannel = new float[samples.length / 2];
        for(int i = 0, j = 0; i < samples.length; i += 2, j++)
        {
            leftChannel[j] = samples[i];
            rightChannel[j] = samples[i + 1];
        }
    }

    private void drawStereoWaveForm(Graphics2D g2, float[] channelSamples, int yStart, int yEnd)
    {

        int panelWidth = getWidth();
        int midY = (yStart + yEnd) / 2;
        int numberOfSamples = channelSamples.length;
        float xScale = (float) panelWidth / numberOfSamples;

        for(int i = 0; i < numberOfSamples - 1; i++)
        {
            int x1 = Math.round(i * xScale);
            int x2 = Math.round((i + 1) * xScale);

            int y1 = midY - Math.round(channelSamples[i] * (yEnd - yStart) / 2);
            int y2 = midY - Math.round(channelSamples[i + 1] * (yEnd - yStart) / 2);

            g2.drawLine(x1, y1, x2, y2);
        }
    }
    
}
