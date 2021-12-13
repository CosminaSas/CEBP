package clientbroker;

import java.util.List;
import java.util.function.Consumer;

import client.dtos.StockOffer;
import client.dtos.StockTransaction;

public interface ICBroker {

	public boolean addOffer(String stockID, StockOffer offer, Consumer<StockTransaction> callback);
    public double getStockPrice(String stockID);
    public List<String> getStockList();
    public List<StockOffer> getStockBuyOffers(String stockID);
    public List<StockOffer>  getStockSellOffers(String stockID);
    public List<StockTransaction> getStockHistory(String stockID);
    public String modifyOffer(String offerID, StockOffer newOffer,Consumer<StockTransaction> callback);
    public boolean deleteOffer(List<String> offerIDs,String stockID);

}
