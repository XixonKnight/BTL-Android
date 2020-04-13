package com.example.booking.model;

public class ItemHistory {
    private String lblNameHotel;
    private String nameTypeRoom;
    private String codeRoom;
    private String timeRegister;
    private String timeGoRoom;
    private int countRoom;
    private int countPerson;

    public ItemHistory() {
    }

    public ItemHistory(String lblNameHotel, String nameTypeRoom, String codeRoom, String timeRegister, String timeGoRoom, int countRoom, int countPerson) {
        this.lblNameHotel = lblNameHotel;
        this.nameTypeRoom = nameTypeRoom;
        this.codeRoom = codeRoom;
        this.timeRegister = timeRegister;
        this.timeGoRoom = timeGoRoom;
        this.countRoom = countRoom;
        this.countPerson = countPerson;
    }

    public String getLblNameHotel() {
        return lblNameHotel;
    }

    public void setLblNameHotel(String lblNameHotel) {
        this.lblNameHotel = lblNameHotel;
    }

    public String getNameTypeRoom() {
        return nameTypeRoom;
    }

    public void setNameTypeRoom(String nameTypeRoom) {
        this.nameTypeRoom = nameTypeRoom;
    }

    public String getCodeRoom() {
        return codeRoom;
    }

    public void setCodeRoom(String codeRoom) {
        this.codeRoom = codeRoom;
    }

    public String getTimeRegister() {
        return timeRegister;
    }

    public void setTimeRegister(String timeRegister) {
        this.timeRegister = timeRegister;
    }

    public String getTimeGoRoom() {
        return timeGoRoom;
    }

    public void setTimeGoRoom(String timeGoRoom) {
        this.timeGoRoom = timeGoRoom;
    }

    public int getCountRoom() {
        return countRoom;
    }

    public void setCountRoom(int countRoom) {
        this.countRoom = countRoom;
    }

    public int getCountPerson() {
        return countPerson;
    }

    public void setCountPerson(int countPerson) {
        this.countPerson = countPerson;
    }
}
