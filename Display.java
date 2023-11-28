import java.io.*;
import java.lang.*;
import java.util.Scanner;
import java.util.ArrayList;

public class Display {
    double audioLength;
    double audioPosition;
    boolean audioLoop;
    PlayList playList;

    public Display(PlayList playList) {
        this.audioLength = 0;
        this.audioPosition = 0;
        this.playList = playList;
    }

    // When you display the audio screen, you'd need the audio's length and the current playing position of the audio
    // This method is used by AudioPlayer thread, every 250 mil seconds, the audio player thread will update the current playing
    // position and send them infos over to here for display
    public void setAudioProperties(double audioLength, double audioPosition, boolean audioLoop) {
        this.audioLength = audioLength;
        this.audioPosition = audioPosition;
        this.audioLoop = audioLoop;
    }

    // Flush the screen blank. This is where the app will fail if ran in any IDE. It only works within Linux/Unix command line or Windows CMD
    public void flushScreen() {
        try {
            // Flush screen for Windows CMD
            new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
        } catch (IOException | InterruptedException e) {
            // Flush screen for Linux/Unix command line
            System.out.print("\033[H\033[2J");
            System.out.flush();
        }
    }

    // output of the menu screen
    public void menuScreen() {
        flushScreen();
        // 0 - menu state
        // 1 - audio progress bar state
        // 2 - loop audio
        // 3 - pause
        // 4 - resume
        // 5 - add new file to playlist
        // 6 - exit
        System.out.println("<<<<<<< MENU >>>>>>>\n");
        System.out.println("-- AUDIO  CONTROL --");
        System.out.println("1. View audio player");
        System.out.println("2. " + (audioLoop ? "Remove audio loop" : "Loop current audio"));
        System.out.println("3. Pause");
        System.out.println("4. Resume");
        System.out.println("----- PLAYLIST -----");
        System.out.println("5. Add file to playlist");
        System.out.println("--------------------");
        System.out.println("6. Exit App");
        System.out.print("\nYour selection: ");
    }

    // A sample audio playing screen, with a play list underneath
    public void audioScreen() {
        flushScreen();
        System.out.println("AUDIO PLAYER");
        System.out.println("\n");
        int audioPercent = (int)Math.ceil(audioPosition / audioLength * 100);
        System.out.printf("%02d:%02d ", (int)audioPosition / 60, (int)audioPosition % 60);
        for (int i = 0; i < 50; i++) {
            System.out.print((i <= audioPercent / 2) ? "█" : "░");
        }
        System.out.printf(" %02d:%02d\n", (int)audioLength / 60, (int)audioLength % 60);
        if (audioLoop) {
            System.out.println("                         Loop - ON");
        }

        System.out.println("\n");
        System.out.println("Playlist: ");
        playList.getList();
        System.out.println("\n");
        System.out.println("Current Audio: " + playList.getCurrent().getFileName());
        System.out.println("\n");
        System.out.println("Press RETURN to go the MENU");
    }

    // Screen for add file to playlist
    public void addFileScreen(PlayList list) {
        flushScreen();
        
        System.out.println("Enter the file you wanna add:");
        
        int originalLength = list.playList.size();
        
        Scanner scanner = new Scanner(System.in); 
        String fileName = scanner.nextLine();
        
        list.addToPlayList(fileName);
        
        int newLength = list.playList.size();
        
        if (originalLength != newLength) {
            System.out.println("Successfully added! Press RETURN to go to the MENU.");
        } else {
            System.out.println("Error adding file. Press RETURN to go to the MENU.");
        }
        scanner.nextLine();
        
    }
}