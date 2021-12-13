package client;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

import client.dtos.StockOffer;
import client.dtos.StockTransaction;
import clientbroker.ICBroker;
import common.ChanceGenerator;
import common.Logger;
import common.MultiReadSingleWriteCollection;
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

	private String ID;
	private ICBroker cBroker;
	private MultiReadSingleWriteCollection<StockTransaction> transactionHistory;
	private MultiReadSingleWriteCollection<StockOffer> pendingOffers;
	private Map<String,Integer> ownedStocks;
	private List<String> stocks;
	public double new_p;
	private volatile Boolean running = false;

	

	/**
	 * @param id
	 * @param cBroker
	 * @param running
	 * @param stockID
	 */
	public Client(String id, ICBroker cBroker) {
		this.ID = id;
		this.cBroker = cBroker;
		this.ownedStocks = new ConcurrentHashMap<String,Integer>();
		this.pendingOffers = new MultiReadSingleWriteCollection<StockOffer>( new ArrayList<StockOffer>());
		this.transactionHistory = new MultiReadSingleWriteCollection<StockTransaction>(new ArrayList<StockTransaction>());

	}

	public Client(String id, ICBroker cBroker,Map<String,Integer> ownedStocks) {
		this.ID = id;
		this.cBroker = cBroker;
		this.ownedStocks = new ConcurrentHashMap<String,Integer>(ownedStocks);
		this.pendingOffers = new MultiReadSingleWriteCollection<StockOffer>( new ArrayList<StockOffer>());
		this.transactionHistory = new MultiReadSingleWriteCollection<StockTransaction>(new ArrayList<StockTransaction>());

	}

	@Override
	public void run() {
		synchronized(running){
			running = true;
		}

		while(true){
			synchronized(running){
				if(running == false) break;
			}
			cyclic();
			synchronized(this){
				try {
					this.wait(new Random().nextInt(10000));
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		
		
	}

	private void cyclic() {
		
		// Logger.log(ID,"cyclic " + id);
	
		//read stock list[]
		stocks = cBroker.getStockList();
	
		//decide if will buy new stocks
		if(ChanceGenerator.getChance(5, 10)){
			//get a random stock
			int stockIndex = new Random().nextInt(stocks.size());
			
			if(!haveSellOffer(stocks.get(stockIndex))){
				//get its price
				double stockPrice = cBroker.getStockPrice(stocks.get(stockIndex));
							
				// Logger.log(ID, "Stock " + stocks.get(stockIndex) + " price " + stockPrice);
				if(stockPrice < 0){
					// Logger.log(ID,"There are no offers for  " + stocks.get(stockIndex));
					if(ownedStocks.get(stocks.get(stockIndex)) != null){
						deleteBuyOffers(stocks.get(stockIndex));
					}
				}
				else {
					//TODO: deviate price and no of stocks
					double price = stockPrice;
					int nostocks = 1;

					price += ((new Random().nextDouble() - 0.5)/10) *price;
					
					nostocks = new Random().nextInt(100) + 1;

					Logger.log(ID,"Decided to buy stock " + nostocks+ "X " + stocks.get(stockIndex) + " at price " + price);

					newOffer(stocks.get(stockIndex), price, nostocks, OfferType.BUY);		
				}
			}					
		}
		
		//decide if new sell offer for owned stocks
		if(ChanceGenerator.getChance(5, 10)){
			//get a stock that we own
			if(ownedStocks.size() > 0){
				int stockIndex = new Random().nextInt(ownedStocks.size());
				String stockID = (String) ownedStocks.keySet().toArray()[stockIndex];
				if(!haveBuyOffer(stockID)){
					//get the price of the stock
					double stockPrice = cBroker.getStockPrice(stockID);
					// Logger.log(ID, "Stock " + stockID + " price " + stockPrice);
					double price = stockPrice;
					int nostocks = 1;

					if(stockPrice < 0){
						price = (new Random().nextDouble() + 1) * 100;
					}else{
						price += ((new Random().nextDouble() - 0.5)/10) *price;
					}

					nostocks = new Random().nextInt(ownedStocks.get(stockID)) + 1;

					Logger.log(ID,"Decided to sell stock "+ nostocks+ "X "  + stockID + " at price " + price);
					newOffer(stockID, price, nostocks, OfferType.SELL);
				}
			}
		}

		for(StockOffer o: pendingOffers.getCollection()){

			double price = cBroker.getStockPrice(o.getStockId());

			if((price - o.getPrice()) > price/10){
				modifyOffer(o,price);
			}

		}
	}
	private void deleteBuyOffers(String stockID) {
		List<StockOffer> todeleteo = new ArrayList<>();
		List<String> todelete = new ArrayList<>();

		pendingOffers.getCollection().forEach((o)->{if(o.getStockID() == stockID){ todeleteo.add(o); todelete.add(o.getID());}});

		if(cBroker.deleteOffer(todelete,stockID))
			pendingOffers.delete(todeleteo.toArray(new StockOffer[0]));
	}

	private boolean val;
	private boolean haveBuyOffer(String stockID) {
		val = false;
		pendingOffers.getCollection().forEach((o)->{
			if(o.getType() == OfferType.BUY && o.getStockID().equals(stockID)) 
			val = true;
		});
		return val;
	}

	private boolean haveSellOffer(String stockID) {
		val = false;
		pendingOffers.getCollection().forEach((o)->{
			if(o.getType() == OfferType.SELL && o.getStockID().equals(stockID)) 
			val = true;
		});
		return val;
	}

	private void modifyOffer(StockOffer o, double price) {
		
		price += ((new Random().nextDouble() - 0.5)/10) *price;
		StockOffer no = new StockOffer(o.getID(),ID,o.getStockID(),o.getType(),price,o.getQuantity());
		cBroker.modifyOffer(o.getID(), no, this.new OfferCallback(no));
		pendingOffers.delete(o);
		pendingOffers.add(no);
		Logger.log(ID,"Decided to modify offer " + o + "with " + no);
		

	}

	/**
	 * @param running the running to set
	 */
	public void setRunning(boolean running) {
		synchronized(this.running){
			this.running = running;
		}
	}
	
	private StockOffer newOffer(String stockID, double price, int amount, OfferType type){
		
		if(type == OfferType.SELL){
			ownedStocks.computeIfPresent(stockID,(id,val)->{
				if(val - amount > 0)
					return val - amount;
				return null;
			});
		}
		
		StockOffer offer = new StockOffer("",ID, stockID, type, price, amount);
		
		cBroker.addOffer(stockID, offer, this.new OfferCallback(offer));
		
		pendingOffers.add(offer);
		
		return offer;
	}

	public void getStocks(){
		List<String> stocks = cBroker.getStockList();
		printList(stocks);
	}

	private <T> void  printList(List<T> list) {
		Logger.log(ID,"list : ");
		if(list == null) return;
		list.forEach((e) -> {Logger.log(ID,"\t" + e.toString());});
	}

	public void getBuyOffers(String stockID){
		List<StockOffer> off = cBroker.getStockBuyOffers(stockID);
		printList(off);
	}

	public void getSellOffers(String stockID){
		List<StockOffer> off = cBroker.getStockSellOffers(stockID);
		printList(off);
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
			Logger.log(ID, "Transaction " + tr+ " for offer " + offer);
			if(tr!=null){
				pendingOffers.delete(offer);
				if(tr.getNewOfferID() != null){
					StockOffer nOffer = new StockOffer(tr.getNewOfferID(),ID,offer.getStockID(),offer.getType(),offer.getPrice(),offer.getQuantity() - tr.getQuantity());
					pendingOffers.add(nOffer);
				}
				if(offer.getType() == OfferType.BUY){
					Integer sc = ownedStocks.get(offer.getStockID());
					int c =  tr.getQuantity();
					if(sc != null)
						c += sc;
					ownedStocks.put(offer.getStockID(),c);
				}
				transactionHistory.add(tr);
			}else{
	
				pendingOffers.delete(offer);
			}

		}
	
		
	}

	public void printInfo() {
		Logger.log(ID, "\n\nInfo for " + ID);

		Logger.log(ID, "\tOwned stocks :");
		ownedStocks.forEach((s,c)->{Logger.log(ID,"\t\t"+ s + " : " + c);});
		Logger.log(ID, "\tPending offers:");
		pendingOffers.getCollection().forEach((o)->{Logger.log(ID,"\t\t" + o);});
		Logger.log(ID, "\tTransaction history:");
		transactionHistory.getCollection().forEach((t)->{Logger.log(ID, "\t\t" + t);});


	}
}
