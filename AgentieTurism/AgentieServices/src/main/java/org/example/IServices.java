package org.example;

import org.example.domain.Agentie;
import org.example.domain.Excursie;

import java.time.LocalTime;
import java.util.List;

public interface IServices {
    Agentie login(Agentie agent, IObserver client) throws AgentieException;
    void logout(Agentie agent, IObserver client) throws AgentieException;
//    void updateExcursieAfterReservation(Integer id, Excursie newExcursie);
    List<String> findAllLandmarks() throws AgentieException;
    List<Excursie> findByLandmarkAndInterval(String landmark, LocalTime t1, LocalTime t2) throws AgentieException;
    void addReservation(String name, String phoneNo, Integer ticketsNo, Excursie exc, LocalTime t1, LocalTime t2) throws AgentieException;
    List<Excursie> getAllExcursies() throws AgentieException;
}
