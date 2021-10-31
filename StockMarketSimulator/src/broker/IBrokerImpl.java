package broker;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import stock.Stock;
import stock.dtos.Offer;
import stock.dtos.Transaction;

public class IBrokerImpl implements IBroker{

	private volatile boolean running;
	private Map<String,Stock> stocks = Collections.synchronizedMap(new HashMap<>());
	private List<Transaction> transactions;
	private List<Transaction> completedTransactions  = Collections.synchronizedList(new ArrayList<>());
	private List<Offer> newOffers = Collections.synchronizedList(new ArrayList<>());
	private List<String> delOffer;
	private List<Offer> modOffer;

	@Override
	public void run() {
		running = true;
		while(running){
			cyclic();
		}
		
	}

	private void cyclic(){

	}

	@Override
	public String addOffer(Offer offer, String stockID) {
		
		newOffers.add(offer);
		
		return null;
	}

	@Override
	public String modifyOffer(String offerID, Offer newOffer) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public double getStockPrice(String stockID) {
		return stocks.get(stockID).getPrice();
	}

	@Override
	public List<String> getStockList() {
		return new ArrayList(stocks.values());
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
		return s.getHistory();
	}

	@Override
	public String subscribe(Stock stock) {
		stocks.put(stock.getID(), stock);
		return null;
	}

	@Override
	public int addTransaction(Transaction transaction) {
		completedTransactions.add(transaction);
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
