package main.java.com.hit.view;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeSupport;
import java.io.File;
import java.io.FileReader;
import java.lang.*;
import java.awt.event.ActionListener;

import java.beans.PropertyChangeListener;
import java.util.Scanner;

public class CacheUnitView extends Object {
    JFrame frame = new JFrame("Cache Unit View");
    JButton button1 = new JButton("📂 load a request");
    JButton button2 = new JButton("📊 show statistics");
    JTextArea textArea = new JTextArea(17, 30);
    //private ActionListener listener;
    private PropertyChangeSupport support;
    private ButtonListener buttonListener;

    public CacheUnitView() {
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        button1.setActionCommand("load a request");
        button2.setActionCommand("show statistics");
        button1.setBackground(Color.LIGHT_GRAY);
        button2.setBackground(Color.LIGHT_GRAY);
        textArea.setText("\nMMU client!\n\nChoose an action:\n" +
                "press 'load a request' to load your Json file\npress 'show statistics' to see statistics so far");

        frame.setLayout(new FlowLayout(3, 50, 4));
        //frame.setLayout(new GridLayout(4,2));
        frame.setVisible(true);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setBackground(Color.darkGray);
        frame.setSize(460, 450);
        frame.setLocation(dim.width / 2 - frame.getSize().width / 2, dim.height / 2 - frame.getSize().height / 2);
        //
        // textArea.setSize(400,300);
        textArea.setFont(new Font("Ariel", Font.BOLD, 12));
        frame.add(button1);
        frame.add(button2);
        frame.add(textArea);
        support = new PropertyChangeSupport(this);
        buttonListener = new ButtonListener();
        button1.addActionListener(buttonListener);
        button2.addActionListener(buttonListener);
    }

    public class ButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            switch (e.getActionCommand()) {
                case "load a request":
                    JFileChooser chooser = new JFileChooser();

                    FileNameExtensionFilter filter = new FileNameExtensionFilter(".json file", "json");
                    chooser.setFileFilter(filter);
                    if (chooser.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION) {
                        File file = chooser.getSelectedFile();
                        try {
                            Scanner scanner = new Scanner(new FileReader(file.getAbsolutePath()));
                            support.firePropertyChange("json", null, scanner);
                            //scanner.close();
                        } catch (Exception exception) {
                            exception.printStackTrace();
                        }
                        break;
                    }
                case "show statistics":
                    String stats = "statistics";
                    support.firePropertyChange("json", null, stats);
                    break;
                default:
                    textArea.setText("no text to show");
                    break;
            }
        }
    }

    public void addPropertyChangeListener(PropertyChangeListener pcl) {
        support.addPropertyChangeListener(pcl);
    }

    public void removePropertyChangeListener(PropertyChangeListener pcl) {
        support.removePropertyChangeListener(pcl);
    }

    public void start() {

    }

    public <T> void updateUIData(T t) {
        if (t.toString().equals("success")) {
            textArea.setForeground(Color.black);
            textArea.setText("\n\n\n                       succeeded!!\n");
        } else if (t.toString().equals("failed")) {
            textArea.setText("\n\n\n                   failed :( \n");
        } else {
            textArea.setText(t.toString());
        }

    }
}
