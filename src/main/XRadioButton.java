package main;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JComponent;
import javax.swing.JRadioButton;

/**
 * @author Jaroslaw Pawlak
 */
public class XRadioButton extends JRadioButton {
    private XRadioButton() {}
    public XRadioButton(String text,
            final JComponent next,
            final JComponent[] groupToDisable,
            final JComponent[] groupToEnable) {
        super(text);
        this.setFont((Font) Settings.get(Settings.KEY_FONT));
        this.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                for (JComponent c : groupToDisable) {
                    c.setEnabled(false);
                }
                for (JComponent c : groupToEnable) {
                    c.setEnabled(true);
                }
                next.requestFocusInWindow();
            }
        });
    }
}