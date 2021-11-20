package common;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class MultiReadSingleWriteQueue<T> {

    private static Logger logger = new Logger(MultiReadSingleWriteQueue.class);

    public static void main(String[] args) {
        MultiReadSingleWriteQueue<Integer> q = new MultiReadSingleWriteQueue<>();
        List<Thread> runnables = new ArrayList<>();

        for(int i = 0 ; i < 10 ; i++){
            runnables.add(new Thread(new WR(q)));
        }
        for(int i = 0 ; i < 100 ; i++){
            runnables.add(new Thread(new RR(q)));
        }
        
        runnables.forEach((t) -> {t.start();});
        
        runnables.forEach((t) -> {try {
            t.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }});

        q.queue.forEach((i)->{logger.log(i+"");});

    }

    private static class WR implements Runnable{
        Random rnd = new Random();
        MultiReadSingleWriteQueue<Integer> q;

        public WR(MultiReadSingleWriteQueue<Integer> q){
            this.q = q;
        }

        @Override
        public void run() {
            for(int i = 0 ; i < 10 ; i++){
                q.add(i);
            }
            
        }
        
        
    }

    private static class RR implements Runnable{
        MultiReadSingleWriteQueue<Integer> q;

        public RR(MultiReadSingleWriteQueue<Integer> q){
            this.q = q;
        }

        @Override
        public void run() {
            for(int i = 0 ; i < 100; i++){
                q.getArray();
            }
            
        }
        
        
    }



    private PriorityQueue<T> queue;
    private ReentrantReadWriteLock rwl;
    private Lock r;
    private Lock w;
    private Lock queueLock;
    private volatile boolean queing = false;
    private Condition qc;
    private Queue<T> writeQueue;


    private void initLock(){
        rwl = new ReentrantReadWriteLock(true);
        r = rwl.readLock();
        w = rwl.writeLock();
        writeQueue = new LinkedList<>();
        queueLock = new ReentrantLock(true);
        qc = queueLock.newCondition();
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
        queueLock.lock();
        try{
            writeQueue.add(e);
            while(queing){
                logger.log("wait");
                qc.await();
            }
            if(writeQueue.isEmpty())
                return;
            queing = true;
        } catch (InterruptedException e1) {
            e1.printStackTrace();
        }finally{
            queueLock.unlock();
        }
        w.lock();
        try{
            queueLock.lock();
            try{
                logger.log("write");
                queue.addAll(writeQueue);
                writeQueue.clear();
                queing = false;
                qc.signalAll();
            }finally{
                queueLock.unlock();
            }
        }finally{
            w.unlock();
        }
    }
    
    public T[] getArray(){
        r.lock();
        try{
            return (T[]) queue.toArray();
        }finally{
            r.unlock();
        }
    }
    
}
