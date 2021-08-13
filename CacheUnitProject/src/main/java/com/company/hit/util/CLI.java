package main.java.com.company.hit.util;

import java.io.*;
import java.lang.*;
import java.beans.PropertyChangeListener;

import java.beans.PropertyChangeSupport;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.Scanner;

public class CLI extends Object implements Runnable {

    private final BufferedReader in;
    private final OutputStream out;
    private final PropertyChangeSupport observer;

    public CLI(InputStream in, OutputStream out) throws IOException {
        this.in = new BufferedReader(new InputStreamReader(in));
        this.out = out;
        observer = new PropertyChangeSupport(this);
    }

    public void addPropertyChangeListener(PropertyChangeListener pcl) {
        observer.addPropertyChangeListener(pcl);
    }

    public void removePropertyChangeListener(PropertyChangeListener pcl) {
        observer.removePropertyChangeListener(pcl);

    }


    @Override
    public void run() {

        String command;
        while (true) {
            try {
                write("Please enter your command\n");
                command = in.readLine();
                switch (command) {
                    case "start":
                        write("starting server.....\n");
                        observer.firePropertyChange("command", null, command);
                        break;
                    case "stop":
                        //run=false
                        write("Shutdown server\n");
                        observer.firePropertyChange("command", null, command);
                        break;
                    default:
                        write("Not a valid command\n");
                        break;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void write(String string) throws IOException {
        try {
            out.write(string.getBytes(StandardCharsets.UTF_8));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}