package stock;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

import broker.IBroker;
import common.OfferType;
import stock.dtos.Offer;
import stock.dtos.Transaction;

public class Stock {

	private String ID;
	private List<Offer> buyOffers = new ArrayList<>();
	private List<Offer> sellOffers = new ArrayList<>();
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
		return buyOffers;
	}

	public void setBuyOffers(List<Offer> buyOffers) {
		this.buyOffers = buyOffers;
	}

	public List<Offer> getSellOffers() {
		return sellOffers;
	}

	public void setSellOffers(List<Offer> sellOffers) {
		this.sellOffers = sellOffers;
	}

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
		for (Offer offer : buyOffers) {
			if (offer.getID() == buyOffer.getID()) {
				buyOffers.remove(offer);
			}
		}

		// delete sell offer
		for (Offer offer : sellOffers) {
			if (offer.getID() == sellOffer.getID()) {
				sellOffers.remove(offer);
			}
		}

		// add transaction in transactionHistory
		transactionHistory.add(transaction);
	}

	public double getPrice() {
		return minSell.getPrice();
	}

	public boolean addOffer(Offer offer) {

		System.out.println("stock " + ID + "adding offer " + offer);

		if (offer.getOfferType() == OfferType.BUY) {
			buyOffers.add(offer);
			System.out.println("offer added: " + offer.getID());
			if (maxBuy.getPrice() < offer.getPrice())
				maxBuy = offer;
			this.notify();
			return true;

		} else if (offer.getOfferType() == OfferType.SELL) {
			sellOffers.add(offer);
			System.out.println("offer added: " + offer.getID());
			if (minSell.getPrice() > offer.getPrice())
				minSell = offer;
			this.notify();
			return true;
		}

		return false;
	}

	public int modifyOffer(String offerID, Offer newOffer) {
		if (newOffer.getOfferType() == OfferType.BUY) {
			for (Offer offer : buyOffers) {
				if (offer.getID() == offerID) {
					if (maxBuy.getID() == offerID) {
						maxBuy = getMax();
					}
					buyOffers.remove(offer);
					buyOffers.add(newOffer);
					this.notify();
					return 1;
				}
			}

		} else if (newOffer.getOfferType() == OfferType.SELL) {
			for (Offer offer : sellOffers) {
				if (offer.getID() == offerID) {
					if (minSell.getID() == offerID) {
						minSell = getMin();
					}
					sellOffers.remove(offer);
					sellOffers.add(newOffer);
					this.notify();
					return 1;
				}
			}
		}

		return 0;
	}

	// cu for
	private Offer getMin() {
		double min_price = sellOffers.get(0).getPrice();
		Offer min_offer = sellOffers.get(0);

		for (Offer offer : sellOffers) {
			if (min_price > offer.getPrice())
				min_offer = offer;
		}

		return min_offer;
	}

	private Offer getMax() {
		double max_price = sellOffers.get(0).getPrice();
		Offer max_offer = sellOffers.get(0);

		for (Offer offer : sellOffers) {
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
					this.wait(1000);
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

		for (Offer sellOffer : sellOffers) {
			for (Offer buyOffer : buyOffers) {
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

}
