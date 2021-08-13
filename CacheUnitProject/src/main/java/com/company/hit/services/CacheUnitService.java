package main.java.com.company.hit.services;

import main.java.com.company.hit.dao.DaoFileImpl;
import main.java.com.company.hit.dao.IDao;
import main.java.com.company.hit.dm.DataModel;
import main.java.com.company.hit.memory.CacheUnit;
import main.java.com.hit.algorithm.IAlgoCache;
import main.java.com.hit.algorithm.LRUAlgoCacheImpl;

import java.lang.*;
import java.util.Arrays;
import java.util.Map;

public class CacheUnitService<T> extends Object {
    int capacity = 100;
    private CacheUnit<T> cacheUnit;
    //private IDao<Long, DataModel<T>> iDao;
    private LRUAlgoCacheImpl algorithm = new LRUAlgoCacheImpl<>(capacity);
    private final DaoFileImpl<T> iDao;
    private static  int requests=0;
    private static int dmRequests=0;
    public CacheUnitService() {
        cacheUnit = new CacheUnit<T>(algorithm);
        iDao = new DaoFileImpl<>("src/main/resources/file.json", capacity);
    }

    public boolean delete(DataModel<T>[] dataModels) {
        requests++;
        dmRequests+= dataModels.length;
        try {
            for (int i = 0; i < dataModels.length; i++) {
                iDao.delete(dataModels[i]);
            }
            Long[] ids = new Long[dataModels.length];
            for (int i = 0; i < ids.length; i++) {
                ids[i] = dataModels[i].getDataModelId();
            }
            DataModel<T>[] models = new DataModel[dataModels.length];
            models = cacheUnit.getDataModels(ids);
            for (int i = 0; i < dataModels.length; i++) {
                if (models[i] != null) {
                    cacheUnit.removeDataModels(ids);
                }
            }

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }

    public DataModel<T>[] get(DataModel<T>[] dataModels) {
        requests++;
        dmRequests+=dataModels.length;
        Long[] ids = new Long[dataModels.length];
        for (int i = 0; i < ids.length; i++) {
            ids[i] = dataModels[i].getDataModelId();
        }
        DataModel<T>[] models = new DataModel[dataModels.length];
        models = cacheUnit.getDataModels(ids);
        for (int i = 0; i < dataModels.length; i++) {
            if (models[i] == null) {
                models[i] = iDao.find(dataModels[i].getDataModelId());
            }
        }
        return models;
    }

    public boolean update(DataModel<T>[] dataModels) {
        requests++;
        dmRequests+=dataModels.length;
        DataModel<T>[] dataModelTemp = cacheUnit.putDataModels(dataModels);
        System.out.println(dataModelTemp[0] == null);
        for (DataModel<T> elem : dataModelTemp) { if (elem != null) {
                iDao.save(elem);
            }
        }
        return true;
    }

    public Boolean stopServer() {
        try {
            Map<Long, DataModel<T>> cache = cacheUnit.getAllModels();
            for (Map.Entry<Long, DataModel<T>> entry : cache.entrySet()) {
                iDao.save((DataModel<T>) entry.getValue());
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public String showStatistics() {
        return "capacity: " + capacity + "\n"
                + "algorithm: LRU"  + "\n"
                +"Total number of requests: "+requests+"\n"
                +"Total number of DataModels(GET/DELETE/UPDATE) requests: "+dmRequests;
    }
}

