package clientbroker;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;

import client.dtos.StockOffer;
import client.dtos.StockTransaction;

public interface ICBroker {

	public void addOffer(String stockID, StockOffer offer, BiConsumer<Boolean,StockTransaction> callback);
    public double getStockPrice(String stockID);
    public List<String> getStockList();
    public List<StockOffer> getStockBuyOffers(String stockID);
    public List<StockOffer>  getStockSellOffers(String stockID);
    public List<StockTransaction> getStockHistory(String stockID);
    public String modifyOffer(String offerID, StockOffer newOffer);
    public int offerCallback(boolean trSucc, StockTransaction transaction);

}
