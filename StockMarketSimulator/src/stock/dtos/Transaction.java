package stock.dtos;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class Transaction implements Comparable<Transaction>{

	private static int IDs = 0;

	private String ID;
	private Offer sellOffer;
	private Offer buyOffer;
	private String stockID;
	private LocalDateTime timestamp;
	private long createdAt;

	public Transaction(Offer sellOffer, Offer buyOffer, String stockID) {
		this.createdAt = System.nanoTime();
		this.ID = "" + Transaction.IDs++;
		this.sellOffer = sellOffer;
		this.buyOffer = buyOffer;
		this.stockID = stockID;
		this.timestamp = Instant.ofEpochMilli(createdAt).atZone(ZoneId.systemDefault()).toLocalDateTime();
	}

	public String getID() {
		return ID;
	}

	public void setID(String iD) {
		ID = iD;
	}

	public Offer getSellOffer() {
		return sellOffer;
	}

	public void setSellOffer(Offer sellOffer) {
		this.sellOffer = sellOffer;
	}

	public Offer getBuyOffer() {
		return buyOffer;
	}

	public void setBuyOffer(Offer buyOffer) {
		this.buyOffer = buyOffer;
	}

	public String getStockID() {
		return stockID;
	}

	public void setStockID(String stockID) {
		this.stockID = stockID;
	}

	public LocalDateTime getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(LocalDateTime timestamp) {
		this.timestamp = timestamp;
	}

	public double getPrice() {
		return 0;
	}

	public int getQuantity() {
		return 0;
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

	/**
	 * @param createdAt the createdAt to set
	 */
	public void setCreatedAt(long createdAt) {
		this.createdAt = createdAt;
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
