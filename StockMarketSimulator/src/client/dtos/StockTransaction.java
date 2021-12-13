package client.dtos;

import java.time.LocalDateTime;

import common.OfferType;

public class StockTransaction {

    private final String trID;
    private final String stockId;
    private final Double price;
    private final int quantity;
    private final LocalDateTime timestamp;
    private final String offerID;
    private final String newOfferID;
    private final OfferType type;

    public StockTransaction (String trID,String stockId, String offerID,double price, int quantity,String newOfferID,OfferType type, LocalDateTime timestamp) {
        this.trID = trID;
        this.offerID = offerID;
        this.stockId = stockId;
        this.price = price;
        this.quantity = quantity;
        this.type = type;
        this.timestamp = timestamp;
        this.newOfferID = newOfferID;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("StockTransaction [trID=");
        builder.append(trID);
        builder.append(", offerID=");
        builder.append(offerID);
        builder.append(", price=");
        builder.append(price);
        builder.append(", quantity=");
        builder.append(quantity);
        builder.append(", stockId=");
        builder.append(stockId);
        builder.append(", timestamp=");
        builder.append(timestamp);
        builder.append(", newOfferID=");
        builder.append(newOfferID);
        builder.append(", type=");
        builder.append(type);
        builder.append("]");
        return builder.toString();
    }













    /**
     * @return the newOfferID
     */
    public String getNewOfferID() {
        return newOfferID;
    }

    /**
     * @return the offerID
     */
    public String getOfferID() {
        return offerID;
    }



    public String getStockId() {
        return stockId;
    }

    public double getPrice() {
        return price;
    }

 
    public int getQuantity() {
        return quantity;
    }


    public LocalDateTime getTimestamp() {
		return timestamp;
	}



    public String getID() {
        return trID;
    }
}
