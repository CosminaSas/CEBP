package client.dtos;

import common.OfferType;

public class StockOffer {

    private String clientID;
    private String ID;
    private String stockID;
    private OfferType type;
    private Double price;
    private int quantity;

    public StockOffer (String id, String stockID, OfferType type, double price, int quantity) {

        ID = id;
        this.stockID = stockID;
        this.type = type;
        this.price = price;
        this.quantity = quantity;
    
    }

    /**
     * @param clientID the clientID to set
     */
    public void setClientID(String clientID) {
        this.clientID = clientID;
    }

    /**
     * @return the stockID
     */
    public String getStockID() {
        return stockID;
    }

    /**
     * @param stockID the stockID to set
     */
    public void setStockID(String stockID) {
        this.stockID = stockID;
    }

    /**
     * @param price the price to set
     */
    public void setPrice(Double price) {
        this.price = price;
    }

    public String getID() {
        return ID;
    }

    public void setID(String id) {
        ID = id;
    }

    public String getStockId() {
        return stockID;
    }

    public void setStockId(String stockID) {
        this.stockID = stockID;
    }

    public OfferType getType() {
        return type;
    }

    public void setType(OfferType type) {
        this.type = type;
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

    public String getClientID() {
        return clientID;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("StockOffer [ClientID=");
        builder.append(clientID);
        builder.append(", ID=");
        builder.append(ID);
        builder.append(", price=");
        builder.append(price);
        builder.append(", quantity=");
        builder.append(quantity);
        builder.append(", stockID=");
        builder.append(stockID);
        builder.append(", type=");
        builder.append(type);
        builder.append("]");
        return builder.toString();
    }

}
