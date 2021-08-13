package main.java.com.hit.client;

import main.java.com.hit.view.CacheUnitView;

import java.beans.PropertyChangeListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileReader;
import java.lang.*;
import java.beans.PropertyChangeEvent;
import java.net.InetAddress;
import java.net.Socket;
import java.util.EventListener;
import java.util.Scanner;

public class CacheUnitClientObserver extends Object implements PropertyChangeListener, EventListener {
    private CacheUnitClient unitClient;
    private CacheUnitView unitView;

    public CacheUnitClientObserver() {
        unitClient = new CacheUnitClient();

    }

    public void propertyChange(PropertyChangeEvent evt) {
        unitView = (CacheUnitView) evt.getSource();

        if (evt.getNewValue().equals("statistics")) {
            try {
                String response = unitClient.send("statistics");
                String response2="";
                if (!response.isEmpty()) {
                    response2 = response;
                }
                unitView.updateUIData(response2);
            } catch (Exception exception) {
                unitView.updateUIData("error:" + exception.getMessage());
            }
        }
        Scanner command = (Scanner) evt.getNewValue();
        String content = "";
        while (command.hasNextLine()) {
            content = content + command.nextLine();
        }

        try {
            String response = unitClient.send(content);
            String response2;
            if (response.equals("true")) {
                response2 = "success";
            } else if (response.equals("false")) {
                response2 = "failed";
            } else if (response.split(" ")[0].equals("\"error:")) {
                if (response.contains("\"")) {
                    response2 = ((String) response).split("\"")[1] + "failed";
                } else {
                    response2 = response + ";fail";
                }
            } else {
                response2 = response + ";success";
            }
            unitView.updateUIData(response2);
        } catch (Exception exception) {
            unitView.updateUIData("error:" + exception.getMessage());
        }
    }
}
