package org.example.domain;

import java.util.Objects;

public class Agentie extends Entity<Integer>{
    private String name;
    private String user;
    private String password;

    public Agentie(String name, String user, String password) {
        this.name = name;
        this.user = user;
        this.password = password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Agentie agentie = (Agentie) o;
        return Objects.equals(name, agentie.name) && Objects.equals(user, agentie.user) && Objects.equals(password, agentie.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, user, password);
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "User: " + user + " de la " + name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
