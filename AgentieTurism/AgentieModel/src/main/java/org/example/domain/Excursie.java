package org.example.domain;

import java.time.LocalTime;
import java.util.Objects;

public class Excursie extends Entity<Integer>{
    private String landmark;
    private String transport_company;
    private LocalTime departure_time;
    private Integer available_tickets;

    private Float price;

    public Excursie(String landmark, String transport_company, LocalTime departure_time, Integer available_tickets, Float price) {
        this.landmark = landmark;
        this.transport_company = transport_company;
        this.departure_time = departure_time;
        this.available_tickets = available_tickets;
        this.price = price;
    }

    @Override
    public String toString() {
        return "Excursie la " + landmark +
                " pentru " + available_tickets +
                " persoane, cu plecare de la ora " + departure_time +
                " cu firma: " + transport_company + " | " + price + " RON";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Excursie excursie = (Excursie) o;
        return Objects.equals(landmark, excursie.landmark) && Objects.equals(transport_company, excursie.transport_company) && Objects.equals(departure_time, excursie.departure_time) && Objects.equals(available_tickets, excursie.available_tickets) && Objects.equals(price, excursie.price);
    }

    @Override
    public int hashCode() {
        return Objects.hash(landmark, transport_company, departure_time, available_tickets, price);
    }

    public String getLandmark() {
        return landmark;
    }

    public void setLandmark(String landmark) {
        this.landmark = landmark;
    }

    public String getTransport_company() {
        return transport_company;
    }

    public void setTransport_company(String transport_company) {
        this.transport_company = transport_company;
    }

    public LocalTime getDeparture_time() {
        return departure_time;
    }

    public void setDeparture_time(LocalTime departure_time) {
        this.departure_time = departure_time;
    }

    public Integer getAvailable_tickets() {
        return available_tickets;
    }

    public void setAvailable_tickets(Integer available_tickets) {
        this.available_tickets = available_tickets;
    }

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }
}
