import java.awt.BorderLayout;
import java.io.File;
import java.io.IOException;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.OverlayLayout;

public class Main
{
    public static int WIDTH  = 850;
    public static int HEIGHT = 450;
    public static DrawWaveformPanel drawWaveformPanel;
    public static DrawWaveformPanel3 drawWaveformPanel3;
    public static void main(String[] args) throws UnsupportedAudioFileException, IOException, LineUnavailableException
    {
        File audioFile = loadFile("media\\Audio2.wav");
        // File audioFile = loadFile("media\\JCole.wav");
        // File audioFile = loadFile("media\\DarkFant.wav");
        // File audioFile = loadFile("media\\LeftRight.wav");

        AudioDataExtractor audioDataExtractor = new AudioDataExtractor(audioFile);
        TimePanel timePanel = new TimePanel((int) audioDataExtractor.getSampleRate());

        if(audioDataExtractor.getChannels() == 1)
        {
            drawWaveformPanel3 = new DrawWaveformPanel3(audioDataExtractor.getMonoSamples(), audioDataExtractor.getChannels(), timePanel);
        }
        else if(audioDataExtractor.getChannels() == 2)
        {
            drawWaveformPanel3 = new DrawWaveformPanel3(audioDataExtractor.getStereoSamples(), audioDataExtractor.getChannels(), timePanel);
        }
        
        JPanel waveformContainer = new JPanel();
        waveformContainer.setLayout(new OverlayLayout(waveformContainer));
        
        waveformContainer.add(drawWaveformPanel3);

        JFrame frame = new JFrame("OOGA");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(WIDTH, HEIGHT);
        
        frame.add(timePanel, BorderLayout.NORTH);
        frame.add(waveformContainer, BorderLayout.CENTER);
        // frame.pack();
        frame.setVisible(true);
    }

    private static File loadFile(String filePath)
    {
        File audioFile = new File(filePath);

        // Check if file exists
        if(!audioFile.exists() || !audioFile.isFile())
        {
            throw new IllegalArgumentException("Invalid file path: The file does not exist or is not a file...");
        }

        // Check if file is a .wav
        if(!filePath.toLowerCase().endsWith(".wav"))
        {
            throw new IllegalArgumentException("Invalid file type: The file must be .wav...");
        }
        
        return audioFile;
    }
}