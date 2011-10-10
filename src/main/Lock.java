package main;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Jaroslaw Pawlak
 */
public class Lock {
    private static final File FILE = new File("lock");
    private static FileChannel channel;
    private static FileLock lock = null;

    private Lock() {}

    /**
     * Returns true if lock acquired or false if is already locked.
     * @return true if lock acquired or false if is already locked
     */
    public static boolean acquire() {
        try {
            if (FILE.exists()) {
                FILE.delete();
            }
            channel = new RandomAccessFile(FILE, "rw").getChannel();
            lock = channel.tryLock();
            if (lock == null) {
                channel.close();
                return false;
            }
        } catch (Exception ex) {
            Logger.getLogger(Settings.class.getName()).log(Level.SEVERE, null, ex);
            MyOptionPane.showDialog(null, "Could not acquire lock:\n" + ex,
                    Main.NAME, MyOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }
        return true;
    }
    public static void release() {
        try {
            if(lock != null) {
                lock.release();
                channel.close();
                FILE.delete();
            }
        } catch (IOException ex) {
            Logger.getLogger(Settings.class.getName()).log(Level.SEVERE, null, ex);
            MyOptionPane.showDialog(null, "Could not release lock:\n" + ex,
                    Main.NAME, MyOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }
        Debug.print("lock released");
    }
}