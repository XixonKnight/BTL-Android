package com.example.booking.model.dbo;

public class Area {
    private int idArea;
    private String name;

    public Area() {
    }

    public Area(int idArea, String name) {
        this.idArea = idArea;
        this.name = name;
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
}
