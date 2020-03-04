package com.example.booking.model.dbo;

public class Room {
    private int id;
    private int idCategories;
    private int idHotel;
    private int roomCode;
    private double price;
    private int amount_max;
    private String image;

    public Room() {
    }

    public Room(int id, int idCategories, int idHotel, int roomCode, double price, int amount_max) {
        this.id = id;
        this.idCategories = idCategories;
        this.idHotel = idHotel;
        this.roomCode = roomCode;
        this.price = price;
        this.amount_max = amount_max;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdCategories() {
        return idCategories;
    }

    public void setIdCategories(int idCategories) {
        this.idCategories = idCategories;
    }

    public int getIdHotel() {
        return idHotel;
    }

    public void setIdHotel(int idHotel) {
        this.idHotel = idHotel;
    }

    public int getRoomCode() {
        return roomCode;
    }

    public void setRoomCode(int roomCode) {
        this.roomCode = roomCode;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getAmount_max() {
        return amount_max;
    }

    public void setAmount_max(int amount_max) {
        this.amount_max = amount_max;
    }
}

