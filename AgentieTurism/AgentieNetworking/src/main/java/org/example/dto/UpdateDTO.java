package org.example.dto;

import org.example.domain.Excursie;

import java.io.Serializable;
import java.util.List;

public class UpdateDTO implements Serializable {
    private List<Excursie> forSelector;
    private List<Excursie> forMainPage;

    public UpdateDTO(List<Excursie> forSelector, List<Excursie> forMainPage) {
        this.forSelector = forSelector;
        this.forMainPage = forMainPage;
    }

    public List<Excursie> getForSelector() {
        return forSelector;
    }

    public void setForSelector(List<Excursie> forSelector) {
        this.forSelector = forSelector;
    }

    public List<Excursie> getForMainPage() {
        return forMainPage;
    }

    public void setForMainPage(List<Excursie> forMainPage) {
        this.forMainPage = forMainPage;
    }
}
