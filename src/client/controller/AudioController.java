package client.controller;

import client.App;
import javafx.scene.media.AudioClip;

/**
 * Handles short sound effects that can be played at any time.
 */
public class AudioController
{
    public static AudioClip blunk = loadAudio("blunk.wav");
    public static AudioClip click = loadAudio("click.mp3");
    public static AudioClip mapChanged = loadAudio("map-changed.mp3");
    public static AudioClip error = loadAudio("error.mp3");
    public static AudioClip gunshot = loadAudio("gunshot.wav");
    public static AudioClip newMessage = loadAudio("new-message.wav");

    /**
     * Volume is a value between 0 and 100 (%)
     */
    public static int volume = 20;

    public static void play(AudioClip clip)
    {
        clip.play((double)volume / 100.0d);
    }

    private static AudioClip loadAudio(String name)
    {
        String path = "resources/audio/" + name;
        return new AudioClip(App.getPath(path));
    }
}
