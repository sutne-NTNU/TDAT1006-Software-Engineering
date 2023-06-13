package client.controller;

import client.App;
import java.net.URL;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;

public class MusicController
{
    private static Clip clip;

    public static String intro = "intro.wav";
    public static String grieg = "grieg-action-combo.wav";
    public static String metal = "metal.wav";

    /**
     * Volume is a value between 0 and 100 (%).
     */
    public static int volume = 20;

    public static void start(String name)
    {
        if (clip != null) stop();
        URL url = App.getURL("resources/music/" + name);
        try (AudioInputStream stream = AudioSystem.getAudioInputStream(url))
        {
            clip = AudioSystem.getClip();
            clip.open(stream);
            clip.loop(Clip.LOOP_CONTINUOUSLY);
            FloatControl gainControl = (FloatControl)clip.getControl(FloatControl.Type.MASTER_GAIN);
            gainControl.setValue(20f * (float)Math.log10((float)volume / 100.0f));
            clip.start();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public static void stop()
    {
        clip.stop();
        clip.close();
        clip = null;
    }
}
