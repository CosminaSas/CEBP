package broker;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import stock.Stock;
import stock.dtos.Offer;
import stock.dtos.Transaction;

public class IBrokerImpl implements IBroker {

	private volatile boolean running;
	private Map<String, Stock> stocks = new ConcurrentHashMap<String, Stock>();
	private List<Transaction> transactions = new ArrayList<>();
	private List<Transaction> completedTransactions = Collections.synchronizedList(new ArrayList<>());

	@Override
	public void run() {
		running = true;
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

	private synchronized void cyclic() {
		System.out.println("cyclic");
		if (completedTransactions.size() > 0) {

			synchronized (completedTransactions) {
				transactions.addAll(completedTransactions);
				completedTransactions.forEach((t) -> {
					System.out.println("transaction copied " + t.getID());
				});
				completedTransactions.clear();
			}
		}
	}

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
		return s.getPrice();
	}

	@Override
	public List<String> getStockList() {
		return new ArrayList<String>(stocks.keySet());
	}

	@Override
	public List<Offer> getBuyOffers(String stockID) {
		Stock s = stocks.get(stockID);
		return s.getBuyOffers();
	}

	@Override
	public List<Offer> getSellOffers(String stockID) {
		Stock s = stocks.get(stockID);
		return s.getSellOffers();
	}

	@Override
	public List<Transaction> getStockHistory(String stockID) {
		Stock s = stocks.get(stockID);
		return s.getTransactionHistory();
	}

	@Override
	public boolean subscribe(Stock stock) {
		Stock val = stocks.put(stock.getID(), stock);
		if(val != null)
			stock.setBroker(this);
		return val == null;
	}

	@Override
	public boolean unsubscribe(Stock stock) {
		Stock val = stocks.remove(stock.getID());
		return val != null;
	}

	@Override
	public int addTransaction(Transaction transaction) {
		completedTransactions.add(transaction);
		synchronized (this) {
			this.notify();
		}
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

}
