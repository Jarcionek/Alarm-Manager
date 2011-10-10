package main;

import java.awt.Toolkit;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import javazoom.jl.player.advanced.AdvancedPlayer;

/**
 * @author Jaroslaw Pawlak
 */
public class SoundPlayer {
    private static Thread thread;

    private SoundPlayer() {}

    public static void play(final File file) {
        if (thread != null) {
            thread.interrupt();
            Debug.print("player interrupted");
        }

        thread = new Thread() {
            private AdvancedPlayer player;
            @Override
            public void run() {
//                try {
//                    FileInputStream fis = new FileInputStream(file);
//                    BufferedInputStream bis = new BufferedInputStream(fis);
//
//                    player = new AdvancedPlayer(bis);
//                    player.play();
//                } catch (Exception ex) {}
                try {
                    while (!isInterrupted()) {
                        for (int i = 0; i < 5; i++) {
                            Toolkit.getDefaultToolkit().beep();
                            Thread.sleep(100);
                        }
                        Thread.sleep(400);
                    }
                } catch (InterruptedException ex) {}
            }
            @Override
            public void interrupt() {
//                if (player != null) {
//                    player.close();
//                }
                super.interrupt();
            }
        };

        thread.start();
    }

    public static void stop() {
        if (thread != null) {
            thread.interrupt();
        }
    }
}