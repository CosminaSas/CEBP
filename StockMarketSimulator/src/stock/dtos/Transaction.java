package stock.dtos;

import java.time.LocalDateTime;

public class Transaction {

	private String ID;
	private Offer sellOffer;
	private Offer buyOffer;
	private String stockID;
	private LocalDateTime timestamp;

	public Transaction(String iD, Offer sellOffer, Offer buyOffer, String stockID, LocalDateTime timestamp) {
		ID = iD;
		this.sellOffer = sellOffer;
		this.buyOffer = buyOffer;
		this.stockID = stockID;
		this.timestamp = timestamp;
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

}
