package org.example.dto;

import org.example.domain.Excursie;

import java.io.Serializable;
import java.time.LocalTime;

public class ReservationDTO implements Serializable {
    private String name;
    private String phoneNo;
    private Integer ticketsNo;

    private Excursie exc;
    private LocalTime t1;
    private LocalTime t2;

    public ReservationDTO(String name, String phoneNo, Integer ticketsNo, Excursie exc, LocalTime t1, LocalTime t2) {
        this.name = name;
        this.phoneNo = phoneNo;
        this.ticketsNo = ticketsNo;
        this.exc = exc;
        this.t1 = t1;
        this.t2 = t2;
    }

    public LocalTime getT1() {
        return t1;
    }

    public void setT1(LocalTime t1) {
        this.t1 = t1;
    }

    public LocalTime getT2() {
        return t2;
    }

    public void setT2(LocalTime t2) {
        this.t2 = t2;
    }

    public Excursie getExc() {
        return exc;
    }

    public void setExc(Excursie exc) {
        this.exc = exc;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public Integer getTicketsNo() {
        return ticketsNo;
    }

    public void setTicketsNo(Integer ticketsNo) {
        this.ticketsNo = ticketsNo;
    }
}
