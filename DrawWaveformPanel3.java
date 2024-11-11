import javax.swing.JPanel;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

public class DrawWaveformPanel3 extends JPanel
{
    private float[] monoSamples;
    private float[][] stereoSamples;
    private int numberOfChannels;
    private int startIndex = 0;
    private int visibleSampleCount;
    private TimePanel timePanel;
    private final int LEFT_CHANNEL = 0;
    private final int RIGHT_CHANNEL = 1;

    public DrawWaveformPanel3(float[] monoSamples, int numberOfChannels, TimePanel timePanel)
    {
        this.timePanel = timePanel;
        this.monoSamples = monoSamples;
        this.numberOfChannels = numberOfChannels;
        this.visibleSampleCount = (monoSamples != null && monoSamples.length > 0) ? monoSamples.length : 0; // Initially, show all samples if available

        setBackground(new Color(127, 117, 121));
        setPreferredSize(new Dimension(getWidth(), getHeight() - 50)); // Set a default dimension to avoid NullPointerException

        addMouseWheelListener(e -> 
        {
            int cursorX = e.getX();
            double wheelRotation = e.getPreciseWheelRotation();
            handleZoom(cursorX, wheelRotation);
            repaint();
            printMouseInfo(cursorX, wheelRotation);
        });
    }

    public DrawWaveformPanel3(float[][] stereoSamples, int numberOfChannels, TimePanel timePanel)
    {
        this.timePanel = timePanel;
        this.stereoSamples = stereoSamples;
        this.numberOfChannels = numberOfChannels;

        this.visibleSampleCount = (stereoSamples[0] != null && stereoSamples[LEFT_CHANNEL].length > 0) ? stereoSamples[LEFT_CHANNEL].length : 0;

        setBackground(new Color(127, 117, 121));
        setPreferredSize(new Dimension(getWidth(), getHeight() - 50)); 

        addMouseWheelListener(e -> 
        {
            int cursorX = e.getX();
            double wheelRotation = e.getPreciseWheelRotation();
            handleZoom(cursorX, wheelRotation);
            repaint();
            printMouseInfo(cursorX, wheelRotation);
        });
    }

    private void handleZoom(int cursorX, double wheelRotation)
    {
        double zoomFactor = 1.25;
        int panelWidth = getWidth();
        double cursorPositionRatio = (double) cursorX / panelWidth;

        // Determine the sample index currently under the cursor
        int cursorSampleIndex = startIndex + (int) (cursorPositionRatio * visibleSampleCount);

        if(numberOfChannels == 1)
        {
            // Update zoom factor based on wheel rotation
            if (wheelRotation < 0) // Zoom in
            {
                visibleSampleCount = (int) (visibleSampleCount / zoomFactor);
            } 
            else if (wheelRotation > 0) // Zoom out
            {
                visibleSampleCount = (int) (visibleSampleCount * zoomFactor);
            }
    
            // Ensure visibleSampleCount stays within valid bounds
            if (visibleSampleCount < 100) {
                visibleSampleCount = 100;
            } else if (monoSamples != null && monoSamples.length > 0 && visibleSampleCount > monoSamples.length) {
                visibleSampleCount = monoSamples.length;
            }
    
            // Recalculate start index to keep the sample under the cursor fixed
            startIndex = cursorSampleIndex - (int) (cursorPositionRatio * visibleSampleCount);
    
            // Ensure startIndex stays within valid bounds
            if (startIndex < 0) {
                startIndex = 0;
            } else if (monoSamples != null && monoSamples.length > 0 && startIndex > monoSamples.length - visibleSampleCount) {
                startIndex = monoSamples.length - visibleSampleCount;
            }
        }
        else
        {
            // Update zoom factor based on wheel rotation
            if (wheelRotation < 0) // Zoom in
            {
                visibleSampleCount = (int) (visibleSampleCount / zoomFactor);
            } 
            else if (wheelRotation > 0) // Zoom out
            {
                visibleSampleCount = (int) (visibleSampleCount * zoomFactor);
            }
    
            // Ensure visibleSampleCount stays within valid bounds
            if (visibleSampleCount < 100) {
                visibleSampleCount = 100;
            } else if (stereoSamples[LEFT_CHANNEL] != null && stereoSamples[LEFT_CHANNEL].length > 0 && visibleSampleCount > stereoSamples[LEFT_CHANNEL].length) {
                visibleSampleCount = stereoSamples[LEFT_CHANNEL].length;
            }
    
            // Recalculate start index to keep the sample under the cursor fixed
            startIndex = cursorSampleIndex - (int) (cursorPositionRatio * visibleSampleCount);
    
            // Ensure startIndex stays within valid bounds
            if (startIndex < 0) {
                startIndex = 0;
            } else if (stereoSamples[LEFT_CHANNEL] != null && stereoSamples[LEFT_CHANNEL].length > 0 && startIndex > stereoSamples[LEFT_CHANNEL].length - visibleSampleCount) {
                startIndex = stereoSamples[LEFT_CHANNEL].length - visibleSampleCount;
            }
        }
    }


    private void drawStereoWaveForm(Graphics2D g2) 
    {
        int panelWidth = getWidth();
        int panelHeight = getHeight();

        // Calculate separate center Y positions for left and right channels
        int leftChannelYCenter = panelHeight / 4; // Top half center
        int rightChannelYCenter = 3 * panelHeight / 4; // Bottom half center

        // Debugging print statements
        System.out.println("Left Channel Center Y: " + leftChannelYCenter);
        System.out.println("Right Channel Center Y: " + rightChannelYCenter);
        System.out.println("Panel Height: " + panelHeight);

        // Draw separation line
        g2.setColor(Color.RED);
        g2.drawLine(0, panelHeight / 2, panelWidth, panelHeight / 2); // Middle divider line
        g2.setColor(Color.BLACK);

        if (stereoSamples[LEFT_CHANNEL] != null && stereoSamples[LEFT_CHANNEL].length > 0) 
        {
            for (int i = 0; i < panelWidth; i++) 
            {
                // Calculate sample indices for the left channel
                int startSampleIndex = startIndex + (int) ((double) i / panelWidth * visibleSampleCount);
                int endSampleIndex = startIndex + (int) ((double) (i + 1) / panelWidth * visibleSampleCount);

                if (endSampleIndex >= stereoSamples[LEFT_CHANNEL].length) 
                {
                    endSampleIndex = stereoSamples[LEFT_CHANNEL].length - 1;
                }

                // Find min and max values in this range
                float minValue = Float.MAX_VALUE;
                float maxValue = -Float.MAX_VALUE;
                for (int j = startSampleIndex; j <= endSampleIndex; j++) 
                {
                    float sampleValue = stereoSamples[LEFT_CHANNEL][j];
                    if (sampleValue < minValue) minValue = sampleValue;
                    if (sampleValue > maxValue) maxValue = sampleValue;
                }

                // Scale min and max values to fit the panel height for left channel
                int yMin = leftChannelYCenter - (int) (minValue * leftChannelYCenter);
                int yMax = leftChannelYCenter - (int) (maxValue * leftChannelYCenter);

                // Draw a line representing the min and max values for this pixel
                g2.drawLine(i, yMin, i, yMax);
            }

            for (int i = 0; i < panelWidth; i++) {
                // Calculate sample indices for the right channel
                int startSampleIndex = startIndex + (int) ((double) i / panelWidth * visibleSampleCount);
                int endSampleIndex = startIndex + (int) ((double) (i + 1) / panelWidth * visibleSampleCount);

                if (endSampleIndex >= stereoSamples[RIGHT_CHANNEL].length) 
                {
                    endSampleIndex = stereoSamples[RIGHT_CHANNEL].length - 1;
                }

                // Find min and max values in this range
                float minValue = Float.MAX_VALUE;
                float maxValue = -Float.MAX_VALUE;
                for (int j = startSampleIndex; j <= endSampleIndex; j++) 
                {
                    float sampleValue = stereoSamples[RIGHT_CHANNEL][j];
                    if (sampleValue < minValue) minValue = sampleValue;
                    if (sampleValue > maxValue) maxValue = sampleValue;
                }

                // Scale min and max values to fit the panel height for right channel
                int yMin = rightChannelYCenter - (int) (minValue * panelHeight / 4);
                int yMax = rightChannelYCenter - (int) (maxValue * panelHeight / 4);

                // Draw a line representing the min and max values for this pixel
                g2.drawLine(i, yMin, i, yMax);
            }
        }
    }

    private void drawMonoWaveForm(Graphics2D g2)
    {
        if (monoSamples != null && monoSamples.length > 0)
        {
            int panelWidth = getWidth();
            int panelHeight = getHeight();
            int centerY = panelHeight / 2;
            float[] currentSamples = monoSamples;

            // Draw waveform for visible samples
            for (int i = 0; i < panelWidth; i++)
            {
                // Calculate the range of samples that correspond to this pixel
                int startSampleIndex = startIndex + (int) ((double) i / panelWidth * visibleSampleCount);
                int endSampleIndex = startIndex + (int) ((double) (i + 1) / panelWidth * visibleSampleCount);
                if (endSampleIndex >= currentSamples.length) endSampleIndex = currentSamples.length - 1;

                // Find min and max values in this range
                float minValue = Integer.MAX_VALUE;
                float maxValue = Integer.MIN_VALUE;
                for (int j = startSampleIndex; j <= endSampleIndex; j++)
                {
                    float sampleValue = currentSamples[j];
                    if (sampleValue < minValue) minValue = sampleValue;
                    if (sampleValue > maxValue) maxValue = sampleValue;
                }

                // Scale min and max values to fit the panel height
                int yMin = (int) ((1.0 - (minValue )) * centerY);
                int yMax = (int) ((1.0 - (maxValue )) * centerY);

                // Draw a line representing the min and max values for this pixel
                g2.drawLine(i, yMin, i, yMax);
            }
        }
    }
    
    @Override
    protected void paintComponent(Graphics g)
    {
        timePanel.updateParameters(startIndex, visibleSampleCount);
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        if(numberOfChannels == 1)
        {

            drawMonoWaveForm(g2d);
        }
        else
        {
            drawStereoWaveForm(g2d);
        }
    }

    private void printMouseInfo(int cursorX, double wheelRotation)
    {
        System.out.println("Cursor X: " + cursorX + ", Wheel Rotation: " + wheelRotation);
    }
}