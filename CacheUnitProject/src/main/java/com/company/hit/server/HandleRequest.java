package main.java.com.company.hit.server;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import main.java.com.company.hit.dm.DataModel;
import main.java.com.company.hit.services.CacheUnitController;

import java.io.*;
import java.lang.*;
import java.net.Socket;
import java.lang.reflect.Type;
import java.util.stream.Stream;

public class HandleRequest<T> extends Object implements Runnable {

    private CacheUnitController<T> stringCacheUnitController;
    private Socket socket;
    private DataOutputStream output;
    private InputStream input;
    //JsonObject jsonObject = null;

    public HandleRequest(Socket s, CacheUnitController<T> stringCacheUnitController) {
        //new Thread(this);
        try {
            socket = s;
            output = new DataOutputStream(s.getOutputStream());
            input = s.getInputStream();
            this.stringCacheUnitController = stringCacheUnitController;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            DataInputStream reader = new DataInputStream(new BufferedInputStream(input));
            StringBuilder sb = new StringBuilder();
            String content = "";
            do {
                content = reader.readUTF();
                sb.append(content);
            } while (reader.available() != 0);
            content = sb.toString();
            if (content.equals("statistics")) {
                output.writeUTF(stringCacheUnitController.getStatistics());
                socket.close();
                return;
            } else {
                Type ref = (Type) new TypeToken<Request<DataModel<T>[]>>() {
                }.getType();
                Request<DataModel<T>[]> request = new Gson().fromJson(content, ref);
                String action = request.getHeaders().get("action");
                DataModel<T>[] body = request.getBody();
                switch (action) {
                    case "UPDATE":
                        output.writeUTF(String.valueOf(stringCacheUnitController.update(body)));
                        break;
                    case "GET":
                        output.writeUTF(new Gson().toJson(stringCacheUnitController.get(body)));
                        break;
                    case "DELETE":
                        output.writeUTF(String.valueOf(stringCacheUnitController.delete(body)));
                        break;
                    default:
                        break;
                }
            }

            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
