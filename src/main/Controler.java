package main;

import javax.swing.JOptionPane;

/**
 * @author Jaroslaw Pawlak
 */
public class Controler {
    private static Thread waiterThread;

    private Controler() {}

    public static void start() {
        Debug.print("controler started");
        
        Alarm alarm;
        while ((alarm = Alarms.getFirstAlarm()) != null
                && alarm.getTime() < System.currentTimeMillis()) {
            JOptionPane.showMessageDialog(null, "Alarm missed:\n" + alarm,
                    Main.NAME, JOptionPane.WARNING_MESSAGE);
            Alarms.remove(alarm);
            AlarmsMainFrame.update();
        }
        Debug.print("missed alarms removed");

        if ((alarm = Alarms.getFirstAlarm()) == null) {
            Debug.print("no alarm in queue");
            return;
        }

        final Alarm finalAlarm = alarm;

        if (waiterThread != null) {
            waiterThread.interrupt();
            Debug.print("waiter interrupted");
        }
        Debug.print("waiting for: " + alarm);

        waiterThread = new Thread() {
            @Override
            public void run() {
                try {
                    Thread.sleep(finalAlarm.getTime() - System.currentTimeMillis());
                } catch (InterruptedException ex) {
                    Debug.print("waiting interrupted");
                    return;
                }
                Debug.print("waiting done");
                
                SoundPlayer.play(finalAlarm.getSound());
                JOptionPane.showMessageDialog(null, "Alarm:\n" + Alarms.getFirstAlarm(),
                        Main.NAME, JOptionPane.PLAIN_MESSAGE);
                SoundPlayer.stop();
                Alarms.removeFirstAlarm();
                AlarmsMainFrame.update();
                Controler.start();
            }
        };

        waiterThread.start();
    }
}