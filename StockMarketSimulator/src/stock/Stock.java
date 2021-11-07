package stock;

import java.time.LocalDateTime;
import java.util.List;

import broker.IBroker;
import common.OfferType;
import stock.dtos.Offer;
import stock.dtos.Transaction;

public class Stock {

	private String ID;
	private List<Offer> buyOffers;
	private List<Offer> sellOffers;
	private List<Transaction> transactionHistory;
	private IBroker broker;

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
		double min_price = 0;

		for (Offer offer : sellOffers) {
			min_price = Math.min(min_price, offer.getPrice());
		}

		return min_price;
	}

	public int addOffer(Offer offer) {

		if (offer.getOfferType() == OfferType.BUY) {
			buyOffers.add(offer);
			return 1;

		} else if (offer.getOfferType() == OfferType.SELL) {
			sellOffers.add(offer);
			return 1;
		}

		return 0;
	}

	public int modifyOffer(String offerID, Offer newOffer) {
		if (newOffer.getOfferType() == OfferType.BUY) {
			for (Offer offer : buyOffers) {
				if (offer.getID() == offerID) {
					buyOffers.remove(offer);
					buyOffers.add(newOffer);
					return 1;
				}
			}

		} else if (newOffer.getOfferType() == OfferType.SELL) {
			for (Offer offer : sellOffers) {
				if (offer.getID() == offerID) {
					sellOffers.remove(offer);
					sellOffers.add(newOffer);
					return 1;
				}
			}
		}

		return 0;
	}

	public void run() {
		while (running) {
			cyclic();
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
