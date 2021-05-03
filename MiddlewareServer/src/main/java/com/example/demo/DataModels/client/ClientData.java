package com.example.demo.DataModels.client;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "client_data")
public class ClientData {
    String firstName;
    String lastName;
    String address;
    PaymentMethod paymentMethod;
    String phoneNumber;
    Coordinates coordinates;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(Coordinates coordinates) {
        this.coordinates = coordinates;
    }
}

class Coordinates{
    long latitude;
    long longitude;
}

enum PaymentMethod {
    CASH,
    CHECK,
    BITCOIN,
    CREDIT,
    DEBIT
}
