package client;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import client.dtos.StockOffer;
import client.dtos.StockTransaction;
import clientbroker.ICBroker;
import clientbroker.ICBrokerImpl;
import common.ChanceGenerator;

public class Client implements Runnable{



	public static void main(String[] args) throws InterruptedException {
		Client c1 = new Client("1",new ICBrokerImpl());
		Client c2 = new Client("2",new ICBrokerImpl());

		new Thread(c1).start();
		new Thread(c2).start();

		Thread.sleep(10000);

		c1.setRunning(false);
		c2.setRunning(false);

	}


	private String id;
	private ICBroker cBroker;
	private List<StockTransaction> transactionHistory;
	private List<StockOffer> pendingOffers;
	private Map<String,Double> ownedStocks;

	private volatile boolean running;

	

	/**
	 * @param id
	 * @param cBroker
	 * @param running
	 */
	public Client(String id, ICBroker cBroker) {
		this.id = id;
		this.cBroker = cBroker;

		this.transactionHistory = new ArrayList<StockTransaction>();

	}

	@Override
	public void run() {
		running = true;

		while(running){
			cyclic();
			synchronized(this){
				try {
					this.wait(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		
		
	}

	private void cyclic() {
		
		System.out.println("cyclic " + id);
		if(new ChanceGenerator().getChance(5, 10)){
			newOffer(id, 0, 0);
		}
		//read stock list
		//decide if new buy offer
		//	* get sell offer list
		//decide if new sell offer for owned stocks
		//	* get buy offer list
		//decide if offer modification is necesarry

	}

	public void transactionCallback(boolean trSucc, StockTransaction tr){
		if(trSucc){
			//transaction success
			pendingOffers.removeIf((o)->{return o.getId() == tr.getOfferId();});
			transactionHistory.add(tr);
			// add to owned stocks if we bought
		}else{
			//offer creation failed
		}
		System.out.println("tr " + id);
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
	
	private int newOffer(String stockID, double price, double amount){

		StockOffer offer = new StockOffer();

		cBroker.addOffer(stockID, offer, (Boolean s,StockTransaction t) -> {transactionCallback(s,t);});
		return 0;
	}

}
