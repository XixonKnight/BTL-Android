package com.example.booking.model.dbo;

public class HistoryRoom {
    private int idHistoryRoom;
    private int idRegistration;
    private int idRoom;
    private int amountPerson;
    private int amountRoom;

    public HistoryRoom() {
    }

    public HistoryRoom(int idHistoryRoom, int idRegistration, int idRoom, int amountPerson, int amountRoom) {
        this.idHistoryRoom = idHistoryRoom;
        this.idRegistration = idRegistration;
        this.idRoom = idRoom;
        this.amountPerson = amountPerson;
        this.amountRoom = amountRoom;
    }

    public int getIdHistoryRoom() {
        return idHistoryRoom;
    }

    public void setIdHistoryRoom(int idHistoryRoom) {
        this.idHistoryRoom = idHistoryRoom;
    }

    public int getIdRegistration() {
        return idRegistration;
    }

    public void setIdRegistration(int idRegistration) {
        this.idRegistration = idRegistration;
    }

    public int getIdRoom() {
        return idRoom;
    }

    public void setIdRoom(int idRoom) {
        this.idRoom = idRoom;
    }

    public int getAmountPerson() {
        return amountPerson;
    }

    public void setAmountPerson(int amountPerson) {
        this.amountPerson = amountPerson;
    }

    public int getAmountRoom() {
        return amountRoom;
    }

    public void setAmountRoom(int amountRoom) {
        this.amountRoom = amountRoom;
    }
}
