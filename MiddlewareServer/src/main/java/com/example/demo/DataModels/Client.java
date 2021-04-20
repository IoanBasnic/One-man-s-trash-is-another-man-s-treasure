package com.example.demo.DataModels;

import java.util.UUID;

class ClientData {
    UUID id;
    UUID clientId;
    String firstName;
    String secondName;
    String address;
    PaymentMethod paymentMethod;
    String phoneNumber;
    Coordinates coordinates;

    public ClientData(UUID id, UUID clientId, String firstName, String secondName, String address, PaymentMethod paymentMethod, String phoneNumber) {
        this.id = id;
        this.clientId = clientId;
        this.firstName = firstName;
        this.secondName = secondName;
        this.address = address;
        this.paymentMethod = paymentMethod;
        this.phoneNumber = phoneNumber;
    }
}

public class Client { //password encrypted
    UUID id;
    String email;
    String password;
    String username;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}

class PaymentInfo{
    UUID id;
    UUID clientId;

    //to complete
}

class ImageData{
    UUID id;
    UUID clientId;
}

class Coordinates{
    long latitude;
    long longitude;
}

enum PaymentMethod{
    CASH,
    CHECK,
    BITCOIN,
    CREDIT,
    DEBIT
}