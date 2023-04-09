package org.example;

import org.example.domain.Excursie;
import org.example.domain.Rezervare;

import java.util.List;

public interface IObserver {
    void update(List<Excursie> forSelector, List<Excursie> forMainPage) throws AgentieException;
}