package com.stocksScreener.model;

public class GoogleIdToken {
    private String idToken;

    public String getIdToken() {
        return idToken;
    }

    public void setIdToken(String idToken) {
        this.idToken = idToken;
    }

    @Override
    public String toString() {
        return "GoogleIdToken{" +
                "idToken='" + idToken + '\'' +
                '}';
    }
}
