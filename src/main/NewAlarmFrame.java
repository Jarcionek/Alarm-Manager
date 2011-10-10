package main;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.DecimalFormat;
import java.util.Calendar;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

/**
 * @author Jaroslaw Pawlak
 */
public class NewAlarmFrame extends JFrame {
    private JPanel contentPane;
        private JPanel optionsPane;
            private JPanel timerPane;
                private JRadioButton timerRadioButton;
                private JPanel timerOptionsPane;
                    private JTextField timerDayValue;
                    private JLabel timerDayInfo;
                    private JTextField timerHoursValue;
                    private JLabel timerHoursInfo;
                    private JTextField timerMinutesValue;
                    private JLabel timerMinutesInfo;
                    private JTextField timerSecondsValue;
                    private JLabel timerSecondsInfo;
                    private JTextField timerNote;
            private JPanel alarmPane;
                private JRadioButton alarmRadioButton;
                private JPanel alarmOptionsPane;
                    private JTextField alarmMonthValue;
                    private JLabel alarmMonthInfo;
                    private XDayField alarmDayValue;
                    private JLabel alarmDayInfo;
                    private JTextField alarmHoursValue;
                    private JLabel alarmHoursInfo;
                    private JTextField alarmMinutesValue;
                    private JLabel alarmMinutesInfo;
                    private JTextField alarmSecondsValue;
                    private JLabel alarmSecondsInfo;
                    private JTextField alarmNote;
        private JPanel buttonsPane;
            private JButton buttonAccept;
            private JButton buttonCancel;

    public NewAlarmFrame() {
        super(Main.NAME + " - new alarm");
        this.setLocation((Point) Settings.get(Settings.KEY_NEW_ALARM_FRAME_POSITION));
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setResizable(false);

        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                Settings.put(Settings.KEY_NEW_ALARM_FRAME_POSITION, NewAlarmFrame.this.getLocation());
            }
        });

        this.setIconImage(Main.IMAGE);

        this.createComponents();
        this.createLayout();
        this.setContentPane(contentPane);
        this.pack();
        
        this.setVisible(true);
        timerMinutesValue.requestFocusInWindow();
    }

    private void createComponents() {
        timerNote = new XTextField();
        timerSecondsValue = new XNumberField(99, "00", timerNote);
        timerMinutesValue = new XNumberField(99, "10", timerSecondsValue);
        timerHoursValue = new XNumberField(99, "00", timerMinutesValue);
        timerDayValue = new XNumberField(99, "00", timerHoursValue);
        timerSecondsInfo = new XLabel("s");
        timerMinutesInfo = new XLabel("m");
        timerHoursInfo = new XLabel("h");
        timerDayInfo = new XLabel("D");
        JComponent[] timerGroup = new JComponent[] {
            timerDayValue,
            timerDayInfo,
            timerHoursValue,
            timerHoursInfo,
            timerMinutesValue,
            timerMinutesInfo,
            timerSecondsValue,
            timerSecondsInfo,
            timerNote
        };

        alarmNote = new XTextField();
        DecimalFormat x = new DecimalFormat("00");
        alarmSecondsValue = new XNumberField(59, x.format(Calendar.getInstance().get(Calendar.SECOND)), alarmNote);
        alarmMinutesValue = new XNumberField(59, x.format(Calendar.getInstance().get(Calendar.MINUTE)), alarmSecondsValue);
        alarmHoursValue = new XNumberField(23, x.format(Calendar.getInstance().get(Calendar.HOUR_OF_DAY)), alarmMinutesValue);
        alarmDayValue = new XDayField(x.format(Calendar.getInstance().get(Calendar.DAY_OF_MONTH)), alarmHoursValue);
        alarmMonthValue = new XNumberField(12, x.format(Calendar.getInstance().get(Calendar.MONTH) + 1), alarmDayValue);
        alarmDayValue.setMonthField(alarmMonthValue);
        alarmSecondsInfo = new XLabel("s");
        alarmMinutesInfo = new XLabel("m");
        alarmHoursInfo = new XLabel("h");
        alarmDayInfo = new XLabel("D");
        alarmMonthInfo = new XLabel("M");
        JComponent[] alarmGroup = new JComponent[] {
            alarmMonthValue,
            alarmMonthInfo,
            alarmDayValue,
            alarmDayInfo,
            alarmHoursValue,
            alarmHoursInfo,
            alarmMinutesValue,
            alarmMinutesInfo,
            alarmSecondsValue,
            alarmSecondsInfo,
            alarmNote
        };


        timerRadioButton = new XRadioButton("Timer", timerDayValue, alarmGroup, timerGroup);
        alarmRadioButton = new XRadioButton("Alarm", alarmMonthValue, timerGroup, alarmGroup);

        timerRadioButton.setSelected(true);
        ButtonGroup group = new ButtonGroup();
        group.add(timerRadioButton);
        group.add(alarmRadioButton);

        for (JComponent c : alarmGroup) {
            c.setEnabled(false);
        }


        buttonAccept = new JButton("Accept");
        buttonAccept.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                NewAlarmFrame.this.accept();
            }
        });
        buttonCancel = new JButton("Cancel");
        buttonCancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                NewAlarmFrame.this.dispose();
            }
        });

        //TODO //TEMP
        for (JTextField e : new JTextField[] {timerDayValue, timerHoursValue,
                timerMinutesValue, timerSecondsValue, timerNote, alarmMonthValue,
                alarmDayValue, alarmHoursValue, alarmMinutesValue,
                alarmSecondsValue, alarmNote}) {
            e.addKeyListener(new KeyAdapter() {
                @Override
                public void keyPressed(KeyEvent e) {
                    if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                        NewAlarmFrame.this.accept();
                    }
                }
            });
        }
    }

    private void createLayout() {
        contentPane = new JPanel(new BorderLayout());
            optionsPane = new JPanel(new GridLayout(2, 1));
                timerPane = new JPanel(new BorderLayout());
                    timerOptionsPane = new JPanel(new GridBagLayout());
                alarmPane = new JPanel(new BorderLayout());
                    alarmOptionsPane = new JPanel(new GridBagLayout());
            buttonsPane = new JPanel(new GridBagLayout());

            timerPane.add(timerRadioButton, BorderLayout.NORTH);
        createTimerLayout();
            timerPane.add(timerOptionsPane, BorderLayout.CENTER);
                optionsPane.add(timerPane);
            alarmPane.add(alarmRadioButton, BorderLayout.NORTH);
        createAlarmLayout();
            alarmPane.add(alarmOptionsPane, BorderLayout.CENTER);
                optionsPane.add(alarmPane);
                    contentPane.add(optionsPane, BorderLayout.CENTER);
                createButtonsLayout();
                    contentPane.add(buttonsPane, BorderLayout.SOUTH);
    }

    private void createTimerLayout() {
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(5, 5, 5, 0);
        c.gridx = 0;
        c.gridy = 0;

        c.gridwidth = 2;
        timerOptionsPane.add(timerDayValue, c);

        c.gridx += c.gridwidth;
        c.gridwidth = 1;
        timerOptionsPane.add(timerDayInfo, c);


        c.gridx += c.gridwidth;
        c.gridwidth = 2;
        timerOptionsPane.add(timerHoursValue, c);

        c.gridx += c.gridwidth;
        c.gridwidth = 1;
        timerOptionsPane.add(timerHoursInfo, c);

        c.gridx += c.gridwidth;
        c.gridwidth = 2;
        timerOptionsPane.add(timerMinutesValue, c);

        c.gridx += c.gridwidth;
        c.gridwidth = 1;
        timerOptionsPane.add(timerMinutesInfo, c);

        c.gridx += c.gridwidth;
        c.gridwidth = 2;
        timerOptionsPane.add(timerSecondsValue, c);

        c.insets = new Insets(5, 5, 5, 5);
        c.gridx += c.gridwidth;
        c.gridwidth = 1;
        timerOptionsPane.add(timerSecondsInfo, c);

        c.insets = new Insets(0, 5, 5, 5);
        c.gridx = 0;
        c.gridy = 1;
        c.gridwidth = 15;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1;
        timerOptionsPane.add(timerNote, c);
    }

    private void createAlarmLayout() {
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(5, 5, 5, 0);
        c.gridx = 0;
        c.gridy = 0;

        c.gridwidth = 2;
        alarmOptionsPane.add(alarmMonthValue, c);

        c.gridx += c.gridwidth;
        c.gridy = 0;
        c.gridwidth = 1;
        alarmOptionsPane.add(alarmMonthInfo, c);

        c.gridx += c.gridwidth;
        c.gridwidth = 2;
        alarmOptionsPane.add(alarmDayValue, c);

        c.insets = new Insets(5, 5, 5, 25);
        c.gridx += c.gridwidth;
        c.gridwidth = 1;
        alarmOptionsPane.add(alarmDayInfo, c);

        c.insets = new Insets(5, 5, 5, 0);
        c.gridx += c.gridwidth;
        c.gridwidth = 2;
        alarmOptionsPane.add(alarmHoursValue, c);

        c.gridx += c.gridwidth;
        c.gridwidth = 1;
        alarmOptionsPane.add(alarmHoursInfo, c);

        c.gridx += c.gridwidth;
        c.gridwidth = 2;
        alarmOptionsPane.add(alarmMinutesValue, c);

        c.gridx += c.gridwidth;
        c.gridwidth = 1;
        alarmOptionsPane.add(alarmMinutesInfo, c);

        c.gridx += c.gridwidth;
        c.gridwidth = 2;
        alarmOptionsPane.add(alarmSecondsValue, c);

        c.insets = new Insets(5, 5, 5, 5);
        c.gridx += c.gridwidth;
        c.gridwidth = 1;
        alarmOptionsPane.add(alarmSecondsInfo, c);

        c.insets = new Insets(0, 5, 5, 5);
        c.gridx = 0;
        c.gridy = 1;
        c.gridwidth = 15;
        c.fill = GridBagConstraints.HORIZONTAL;
        alarmOptionsPane.add(alarmNote, c);
    }

    private void createButtonsLayout() {
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(5, 5, 5, 5);
        buttonsPane.add(buttonAccept, c);
        buttonsPane.add(buttonCancel, c);
    }

    private void accept() {
        Calendar c = Calendar.getInstance();
        String note = "";
        if (timerRadioButton.isSelected()) {
           c.set(Calendar.DAY_OF_MONTH, c.get(Calendar.DAY_OF_MONTH) + Integer.parseInt(timerDayValue.getText()));
           c.set(Calendar.HOUR_OF_DAY, c.get(Calendar.HOUR_OF_DAY) + Integer.parseInt(timerHoursValue.getText()));
           c.set(Calendar.MINUTE, c.get(Calendar.MINUTE) + Integer.parseInt(timerMinutesValue.getText()));
           c.set(Calendar.SECOND, c.get(Calendar.SECOND) + Integer.parseInt(timerSecondsValue.getText()));
           note = timerNote.getText();
        } else if (alarmRadioButton.isSelected()) {
           c.set(Calendar.MONTH, Integer.parseInt(alarmMonthValue.getText()) - 1);
           c.set(Calendar.DAY_OF_MONTH, Integer.parseInt(alarmDayValue.getText()));
           c.set(Calendar.HOUR_OF_DAY, Integer.parseInt(alarmHoursValue.getText()));
           c.set(Calendar.MINUTE, Integer.parseInt(alarmMinutesValue.getText()));
           c.set(Calendar.SECOND, Integer.parseInt(alarmSecondsValue.getText()));
           if (c.getTimeInMillis() < System.currentTimeMillis()) {
               c.set(Calendar.YEAR, c.get(Calendar.YEAR) + 1);
           }
           note = alarmNote.getText();
        }

        Alarms.addAlarm(new Alarm(c, note));
        this.dispose();
        AlarmsMainFrame.update();
    }
}