package fx.controllers;

import javax.sound.sampled.*;

public class Backround implements Runnable{
    
	private static volatile Clip clip;
    
    private final int START		= 0;
    private final int GRIEGG 	= 5784817;
    private final int INTENSE 	= 9037148;
    private final int END 	    = 15257056;
//    private final int FANFARE = 15257056;
//    private final int SADFARE = 15462254;

    /** This method sets an audioclip object, and plays the main Menu audio.
     *
     */
    public void run(){
        try{
            ClassLoader classloader = Thread.currentThread().getContextClassLoader();
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(getClass().getResource("/Image/Audiotrack.wav"));
            clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.setLoopPoints(START ,GRIEGG);
            clip.loop(Clip.LOOP_CONTINUOUSLY);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    /** Takes in a boolean whether or not the player is playing and changes the audio accordingly
     * @param inGame is true if the user is playing the game
     */
    public void changeScene(boolean inGame) {
       try {
           if(inGame) {
               clip.stop();
               clip.setFramePosition(GRIEGG);
               clip.setLoopPoints(INTENSE , END);
               clip.loop(	Clip.LOOP_CONTINUOUSLY);
           }else{
               clip.stop();
               clip.setFramePosition(START);
               clip.setLoopPoints(START , GRIEGG);
               clip.loop(Clip.LOOP_CONTINUOUSLY);
           }
       }catch(Exception e){}
    }

    /** this method takes in true if the player has won, and changes the audio accordingly.
     * @param winner is true if the user has won the game
     */
    public void gameEnd(boolean winner) {
        /*try {
            if(winner) {
                clip.stop();
                clip.setFramePosition(FANFARE);
                clip.setLoopPoints(END , SADFARE);
                clip.loop(	Clip.LOOP_CONTINUOUSLY);
            }else{
                clip.stop();
                clip.setFramePosition(SADFARE);
                clip.setLoopPoints(SADFARE,0);
               clip.loop(Clip.LOOP_CONTINUOUSLY);

            }
        }catch(Exception e){}*/
    }

    /** Adjusts the volume
     * @param volume float value determined by the volume slider
     */
    public static void setVolume(float volume){
        try{
            FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            gainControl.setValue(volume);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**Stops and closes the backround audio
     *
     */
    public static void stop(){
        try{
            clip.stop();
            clip.close();
        }catch(Exception e){}
    }
}
