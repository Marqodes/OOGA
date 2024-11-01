import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class AudioDataExtractor 
{
    private int bytesPerFrame;
    private int sampleSizeInBits;
    private int numberOfChannels;

    private float[] monoSamples;
    private float[][] stereoSamples;

    AudioInputStream audioInputStream;
    AudioFormat audioFormat;

    public AudioDataExtractor(File filePath) throws UnsupportedAudioFileException, IOException, LineUnavailableException
    {
        audioInputStream = AudioSystem.getAudioInputStream(filePath);
        audioFormat = audioInputStream.getFormat();
        bytesPerFrame = audioFormat.getFrameSize();
        sampleSizeInBits = audioFormat.getSampleSizeInBits();
        numberOfChannels = audioFormat.getChannels();

        if(getChannels() == 1)
        {
            extractMonoSamples();
        }
        else
        {
            extractStereoSamples();
        }
    }

    public int getChannels()
    {
        return numberOfChannels;
    }

    public float[][] getStereoSamples()
    {
        return stereoSamples;
    }

    public float[] getMonoSamples()
    {
        return monoSamples;
    }

    private void extractMonoSamples() throws IOException, UnsupportedAudioFileException
    {
        byte[] audioBytes = audioInputStream.readAllBytes();
        int numberOfSamples = audioBytes.length / bytesPerFrame;

        monoSamples = new float[numberOfSamples];

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
            monoSamples[i] = currentSample / normalizationFactor;
        }
    }

    // currently only 16-bit audio
    private void extractStereoSamples() throws IOException
    {
        byte[] audioBytes = audioInputStream.readAllBytes();
        int numberOfSamples = audioBytes.length / bytesPerFrame;

        stereoSamples = new float[2][numberOfSamples];

        ByteBuffer buffer = ByteBuffer.wrap(audioBytes).order(ByteOrder.LITTLE_ENDIAN);
        
        for(int i = 0; i < numberOfSamples; i++)
        {
            // left channel
            short leftSample = buffer.getShort();
            stereoSamples[0][i] = leftSample / 32768f; // Normalize to -1.0f to 1.0f

            // Extract right channel sample
            short rightSample = buffer.getShort();
            stereoSamples[1][i] = rightSample / 32768f; // Normalize to -1.0f to 1.0f
        }
    }
    
}
