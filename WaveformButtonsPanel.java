import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JButton;
import javax.swing.JPanel;

public class WaveformButtonsPanel extends JPanel 
{

    private PlayAudio playAudio;

    public WaveformButtonsPanel(PlayAudio playAudio)
    {
        this.playAudio = playAudio;
        setBackground(Color.GRAY);
        setPreferredSize(new Dimension(50, getHeight()));

        JButton playButton = new JButton("Play");
        playButton.addActionListener(e -> playButtonAction());

        JButton stopButton = new JButton("Stop");
        stopButton.addActionListener(e -> stopButtonAction());

        add(playButton);
        add(stopButton);
    }

    private void playButtonAction()
    {
        playAudio.startAudioClip();
        System.out.println("Play Button");
    }

    private void stopButtonAction()
    {
        playAudio.stopAudioClip();
        System.out.println("Stop Button");
    }

    
}
