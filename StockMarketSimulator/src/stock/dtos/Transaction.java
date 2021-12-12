package stock.dtos;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

import javax.swing.text.html.HTMLDocument.HTMLReader.PreAction;

public class Transaction implements Comparable<Transaction>{

	private volatile static Integer IDs = 0;

	private final String ID;
	private final Offer sellOffer;
	private final Offer buyOffer;
	private final String stockID;
	private final LocalDateTime timestamp;
	private final long createdAt;
	private final double price;
	private final int quantity;



	public Transaction(Offer sellOffer, Offer buyOffer, String stockID,double price, int quantity) {
		this.createdAt = System.nanoTime();
		this.timestamp = LocalDateTime.now();
		synchronized(Transaction.IDs){
			this.ID = "" + Transaction.IDs++;
		}
		this.sellOffer = sellOffer;
		this.buyOffer = buyOffer;
		this.stockID = stockID;
		this.price = price;
		this.quantity = quantity;
	}

	public String getID() {
		return ID;
	}

	public Offer getSellOffer() {
		return sellOffer;
	}

	public Offer getBuyOffer() {
		return buyOffer;
	}

	public String getStockID() {
		return stockID;
	}


	public LocalDateTime getTimestamp() {
		return timestamp;
	}

	public double getPrice() {
		return price;
	}

	public int getQuantity() {
		return quantity;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Transaction [ID=");
		builder.append(ID);
		builder.append(", buyOffer=");
		builder.append(buyOffer);
		builder.append(", sellOffer=");
		builder.append(sellOffer);
		builder.append(", stockID=");
		builder.append(stockID);
		builder.append(", timestamp=");
		builder.append(timestamp);
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
	public int compareTo(Transaction o) {
		if(this.createdAt < o.getCreatedAt())
            return -1;
        if(this.createdAt > o.getCreatedAt())
            return 1;
        return 0;
	}

}
