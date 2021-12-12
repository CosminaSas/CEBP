package client.dtos;

import java.time.LocalDateTime;

import common.OfferType;

public class StockTransaction {

    private String stockId;
    private Double price;
    private int quantity;
    private LocalDateTime timestamp;
    private String offerID;
    private String newOfferID;
    private OfferType type;

    public StockTransaction (String stockId, String offerID,double price, int quantity,String newOfferID,OfferType type, LocalDateTime timestamp) {

        this.offerID = offerID;
        this.stockId = stockId;
        this.price = price;
        this.quantity = quantity;
        this.type = type;
        this.timestamp = timestamp;
    
    }
 

    
    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("StockTransaction [newOfferID=");
        builder.append(newOfferID);
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
     * @param newOfferID the newOfferID to set
     */
    public void setNewOfferID(String newOfferID) {
        this.newOfferID = newOfferID;
    }



    /**
     * @param price the price to set
     */
    public void setPrice(Double price) {
        this.price = price;
    }

    /**
     * @return the offerID
     */
    public String getOfferID() {
        return offerID;
    }

    /**
     * @param offerID the offerID to set
     */
    public void setOfferID(String offerID) {
        this.offerID = offerID;
    }



    public String getStockId() {
        return stockId;
    }

    public void setStockId(String stockId) {
        this.stockId = stockId;
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

    public LocalDateTime getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(LocalDateTime timestamp) {
		this.timestamp = timestamp;
	}
}
