package client;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import client.dtos.StockOffer;
import client.dtos.StockTransaction;
import clientbroker.ICBroker;
import clientbroker.ICBrokerImpl;
import common.ChanceGenerator;
import common.OfferType;

public class Client implements Runnable{



	// public static void main(String[] args) throws InterruptedException {
	// 	Client c1 = new Client("1",new ICBrokerImpl());
	// 	Client c2 = new Client("2",new ICBrokerImpl());

	// 	new Thread(c1).start();
	// 	new Thread(c2).start();

	// 	Thread.sleep(10000);

	// 	c1.setRunning(false);
	// 	c2.setRunning(false);

	// }


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

	public void transactionCallback(StockTransaction tr){
		if(tr!=null){
			//transaction success
			pendingOffers.removeIf((o)->{return o.getID() == tr.getOfferID();});
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

		StockOffer offer = new StockOffer(stockID, stockID, null, amount, 0);

		cBroker.addOffer(stockID, offer, (t) -> {transactionCallback(t);});
		return 0;
	}

	public void getStocks(){
		List<String> stocks = cBroker.getStockList();
		printList(stocks);
	}

	private <T> void  printList(List<T> list) {
		System.out.println("list : ");
		if(list == null) return;
		list.forEach((e) -> {System.out.println("\t" + e.toString());});
	}

	public void getBuyOffers(String stockID){
		List<StockOffer> off = cBroker.getStockBuyOffers(stockID);
		printList(off);
	}

	public void getSellOffers(String stockID){
		List<StockOffer> off = cBroker.getStockSellOffers(stockID);
		printList(off);
	}

	public void addOffer(String stockID, int q, double p,OfferType t){
		StockOffer off = new StockOffer("", stockID, t, p, q);
		cBroker.addOffer(stockID, off, (tr) -> {transactionCallback(tr);});
	}

}
