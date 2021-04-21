package com.example.demo.DataModels;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


@Document(collection = "Product")
public class Product {
    @Id
    String id;

    String clientId;

    String title;

    String description;

    Float askingPrice;

    public Product(
//            String id, String clientId, String title, String description, Float askingPrice
    ) {
//        this.id = id;
//        this.clientId = clientId;
//        this.title = title;
//        this.description = description;
//        this.askingPrice = askingPrice;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
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
