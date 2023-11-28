import java.io.*;
import java.util.Scanner;

public class Main {
    public static void main(String [] args) throws IOException {
        // Clip audioClip;
        PlayList playList = new PlayList();
        Display display = new Display(playList);  
        AudioPlayer audioPlayer = new AudioPlayer(display, playList);
        Thread audioThread = new Thread(audioPlayer);
        Scanner scanner = new Scanner(System.in);

        // So the states so far are....
        // 0 - menu state
        // 1 - audio progress bar state
        // 2 - loop audio
        // 3 - pause
        // 4 - resume
        // 5 - add new file to playlist
        // 6 - exit
        int state = 1;
        do {
            switch (state) {
                case 0: // Menu
                    display.menuScreen();
                    
                    int selection = 0;
                    do {
                        selection = scanner.nextInt();
                        scanner.nextLine();

                        // If selection is legit, translate it to a state, otherwise,
                        // mark selection as 0 and keep looping until user enter the right number
                        if (selection > 0 && selection <= 6) {
                            state = selection;
                        } else {
                            selection = 0;  
                        }

                    } while (selection == 0);
                    break;
                case 1: // Audio progress bar

                    // If the audio thread is not alive, start it (and play music)
                    if (!audioThread.isAlive()) {
                        audioThread.start();
                    }

                    // Display the audio screen
                    display.audioScreen();

                    // This is an input buffer with a timer set to 0.25 seconds
                    // After 0.25 seconds it cancels the input and go on with the rest of the code
                    // Since this is within a do-while loop, it's gonna keep looping, keep flushing
                    // the screen, keep regenerating the newest progress bar, and keep waiting for
                    // user input for 0.25 seconds.
                    BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
                    long startTime = System.currentTimeMillis();
                    while ((System.currentTimeMillis() - startTime) < 250 && !in.ready()) {}

                    // If the user hit enter, we'll go to menu
                    if (in.ready()) {
                        in.read();
                        state = 0;
                    }
                    break;
                case 2: // loop
                    audioPlayer.loopAudio();
                    state = 1;
                    break;
                case 3: // pause
                    audioPlayer.pause();
                    state = 1;
                    break;
                case 4: // resume
                    try {
                        audioPlayer.resume();
                        state = 1;
                    } catch (Exception e){
                        e.printStackTrace();
                    }
                    break;
                case 5: // Add to play list
                    display.addFileScreen(playList);
                    state = 1;
                    break;
                case 6: // Exit program
                    System.exit(0);
            }
        } while (true);

    }
}