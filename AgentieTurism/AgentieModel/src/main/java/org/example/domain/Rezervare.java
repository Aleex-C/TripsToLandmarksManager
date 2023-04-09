package org.example.domain;

import java.util.Objects;

public class Rezervare extends Entity<Integer>{
    private String name;
    private String phone_number;
    private Integer number_of_tickets;

    public Rezervare(String name, String phone_number, Integer number_of_tickets) {
        this.name = name;
        this.phone_number = phone_number;
        this.number_of_tickets = number_of_tickets;
    }

    @Override
    public String toString() {
        return "Rezervare pentru: " + name + " la numarul de telefon: " + phone_number + " pentru " + number_of_tickets + " bilete!";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Rezervare rezervare = (Rezervare) o;
        return Objects.equals(name, rezervare.name) && Objects.equals(phone_number, rezervare.phone_number) && Objects.equals(number_of_tickets, rezervare.number_of_tickets);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, phone_number, number_of_tickets);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public Integer getNumber_of_tickets() {
        return number_of_tickets;
    }

    public void setNumber_of_tickets(Integer number_of_tickets) {
        this.number_of_tickets = number_of_tickets;
    }
}
