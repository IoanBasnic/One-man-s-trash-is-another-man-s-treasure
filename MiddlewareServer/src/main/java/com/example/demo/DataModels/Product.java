package com.example.demo.DataModels;

import java.util.UUID;

public class Product {
    UUID id;
    UUID clientId;
    String title;
    String description;
    Float askingPrice;

    public Product(
//            UUID id, UUID clientId, String title, String description, Float askingPrice
    ) {
//        this.id = id;
//        this.clientId = clientId;
//        this.title = title;
//        this.description = description;
//        this.askingPrice = askingPrice;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getClientId() {
        return clientId;
    }

    public void setClientId(UUID clientId) {
        this.clientId = clientId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Float getAskingPrice() {
        return askingPrice;
    }

    public void setAskingPrice(Float askingPrice) {
        this.askingPrice = askingPrice;
    }
}
