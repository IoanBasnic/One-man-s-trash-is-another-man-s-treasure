package com.example.demo.DataModels.client;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Objects;


@Document(collection = "client")
public class Client {
    @Id
    String id;

    @Indexed(unique = true)
    String authId;

    String email;

    String givenName;

    String familyName;

    String nickname;

    String phoneNumber;

    String Address;

    PaymentMethod paymentMethod;

    Coordinates coordinates;

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

    public String getGivenName() {
        return givenName;
    }

    public void setGivenName(String givenName) {
        this.givenName = givenName;
    }

    public String getFamilyName() {
        return familyName;
    }

    public void setFamilyName(String familyName) {
        this.familyName = familyName;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getAuthId() {
        return authId;
    }

    public void setAuthId(String authId) {
        this.authId = authId;
    }

    @Override
    public String toString() {
        return "Client{" +
                "id='" + id + '\'' +
                ", AuthId='" + authId + '\'' +
                ", email='" + email + '\'' +
                ", givenName='" + givenName + '\'' +
                ", familyName='" + familyName + '\'' +
                ", nickname='" + nickname + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", Address='" + Address + '\'' +
                ", paymentMethod=" + paymentMethod +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Client client = (Client) o;
        return id.equals(client.id) &&
                authId.equals(client.authId) &&
                email.equals(client.email) &&
                Objects.equals(givenName, client.givenName) &&
                Objects.equals(familyName, client.familyName) &&
                Objects.equals(nickname, client.nickname) &&
                Objects.equals(phoneNumber, client.phoneNumber) &&
                Objects.equals(Address, client.Address) &&
                paymentMethod == client.paymentMethod;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, authId, email, givenName, familyName, nickname, phoneNumber, Address, paymentMethod);
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(Coordinates coordinates) {
        this.coordinates = coordinates;
    }

    enum PaymentMethod{
        CASH,
        CARD,
        PAYPAL,
        OTHER
    }


    public void mapToPaymentMethod(String paymentMethod){
        if(paymentMethod.equals("cash") || paymentMethod.equals("CASH") || paymentMethod.equals("Cash"))
            this.paymentMethod = PaymentMethod.CASH;

        if(paymentMethod.equals("card") || paymentMethod.equals("CARD") || paymentMethod.equals("Card"))
            this.paymentMethod = PaymentMethod.CARD;

        if(paymentMethod.equals("paypal") || paymentMethod.equals("PAYPAL") || paymentMethod.equals("Paypal"))
            this.paymentMethod = PaymentMethod.PAYPAL;

        this.paymentMethod = PaymentMethod.OTHER;
    }

}


