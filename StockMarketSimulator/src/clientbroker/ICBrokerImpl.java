package clientbroker;

import java.util.function.BiConsumer;

import client.dtos.StockOffer;
import client.dtos.StockTransaction;

public class ICBrokerImpl implements ICBroker{

	@Override
	public void addOffer(String stockID, StockOffer offer, BiConsumer<Boolean, StockTransaction> callback) {
		
		callback.accept(false, null);
		
	}
	
}
