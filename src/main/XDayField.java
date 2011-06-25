package main;

import java.awt.Font;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.text.DecimalFormat;
import java.util.Calendar;
import javax.swing.JTextField;

/**
 * @author Jaroslaw Pawlak
 */
public class XDayField extends JTextField {
    private JTextField monthField;
    private int max;
    private DecimalFormat df = new DecimalFormat("00");

    private XDayField() {}

    public XDayField(String initialValue, final JTextField next) {
        super(initialValue);
        this.setFont((Font) Settings.get(Settings.KEY_FONT));
        this.setMargin(new Insets(2, 5, 2, 5));

        this.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                XDayField.this.setSelectionStart(0);
                XDayField.this.setSelectionEnd(XDayField.this.getText().length());
            }
            @Override
            public void focusLost(FocusEvent e) {
                if (XDayField.this.getText().equals("")) {
                    XDayField.this.setText("00");
                } else {
                    XDayField.this.setText(df.format(Integer.parseInt(XDayField.this.getText())));
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
                String text = XDayField.this.getText();
                String x = text.substring(0, XDayField.this.getSelectionStart())
                        + e.getKeyChar() + text.substring(XDayField.this.getSelectionEnd());
                int result;
                try {
                    result = Integer.parseInt(x);
                } catch (NumberFormatException ex) {
                    e.consume();
                    Toolkit.getDefaultToolkit().beep();
                    return;
                }

                if (XDayField.this.monthField.getText().equals("00")) {
                    monthField.setText(df.format(Calendar.getInstance().get(Calendar.MONTH)));
                }

                switch (Integer.parseInt(XDayField.this.monthField.getText())) {
                    case 1: max = 31; break;
                    case 2: max = 29; break;
                    case 3: max = 31; break;
                    case 4: max = 30; break;
                    case 5: max = 31; break;
                    case 6: max = 30; break;
                    case 7: max = 31; break;
                    case 8: max = 31; break;
                    case 9: max = 30; break;
                    case 10: max = 31; break;
                    case 11: max = 30; break;
                    case 12: max = 31; break;
                }

                if (result < 0 || result > max) {
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

    public void setMonthField(JTextField monthField) {
        this.monthField = monthField;
    }

    @Override
    public void paste() {}
    @Override
    public void copy() {}
    @Override
    public void cut() {}
}