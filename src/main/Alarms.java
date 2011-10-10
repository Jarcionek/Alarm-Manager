package main;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.PriorityQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Jaroslaw Pawlak
 */
public class Alarms {
    private static final File FILE = new File("alarms.ser");

    private static PriorityQueue<Alarm> alarms = new PriorityQueue<Alarm>();

    private Alarms() {}

    public static void addAlarm(Alarm alarm) {
        alarms.add(alarm);
        Debug.print("alarm " + alarm + " added");
        Alarms.save();
        Controler.start();
    }

    public static Alarm[] getAlarmsAsArray() {
        return alarms.toArray(new Alarm[] {});
    }

    public static void remove(Alarm alarm) {
        alarms.remove(alarm);
        Alarms.save();
    }

    public static Alarm getFirstAlarm() {
        return alarms.peek();
    }
    public static void removeFirstAlarm() {
        alarms.poll();
        Alarms.save();
    }

    public static void save() {
        if (!FILE.exists()) {
            try {
                FILE.createNewFile();
            } catch (IOException ex) {
                Logger.getLogger(Settings.class.getName()).log(Level.SEVERE, null, ex);
                MyOptionPane.showDialog(null, "Could not create a file " +
                        FILE + "\n" + ex, Main.NAME, MyOptionPane.ERROR_MESSAGE);
                System.exit(1);
            }
        }

        try {
            FileOutputStream fos = new FileOutputStream(FILE);
            ObjectOutputStream oos = new ObjectOutputStream(fos);

            oos.writeObject(alarms);
            oos.close();
            fos.close();
        } catch (Exception ex) {
            Logger.getLogger(Settings.class.getName()).log(Level.SEVERE, null, ex);
            MyOptionPane.showDialog(null, "Could not save alarms:\n" +
                    ex, Main.NAME, MyOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }
        Debug.print("alarms saved");
    }

    public static void load() {
        if (!FILE.exists()) {
            Debug.print("no alarms file");
            return;
        }
        try {
            FileInputStream fis = new FileInputStream(FILE);
            ObjectInputStream ois = new ObjectInputStream(fis);

            alarms = (PriorityQueue<Alarm>) ois.readObject();

            ois.close();
            fis.close();
        } catch (Exception ex) {
            Logger.getLogger(Settings.class.getName()).log(Level.SEVERE, null, ex);
            MyOptionPane.showDialog(null, "Could not load alarms from file " +
                    FILE + "\n" + ex, Main.NAME, MyOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }
        Debug.print("alarms loaded");
    }
}