package stock;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.BiConsumer;

import broker.IBroker;
import broker.IBrokerImpl;
import common.MultiReadSingleWriteQueue;
import common.OfferType;
import stock.dtos.Offer;
import stock.dtos.Transaction;

public class Stock {

	// testing purpose
	private static final BiConsumer<Boolean, Transaction> callback = null;
	//

	private String ID;
	private MultiReadSingleWriteQueue<Offer> buyOffers = new MultiReadSingleWriteQueue<>();
	private MultiReadSingleWriteQueue<Offer> sellOffers = new MultiReadSingleWriteQueue<>();
	private List<Transaction> transactionHistory = new ArrayList<>();
	private IBroker broker;
	private Offer minSell;
	private Offer maxBuy;

	/**
	 * @param iD
	 */
	public Stock(String iD) {
		ID = iD;
	}

	volatile boolean running = true;
	// Getters & Setters

	public String getID() {
		return ID;
	}

	public void setID(String iD) {
		ID = iD;
	}

	public List<Offer> getBuyOffers() {
		return Arrays.asList(buyOffers.getArray());
	}

	// public void setBuyOffers(List<Offer> buyOffers) {
	// 	this.buyOffers = buyOffers;
	// }

	public List<Offer> getSellOffers() {
		return Arrays.asList(sellOffers.getArray());
	}

	// public void setSellOffers(List<Offer> sellOffers) {
	// 	this.sellOffers = sellOffers;
	// }

	public List<Transaction> getTransactionHistory() {
		return transactionHistory;
	}

	public void setTransactionHistory(List<Transaction> transactionHistory) {
		this.transactionHistory = transactionHistory;
	}

	public IBroker getBroker() {
		return broker;
	}

	public void setBroker(IBroker broker) {
		this.broker = broker;
	}

	// Methods

	public void makeTransaction(Offer sellOffer, Offer buyOffer) {
		ID = getAlphaNumericString(10);

		// create transaction
		Transaction transaction = new Transaction(ID, sellOffer, buyOffer, this.ID, LocalDateTime.now());

		// add transaction
		broker.addTransaction(transaction);

		// delete buy offer
		buyOffers.delete(buyOffer);
		// delete sell offer
		sellOffers.delete(sellOffer);
		

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
			for (Offer offer : buyOffers.getArray()) {
				if (offer.getID() == offerID) {
					if (maxBuy.getID() == offerID) {
						maxBuy = getMax(buyOffers.getArray());
					}
					buyOffers.delete(offer);
					buyOffers.add(newOffer);
					this.notify();
					return 1;
				}
			}

		} else if (newOffer.getOfferType() == OfferType.SELL) {
			for (Offer offer : sellOffers.getArray()) {
				if (offer.getID() == offerID) {
					if (minSell.getID() == offerID) {
						minSell = getMin(sellOffers.getArray());
					}
					sellOffers.delete(offer);
					sellOffers.add(newOffer);
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
		while (running) {
			cyclic();
			synchronized (this) {
				try {
					this.wait(1);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	public void stopRunning() {
		running = false;
	}

	public void cyclic() {

		for (Offer sellOffer : sellOffers.getArray()) {
			for (Offer buyOffer : buyOffers.getArray()) {
				if (sellOffer.getPrice() <= buyOffer.getPrice())
					makeTransaction(sellOffer, buyOffer);
			}
		}
	}

	// Generate random string
	static String getAlphaNumericString(int n) {

		// chose a Character random from this String
		String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ" + "0123456789" + "abcdefghijklmnopqrstuvxyz";

		// create StringBuffer size of AlphaNumericString
		StringBuilder sb = new StringBuilder(n);

		for (int i = 0; i < n; i++) {

			// generate a random number between
			// 0 to AlphaNumericString variable length
			int index = (int) (AlphaNumericString.length() * Math.random());

			// add Character one by one in end of sb
			sb.append(AlphaNumericString.charAt(index));
		}

		return sb.toString();
	}

	public static void main(String args[]) {
		Stock stock = new Stock("123");
		IBroker broker = new IBrokerImpl();
		stock.setBroker(broker);

		Offer buyOffer1 = new Offer("client1", "123", 31.4, 1, OfferType.BUY, callback);
		// Offer buyOffer2 = new Offer("client2", "123", 130.4, 1, OfferType.BUY,
		// callback);
		Offer sellOffer1 = new Offer("seller1", "123", 30.4, 1, OfferType.SELL, callback);

		stock.addOffer(buyOffer1);
		// stock.addOffer(buyOffer2);
		stock.addOffer(sellOffer1);
		stock.run();
		// stock.running = false;
		System.out.println("Gata");
	}
}
