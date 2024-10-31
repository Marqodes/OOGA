import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class AudioDataExtractor 
{
    private float[] samples;
    private int bytesPerFrame;
    private int sampleSizeInBits;
    private int numberOfChannels;


    AudioInputStream audioInputStream;
    AudioFormat audioFormat;

    public AudioDataExtractor(File filePath) throws UnsupportedAudioFileException, IOException, LineUnavailableException
    {
        audioInputStream = AudioSystem.getAudioInputStream(filePath);
        audioFormat = audioInputStream.getFormat();
        bytesPerFrame = audioFormat.getFrameSize();
        sampleSizeInBits = audioFormat.getSampleSizeInBits();
        numberOfChannels = audioFormat.getChannels();

        extractSamples();
    }

    public float[] getSamples()
    {
        return samples;
    }

    public int getChannels()
    {
        return numberOfChannels;
    }

    private void extractSamples() throws IOException, UnsupportedAudioFileException
    {
        byte[] audioBytes = audioInputStream.readAllBytes();
        int numberOfSamples = audioBytes.length / bytesPerFrame;

        samples = new float[numberOfSamples];

        float normalizationFactor;
        if(sampleSizeInBits == 16)
        {
            normalizationFactor = 32768.0f; // For 16-bit signed PCM
        }
        else if(sampleSizeInBits == 8)
        {
            normalizationFactor = 128.0f; // For 8-bit unsigned PCM
        }
        else
        {
            throw new UnsupportedAudioFileException("Unsupported sample size: " + sampleSizeInBits);
        }

        for(int i = 0; i < numberOfSamples; i++)
        {
            int currentSample = 0;

            if(sampleSizeInBits == 16)
            {
                // Read 16-bit sample
                currentSample = (audioBytes[2 * i + 1] << 8) | (audioBytes[2 * i] & 0xFF);
            }
            else if(sampleSizeInBits == 8)
            {
                // Read 8-bit sample
                currentSample = audioBytes[i] - 128; // Convert to range -128 to 127
            }

            // Normalize the sample
            samples[i] = currentSample / normalizationFactor;
        }
    }
    
}
