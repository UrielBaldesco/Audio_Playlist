import java.io.*;
import java.util.ArrayList;
import javax.sound.sampled.*;
// import javax.swing.JOptionPane;

public class PlayList {
    ArrayList<Audio> playList = new ArrayList<Audio>();
    int current;
    File audioFile; 
    Clip audioClip;

    public PlayList() {
        current = 0;
        // The default playlist has 3 songs.
        String [] fileNames = {"PinkPanther30.wav", "BabyElephantWalk60.wav", "CantinaBand60.wav"};
        for (String fileName : fileNames) {
            boolean addedSuccessfully = addToPlayList(fileName);
        }
    }

    // function to pull the audio length, and append the new Audio(fileName, audioLength) to the arrayList
    public boolean addToPlayList(String fileName) {
        try {
            audioFile = new File(fileName);
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile);
            Clip audioClip = AudioSystem.getClip();
            audioClip.open(audioStream);
            playList.add(new Audio(fileName, audioClip.getMicrosecondLength() / 1000000));
            audioStream.close();
        } catch (LineUnavailableException | UnsupportedAudioFileException | IOException e) {
            return false;
        }
        return true;
    }

    // Return list of audios
    public void getList() {
        for(int i = 0; i < playList.size(); i++){
            System.out.println(i+1 + ")" + " " + playList.get(i).fileName + " \n " + "file length: " + playList.get(i).audioLength);
        }
    }

    public Audio getCurrent() {
        return playList.get(current);
    }

    // next audio
    public void next() {
        current++;
        if (current >= playList.size()) {
            current = 0;
        }
    }
}