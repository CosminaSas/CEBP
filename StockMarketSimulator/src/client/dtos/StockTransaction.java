package client.dtos;

import java.time.LocalDateTime;

public class StockTransaction {

    private String stockId;
    private Double price;
    private int quantity;
    private LocalDateTime timestamp;

    public StockTransaction (String stockId, double price, int quantity, LocalDateTime timestamp) {

        this.stockId = stockId;
        this.price = price;
        this.quantity = quantity;
        this.timestamp = timestamp;
    
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
