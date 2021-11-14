package common;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class MultiReadSingleWriteList<T> {

    private List<T> list;
    private ReentrantReadWriteLock rwl;
    private Lock r;
    private Lock w;

    private void initLock(){
        rwl = new ReentrantReadWriteLock(true);
        r = rwl.readLock();
        w = rwl.writeLock();
    }

    public MultiReadSingleWriteList(List<T> ls){
        this.list = ls;
        initLock();
    }

    public MultiReadSingleWriteList(Collection<T> e){
        list = new ArrayList<T>(e);
        initLock();
    }

    public MultiReadSingleWriteList(T...elm){
        list = new ArrayList<T>();
        list.addAll(Arrays.asList(elm));
        initLock();
    }
    
    public MultiReadSingleWriteList(){
        list = new ArrayList<T>();
        initLock();
    }

    public T add(T elm){

        w.lock();
        try{
            if(list.add(elm))
                return elm;
            else
                return null;
        }finally{
            w.unlock();
        }
    }

    public T get(int index){

        r.lock();
        try{
            return list.get(index);
        }finally{
            r.unlock();
        }
    }

    public int size(){
        r.lock();
        try{
            return list.size();
        }finally{
            r.unlock();
        }
    }



    
}
