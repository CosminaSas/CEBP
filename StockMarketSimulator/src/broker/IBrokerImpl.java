package broker;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import common.OfferType;
import stock.Stock;
import stock.dtos.Offer;
import stock.dtos.Transaction;

public class IBrokerImpl implements IBroker {

	private volatile boolean running;
	private Map<String, Stock> stocks = new ConcurrentHashMap<String, Stock>();
	private List<Transaction> completedTransactions = Collections.synchronizedList(new ArrayList<>());

	@Override
	public boolean addOffer(Offer offer, String stockID) {
		Stock s = stocks.get(stockID);
		return s.addOffer(offer);
	}

	@Override
	public String modifyOffer(String stockID, String offerID, Offer newOffer) {
		Stock s = stocks.get(stockID);
		s.modifyOffer(offerID, newOffer);
		return null;
	}

	@Override
	public double getStockPrice(String stockID) {
		Stock s = stocks.get(stockID);
		return s.getMinPrice();
	}

	@Override
	public List<String> getStockList() {
		return new ArrayList<String>(stocks.keySet());
	}

	@Override
	public Collection<Offer> getOffers(String stockID) {
		Stock s = stocks.get(stockID);
		return s.getOffers();
	}

	@Override
	public Collection<Transaction> getStockHistory(String stockID) {
		Stock s = stocks.get(stockID);
		return s.getTransactionHistory();
	}

	@Override
	public boolean subscribe(Stock stock) {
		if(stocks.get(stock.getID()) != null)
			return false;
		stocks.put(stock.getID(), stock);
		stock.setBroker(this);
		return true;
	}

	@Override
	public boolean unsubscribe(Stock stock) {
		Stock val = stocks.remove(stock.getID());
		return val != null;
	}

	@Override
	public int addTransaction(Offer newOffer,Transaction transaction) {
		
		completedTransactions.add(transaction);
		String nob = null,nos = null;
		
		if(newOffer != null){
			if(newOffer.getOfferType() == OfferType.BUY)
				nob = newOffer.getID();
			if(newOffer.getOfferType() == OfferType.SELL)
				nos = newOffer.getID();
		}

		transaction.getBuyOffer().getCallback().accept(nob, transaction);
		
		transaction.getSellOffer().getCallback().accept(nos, transaction);

		// synchronized (this) {
		// 	this.notify();
		// }
		return 0;
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

	@Override
	public void deleteOffer(List<Offer> offers,String stockID) {
		Stock s = stocks.get(stockID);
		s.deleteOffers(offers);
	}

}
