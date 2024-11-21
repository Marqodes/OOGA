import java.io.File;
import java.io.IOException;


import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import javax.swing.Timer;

public class PlayAudio 
{
    private Clip audioClip;
    private PlayheadPanel playheadPanel;
    private boolean isRunning = true;
    private Timer timer;
    private long clipLength;

    public PlayAudio(File audioFile, PlayheadPanel playheadPanel)
    {
        this.playheadPanel = playheadPanel;

        try
        {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(audioFile);
            audioClip = AudioSystem.getClip();
            audioClip.open(audioInputStream);
            clipLength = audioClip.getMicrosecondLength();
            
        }
        catch(UnsupportedAudioFileException | IOException | LineUnavailableException e)
        {
            e.printStackTrace();
        }
    }

    public void startAudioClip()
    {
        audioClip.start();

        Timer timer = new Timer(10, e -> 
        {
            long currentPosition = audioClip.getMicrosecondPosition();
            float progress = (float) currentPosition / clipLength;
            playheadPanel.updatePlayheadPosition(progress);

        });
        timer.start();
    }
    
    public void stopAudioClip()
    {
        audioClip.stop();
        audioClip.setFramePosition(0);
    }
}

    
