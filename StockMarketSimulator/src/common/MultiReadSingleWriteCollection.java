package common;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class MultiReadSingleWriteCollection<T> {

    // public static void main(String[] args) {
    //     // MultiReadSingleWriteCollection<Integer> q = new MultiReadSingleWriteCollection<Integer> (new PriorityQueue<Integer>());
    //     // List<Thread> runnables = new ArrayList<>();

    //     // for(int i = 0 ; i < 10 ; i++){
    //     //     runnables.add(new Thread(new WR(q)));
    //     // }
    //     // for(int i = 0 ; i < 100 ; i++){
    //     //     runnables.add(new Thread(new RR(q)));
    //     // }
        
    //     // runnables.forEach((t) -> {t.start();});
        
    //     // runnables.forEach((t) -> {try {
    //     //     t.join();
    //     // } catch (InterruptedException e) {
    //     //     e.printStackTrace();
    //     // }});

    //     // q.collection.forEach((i)->{Logger.log(null,i+"");});

    //     // MultiReadSingleWriteCollection<Offer> q = new MultiReadSingleWriteCollection<>(new ArrayList<>());
    //     // Offer o = new Offer("clientID", "stockID", 1, 1, OfferType.SELL, null);
    //     // Offer o2 = new Offer("clientID", "stockID", 1, 1, OfferType.SELL, null);
    //     // q.add(o);
    //     // q.add(o2);
    //     // q.getCollection().forEach((os)->{Logger.log("test", os.toString());});

    //     // q.delete(new Offer[]{Offer.getOfferForCompare(o.getID()),Offer.getOfferForCompare(o2.getID())});

    //     // q.getCollection().forEach((os)->{Logger.log("test", os.toString());});

    // }

    // private static class WR implements Runnable{
    //     Random rnd = new Random();
    //     MultiReadSingleWriteCollection<Integer> q;

    //     public WR(MultiReadSingleWriteCollection<Integer> q){
    //         this.q = q;
    //     }

    //     @Override
    //     public void run() {
    //         for(int i = 0 ; i < 10 ; i++){
    //             q.add(i);
    //         }
            
    //     }
        
        
    // }

    // private static class RR implements Runnable{
    //     MultiReadSingleWriteCollection<Integer> q;

    //     public RR(MultiReadSingleWriteCollection<Integer> q){
    //         this.q = q;
    //     }

    //     @Override
    //     public void run() {
    //         for(int i = 0 ; i < 100; i++){
    //             q.getCollection();
    //         }
            
    //     }
        
        
    // }



    private Collection<T> collection;
    private ReentrantReadWriteLock rwl;
    private Lock r;
    private Lock w;
    private Lock queueLock;
    private volatile boolean queing = false;
    private Condition qc;
    private Queue<T> writeQueue;
    private Collection<T> collectionRep = Collections.unmodifiableCollection(Collections.emptyList());


    private void initLock(){
        rwl = new ReentrantReadWriteLock(true);
        r = rwl.readLock();
        w = rwl.writeLock();
        writeQueue = new LinkedList<>();
        queueLock = new ReentrantLock(true);
        qc = queueLock.newCondition();
    }

    private static <T> Collection<T> getUnmodifiableCopy(Collection<T> original){
        return Collections.unmodifiableCollection(new ArrayList<T>(original));
    }

    public MultiReadSingleWriteCollection(Collection<T> ls){
        this.collection = ls;
        this.collectionRep = getUnmodifiableCopy(ls);
        initLock();
    }

    public boolean contains(T elm){
        r.lock();
        try{
            return collection.contains(elm);
        }finally{
            r.unlock();
        }
    }


    public void delete(T ...elms){
        w.lock();
        try{
            collection.removeAll(Arrays.asList(elms));
            collectionRep = getUnmodifiableCopy(collection);
        }finally{
            w.unlock();
        }
    }
    
    public void add(T e){
        queueLock.lock();
        try{
            writeQueue.add(e);
            while(queing){
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
                collection.addAll(writeQueue);
                collectionRep = getUnmodifiableCopy(collection);
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
    
    public Collection<T> getCollection(){
        r.lock();
        try{
            return collectionRep;
        }finally{
            r.unlock();
        }
    }
    
}
