package main;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileFilter;
import javax.swing.table.AbstractTableModel;

/**
 * @author Jaroslaw Pawlak
 */
public class AlarmsMainFrame extends JFrame {
    private static AlarmsMainFrame ALARMS_MAIN_FRAME = null;
    private Alarm[] alarms;
    private Boolean[] deleteFlags;

    private AlarmsMainFrame() {
        super(Main.NAME);
        this.setLocation((Point) Settings.get(Settings.KEY_MAIN_FRAME_POSITION));
        this.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);

        
        this.setIconImage(Main.IMAGE);

        this.createComponents();
        this.createMenuBar();

        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                Settings.put(Settings.KEY_MAIN_FRAME_VISIBLE, Boolean.FALSE);
            }
        });
        this.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                Debug.print("main window lost focus");
                Settings.put(Settings.KEY_MAIN_FRAME_POSITION, AlarmsMainFrame.this.getLocation());
                Settings.put(Settings.KEY_MAIN_FRAME_SIZE, AlarmsMainFrame.this.getSize());
            }
        });

        Object o;
        if ((o = Settings.get(Settings.KEY_MAIN_FRAME_SIZE)) != null) {
            this.setSize((Dimension) o);
        } else {
            this.pack();
        }

        this.setVisible((Boolean) Settings.get(Settings.KEY_MAIN_FRAME_VISIBLE));
    }

    private void createComponents() {
        alarms = Alarms.getAlarmsAsArray();
        deleteFlags = new Boolean[alarms.length];
        for (int i = 0; i < alarms.length; i++) {
            deleteFlags[i] = new Boolean(false);
        }
        JTable table = new JTable(new AbstractTableModel() {
            @Override
            public int getRowCount() {
                return alarms.length;
            }
            @Override
            public int getColumnCount() {
                return 2;
            }
            @Override
            public Object getValueAt(int rowIndex, int columnIndex) {
                switch (columnIndex) {
                    case 0: return alarms[rowIndex];
                    case 1: return deleteFlags[rowIndex];
                    default: return null;
                }
            }
            @Override
            public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
                if (columnIndex == 1) {
                    deleteFlags[rowIndex] = (Boolean) aValue;
                }
                fireTableCellUpdated(rowIndex, columnIndex);
            }
            @Override
            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return columnIndex == 1;
            }
            @Override
            public Class getColumnClass(int c) {
                switch (c) {
                    case 0: return Alarm.class;
                    case 1: return Boolean.class;
                    default: return null;
                }
            }

            @Override
            public String getColumnName(int column) {
                return "";
            }
        });

        table.getTableHeader().setResizingAllowed(false);
        table.getTableHeader().setReorderingAllowed(false);
        table.getColumnModel().getColumn(0).setPreferredWidth(2147483647);
        table.setColumnSelectionAllowed(false);
        table.setRowSelectionAllowed(false);
        table.setCellSelectionEnabled(false);
        table.setAutoCreateRowSorter(true);
        table.setFocusable(false);
        table.setShowGrid(false);
        table.setBackground(this.getBackground());
        table.setFont((Font) Settings.get(Settings.KEY_FONT));
        table.setRowHeight((int) (((Font) Settings.get(Settings.KEY_FONT)).getSize() * 1.5));
        this.setContentPane(new JScrollPane(table));
    }

    private void createMenuBar() {
        JMenuBar menuBar = new JMenuBar();


        JMenu program = new JMenu("Program");
            JMenuItem exit = new JMenuItem("Exit");
            exit.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    Runtime.getRuntime().exit(0);
                }
            });
            program.add(exit);
        menuBar.add(program);

        JMenu alarmsMenu = new JMenu("Alarms");
            JMenuItem add = new JMenuItem("Add");
            add.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    SwingUtilities.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                           new NewAlarmFrame();
                        }
                    });
                }
            });
            alarmsMenu.add(add);
            JMenuItem delete = new JMenuItem("Delete selected");
            delete.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    for (int i = 0; i < alarms.length; i++) {
                        if (deleteFlags[i]) {
                            Alarms.remove(alarms[i]);
                        }
                    }
                    AlarmsMainFrame.update();
                    Controler.start();
                }
            });
            alarmsMenu.add(delete);
        menuBar.add(alarmsMenu);

        JMenu preferences = new JMenu("Preferences");
            JMenuItem defSound = new JMenuItem("Set default sound");
            defSound.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    JFileChooser jfc = new JFileChooser();
                    jfc.setMultiSelectionEnabled(false);
                    jfc.setDialogTitle("Choose default sound");
                    jfc.addChoosableFileFilter(new FileFilter() {
                        @Override
                        public boolean accept(File f) {
                            if (f.isDirectory()) {
                                return true;
                            } else if (!f.toString().contains(".")) {
                                return false;
                            } else {
                                String t = f.toString();
                                return t.substring(t.lastIndexOf('.') + 1)
                                        .toLowerCase().equals("mp3");
                            }
                        }
                        @Override
                        public String getDescription() {
                            return "*.mp3";
                        }
                    });
                    int choice = jfc.showOpenDialog(AlarmsMainFrame.this);
                    if (choice == JFileChooser.APPROVE_OPTION) {
                        Settings.put(Settings.KEY_ALARM_SOUND, new File(jfc.getSelectedFile().getPath()));
                    }
                }
            });
            preferences.add(defSound);
            defSound.setEnabled(false);
        menuBar.add(preferences);

        JMenu help = new JMenu("Help");
            JMenuItem about = new JMenuItem("About");
            about.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    MyOptionPane.showDialog(AlarmsMainFrame.this,
                            "VERSION: " + Main.VERSION + "\n" +
                            "RELEASED: " + Main.RELEASED + "\n" +
                            "AUTHOR: " + Main.AUTHOR,
                            Main.NAME + " - about", MyOptionPane.INFORMATION_MESSAGE);
                }
            });
            help.add(about);
        menuBar.add(help);

        
        this.setJMenuBar(menuBar);
    }
    


    public static void update() {
        ALARMS_MAIN_FRAME.createComponents();
        ALARMS_MAIN_FRAME.validate();
    }

    public static void init() {
        if (ALARMS_MAIN_FRAME == null) {
            ALARMS_MAIN_FRAME = new AlarmsMainFrame();
        }
    }

    public static void setDisplayed(boolean displayed) {
        ALARMS_MAIN_FRAME.setVisible(displayed);
        Settings.put(Settings.KEY_MAIN_FRAME_VISIBLE, displayed);
    }
    
    public static boolean isDisplayed() {
        return ALARMS_MAIN_FRAME.isVisible();
    }
}