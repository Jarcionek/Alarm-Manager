package main;

import java.awt.Image;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

/**
 * @author Jaroslaw Pawlak
 */
public class Main {
    public static final String VERSION = "1.0* beta";
    public static final String RELEASED = "10.10.2011";
    public static final String AUTHOR = "Jaroslaw Pawlak";

    public static final String NAME = "Alarm Manager " + VERSION;
    
    public static final Image IMAGE = loadImage();

    public static void main(String[] args) {
        if (!Lock.acquire()) {
            Debug.print("already running");
            try {
                new Socket("127.0.0.1", 6013);
            } catch (UnknownHostException ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            }
            System.exit(0);
        }

        Runtime.getRuntime().addShutdownHook(new Thread(){
            @Override
            public void run() {
                Debug.print("program closing");
                Settings.save();
                Lock.release();
                Debug.print("program closed");
            }
        });

        Tray.init();

        Settings.load();
        Alarms.load();

        AlarmsMainFrame.init();

        Debug.print("Loaded alarms:");
        for (Alarm e : Alarms.getAlarmsAsArray()) {
            Debug.print(e);
        }

        Controler.start();

        ServerSocket socket;
        try {
            socket = new ServerSocket(6013);
        } catch (IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            return;
        }
        while (true) {
            try {
                socket.accept();
            } catch (IOException ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            }
            new NewAlarmFrame();
        }
    }
    
    private static Image loadImage() {
        try {
            return ImageIO.read(Main.class.getResource("/resources/sandglass.png"));
        } catch (Exception ex) {
            System.err.println("Could not load sandglass.png");
            return null;
        }
    }
}