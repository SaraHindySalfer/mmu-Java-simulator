package main.test;

import com.google.gson.Gson;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import main.java.com.company.hit.server.Request;
import main.java.com.hit.algorithm.IAlgoCache;
import main.java.com.hit.algorithm.LRUAlgoCacheImpl;
import main.java.com.hit.algorithm.MFUAlgoCacheImpl;
import main.java.com.company.hit.dao.DaoFileImpl;
import main.java.com.company.hit.dm.DataModel;
import main.java.com.company.hit.memory.CacheUnit;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.*;
import java.lang.reflect.Type;
import java.net.Socket;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Asserting the correctness of cache unit and daoFile algorithms.
 */
/*
public class CacheUnitTest {
    private static DataModel<String> dataModel;
    private static DataModel<String> dataModel2;
    private static DataModel<String> dataModel3;
    private static DataModel<String> dataModel4;
    private static DaoFileImpl<String> daoFile;
    private static CacheUnit<String> cacheUnit;
    private static MFUAlgoCacheImpl mfu;

    @BeforeClass
    public static void startTest() throws Exception {
        daoFile = new DaoFileImpl("src/main/resources/Data.json");
        mfu = new MFUAlgoCacheImpl<Long, DataModel<String>>(20);
        cacheUnit = new CacheUnit((IAlgoCache<Long, DataModel>) mfu);
        dataModel = new DataModel(25L, "א");
        daoFile.save(dataModel);
        dataModel2 = new DataModel(26L, "ב");
        dataModel3 = new DataModel(7L, "ג");
        dataModel4 = new DataModel(8L, "ד");

    }

    @Test
    public void daoFileTest() throws Exception {
        Assert.assertNull(daoFile.find(18L));
        daoFile.save(dataModel);
        Assert.assertEquals(dataModel, daoFile.find(25L));
        daoFile.save(dataModel2);
        Assert.assertEquals(dataModel2, daoFile.find(26L));
        daoFile.delete(dataModel);
        Assert.assertNull(daoFile.find(25L));
    }

    @Test
    public void cacheUnitTest() {
        Long[] ids = new Long[15];
        DataModel[] dataModels = new DataModel[15];
        for (int i = 0; i < 15; i++) {
            ids[i] = Long.valueOf(i);
            dataModels[i] = new DataModel(Long.valueOf(i), i);
        }
        cacheUnit.putDataModels(dataModels);
        Assert.assertEquals(cacheUnit.getDataModels(ids)[0], dataModels[0]);
        cacheUnit.removeDataModels(ids);
        Assert.assertNotEquals(cacheUnit.getDataModels(ids)[0], dataModels[0]);
    }
}
*/

public class CacheUnitTest {


    @Test
    public void testGet() {
        Map<String, String> headerReq = new HashMap<>();
        headerReq.put("action", "GET");

        DataModel<String>[] dmArray = new DataModel[]{new DataModel<String>(1L, "bb"), new DataModel<String>(2L, "aa")};
        Request<DataModel<String>[]> req = new Request<>(headerReq, dmArray);

        Gson gson = new Gson();
        try {
            Socket socket = new Socket("127.0.0.1", 12345);
            DataOutputStream writer = new DataOutputStream(socket.getOutputStream());
            writer.writeUTF(gson.toJson(req));
            writer.flush();

            DataInputStream in = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String content = "";

            do {
                content = in.readUTF();
                sb.append(content);
            } while (in.available() != 0);

            content = sb.toString();
            try {
                JsonParser parser = new JsonParser();
                parser.parse(content);
            } catch (JsonSyntaxException e) {
                System.out.println(content);
                return;
            }
            Type requestType = new TypeToken<DataModel<String>[]>() {
            }.getType();
            DataModel<String>[] response = new Gson().fromJson(content, requestType);
            System.out.println("message from server: " + Arrays.toString(response));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Test
    public void testDelete() {
        Map<String, String> headerReq = new HashMap<>();
        headerReq.put("action", "DELETE");

        DataModel<String>[] dmArray = new DataModel[]{new DataModel<String>(1L, "b")};
        Request<DataModel<String>[]> req = new Request<>(headerReq, dmArray);

        Gson gson = new Gson();
        try {
            Socket socket = new Socket("127.0.0.1", 12345);
            DataOutputStream writer = new DataOutputStream(socket.getOutputStream());
            writer.writeUTF(gson.toJson(req));
            writer.flush();

            DataInputStream in = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String content = "";

            do {
                content = in.readUTF();
                sb.append(content);
            } while (in.available() != 0);

            content = sb.toString();
            try {
                JsonParser parser = new JsonParser();
                parser.parse(content);
            } catch (JsonSyntaxException e) {
                System.out.println(content);
                return;
            }
            Boolean response = true;
            response = new Gson().fromJson(content, response.getClass());
            System.out.println("message from server: " + response);


        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    @Test
    public void testC() {

        Map<String, String> headerReq = new HashMap<>();
        headerReq.put("action", "GET");
        //System.out.println(headerReq);
        DataModel<String>[] dmArray = new DataModel[]{new DataModel<String>(7L, "555"), new DataModel<String>(9L, "ayala")};
        Request<DataModel<String>[]> req = new Request<>(headerReq, dmArray);

        Gson gson = new Gson();
        try {
            System.out.println("test1");
            Socket socket = new Socket("127.0.0.1", 12345);
            System.out.println("test2");
            DataOutputStream writer = new DataOutputStream(socket.getOutputStream());
            writer.writeUTF(gson.toJson(req));
            writer.flush();
            DataInputStream input = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String content = "";
            do {
                content = input.readUTF();
                System.out.println(content);
                sb.append(content);
                System.out.println(input.available());
            }
            while (input.available() != 0);
            content = sb.toString();
            System.out.println(content + "\nconterrr");
            String response = "true";
            Type requestType = new TypeToken<DataModel<String>[]>() {
            }.getType();
            DataModel<String>[] request = new Gson().fromJson(content, requestType);
            //response = new Gson().fromJson(content);
            System.out.println("message from server: " + Arrays.toString(request));


        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}