package clientbroker;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiConsumer;

import client.dtos.StockOffer;
import client.dtos.StockTransaction;
import stock.Stock;

public class ICBrokerImpl implements ICBroker{

	private volatile boolean running;
	private Map<String, Stock> stocks = new ConcurrentHashMap<String, Stock>(); //client-stock, broker=stocks?
	private List<StockTransaction> transactions = new ArrayList<>();

	@Override
	public void addOffer(String stockID, StockOffer offer, BiConsumer<Boolean, StockTransaction> callback) {
		
		Map<String, Stock> broker;
		Stock so = broker.get(stockID);
		so.addOffer(offer);
		//callback.accept(false, null);
		
	}
	
    @Override
    public double getStockPrice(String stockID) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public List<String> getStockList() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<StockOffer> getStockBuyOffers(String stockID) {
        // TODO Auto-generated method stub //return lista de offer, pt fiecare offer fac un for pt, return lista de stock offers, 
		//ma uit ce apping am in offers, pot sa le iau pe toate su ke oun in stockOffer
		List<Offer> off = broker.getStockSellOffers(stockID);
		
        return null;
    }

    @Override
    public List<StockOffer> getStockSellOffers(String stockID) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<StockTransaction> getStockHistory(String stockID) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String modifyOffer(String offerID, StockOffer newOffer) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public int offerCallback(boolean trSucc, StockTransaction transaction) {
        // TODO Auto-generated method stub
        return 0;
}
}

