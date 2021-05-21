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

    Category category;

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

        String c = productJson.get("category").getAsString();
        if(c.equals("electronics"))
            this.category = Category.ELECTRONICS;
        else if(c.equals("books"))
            this.category = Category.BOOKS;
        else if(c.equals("electronic_games"))
            this.category = Category.ELECTRONIC_GAMES;
        else if(c.equals("recreation"))
            this.category = Category.RECREATION;
        else if(c.equals("fashion"))
            this.category = Category.FASHION;
        else if(c.equals("cosmetics"))
            this.category = Category.COSMETICS;
        else if(c.equals("house_stuff"))
            this.category = Category.HOUSE_STUFF;
        else if(c.equals("sports"))
            this.category = Category.SPORTS;
        else if(c.equals("auto"))
            this.category = Category.AUTO;
        else if(c.equals("kids"))
            this.category = Category.KIDS;
        else if(c.equals("supermarket"))
            this.category = Category.SUPERMARKET;
        else
            this.category = Category.OTHER;


    }

    enum Category{
        ELECTRONICS,
        BOOKS,
        ELECTRONIC_GAMES,
        RECREATION,
        FASHION,
        COSMETICS,
        HOUSE_STUFF,
        SPORTS,
        AUTO,
        KIDS,
        SUPERMARKET,
        OTHER
    }
}
