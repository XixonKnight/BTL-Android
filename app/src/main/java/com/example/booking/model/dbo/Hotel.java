package com.example.booking.model.dbo;

import java.io.Serializable;

public class Hotel implements Serializable {
    private int idHotel;
    private int idArea;
    private String name;
    private String address;
    private String image;

    public Hotel() {
    }

    public Hotel(int idHotel, int idArea, String name, String address, String image) {
        this.idHotel = idHotel;
        this.idArea = idArea;
        this.name = name;
        this.address = address;
        this.image = image;
    }

    public int getIdHotel() {
        return idHotel;
    }

    public void setIdHotel(int idHotel) {
        this.idHotel = idHotel;
    }

    public int getIdArea() {
        return idArea;
    }

    public void setIdArea(int idArea) {
        this.idArea = idArea;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
