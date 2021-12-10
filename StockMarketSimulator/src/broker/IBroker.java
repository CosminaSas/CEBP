package broker;

import java.util.Collection;
import java.util.List;

import stock.Stock;
import stock.dtos.Offer;
import stock.dtos.Transaction;

public interface IBroker extends Runnable{
	public boolean addOffer(Offer offer, String stockID);
	public String modifyOffer(String stockID,String offerID, Offer newOffer);
	public double getStockPrice(String stockID);
	public List<String> getStockList();
	public Collection<Offer> getOffers(String stockID);
	public Collection<Transaction> getStockHistory(String stockID);
	public boolean subscribe(Stock stock);
	public boolean unsubscribe(Stock stock);
	public int addTransaction(Offer newOffer,Transaction transaction);
}