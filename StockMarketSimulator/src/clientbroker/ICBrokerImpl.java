package clientbroker;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import broker.IBroker;
import client.dtos.StockOffer;
import client.dtos.StockTransaction;
import common.OfferType;
import stock.dtos.Offer;
import stock.dtos.Transaction;

public class ICBrokerImpl implements ICBroker{

    private IBroker broker;
    private String clientID;

	/**
     * @param broker
     * @param clientID
     */
    public ICBrokerImpl(IBroker broker, String clientID) {
        this.broker = broker;
        this.clientID = clientID;
    }

    @Override
	public boolean addOffer(String stockID, StockOffer offer, Consumer<StockTransaction> callback) {
        Offer serverOffer = stockOfferToOffer(offer,callback);
		return broker.addOffer(serverOffer, stockID);
	}
	
    private Offer stockOfferToOffer(StockOffer offer, Consumer<StockTransaction> callback) {
        Offer o = new Offer(offer.getClientID(),offer.getStockID() ,offer.getPrice(),offer.getQuantity(), offer.getType(), new Callback(callback));
        offer.setID(o.getID());
        return o;
    }

    private class Callback implements BiConsumer<String, Transaction>{

        private Consumer<StockTransaction> callback;

        /**
         * @param callback
         */
        public Callback(Consumer<StockTransaction> callback) {
            this.callback = callback;
        }



        @Override
        public void accept(String no, Transaction tr) {
            StockTransaction str = transactionToStockTransaction(no,tr);
            callback.accept(str);
            
        }

    }


    private StockTransaction transactionToStockTransaction(String no,Transaction tr) {

        String oID;
        OfferType tp;


        if(tr.getBuyOffer().getClientID().equals(clientID)){
            oID = tr.getBuyOffer().getID();
            tp = OfferType.BUY;
        }else{
            oID = tr.getSellOffer().getID();
            tp = OfferType.SELL;
        }
        return new StockTransaction(tr.getID(),tr.getStockID(), oID ,tr.getPrice(),tr.getQuantity() ,no,tp, tr.getTimestamp());
    }

    @Override
    public double getStockPrice(String stockID) {
        return broker.getStockPrice(stockID);
    }

    @Override
    public List<String> getStockList() {
        return broker.getStockList();
    }

    @Override
    public List<StockOffer> getStockBuyOffers(String stockID) {
        Collection<Offer> sOffers = broker.getOffers(stockID);
        List<StockOffer> offers = new ArrayList<>();

        sOffers.forEach((o) -> {
            if(o.getOfferType() == OfferType.BUY)
                offers.add(offerToStockOffer(o));
        });

        return offers;
    }

    private StockOffer offerToStockOffer(Offer o) {
        return new StockOffer(o.getID(),clientID,o.getStockID(), o.getOfferType(), o.getPrice(), o.getQuantity());
    }

    @Override
    public List<StockOffer> getStockSellOffers(String stockID) {
        Collection<Offer> sOffers = broker.getOffers(stockID);
        List<StockOffer> offers = new ArrayList<>();

        sOffers.forEach((o) -> {
            if(o.getOfferType() == OfferType.SELL)
                offers.add(offerToStockOffer(o));
        });

        return offers;
    }

    @Override
    public List<StockTransaction> getStockHistory(String stockID) {
        Collection<Transaction> sHist = broker.getStockHistory(stockID);
        List<StockTransaction> hist = new ArrayList<>();

        sHist.forEach((t) -> {hist.add(transactionToStockTransaction(null,t));});

        return hist;
    }

    @Override
    public String modifyOffer(String offerID, StockOffer newOffer,Consumer<StockTransaction> callback) {
        Offer serverOffer = stockOfferToOffer(newOffer,callback);
		return broker.modifyOffer(newOffer.getStockId(), offerID, serverOffer);
    }

    @Override
    public boolean deleteOffer(List<String> offerIDs,String stockID) {
        List<Offer> offers = offerIDs.stream().map((id)->{return Offer.getOfferForCompare(id);}).collect(Collectors.toList());
        broker.deleteOffer(offers,stockID);
        return true;
    }
}

