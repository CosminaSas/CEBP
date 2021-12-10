package clientbroker;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import broker.IBroker;
import client.dtos.StockOffer;
import client.dtos.StockTransaction;
import stock.Stock;
import stock.dtos.Offer;
import stock.dtos.Transaction;

public class ICBrokerImpl implements ICBroker{

	private Map<String, Stock> stocks = new ConcurrentHashMap<String, Stock>(); //client-stock, broker=stocks?
	private List<StockTransaction> transactions = new ArrayList<>();
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
        offer.setClientID(clientID);
        Offer serverOffer = stockOfferToOffer(offer,callback);
		return broker.addOffer(serverOffer, stockID);
	}
	
    private Offer stockOfferToOffer(StockOffer offer, Consumer<StockTransaction> callback) {
        Offer o = new Offer(offer.getClientID(),offer.getStockID() ,offer.getPrice(),offer.getQuantity(), offer.getType(), (no,tr) -> {
            StockTransaction str = transactionToStockTransaction(no,tr);
            callback.accept(str);
        });
        offer.setID(o.getID());
        return o;
    }

    private StockTransaction transactionToStockTransaction(String no,Transaction tr) {

        String oID;

        if(tr.getBuyOffer().getClientID().equals(clientID)){
            oID = tr.getBuyOffer().getID();
        }else{
            oID = tr.getSellOffer().getID();
        }
        return new StockTransaction(tr.getID(), oID ,tr.getPrice(),tr.getQuantity() ,no, tr.getTimestamp());
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
        List<Offer> sOffers = broker.getBuyOffers(stockID);
        List<StockOffer> offers = new ArrayList<>();

        sOffers.forEach((o) -> {offers.add(offerToStockOffer(o));});

        return offers;
    }

    private StockOffer offerToStockOffer(Offer o) {
        return new StockOffer(o.getID(), o.getStockID(), o.getOfferType(), o.getPrice(), o.getQuantity());
    }

    @Override
    public List<StockOffer> getStockSellOffers(String stockID) {
        List<Offer> sOffers = broker.getSellOffers(stockID);
        List<StockOffer> offers = new ArrayList<>();

        sOffers.forEach((o) -> {offers.add(offerToStockOffer(o));});

        return offers;
    }

    @Override
    public List<StockTransaction> getStockHistory(String stockID) {
        List<Transaction> sHist = broker.getStockHistory(stockID);
        List<StockTransaction> hist = new ArrayList<>();

        sHist.forEach((t) -> {hist.add(transactionToStockTransaction(null,t));});

        return hist;
    }

    @Override
    public String modifyOffer(String offerID, StockOffer newOffer,Consumer<StockTransaction> callback) {
        Offer serverOffer = stockOfferToOffer(newOffer,callback);
		return broker.modifyOffer(newOffer.getStockId(), offerID, serverOffer);
    }
}

