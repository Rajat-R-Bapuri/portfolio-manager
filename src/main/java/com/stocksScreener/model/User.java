package com.stocksScreener.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Map;

@Document
public class User {

    @Id
    private String id;

    private String userEmail;
    private String userFullName;
    private String imageUrl;
    private String signInType;
    private Map<String, Integer> stockSymbols;

    public User(String id, String userEmail, String userFullName, String imageUrl, String signInType) {
        this.id = id;
        this.userEmail = userEmail;
        this.userFullName = userFullName;
        this.imageUrl = imageUrl;
        this.signInType = signInType;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserFullName() {
        return userFullName;
    }

    public void setUserFullName(String userFullName) {
        this.userFullName = userFullName;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getSignInType() {
        return signInType;
    }

    public void setSignInType(String signInType) {
        this.signInType = signInType;
    }

    public Map<String, Integer> getStockSymbols() {
        return stockSymbols;
    }

    public void setStockSymbols(Map<String, Integer> stockSymbols) {
        this.stockSymbols = stockSymbols;
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", userEmail='" + userEmail + '\'' +
                ", userFullName='" + userFullName + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", signInType='" + signInType + '\'' +
                ", stockSymbols=" + stockSymbols +
                '}';
    }
}
