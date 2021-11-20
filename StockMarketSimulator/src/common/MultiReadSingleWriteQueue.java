package common;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Random;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class MultiReadSingleWriteQueue<T> {

    private static Logger logger = new Logger(MultiReadSingleWriteQueue.class);

    public static void main(String[] args) {
        MultiReadSingleWriteQueue<Integer> q = new MultiReadSingleWriteQueue<>();
        Random rnd = new Random();
        new Thread(()->{for(int i = 0 ; i < 10 ; i++){
            q.getArray();
            try {
                Thread.sleep(rnd.nextInt(10));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }}).start();
        new Thread(()->{for(int i = 0 ; i < 10 ; i++){
                q.add(i);
                try {
                    Thread.sleep(rnd.nextInt(10));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
        }}).start();
        new Thread(()->{for(int i = 0 ; i < 10 ; i++){
            q.getArray();
            try {
                Thread.sleep(rnd.nextInt(10));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }}).start();
        new Thread(()->{for(int i = 0 ; i < 10 ; i++){
            q.add(i);
            try {
                Thread.sleep(rnd.nextInt(10));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }}).start();
        new Thread(()->{for(int i = 0 ; i < 10 ; i++){
            q.getArray();
            try {
                Thread.sleep(rnd.nextInt(10));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }}).start();
        


    }


    private PriorityQueue<T> queue;
    private ReentrantReadWriteLock rwl;
    private Lock r;
    private Lock w;

    private void initLock(){
        rwl = new ReentrantReadWriteLock(true);
        r = rwl.readLock();
        w = rwl.writeLock();
    }

    public MultiReadSingleWriteQueue(PriorityQueue<T> ls){
        this.queue = ls;
        initLock();
    }

    public MultiReadSingleWriteQueue(Collection<T> e){
        queue = new PriorityQueue<T>(e);
        initLock();
    }

    public MultiReadSingleWriteQueue(T...elm){
        queue = new PriorityQueue<T>();
        queue.addAll(Arrays.asList(elm));
        initLock();
    }
    
    public MultiReadSingleWriteQueue(){
        queue = new PriorityQueue<T>();
        initLock();
    }

    public void add(T e){
        logger.log("write wait " + e);
        w.lock();
        logger.log("writing " + e);
        try{
            queue.add(e);
        }finally{
            w.unlock();
        }

    }
    
    public T[] getArray(){
        logger.log("read wait");
        r.lock();
        logger.log("reading");
        try{
            return (T[]) queue.toArray();
        }finally{
            r.unlock();
        }
    }
    
}
