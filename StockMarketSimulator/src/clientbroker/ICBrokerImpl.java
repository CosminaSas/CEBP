package clientbroker;

import java.util.List;
import java.util.function.BiConsumer;

import client.dtos.StockOffer;
import client.dtos.StockTransaction;

public class ICBrokerImpl implements ICBroker{

	@Override
	public void addOffer(String stockID, StockOffer offer, BiConsumer<Boolean, StockTransaction> callback) {
		
		callback.accept(false, null);
		
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
        // TODO Auto-generated method stub
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

