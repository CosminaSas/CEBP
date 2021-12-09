package broker;

import java.util.List;

import stock.Stock;
import stock.dtos.Offer;
import stock.dtos.Transaction;

public interface IBroker extends Runnable{
	public boolean addOffer(Offer offer, String stockID);
	public String modifyOffer(String stockID,String offerID, Offer newOffer);
	public double getStockPrice(String stockID);
	public List<String> getStockList();
	public List<Offer> getBuyOffers(String stockID);
	public List<Offer> getSellOffers(String stockID);
	public List<Transaction> getStockHistory(String stockID);
	public boolean subscribe(Stock stock);
	public boolean unsubscribe(Stock stock);
	public int addTransaction(Offer newOffer,Transaction transaction);
}