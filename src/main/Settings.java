package main;

import java.awt.Font;
import java.awt.Point;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Hashtable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 * @author Jaroslaw Pawlak
 */
public class Settings {
    public static final String KEY_NEW_ALARM_FRAME_POSITION = "new_alarm_position_frame";
    public static final String KEY_FONT = "font";
    public static final String KEY_MAIN_FRAME_VISIBLE = "main_frame_visible";
    public static final String KEY_MAIN_FRAME_POSITION = "main_frame_position";
    public static final String KEY_MAIN_FRAME_SIZE = "main_frame_size";
    public static final String KEY_ALARM_SOUND = "alarm_sound";

    private static final File FILE = new File("settings.ser");

    private static Hashtable<String, Object> settings;

    public static void save() {
        if (!FILE.exists()) {
            try {
                FILE.createNewFile();
            } catch (IOException ex) {
                Logger.getLogger(Settings.class.getName()).log(Level.SEVERE, null, ex);
                JOptionPane.showMessageDialog(null, "Could not create a file " +
                        FILE + "\n" + ex, Main.NAME, JOptionPane.ERROR_MESSAGE);
                System.exit(1);
            }
        }
        
        try {
            FileOutputStream fos = new FileOutputStream(FILE);
            ObjectOutputStream oos = new ObjectOutputStream(fos);

            oos.writeObject(settings);

            oos.close();
            fos.close();
        } catch (Exception ex) {
            Logger.getLogger(Settings.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, "Could not save settings:\n" +
                    ex, Main.NAME, JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }
        Debug.print("");
        Debug.print("SETTINGS SAVED:");
        for (String s : settings.keySet()) {
            Debug.print(s + " - " + settings.get(s));
        }
        Debug.print("");
    }

    /**
     * Loads settings from the file. Missing settings are set to default.
     * @return true if loading succeed, false otherwise
     */
    public static void load() {
        reset();
        Hashtable<String, Object> temp = null;

        if (!FILE.exists()) {
            Debug.print("no settings file");
            return;
        }

        try {
            FileInputStream fis = new FileInputStream(FILE);
            ObjectInputStream ois = new ObjectInputStream(fis);

            temp = (Hashtable<String, Object>) ois.readObject();

            ois.close();
            fis.close();
        } catch (Exception ex) {
            Logger.getLogger(Settings.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, "Could not load settings from file " +
                    FILE + "\n" + ex, Main.NAME, JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }

        for(String s : temp.keySet()) {
            settings.put(s, temp.get(s));
        }

        Debug.print("");
        Debug.print("SETTINGS LOADED:");
        for (String s : settings.keySet()) {
            Debug.print(s + " - " + settings.get(s));
        }
        Debug.print("");
    }

    /**
     * Sets all settings to default.
     */
    public static void reset() {
        settings = new Hashtable<String, Object>();
        settings.put(KEY_NEW_ALARM_FRAME_POSITION, new Point(50, 50));
        settings.put(KEY_FONT, new Font("", Font.PLAIN, 16));
        settings.put(KEY_MAIN_FRAME_POSITION, new Point(50, 50));
        settings.put(KEY_MAIN_FRAME_VISIBLE, Boolean.TRUE);
//        try {
//            settings.put(KEY_ALARM_SOUND, new File(Settings.class.getResource("/resources/sample.mp3").toURI()));
//        } catch (URISyntaxException ex) {
//            Logger.getLogger(Settings.class.getName()).log(Level.SEVERE, null, ex);
//        }
        Debug.print("settings reseted");
    }

    public static Object get(String key) {
        return settings.get(key);
    }

    public static void put(String key, Object object) {
        Object temp = settings.put(key, object);
        Debug.print("putting " + object + " in " + key + " instead of " + temp);
    }
}