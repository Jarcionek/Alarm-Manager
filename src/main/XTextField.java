package main;

import java.awt.Font;
import java.awt.Insets;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import javax.swing.JTextField;

/**
 * Extension of JTextField. <code>getText</code> returns </code>""</code> if
 * value is equaled to initial value.
 *
 * @author Jaroslaw Pawlak
 */
public class XTextField extends JTextField {
    private static final String INITIAL_VALUE = "optional note";
    private boolean alreadyFocused = false;

    public XTextField() {
        super(INITIAL_VALUE);
        this.setFont((Font) Settings.get(Settings.KEY_FONT));
        this.setMargin(new Insets(2, 5, 2, 5));
        this.setHorizontalAlignment(JTextField.CENTER);
        this.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (!alreadyFocused) {
                    XTextField.this.setText("");
                    alreadyFocused = true;
                }
                XTextField.this.setSelectionStart(0);
                XTextField.this.setSelectionEnd(XTextField.super.getText().length());
            }
        });
    }
    @Override
    public String getText() {
        return alreadyFocused? super.getText() : "";
    }
}