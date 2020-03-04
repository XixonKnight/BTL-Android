package com.example.booking.model.dbo;

public class ImageRoom {
    private int idImageRoom;
    private int idHotel;
    private String image;

    public ImageRoom() {
    }

    public ImageRoom(String image) {
        this.image = image;
    }

    public ImageRoom(int idImageRoom, int idHotel, String image) {
        this.idImageRoom = idImageRoom;
        this.idHotel = idHotel;
        this.image = image;
    }

    public int getIdImageRoom() {
        return idImageRoom;
    }

    public void setIdImageRoom(int idImageRoom) {
        this.idImageRoom = idImageRoom;
    }

    public int getIdHotel() {
        return idHotel;
    }

    public void setIdHotel(int idHotel) {
        this.idHotel = idHotel;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
