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
    private final BiConsumer<String, Transaction> callback;
    private final long createdAt;

    public static Offer getOfferForCompare(String ID){
        return new Offer(ID);
    } 

    private Offer(String ID){
        this.createdAt = 0;
        this.ID = ID;
        this.stockID = null;
        this.clientID = null;
        this.price = 0;
        this.quantity = 0;
        this.offerType = null;
        this.callback = null;
    }

    public Offer(String clientID, String stockID,double price, int quantity, OfferType offerType,BiConsumer<String, Transaction> callback) {
        this.createdAt = System.nanoTime();
        this.ID = Offer.IDs++ + "";
        this.stockID = stockID;
        this.clientID = clientID;
        this.price = price;
        this.quantity = quantity;
        this.offerType = offerType;
        this.callback = callback;
    }
    
    private Offer(String clientID, String stockID,double price, int quantity, OfferType offerType,BiConsumer<String, Transaction> callback,long createdAt) {
        this.createdAt =createdAt;
        this.ID = Offer.IDs++ + "";
        this.stockID = stockID;
        this.clientID = clientID;
        this.price = price;
        this.quantity = quantity;
        this.offerType = offerType;
        this.callback = callback;
    }

    public Offer copy(int quantity){
        return new Offer(clientID, stockID, price, quantity, offerType, callback, createdAt);
    }




    /**
     * @return the callback
     */
    public BiConsumer<String, Transaction> getCallback() {
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
        builder.append(", clientID=");
        builder.append(clientID);
        builder.append(", createdAt=");
        builder.append(createdAt);
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

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((ID == null) ? 0 : ID.hashCode());
        return result;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Offer)) {
            return false;
        }
        Offer other = (Offer) obj;
        if (ID == null) {
            if (other.ID != null) {
                return false;
            }
        } else if (!ID.equals(other.ID)) {
            return false;
        }
        return true;
    }



    
}
