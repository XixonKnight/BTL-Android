package com.example.booking.model.dbo;

public class Comment {
    private int idCmt;
    private int idCustomer;
    private int idHotel;
    private String content;

    public Comment() {
    }

    public Comment(int idCmt, int idCustomer, int idHotel, String content) {
        this.idCmt = idCmt;
        this.idCustomer = idCustomer;
        this.idHotel = idHotel;
        this.content = content;
    }

    public int getIdCmt() {
        return idCmt;
    }

    public void setIdCmt(int idCmt) {
        this.idCmt = idCmt;
    }

    public int getIdCustomer() {
        return idCustomer;
    }

    public void setIdCustomer(int idCustomer) {
        this.idCustomer = idCustomer;
    }

    public int getIdHotel() {
        return idHotel;
    }

    public void setIdHotel(int idHotel) {
        this.idHotel = idHotel;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
