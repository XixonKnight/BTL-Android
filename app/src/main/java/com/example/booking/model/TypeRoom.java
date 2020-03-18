package com.example.booking.model;

import java.io.Serializable;

public class TypeRoom implements Serializable {
    private int image;
    private String lblName;
    private int price;

    public TypeRoom() {
    }

    public TypeRoom(String lblName, int price) {
        this.lblName = lblName;
        this.price = price;
    }

    public TypeRoom(int image, String lblName, int price) {
        this.image = image;
        this.lblName = lblName;
        this.price = price;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getLblName() {
        return lblName;
    }

    public void setLblName(String lblName) {
        this.lblName = lblName;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}
