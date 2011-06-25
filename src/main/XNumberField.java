package main;

import java.awt.Font;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.text.DecimalFormat;
import javax.swing.JTextField;

/**
 * Extension of JTextField:
 * <li>sets all text selected on focus gained
 * <li>consumes typed character if new text is smaller then 0 or greater than
 * value specified in constructor
 * <li>after key typed, if new value approved, lenght of text is 2 and
 * <code>next</code> is not null, requests focus on <code>next</code>
 * <li>does not support <code>copy</code>, <code>cut</code> and <code>paste</code>
 * 
 * @author Jaroslaw Pawlak
 */
public class XNumberField extends JTextField {
    private int max;
    private DecimalFormat df = new DecimalFormat("00");
    
    private XNumberField() {}

    public XNumberField(int max, String initialValue, final JTextField next) {
        super(initialValue);
        this.setFont((Font) Settings.get(Settings.KEY_FONT));
        this.setMargin(new Insets(2, 5, 2, 5));

        this.max = max;

        this.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                XNumberField.this.setSelectionStart(0);
                XNumberField.this.setSelectionEnd(XNumberField.this.getText().length());
            }
            @Override
            public void focusLost(FocusEvent e) {
                if (XNumberField.this.getText().equals("")) {
                    XNumberField.this.setText("00");
                } else {
                    XNumberField.this.setText(df.format(Integer.parseInt(XNumberField.this.getText())));
                }
            }
        });

        this.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                if (e.getKeyChar() == KeyEvent.VK_DELETE
                        || e.getKeyChar() == KeyEvent.VK_BACK_SPACE) {
                    return;
                }
                String text = XNumberField.this.getText();
                String x = text.substring(0, XNumberField.this.getSelectionStart())
                        + e.getKeyChar() + text.substring(XNumberField.this.getSelectionEnd());
                int result;
                try {
                    result = Integer.parseInt(x);
                } catch (NumberFormatException ex) {
                    e.consume();
                    Toolkit.getDefaultToolkit().beep();
                    return;
                }
                if (result < 0 || result > XNumberField.this.max) {
                    e.consume();
                    Toolkit.getDefaultToolkit().beep();
                    return;
                }
                if (next != null && x.length() == 2) {
                    next.requestFocusInWindow();
                }
            }
        });
    }

    @Override
    public void paste() {}
    @Override
    public void copy() {}
    @Override
    public void cut() {}
}