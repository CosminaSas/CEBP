package stock.dtos;

import common.OfferType;

public class Offer {

    private String ID; // sau string??
    private String clientID; // sau string??
    private double price;
    private int quantity;
    private OfferType offerType;

    public Offer(String iD, String clientID, double price, int quantity, OfferType offerType) {
        ID = iD;
        this.clientID = clientID;
        this.price = price;
        this.quantity = quantity;
        this.offerType = offerType;
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

}
