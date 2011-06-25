package main;

import java.awt.Font;
import javax.swing.JLabel;

/**
 * @author Jaroslaw Pawlak
 */
public class XLabel extends JLabel {
    private XLabel() {}
    public XLabel(String text) {
        super(text);
        this.setFont((Font) Settings.get(Settings.KEY_FONT));
    }
}