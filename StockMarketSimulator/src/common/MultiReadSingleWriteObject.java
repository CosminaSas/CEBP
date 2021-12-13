package common;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class MultiReadSingleWriteObject <T>{
	private ReentrantReadWriteLock rwl;
    private Lock r;
    private Lock w;
	private T wrappedObject;

	private void initLock(){
        rwl = new ReentrantReadWriteLock(true);
        r = rwl.readLock();
        w = rwl.writeLock();
    }

	/**
	 * @param wrappedObject
	 */
	public MultiReadSingleWriteObject(T wrappedObject) {
		initLock();
		this.wrappedObject = wrappedObject;
	}

	public void put(T val){
		w.lock();
		try{
			wrappedObject = val;
		}finally{
			w.unlock();
		}
	}
	
	public T get(){
		r.lock();
		try{
			return wrappedObject;
		}finally{
			r.unlock();
		}
	}


}
