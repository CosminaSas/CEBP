package stock.dtos;

import java.util.function.BiConsumer;

import common.OfferType;

public class Offer {

    private static int IDs = 0;

    private String ID; // sau string??
    private String clientID; // sau string??
    private String stockID;
    private double price;
    private int quantity;
    private OfferType offerType;
    private BiConsumer<Boolean, Transaction> callback;

    public Offer(String clientID, String stockID,double price, int quantity, OfferType offerType,BiConsumer<Boolean, Transaction> callback) {
        this.ID = Offer.IDs++ + "";
        this.stockID = stockID;
        this.clientID = clientID;
        this.price = price;
        this.quantity = quantity;
        this.offerType = offerType;
        this.callback = callback;
    }
    
	/**
     * @param stockID the stockID to set
     */
    public void setStockID(String stockID) {
        this.stockID = stockID;
    }

    /**
     * @return the callback
     */
    public BiConsumer<Boolean, Transaction> getCallback() {
        return callback;
    }

    /**
     * @param callback the callback to set
     */
    public void setCallback(BiConsumer<Boolean, Transaction> callback) {
        this.callback = callback;
    }

    public String getID() {
        return ID;
    }

    public void setID(String iD) {
        ID = iD;
    }

    public String getClientID() {
        return clientID;
    }

    public void setClientID(String clientID) {
        this.clientID = clientID;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public OfferType getOfferType() {
        return offerType;
    }

    public void setOfferType(OfferType offerType) {
        this.offerType = offerType;
    }

	public String getStockID() {
		return stockID;
	}

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Offer [ID=");
        builder.append(ID);
        builder.append(", callback=");
        builder.append(callback);
        builder.append(", clientID=");
        builder.append(clientID);
        builder.append(", offerType=");
        builder.append(offerType);
        builder.append(", price=");
        builder.append(price);
        builder.append(", quantity=");
        builder.append(quantity);
        builder.append(", stockID=");
        builder.append(stockID);
        builder.append("]");
        return builder.toString();
    }

}
