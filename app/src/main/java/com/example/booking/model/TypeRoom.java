package com.example.booking.model;

import java.io.Serializable;

public class TypeRoom implements Serializable {
    private int id;
    private int image;
    private String lblName;
    private int price;

    public TypeRoom() {
    }

    public TypeRoom(int id, String lblName, int price) {
        this.id = id;
        this.lblName = lblName;
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
