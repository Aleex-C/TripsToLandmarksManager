package org.example.server;

import org.example.AgentieException;
import org.example.IObserver;
import org.example.IServices;
import org.example.domain.Agentie;
import org.example.domain.Excursie;
import org.example.domain.Rezervare;
import org.example.repository.AgentieRepository;
import org.example.repository.ExcursieRepository;
import org.example.repository.RezervareRepository;

import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class AgentServiceImpl implements IServices {
    private AgentieRepository agentieRepository;
    private ExcursieRepository excursieRepository;
    private RezervareRepository rezervareRepository;
    private Map<Integer, IObserver> loggedAgents;

    public AgentServiceImpl(AgentieRepository agentieRepository, ExcursieRepository excursieRepository, RezervareRepository rezervareRepository) {
        this.agentieRepository = agentieRepository;
        this.excursieRepository = excursieRepository;
        this.rezervareRepository = rezervareRepository;
        loggedAgents = new ConcurrentHashMap<>();
    }

    @Override
    public synchronized Agentie login(Agentie agent, IObserver client) throws AgentieException {
        Agentie agentLogged = agentieRepository.findCredentials(agent.getUser(), agent.getPassword());
        if (agentLogged!=null){
            if(loggedAgents.get(agentLogged.getId())!=null){
                throw new AgentieException("Agent already logged in!");
            }
            loggedAgents.put(agentLogged.getId(), client);
        }
        else{
            throw new AgentieException("Authentificaion Failed");
        }
        return agentLogged;
    }

    @Override
    public synchronized void logout(Agentie agent, IObserver client) throws AgentieException {
        IObserver localClient = loggedAgents.remove(agent.getId());
        if (localClient == null){
            throw new AgentieException("Agent " + agent + " is not logged in!");
        }
    }

    @Override
    public synchronized List<String> findAllLandmarks() throws AgentieException {
        Iterable<String> landmarksIterable = excursieRepository.findAllLandmarks();
        return StreamSupport.stream(landmarksIterable.spliterator(), false).collect(Collectors.toList());
    }

    @Override
    public synchronized List<Excursie> findByLandmarkAndInterval(String landmark, LocalTime t1, LocalTime t2) throws AgentieException {
        Iterable<Excursie> listIterable = excursieRepository.findByLandmarkAndTimeInterval(landmark, t1, t2);
        return StreamSupport.stream(listIterable.spliterator(), false).collect(Collectors.toList());
    }

    @Override
    public synchronized void addReservation(String name, String phoneNo, Integer ticketsNo, Excursie excursie, LocalTime t1, LocalTime t2) throws AgentieException {
        Rezervare newRezervare = new Rezervare(name,phoneNo,ticketsNo);
        rezervareRepository.save(newRezervare);
        excursie.setAvailable_tickets(excursie.getAvailable_tickets()-ticketsNo);
        notifyNewReservation(excursie.getId(), excursie, newRezervare, t1, t2);
    }
    private final int defaultThreadNo = 5;
    public synchronized void notifyNewReservation(Integer id, Excursie newExcursie, Rezervare rezervare, LocalTime t1, LocalTime t2){
        Iterable<Agentie> agents = agentieRepository.findAll();
        excursieRepository.update(id, newExcursie);
        ExecutorService executor = Executors.newFixedThreadPool(defaultThreadNo);
        for (Agentie agent : agents){
            IObserver agentieClient = loggedAgents.get(agent.getId());
            if (agentieClient!=null){
                executor.execute(()->{
                        System.out.println("Notifying [" + agent.getId() + "] reservation was added");
                    try {
                        agentieClient.update((List<Excursie>) excursieRepository.findAll(), excursieRepository.findByLandmarkAndTimeInterval(newExcursie.getLandmark(), t1, t2));
                    } catch (AgentieException e) {
                        e.printStackTrace();
                    }
                });
            }
        }
        executor.shutdown();
    }

    @Override
    public synchronized List<Excursie> getAllExcursies() throws AgentieException {
        Iterable<Excursie> excursieIterable = excursieRepository.findAll();
        return StreamSupport.stream(excursieIterable.spliterator(), false).collect(Collectors.toList());
    }
}
