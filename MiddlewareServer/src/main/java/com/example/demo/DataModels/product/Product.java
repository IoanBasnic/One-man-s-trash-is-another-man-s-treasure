package com.example.demo.DataModels.product;

import com.google.gson.JsonObject;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


@Document(collection = "product")
public class Product {
    @Id
    String id;

    String clientId;

    String title;

    String description;

    Float askingPrice;

    String image;

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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void jsonToProduct(JsonObject productJson){
        this.clientId = productJson.get("clientId").getAsString();
        this.askingPrice = productJson.get("askingPrice").getAsFloat();
        this.description = productJson.get("description").getAsString();
        this.title = productJson.get("title").getAsString();
        this.image = productJson.get("image").getAsString();
    }
}
