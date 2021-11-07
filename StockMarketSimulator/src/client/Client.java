package client;

import client.dtos.StockTransaction;

public class Client implements Runnable{

	private volatile boolean running;

	@Override
	public void run() {
		running = true;

		while(running){
			cyclic();
		}
		
	}

	private void cyclic() {
	}

	public void transactionCallback(boolean trSucc, StockTransaction tr){

	}

	
	/**
	 * @return the running
	 */
	public boolean isRunning() {
		return running;
	}

	/**
	 * @param running the running to set
	 */
	public void setRunning(boolean running) {
		this.running = running;
	}
	
}
