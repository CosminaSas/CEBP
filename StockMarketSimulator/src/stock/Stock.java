package stock;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.PriorityQueue;
import java.util.function.BiConsumer;

import broker.IBroker;
import broker.IBrokerImpl;
import common.Logger;
import common.MultiReadSingleWriteCollection;
import common.MultiReadSingleWriteObject;
import common.OfferType;
import stock.dtos.Offer;
import stock.dtos.Transaction;

public class Stock implements Runnable{

	// testing purpose
	// private static final BiConsumer<String, Transaction> callback = new MockConsumer();
	private static final Offer[] oarr = new Offer[0];
	private static final Transaction[] tarr = new Transaction[0];


	private volatile Boolean running = false;
	private String ID;
	private MultiReadSingleWriteCollection<Transaction> transactionHistory = new MultiReadSingleWriteCollection<Transaction>(
			new ArrayList<Transaction>());
	private MultiReadSingleWriteCollection<Offer> offers = new MultiReadSingleWriteCollection<Offer>(
			new ArrayList<Offer>());
	private IBroker broker;
	private MultiReadSingleWriteObject<Offer> minSell = new MultiReadSingleWriteObject<Offer>(null);

	/**
	 * @param iD
	 */
	public Stock(String iD) {
		ID = iD;
	}

	// Getters & Setters

	public String getID() {
		return ID;
	}

	public Collection<Offer> getOffers() {
		Offer[] os = offers.getCollection().toArray(oarr);
		Arrays.sort(os);
		return Arrays.asList(os);
	}

	public Collection<Transaction> getTransactionHistory() {
		Transaction[] ts = transactionHistory.getCollection().toArray(tarr);
		Arrays.sort(ts);
		return Arrays.asList(ts);
	}

	public IBroker getBroker() {
		return broker;
	}

	public void setBroker(IBroker broker) {
		this.broker = broker;
	}

	// Methods

	public Offer makeTransaction(Offer sellOffer, Offer buyOffer) {

		// delete buy offer
		offers.delete(buyOffer,sellOffer);

		if(sellOffer == minSell.get())
			minSell.put(getMin(offers.getCollection()));

		// create transaction
		Transaction transaction = new Transaction(sellOffer, buyOffer, this.ID,buyOffer.getPrice(),Math.min(sellOffer.getQuantity(), buyOffer.getQuantity()));
		int qdiff = buyOffer.getQuantity() - sellOffer.getQuantity();

		Offer nOffer = null;

		if(qdiff > 0){
			nOffer = buyOffer.copy(qdiff);
			offers.add(nOffer);
		}
		if(qdiff < 0){
			nOffer = sellOffer.copy(-qdiff);
			offers.add(nOffer);
		}
		
		// add transaction
		broker.addTransaction(nOffer,transaction);

		// add transaction in transactionHistory
		transactionHistory.add(transaction);
		Logger.log(ID,"Transaction was made:" + transaction);
		return nOffer;
	}

	public double getMinPrice() {
		if(minSell.get() != null)
			return minSell.get().getPrice();
		return -1;
	}

	public void deleteOffers(List<Offer> offers2) {
		offers.delete(offers2.toArray(oarr));
	}

	public boolean addOffer(Offer offer) {

		Logger.log(ID,"stock " + ID + " adding offer " + offer);

		offers.add(offer);

		if (offer.getOfferType() == OfferType.BUY) {
			Logger.log(ID,"offer added: " + offer.getID());
			return true;

		} else if (offer.getOfferType() == OfferType.SELL) {
			Logger.log(ID,"offer added: " + offer.getID());
			if (minSell.get() == null)
				minSell.put(offer);
			else if (minSell.get().getPrice() > offer.getPrice())
				minSell.put(offer);
			// this.notify();
			return true;
		}

		return false;
	}

	public int modifyOffer(String offerID, Offer newOffer) {
		Logger.log(ID, "Deleteing offer with ID="+offerID + " adding offer " + newOffer);
		Offer old = Offer.getOfferForCompare(offerID);

		offers.delete(old);

		offers.add(newOffer);
		Collection<Offer> off = offers.getCollection();


		if (newOffer.getOfferType() == OfferType.SELL) {
			if (minSell.get().getID() == offerID) {
				minSell.put(getMin(off));
			}
		}

		return 0;
	}

	// cu for
	private Offer getMin(Collection<Offer> offers) {

		double min_price = -1;
		Offer min_offer = null;

		for (Offer offer : offers) {
			if (offer.getOfferType() == OfferType.SELL && min_price > offer.getPrice())
				min_offer = offer;
		}

		return min_offer;
	}

	@Override
	public void run() {
		synchronized(running){
			running = true;
		}

		while (true) {
			synchronized(running){
				if(running == false) break;
			}
			cyclic();
			synchronized (this) {
				try {
					this.wait(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}


	public void setRunning(boolean b) {
		synchronized(running){
			running = b;
		}
	}

	
	public void cyclic() {

		// Logger.log(ID,"cyclic stock " + ID);
		PriorityQueue<Offer> offersBuy = new PriorityQueue<Offer>(new BuyOfferComparator());
		PriorityQueue<Offer> offersSell = new PriorityQueue<Offer>(new SellOfferComparator());

		Offer[] allOffers = offers.getCollection().toArray(oarr);
		Arrays.sort(allOffers);
		
		for (Offer offer : allOffers) {

			if (offer.getOfferType() == OfferType.SELL) {
					
				Offer coff = offer;
				Offer obuy = offersBuy.peek();
				
				while(coff != null){
					if(obuy != null){
						if(coff.getPrice() <= obuy.getPrice()){
							Offer rem = makeTransaction(coff, offersBuy.poll());
							if(rem != null){
								if(rem.getOfferType() == OfferType.SELL){
									coff = rem;
								}else{
									offersBuy.add(rem);
									coff = null;
								}
							}else{
								coff = null;
							}
						}else{
							offersSell.add(coff);
							coff = null;
						}
						obuy = offersBuy.peek();
					}else{
						offersSell.add(coff);
						coff = null;
					}
				}

			} else if (offer.getOfferType() == OfferType.BUY) {
				
				Offer coff = offer;
				Offer osell = offersSell.peek();
				
				while(coff != null){
					if(osell != null){
						if(coff.getPrice() >= osell.getPrice()){
							Offer rem = makeTransaction(offersSell.poll(), coff);
							if(rem != null){
								if(rem.getOfferType() == OfferType.BUY){
									coff = rem;
								}else{
									offersSell.add(rem);
									coff = null;
								}
							}else{
								coff = null;
							}
						}else{
							offersBuy.add(coff);
							coff = null;
						}
						osell = offersSell.peek();
					}else{
						offersBuy.add(coff);
						coff = null;
					}
				}
			}
		}
	}
	
	// private static class MockConsumer implements BiConsumer<String, Transaction>{
	// 	@Override
	// 	public void accept(String t, Transaction u) {			
	// 	}

	// }

	// public static void main(String args[]) {
	// 	Stock stock = new Stock("INTC");
	// 	IBroker broker = new IBrokerImpl();
	// 	stock.setBroker(broker);

	// 	Offer sellOffer1 = new Offer("seller1", "123", 10.4, 1, OfferType.SELL, callback);
	// 	Offer sellOffer2 = new Offer("seller2", "123", 30.4, 3, OfferType.SELL, callback);
	// 	Offer sellOffer3 = new Offer("seller3", "123", 31.4, 1, OfferType.SELL, callback);
	// 	Offer buyOffer1 = new Offer("client1", "123", 31.4, 1, OfferType.BUY, callback);
	// 	Offer buyOffer2 = new Offer("client2", "123", 21.4, 6, OfferType.BUY, callback);
	// 	Offer buyOffer3 = new Offer("client3", "123", 31.4, 1, OfferType.BUY, callback);
	// 	Offer sellOffer6 = new Offer("seller6", "123", 12.4, 1, OfferType.SELL, callback);
	// 	Offer sellOffer7 = new Offer("seller7", "123", 23.4, 1, OfferType.SELL, callback);
	// 	Offer buyOffer5 = new Offer("client5", "123", 31.4, 1, OfferType.BUY, callback);
	// 	Offer buyOffer6 = new Offer("client6", "123", 31.4, 1, OfferType.BUY, callback);
	// 	Offer sellOffer10 = new Offer("seller10", "123", 9.4, 9, OfferType.SELL, callback);
	// 	Offer buyOffer4 = new Offer("client4", "123", 31.4, 1, OfferType.BUY, callback);
	// 	Offer sellOffer4 = new Offer("seller4", "123", 50.4, 1, OfferType.SELL, callback);
	// 	Offer buyOffer7 = new Offer("client7", "123", 31.4, 1, OfferType.BUY, callback);
	// 	Offer sellOffer9 = new Offer("seller9", "123", 35.4, 1, OfferType.SELL, callback);
	// 	Offer sellOffer5 = new Offer("seller5", "123", 5.4, 10, OfferType.SELL, callback);
	// 	Offer buyOffer8 = new Offer("client8", "123", 310.4, 10, OfferType.BUY, callback);
	// 	Offer buyOffer9 = new Offer("client9", "123", 31.4, 1, OfferType.BUY, callback);
	// 	Offer sellOffer8 = new Offer("seller8", "123", 41.4, 1, OfferType.SELL, callback);
	// 	Offer buyOffer10 = new Offer("client10", "123", 31.4, 1, OfferType.BUY, callback);

	// 	stock.addOffer(sellOffer1);
	// 	stock.addOffer(buyOffer1);
	// 	stock.addOffer(sellOffer2);
	// 	stock.addOffer(sellOffer3);
	// 	stock.addOffer(buyOffer2);
	// 	stock.addOffer(buyOffer3);
	// 	stock.addOffer(buyOffer4);
	// 	stock.addOffer(buyOffer5);
	// 	stock.addOffer(sellOffer4);
	// 	stock.addOffer(buyOffer6);
	// 	stock.addOffer(buyOffer7);
	// 	stock.addOffer(sellOffer5);
	// 	stock.addOffer(sellOffer6);
	// 	stock.addOffer(sellOffer7);
	// 	stock.addOffer(sellOffer8);
	// 	stock.addOffer(buyOffer8);
	// 	stock.addOffer(buyOffer9);
	// 	stock.addOffer(buyOffer10);
	// 	stock.addOffer(sellOffer9);
	// 	stock.addOffer(sellOffer10);
	// 	stock.cyclic();
	// 	// stock.running = false;
	// 	Logger.log(null,"Gata");

	// 	stock.getOffers().forEach((e)->{Logger.log(null,e.toString());});
	// 	stock.getTransactionHistory().forEach((e)->{Logger.log(null,e.toString());});
	// }

	
}
