package main;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Jaroslaw Pawlak
 */
public class Main {
    public static final String VERSION = "1.0 beta";
    public static final String RELEASED = "14.06.2011";
    public static final String AUTHOR = "Jaroslaw Pawlak";

    public static final String NAME = "Alarm Manager " + VERSION;

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
}