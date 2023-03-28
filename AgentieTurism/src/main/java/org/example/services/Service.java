package org.example.services;

import org.example.domain.Agentie;
import org.example.domain.Excursie;
import org.example.domain.Rezervare;
import org.example.repository.AgentieRepository;
import org.example.repository.ExcursieRepository;
import org.example.repository.IRepository;
import org.example.repository.RezervareRepository;

import java.time.LocalTime;
import java.util.List;

public class Service {
    private AgentieRepository agentieRepository;

    private RezervareRepository rezervareRepository;
    private ExcursieRepository excursieRepository;

    public Service(AgentieRepository agentieRepository, RezervareRepository rezervareRepository, ExcursieRepository excursieRepository) {
        this.agentieRepository = agentieRepository;
        this.rezervareRepository = rezervareRepository;
        this.excursieRepository = excursieRepository;
    }

    public List<String> findAllLandmarks(){
        return excursieRepository.findAllLandmarks();
    }
    public List<Excursie> findByLandmarkAndInterval(String landmark, LocalTime t1, LocalTime t2){
        return excursieRepository.findByLandmarkAndTimeInterval(landmark, t1, t2);
    }
    public void addReservation(String name, String phoneNo, Integer ticketsNo){
        rezervareRepository.save(new Rezervare(name, phoneNo, ticketsNo));
    }

    public Iterable<Excursie> getAllExcursies() {
        return excursieRepository.findAll();
    }
}
