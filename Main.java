import java.io.File;
import java.io.IOException;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class Main
{
    public static void main(String[] args) throws UnsupportedAudioFileException, IOException, LineUnavailableException
    {
        // File audioFile = loadFile("media\\Audio1.wav");
        File audioFile = loadFile("media\\DarkFant.wav");
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