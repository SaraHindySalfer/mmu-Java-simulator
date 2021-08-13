package main.java.com.company.hit.server;

import main.java.com.company.hit.memory.CacheUnit;
import main.java.com.company.hit.services.CacheUnitController;
import main.java.com.company.hit.services.CacheUnitService;

import java.beans.PropertyChangeEvent;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.Object;
import java.beans.PropertyChangeListener;
import java.net.ServerSocket;
import java.net.Socket;

public class Server extends Object implements PropertyChangeListener, Runnable {
    private ServerSocket server;
    private Socket client;
    private boolean shutdown, isShutdown, hasConnection;
    private final CacheUnitController<String> controller;

    public Server() throws IOException {
        controller = new CacheUnitController<String>();
        shutdown = false;
        isShutdown = true;
        hasConnection = true;
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getNewValue().equals("start")) {
            isShutdown = false;
            hasConnection = true;
            if (!shutdown) {
                shutdown = true;
                new Thread(this).start();

            }
        }
        if (evt.getNewValue().equals("stop") && !isShutdown) {

            isShutdown = true;
            hasConnection = false;
            controller.stopServer();
            if (shutdown) {
                shutdown = false;
            }
        }

    }

    @Override
    public void run() {
        try {
            server = new ServerSocket(12345);
            while (!isShutdown) {
                client = server.accept();
                if (hasConnection) {
                    Thread thread = new Thread(new HandleRequest<String>(client, controller));
                    thread.start();
                }
            }
            controller.stopServer();
            System.out.println("closing server");
            server.close();
        } catch (Exception e) {
            System.out.println("Exception :" + e);
        }
    }

}
