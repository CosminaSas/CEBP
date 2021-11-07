package stock;

import common.OfferType;

public class StockOffer {

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

}
