package clientbroker;

import java.util.function.BiConsumer;
import java.util.function.BiFunction;

import client.dtos.StockOffer;
import client.dtos.StockTransaction;

public interface ICBroker {

	void addOffer(String stockID, StockOffer offer, BiConsumer<Boolean,StockTransaction> callback);
	
}
