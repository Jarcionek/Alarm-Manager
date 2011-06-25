package main;

import java.io.File;
import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.Calendar;

/**
 * @author Jaroslaw Pawlak
 */
public class Alarm implements Comparable<Alarm>, Serializable {
    private long time;
    private String note;
    private File sound;

    public Alarm(Calendar date, String note) {
        this.time = date.getTimeInMillis();
        this.note = note;
        this.sound = (File) Settings.get(Settings.KEY_ALARM_SOUND);
    }

    public String getNote() {
        return note;
    }

    public long getTime() {
        return time;
    }

    public void setSound(File file) {
        this.sound = file;
    }

    public File getSound() {
        return sound;
    }

    @Override
    public String toString() {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(time);
        int[] v = {
            c.get(Calendar.YEAR),
            c.get(Calendar.MONTH),
            c.get(Calendar.DAY_OF_MONTH),
            c.get(Calendar.HOUR_OF_DAY),
            c.get(Calendar.MINUTE),
            c.get(Calendar.SECOND)
        };
        DecimalFormat df = new DecimalFormat("00");
        return v[0] + "." + df.format(v[1] + 1) + "." + df.format(v[2]) + " " + df.format(v[3]) +
                ":" + df.format(v[4]) + ":" + df.format(v[5]) + "     " + note;
    }

    @Override
    public int compareTo(Alarm o) {
        return (int) ((this.time - o.time) / 1000);
    }
}