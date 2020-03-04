package com.example.booking.model.dbo;

import java.util.Date;

public class Registration {
    private int idRegistration;
    private int idCustomer;
    private Date dateRegistration;
    private Date dateOfArrival;
    private Date dateGo;

    public Registration() {
    }

    public Registration(int idRegistration, int idCustomer, Date dateRegistration, Date dateOfArrival, Date dateGo) {
        this.idRegistration = idRegistration;
        this.idCustomer = idCustomer;
        this.dateRegistration = dateRegistration;
        this.dateOfArrival = dateOfArrival;
        this.dateGo = dateGo;
    }

    public int getIdRegistration() {
        return idRegistration;
    }

    public void setIdRegistration(int idRegistration) {
        this.idRegistration = idRegistration;
    }

    public int getIdCustomer() {
        return idCustomer;
    }

    public void setIdCustomer(int idCustomer) {
        this.idCustomer = idCustomer;
    }

    public Date getDateRegistration() {
        return dateRegistration;
    }

    public void setDateRegistration(Date dateRegistration) {
        this.dateRegistration = dateRegistration;
    }

    public Date getDateOfArrival() {
        return dateOfArrival;
    }

    public void setDateOfArrival(Date dateOfArrival) {
        this.dateOfArrival = dateOfArrival;
    }

    public Date getDateGo() {
        return dateGo;
    }

    public void setDateGo(Date dateGo) {
        this.dateGo = dateGo;
    }
}
