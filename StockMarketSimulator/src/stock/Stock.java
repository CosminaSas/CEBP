package stock;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Vector;
import java.util.function.BiConsumer;

import broker.IBroker;
import broker.IBrokerImpl;
import common.MultiReadSingleWriteCollection;
import common.OfferType;
import stock.dtos.Offer;
import stock.dtos.Transaction;

public class Stock {

	// testing purpose
	private static final BiConsumer<Boolean, Transaction> callback = null;
	//
	private static Offer[] oarr = new Offer[0];
	private static Transaction[] tarr = new Transaction[0];

	private volatile boolean running = true;
	private String ID;
	private MultiReadSingleWriteCollection<Offer> buyOffers = new MultiReadSingleWriteCollection<Offer>(
			new PriorityQueue<Offer>());
	private MultiReadSingleWriteCollection<Offer> sellOffers = new MultiReadSingleWriteCollection<Offer>(
			new PriorityQueue<Offer>());
	private MultiReadSingleWriteCollection<Transaction> transactionHistory = new MultiReadSingleWriteCollection<Transaction>(
			new ArrayList<Transaction>());
	private IBroker broker;
	private Offer minSell;
	private Offer maxBuy;

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

	public List<Offer> getBuyOffers() {
		return Arrays.asList(buyOffers.getArray(oarr));
	}

	public List<Offer> getSellOffers() {
		return Arrays.asList(sellOffers.getArray(oarr));
	}

	public List<Transaction> getTransactionHistory() {
		return Arrays.asList(transactionHistory.getArray(tarr));
	}

	public IBroker getBroker() {
		return broker;
	}

	public void setBroker(IBroker broker) {
		this.broker = broker;
	}

	// Methods

	public void makeTransaction(Offer sellOffer, Offer buyOffer) {

		// delete buy offer
		buyOffers.delete(buyOffer);
		// delete sell offer
		sellOffers.delete(sellOffer);

		// create transaction
		Transaction transaction = new Transaction(sellOffer, buyOffer, this.ID);

		// add transaction
		broker.addTransaction(transaction);

		// add transaction in transactionHistory
		transactionHistory.add(transaction);
		System.out.println("Transaction was made:" + transaction);
	}

	public double getPrice() {
		return minSell.getPrice();
	}

	public boolean addOffer(Offer offer) {

		System.out.println("stock " + ID + "adding offer " + offer);

		if (offer.getOfferType() == OfferType.BUY) {
			buyOffers.add(offer);
			System.out.println("offer added: " + offer.getID());
			if (maxBuy == null)
				maxBuy = offer;
			else if (maxBuy.getPrice() < offer.getPrice())
				// this.notify();
				return true;

		} else if (offer.getOfferType() == OfferType.SELL) {
			sellOffers.add(offer);
			System.out.println("offer added: " + offer.getID());
			if (minSell == null)
				minSell = offer;
			else if (minSell.getPrice() > offer.getPrice())
				minSell = offer;
			// this.notify();
			return true;
		}

		return false;
	}

	public int modifyOffer(String offerID, Offer newOffer) {
		if (newOffer.getOfferType() == OfferType.BUY) {
			Offer[] offersBuy = buyOffers.getArray(oarr);
			for (Offer offer : offersBuy) {
				if (offer.getID() == offerID) {
					buyOffers.delete(offer);
					buyOffers.add(newOffer);
					if (maxBuy.getID() == offerID) {
						maxBuy = getMax(offersBuy);
					}
					this.notify();
					return 1;
				}
			}
		} else if (newOffer.getOfferType() == OfferType.SELL) {
			Offer[] offersSell = sellOffers.getArray(oarr);
			for (Offer offer : offersSell) {
				if (offer.getID() == offerID) {
					sellOffers.delete(offer);
					sellOffers.add(newOffer);
					if (minSell.getID() == offerID) {
						minSell = getMin(offersSell);
					}
					this.notify();
					return 1;
				}
			}
		}

		return 0;
	}

	// cu for
	private Offer getMin(Offer[] offers) {

		double min_price = offers[0].getPrice();
		Offer min_offer = offers[0];

		for (Offer offer : offers) {
			if (min_price > offer.getPrice())
				min_offer = offer;
		}

		return min_offer;
	}

	private Offer getMax(Offer[] offers) {
		double max_price = offers[0].getPrice();
		Offer max_offer = offers[0];

		for (Offer offer : offers) {
			if (max_price < offer.getPrice())
				max_offer = offer;
		}

		return max_offer;
	}

	public void run() {
		running = true;

		while (running) {
			cyclic();
			synchronized (this) {
				try {
					this.wait(1);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public void stopRunning() {
		this.running = false;
	}

	public void cyclic() {

		for (Offer sellOffer : sellOffers.getArray(oarr)) {
			for (Offer buyOffer : buyOffers.getArray(oarr)) {
				if (sellOffer.getPrice() <= buyOffer.getPrice())
					makeTransaction(sellOffer, buyOffer);
			}
		}
	}

	// // Generate random string
	// static String getAlphaNumericString(int n) {

	// // chose a Character random from this String
	// String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ" + "0123456789" +
	// "abcdefghijklmnopqrstuvxyz";

	// // create StringBuffer size of AlphaNumericString
	// StringBuilder sb = new StringBuilder(n);

	// for (int i = 0; i < n; i++) {

	// // generate a random number between
	// // 0 to AlphaNumericString variable length
	// int index = (int) (AlphaNumericString.length() * Math.random());

	// // add Character one by one in end of sb
	// sb.append(AlphaNumericString.charAt(index));
	// }

	// return sb.toString();
	// }

	public static void main(String args[]) {
		Stock stock = new Stock("INTC");
		IBroker broker = new IBrokerImpl();
		stock.setBroker(broker);

		Offer buyOffer1 = new Offer("client1", "123", 31.4, 1, OfferType.BUY, callback);
		Offer sellOffer1 = new Offer("seller1", "123", 31.4, 1, OfferType.SELL, callback);
		Offer sellOffer2 = new Offer("seller2", "123", 30.4, 3, OfferType.SELL, callback);
		Offer sellOffer3 = new Offer("seller3", "123", 31.4, 1, OfferType.SELL, callback);
		Offer buyOffer2 = new Offer("client2", "123", 21.4, 6, OfferType.BUY, callback);
		Offer buyOffer3 = new Offer("client3", "123", 31.4, 1, OfferType.BUY, callback);
		Offer buyOffer4 = new Offer("client4", "123", 31.4, 1, OfferType.BUY, callback);
		Offer buyOffer5 = new Offer("client5", "123", 31.4, 1, OfferType.BUY, callback);
		Offer sellOffer4 = new Offer("seller4", "123", 31.4, 1, OfferType.SELL, callback);
		Offer buyOffer6 = new Offer("client6", "123", 31.4, 1, OfferType.BUY, callback);
		Offer buyOffer7 = new Offer("client7", "123", 31.4, 1, OfferType.BUY, callback);
		Offer sellOffer5 = new Offer("seller5", "123", 301.4, 10, OfferType.SELL, callback);
		Offer sellOffer6 = new Offer("seller6", "123", 31.4, 1, OfferType.SELL, callback);
		Offer sellOffer7 = new Offer("seller7", "123", 31.4, 1, OfferType.SELL, callback);
		Offer sellOffer8 = new Offer("seller8", "123", 31.4, 1, OfferType.SELL, callback);
		Offer buyOffer8 = new Offer("client8", "123", 310.4, 10, OfferType.BUY, callback);
		Offer buyOffer9 = new Offer("client9", "123", 31.4, 1, OfferType.BUY, callback);
		Offer buyOffer10 = new Offer("client10", "123", 31.4, 1, OfferType.BUY, callback);
		Offer sellOffer9 = new Offer("seller9", "123", 31.4, 1, OfferType.SELL, callback);
		Offer sellOffer10 = new Offer("seller10", "123", 10.4, 9, OfferType.SELL, callback);

		stock.addOffer(sellOffer1);
		stock.addOffer(buyOffer1);
		stock.addOffer(sellOffer2);
		stock.addOffer(sellOffer3);
		stock.addOffer(buyOffer2);
		stock.addOffer(buyOffer3);
		stock.addOffer(buyOffer4);
		stock.addOffer(buyOffer5);
		stock.addOffer(sellOffer4);
		stock.addOffer(buyOffer6);
		stock.addOffer(buyOffer7);
		stock.addOffer(sellOffer5);
		stock.addOffer(sellOffer6);
		stock.addOffer(sellOffer7);
		stock.addOffer(sellOffer8);
		stock.addOffer(buyOffer8);
		stock.addOffer(buyOffer9);
		stock.addOffer(buyOffer10);
		stock.addOffer(sellOffer9);
		stock.addOffer(sellOffer10);
		stock.cyclic();
		// stock.running = false;
		System.out.println("Gata");
	}

    public void setRunning(boolean b) {
    }
}
