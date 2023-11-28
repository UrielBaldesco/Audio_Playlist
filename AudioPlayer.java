import java.io.*;
import javax.sound.sampled.*;

public class AudioPlayer implements Runnable {
    Display display;
    PlayList playList;
    Clip audioClip;
    int audioPercent;
    boolean audioLoop;
    long currentFrame;

    public AudioPlayer(Display display, PlayList playList) {
        this.display = display;
        this.playList = playList;
        this.audioLoop = false;
    }

    // This shit loops over the entire playlist
    // Then loops over and over a playing audio until the audio is done playing, then it goes to the outer loop and continue on the next song
    // It sleeps for 250 milliseconds. So every 250 milliseconds it send the current playing position to Display
    @Override
    public void run() {
        try {
            do {
                // This whole try shit here is to get the file to ready to play
                try {
                    Audio audio = playList.getCurrent();
                    File audioFile = new File(audio.getFileName());
                    AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile);
                    audioClip = AudioSystem.getClip();
                    audioClip.open(audioStream);
                    audioPercent = 0;
                } catch (LineUnavailableException | UnsupportedAudioFileException | IOException e) {
                    throw new RuntimeException(e);
                }

                // Calculate audio length, reset audio playing position, and start the audio
                double audioLength = audioClip.getMicrosecondLength() / 1000000;
                double audioPosition = 0;
                audioClip.start();

                // Loop every 250 ms and update display with audio information
                do {
                    audioPosition = audioClip.getMicrosecondPosition() / 1000000;
                    while (audioPosition > audioLength) {
                        audioPosition -= audioLength;
                    }
                    display.setAudioProperties(audioLength, audioPosition, audioLoop);
                    Thread.sleep(250);
                } while ((int)Math.ceil(audioPosition) < (int)Math.ceil(audioLength) || audioLoop == true);
                // If the audio position is no longer less than audio length, next song
                playList.next();

                // Loop till death.
            } while (true);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }   
    }

    public void pause() {
    
        try {
            if (audioClip != null && audioClip.isRunning()) {
                // Store the current position in microseconds
                currentFrame = audioClip.getMicrosecondPosition();
    
                // Stop the audio playback
                audioClip.stop();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void resume() throws UnsupportedAudioFileException, IOException, LineUnavailableException{
      
        try{
            if(audioClip != null && !audioClip.isRunning()){
                audioClip.setMicrosecondPosition(currentFrame);
                audioClip.start();
            }
        }catch(Exception e){
            e.printStackTrace();
        }
   }

    public void loopAudio() {
        if (audioLoop) {
            audioLoop = false;
            audioClip.loop(0); 
        } else {
            audioLoop = true;
            audioClip.loop(Clip.LOOP_CONTINUOUSLY);  
        }
    }

}