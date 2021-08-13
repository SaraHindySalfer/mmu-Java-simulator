package main.java.com.company.hit.dao;

import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import main.java.com.company.hit.dm.DataModel;
import com.google.gson.Gson;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.*;
import java.lang.reflect.Type;
import java.util.*;

/**
 * this class simulates the hard disk as a json file
 *
 * @param <T> value type
 */
public class DaoFileImpl<T> extends Object implements IDao<Long, DataModel<T>> {
    private String filePath;
    private Long LastKey = 0L;
    private int capacity;
    private ArrayList<DataModel<T>> buffer;

    private void readerFile(String filePath) {

        Gson gson = new Gson();
        Type listType = new TypeToken<ArrayList<DataModel<T>>>() {
        }.getType();
        ArrayList<DataModel<T>> listArray;
        buffer = new ArrayList<DataModel<T>>(capacity);
        try {
            listArray = gson.fromJson(new FileReader(filePath), listType);
            if (listArray != null) {
                buffer = listArray;
            }
        } catch (FileNotFoundException e) {
            buffer = new ArrayList<DataModel<T>>(capacity);
        } catch (JsonSyntaxException e) {
            System.out.println("Problem with reading file: " + e.getMessage());
        }
    }

    private void writeFile(String filePath, ArrayList<DataModel<T>> buffer) {
        Gson gson = new Gson();
        try {
            FileWriter fileWriter = new FileWriter(filePath);
            gson.toJson(buffer, fileWriter);
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public DaoFileImpl(String filePath) {
        this.filePath = filePath;
        this.capacity = 1024;
        readerFile(filePath);
    }


    public DaoFileImpl(String filePath, int capacity) {

        this.filePath = filePath;
        this.capacity = capacity;
        readerFile(filePath);
    }

    @Override
    public void save(DataModel<T> t) {
        for (int i = 0; i < buffer.size(); i++) {
            if (buffer.get(i).getDataModelId().equals(t.getDataModelId())) {
                buffer.remove(i);
            }
        }
        buffer.add(t);
        writeFile(filePath, buffer);

    }

    @Override
    public void delete(DataModel<T> t) throws IllegalArgumentException {
        if (t == null) {
            throw new IllegalArgumentException("entity can not be null");
        }
        readerFile(filePath);
        buffer.remove(t);
        writeFile(filePath, buffer);
    }

    @Override
    public DataModel<T> find(Long id) throws IllegalArgumentException {
        if (id == null) {
            throw new IllegalArgumentException("entity can not be null");
        }
        for (DataModel<T> dm : buffer) {
            if (dm.getDataModelId().equals(String.valueOf(id))) return dm;
        }

        return null;
    }
}
