package main.java.com.company.hit.services;

import main.java.com.company.hit.dm.DataModel;

import java.lang.*;

public class CacheUnitController<T> extends Object implements Runnable {
    CacheUnitService<T> service = new CacheUnitService<T>();

    public CacheUnitController() {

    }

    public boolean update(DataModel<T>[] dataModels) {
        System.out.println("controller: "+dataModels[0].toString());
        return service.update(dataModels);
    }

    public boolean delete(DataModel<T>[] dataModels) {
        return service.delete(dataModels);
    }

    public DataModel<T>[] get(DataModel<T>[] dataModels) {
        return service.get(dataModels);
    }

    public Boolean stopServer() {
        System.out.println("stop server");
        return service.stopServer();
    }
    public String getStatistics(){return service.showStatistics();}
    @Override
    public void run() {

    }
}
