package main;

import java.awt.AWTException;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

/**
 * @author Jaroslaw Pawlak
 */
public class Tray {
    private Tray() {}
    public static void init() {
        Image image = null;
        try {
            image = ImageIO.read(Main.class.getResource("/resources/sandglass.png"));
        } catch (IOException ex) {
            Logger.getLogger(Settings.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, "Could not load sandglass.png " +
                    "for " + Tray.class.getSimpleName() + ":\n" + ex,
                    Main.NAME, JOptionPane.WARNING_MESSAGE);
            System.exit(1);
        }

        PopupMenu popupMenu = new PopupMenu();
            MenuItem addAlarmMenuItem = new MenuItem("Add alarm");
            addAlarmMenuItem.addActionListener(new ActionListener() {
            @Override
                public void actionPerformed(ActionEvent e) {
                    new NewAlarmFrame();
                }
            });
            popupMenu.add(addAlarmMenuItem);
            MenuItem exitMenuItem = new MenuItem("Exit");
            exitMenuItem.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    Runtime.getRuntime().exit(0);
                }
            });
            popupMenu.add(exitMenuItem);

        TrayIcon trayIcon = new TrayIcon(image, "Alarm Manager", popupMenu);
        trayIcon.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1) { // LEFT MOUSE BUTTON
                    AlarmsMainFrame.setDisplayed(!AlarmsMainFrame.isDisplayed());
                }
            }
        });

        try {
            SystemTray.getSystemTray().add(trayIcon);
        } catch (AWTException ex) {
            Logger.getLogger(Settings.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, "Could not add a tray icon " +
                    "to the system tray:\n" + ex,
                    Main.NAME, JOptionPane.WARNING_MESSAGE);
            System.exit(1);
        }
    }
}