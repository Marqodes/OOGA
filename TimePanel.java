import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

public class TimePanel extends JPanel
{
    private int startIndex;
    private int visibleSampleCount;
    private int sampleRate = 44100; // Assuming a default sample rate of 44.1 kHz

    public TimePanel(int sampleRate)
    {
        this.sampleRate = sampleRate;
        setBackground(Color.GRAY);
        setPreferredSize(new Dimension(getWidth(), 50)); // Set a default dimension
    }

    public void updateParameters(int startIndex, int visibleSampleCount)
    {
        this.startIndex = startIndex;
        this.visibleSampleCount = visibleSampleCount;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        int panelWidth = getWidth();
        double timePerPixel = (double) visibleSampleCount / sampleRate / panelWidth * 1000; // Time per pixel in milliseconds
        int timeTickSpacing = 1000; // Initial spacing in milliseconds

        // Adjust time tick spacing based on zoom level
        while (timeTickSpacing / timePerPixel < 50) {
            timeTickSpacing *= 2; // Increase the spacing to prevent overcrowding
        }

        // Draw time ticks
        for (int i = 0; i < panelWidth; i += (int) (timeTickSpacing / timePerPixel))
        {
            int timeInMillis = (int) ((startIndex + (i / (double) panelWidth) * visibleSampleCount) / sampleRate * 1000);
            String timeLabel = String.format("%d:%02d.%03d", timeInMillis / 60000, (timeInMillis / 1000) % 60, timeInMillis % 1000);
            g2d.drawLine(i, 0, i, 10); // Draw the tick mark
            g2d.drawString(timeLabel, i + 2, 20); // Draw the time label slightly below the tick mark
        }
    }
}
