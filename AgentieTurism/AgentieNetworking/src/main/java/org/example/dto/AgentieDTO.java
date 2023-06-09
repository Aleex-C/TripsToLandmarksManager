package org.example.dto;

import java.io.Serializable;

public class AgentieDTO implements Serializable {
    private String name;
    private String username;
    private String password;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public AgentieDTO(String name, String username, String password) {
        this.name = name;
        this.username = username;
        this.password = password;
    }
}