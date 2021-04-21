package com.example.demo.DataModels;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;


class ClientData {
    String id;
    String clientId;
    String firstName;
    String secondName;
    String address;
    PaymentMethod paymentMethod;
    String phoneNumber;
    Coordinates coordinates;

    public ClientData(String id, String clientId, String firstName, String secondName, String address, PaymentMethod paymentMethod, String phoneNumber) {
        this.id = id;
        this.clientId = clientId;
        this.firstName = firstName;
        this.secondName = secondName;
        this.address = address;
        this.paymentMethod = paymentMethod;
        this.phoneNumber = phoneNumber;
    }
}

@Document(collection = "Client")
public class Client { //password encrypted
    @Id
    String id;

    @Indexed(unique = true)
    String email;

    String password;

    String username;

    public String getId() {
        return id;
    }

    public void setId(String id) {
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
    String id;
    String clientId;

    //to complete
}

class ImageData{
    String id;
    String clientId;
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