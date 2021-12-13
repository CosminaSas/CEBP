package client.dtos;

import common.OfferType;

public class StockOffer {

    private final String clientID;
    private String ID;
    private final String stockID;
    private final OfferType type;
    private final Double price;
    private final int quantity;

    public StockOffer (String id,String clientID, String stockID, OfferType type, double price, int quantity) {
        this.clientID = clientID;
        this.ID = id;
        this.stockID = stockID;
        this.type = type;
        this.price = price;
        this.quantity = quantity;
    
    }

   
    /**
     * @return the stockID
     */
    public String getStockID() {
        return stockID;
    }

 
    public void setID(String id){
        this.ID = id;
    }

    public String getID() {
        return ID;
    }

 
    public String getStockId() {
        return stockID;
    }

    public OfferType getType() {
        return type;
    }



    public double getPrice() {
        return price;
    }

  

    public int getQuantity() {
        return quantity;
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
