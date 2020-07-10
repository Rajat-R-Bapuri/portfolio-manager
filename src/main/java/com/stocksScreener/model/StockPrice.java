package com.stocksScreener.model;

public class StockPrice {
    private String timestamp;
    private Double price;

    public StockPrice(String timestamp, Double price) {
        this.timestamp = timestamp;
        this.price = price;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "StockPrice{" +
                "timestamp='" + timestamp + '\'' +
                ", price=" + price +
                '}';
    }
}
