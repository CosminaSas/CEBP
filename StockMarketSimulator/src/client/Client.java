package client;

import java.security.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;
import java.util.Random;

import client.dtos.StockOffer;
import client.dtos.StockTransaction;
import clientbroker.ICBroker;
import common.ChanceGenerator;
import common.MultiReadSingleWriteCollection;
import common.OfferType;
import stock.Stock;
import clientbroker.*;
import stock.dtos.Transaction;

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


	private static final String stockID = null;
	private String id;
	private ICBroker cBroker;
	private MultiReadSingleWriteCollection<StockTransaction> transactionHistory;
	private MultiReadSingleWriteCollection<StockOffer> pendingOffers;
	private Map<String,Integer> ownedStocks;
	private List<String> stocks;
	private List<ICBrokerImpl> sellOffer;
	private List<StockOffer> decideBuy;
	public double new_p;
	private volatile boolean running;

	

	/**
	 * @param id
	 * @param cBroker
	 * @param running
	 * @param stockID
	 */
	public Client(String id, ICBroker cBroker) {
		this.id = id;
		this.cBroker = cBroker;
		this.ownedStocks = new ConcurrentHashMap<String,Integer>();
		this.pendingOffers = new MultiReadSingleWriteCollection<StockOffer>( new ArrayList<StockOffer>());
		this.transactionHistory = new MultiReadSingleWriteCollection<StockTransaction>(new ArrayList<StockTransaction>());

	}

	public Client(String id, ICBroker cBroker,Map<String,Integer> ownedStocks) {
		this.id = id;
		this.cBroker = cBroker;
		this.ownedStocks = new ConcurrentHashMap<String,Integer>(ownedStocks);
		this.pendingOffers = new MultiReadSingleWriteCollection<StockOffer>( new ArrayList<StockOffer>());
		this.transactionHistory = new MultiReadSingleWriteCollection<StockTransaction>(new ArrayList<StockTransaction>());

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
	
		//read stock list[]
		stocks = cBroker.getStockList();
	
		//decide if new buy offer
		//	* get sell offer list
		if(new ChanceGenerator().getChance(5, 10)){
			int stockIndex = new Random().nextInt(stocks.size());
			double stockPrice = cBroker.getStockPrice(stocks.get(stockIndex));
			if(stockPrice < 0){
				System.out.println("There are no offers for  " + stockIndex);
				return;
			}
			else {
			newOffer(stocks.get(stockIndex), stockPrice, 1, OfferType.BUY);		
			System.out.println("Decided to buy stock " + stocks.get(stockIndex) + " at price " + stockPrice);
			}						
		}
		
		//decide if new sell offer for owned stocks
		//	* get buy offer list
		if(new ChanceGenerator().getChance(5, 10)){
			int stockIndex = new Random().nextInt(ownedStocks.size());
			double stockPrice = cBroker.getStockPrice((String) ownedStocks.keySet().toArray()[stockIndex]);
			newOffer(stocks.get(stockIndex), stockPrice, 1, OfferType.BUY);
			System.out.println("Decided to sell stock " + ownedStocks.get(stockIndex) + " at price " + stockPrice);
		}
		//if(cBroker.getStockPrice(stocks.get(stockIndex)) < 0){
//
		//	System.out.println("There are no offers for  " + stockIndex);
		//	param.setPrice = random()
		//	return;
		//}

		//decide if offer modification is necesarry
		// //if(new ChanceGenerator().getChance(5, 10)){
		// 	LocalDateTime tr = Transaction.getTimestamp()

		// 	System.out.println("Decided to modify stock ... ");
		// }

		// for(StockOffer o: pendingOffers){

		// 	double price = cBroker.getStockPrice(o.getStockId());


		// 	if((price - o.getPrice()) > price/10){
		// 		modifyOffer(o,price);
		// 	}


		// }


		//for(StockOffer o : pendingOffers){
			 //modify => delete
		// if(new ChanceGenerator().getChance(5, 10)){

		// 	 int stockIndex = new Random().nextInt(ownedStocks.size());

		// 	 if ((o.getPrice() - ((StockOffer) pendingOffers).getPrice()) > 110/100 ) {
		// 		modifyOffer(stocks.get(stockIndex), o);
		// 	}
		// }
			
		// }
		for(StockOffer o: pendingOffers.getCollection()){

			double price = cBroker.getStockPrice(o.getStockId());


			if((price - o.getPrice()) > price/10){
				modifyOffer(o,price);
			}

		}
	}

	private void modifyOffer(StockOffer o, double price) {
		
		pendingOffers.delete(o);
		StockOffer no = newOffer(o.getStockID(), (price + price*0.1)  , o.getQuantity(), null);
		cBroker.modifyOffer(o.getID(), no, this.new OfferCallback(no));

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
	
	private StockOffer newOffer(String stockID, double price, int amount, OfferType type){

		StockOffer offer = new StockOffer(stockID, stockID, type, price, amount);
		offer.setClientID(id);
		cBroker.addOffer(stockID, offer, this.new OfferCallback(offer));
		pendingOffers.add(offer);
		
		return offer;

		//return 0;
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
		pendingOffers.add(off);
		cBroker.addOffer(stockID, off, this.new OfferCallback(off));
	}

	private class OfferCallback implements Consumer<StockTransaction>{

		private StockOffer offer;
		// private List<StockOffer> pendingOffers;
		/**
		 * @param offer
		 */
		public OfferCallback(StockOffer offer) {
			this.offer = offer;
			// this.pendingOffers = pendingOffers;
		}
		
		@Override
		public void accept(StockTransaction tr) {
			if(tr!=null){
				if(tr.getNewOfferID() != null){
					offer.setID(tr.getNewOfferID());
					offer.setQuantity(offer.getQuantity() - tr.getQuantity());
				}else{
					pendingOffers.delete(offer);
				}
				if(offer.getType() == OfferType.BUY){
					ownedStocks.put(offer.getStockID(), tr.getQuantity());
				}
				transactionHistory.add(tr);
			}else{
	
				pendingOffers.delete(offer);
			}

		}
	
		
	}
	

	// public void modifyOffer(StockOffer oldOffer, StockOffer newOffer){
		
	// 			for (StockOffer offer : pendingOffers) {
	// 				if (offer.getID() == oldOffer.getID()) {
	// 					pendingOffers.remove(oldOffer);
	// 					pendingOffers.add(newOffer);
	// 					double price = oldOffer.getPrice() * 110 /100;
	// 					newOffer.setPrice(price);
	// 				}
	// 			}
	// }

	

}
