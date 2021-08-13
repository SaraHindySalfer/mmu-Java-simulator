package main.java.com.hit.client;


import main.java.com.hit.view.CacheUnitView;

import java.beans.PropertyChangeEvent;
import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.lang.*;
import java.net.InetAddress;
import java.net.Socket;

public class CacheUnitClient extends Object{
    public static String LOCAL_HOST="localhost";
    public static int PORT=12345;
    private Socket socket;
    private DataInputStream input;
    private DataOutputStream output;
    private InetAddress address;
    private StringBuilder sb;
    public CacheUnitClient() {
    }

    public String send(String request){
        System.out.println("in send");
        String response="";
        try {
            sb=new StringBuilder();
            address=InetAddress.getByName(LOCAL_HOST);
            socket=new Socket("127.0.0.1",PORT);
            output=new DataOutputStream(socket.getOutputStream());
            input=new DataInputStream(new BufferedInputStream(socket.getInputStream()));
            output.writeUTF(request);
            output.flush();
            do{
                response=input.readUTF();
                sb.append(response);
            }while (input.available()!=0);
            response=sb.toString();
            System.out.println("returning response");
            return (String) response;
        }
        catch (Exception e){
            return "no connection to server";
        }

    }

}

