package main;

import java.awt.Component;
import java.awt.Image;
import javax.swing.Icon;
import javax.swing.JDialog;
import javax.swing.JOptionPane;

/**
 * @author Jaroslaw Pawlak
 */
public class MyOptionPane extends JOptionPane {
    public static void showDialog(Component parentComponent, Object message,
            String title, int messageType, int optionType, Icon icon, Image dialogImage) {
        
        JOptionPane pane = new JOptionPane(message, messageType, optionType, icon);
        JDialog dialog = pane.createDialog(parentComponent, title);
        dialog.setIconImage(dialogImage);

        dialog.setAlwaysOnTop(true);
        dialog.setVisible(true);                        
        dialog.dispose();
    }
    
    public static void showDialog(Component parentComponent, Object message,
            String title, int messageType, int optionType, Icon icon) {
        showDialog(parentComponent, message, title, messageType, optionType, icon, null);
    }
    
    public static void showDialog(Component parentComponent, Object message,
            String title, int messageType, Icon icon) {
        showDialog(parentComponent, message, title, messageType, DEFAULT_OPTION, icon);
    }
    
    public static void showDialog(Component parentComponent,
            Object message, String title, int messageType) {
        showDialog(parentComponent, message, title, messageType, DEFAULT_OPTION, null, null);
    }
    
    public static void showDialog(Component parentComponent, Object message) {
        showDialog(parentComponent, message, "", PLAIN_MESSAGE, DEFAULT_OPTION, null, null);
    }
    
    public static void showDialog(Component parentComponent, Object message,
            String title, int messageType, Image dialogImage) {
        showDialog(parentComponent, message, title, messageType, DEFAULT_OPTION, null, dialogImage);
    }
}
