package main.java.com.company.hit.memory;

import main.java.com.company.hit.dm.DataModel;
import main.java.com.hit.algorithm.IAlgoCache;

import java.lang.Long;
import java.util.Map;

/**
 * this class uses the memory pages according to IAlgoCache algorithms
 *
 * @param <T> page value type
 */
public class CacheUnit<T> extends java.lang.Object {

    private IAlgoCache<Long, DataModel<T>> algo;
    //IDao<Long,DataModel<T>> iDao;

    public CacheUnit(IAlgoCache<Long, DataModel<T>> algo) {
        this.algo = algo;
    }

    public DataModel<T>[] getDataModels(Long[] ids) {
        DataModel<T>[] DataModels = new DataModel[ids.length];
        for (Integer i = 0; i < ids.length; i++) {
            DataModels[i] = algo.getElement(ids[i]);
        }
        return DataModels;
    }

    public DataModel<T>[] putDataModels(DataModel<T>[] dataModels) {
        DataModel<T>[] DataModels = new DataModel[dataModels.length];
        System.out.println("length: "+dataModels.length);
        for (int i = 0; i < DataModels.length; i++) {
            DataModels[i] = algo.putElement(dataModels[i].getDataModelId(), dataModels[i]);

        }
        return DataModels;
    }

    public void removeDataModels(Long[] ids) {
        for (int i = 0; i < ids.length; i++) {
            algo.removeElement(ids[i]);
        }
    }

    public Map<Long, DataModel<T>> getAllModels() {
        return algo.getAllElements();
    }
}
