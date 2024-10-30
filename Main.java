import java.io.File;
import java.io.IOException;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.JFrame;

public class Main
{
    public static int WIDTH  = 850;
    public static int HEIGHT = 450;
    public static void main(String[] args) throws UnsupportedAudioFileException, IOException, LineUnavailableException
    {
        File audioFile = loadFile("media\\AudioClip.wav");
        // File audioFile = loadFile("media\\DarkFant.wav");

        AudioDataExtractor audioDataExtractor = new AudioDataExtractor(audioFile);
        DrawWaveformPanel drawWaveformPanel = new DrawWaveformPanel(audioDataExtractor.getSamples());

        JFrame frame = new JFrame("OOGA");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(WIDTH, HEIGHT);
        frame.add(drawWaveformPanel);
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