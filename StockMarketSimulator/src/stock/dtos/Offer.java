package stock.dtos;

import java.util.function.BiConsumer;

import common.OfferType;

public final class Offer implements Comparable<Offer>{

    private static int IDs = 0;

    private final String ID; // sau string??
    private final String clientID; // sau string??
    private final String stockID;
    private final double price;
    private final int quantity;
    private final OfferType offerType;
    private final BiConsumer<Boolean, Transaction> callback;
    private final long createdAt;

    public Offer(String clientID, String stockID,double price, int quantity, OfferType offerType,BiConsumer<Boolean, Transaction> callback) {
        this.createdAt = System.currentTimeMillis();
        this.ID = Offer.IDs++ + "";
        this.stockID = stockID;
        this.clientID = clientID;
        this.price = price;
        this.quantity = quantity;
        this.offerType = offerType;
        this.callback = callback;

    }
    

    /**
     * @return the callback
     */
    public BiConsumer<Boolean, Transaction> getCallback() {
        return callback;
    }

    public String getID() {
        return ID;
    }

    public String getClientID() {
        return clientID;
    }


    public double getPrice() {
        return price;
    }


    public int getQuantity() {
        return quantity;
    }

    public OfferType getOfferType() {
        return offerType;
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

    /**
     * @return the createdAt
     */
    public long getCreatedAt() {
        return createdAt;
    }

    @Override
    public int compareTo(Offer o) {
        if(this.createdAt < o.getCreatedAt())
            return -1;
        if(this.createdAt > o.getCreatedAt())
            return 1;
        return 0;
    }



    
}
