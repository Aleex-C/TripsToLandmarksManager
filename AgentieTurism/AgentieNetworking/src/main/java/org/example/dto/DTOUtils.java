package org.example.dto;

import org.example.domain.Agentie;
import org.example.domain.Excursie;
import org.example.domain.Info;
import org.example.domain.Rezervare;

import java.time.LocalTime;
import java.util.List;

public class DTOUtils {
    public static Agentie getFromDTO(AgentieDTO agentieDTO){
        String name = agentieDTO.getName();
        String username = agentieDTO.getUsername();
        String password = agentieDTO.getPassword();
        return new Agentie(name,username, password);
    }
    public static AgentieDTO getDTO(Agentie agentie){
        String name = agentie.getName();
        String username = agentie.getUser();
        String password = agentie.getPassword();
        return new AgentieDTO(name,username, password);
    }
    public static Info InfoGetFromDTO(InfoDTO infoDTO){
        String landmark = infoDTO.getLandmark();
        LocalTime t1 = infoDTO.getT1();
        LocalTime t2 = infoDTO.getT2();
        return new Info(landmark,t1,t2);
    }
    public static InfoDTO InfoGetDTO(Info info){
        String landmark = info.getLandmark();
        LocalTime t1 = info.getT1();
        LocalTime t2 = info.getT2();
        return new InfoDTO(landmark,t1,t2);
    }
    public static Rezervare GetReservationFromDTO(ReservationDTO reservationDTO){
        String name = reservationDTO.getName();
        String phoneNo = reservationDTO.getPhoneNo();
        Integer ticketsNo = reservationDTO.getTicketsNo();
        return new Rezervare(name,phoneNo,ticketsNo);
    }
    public static ReservationDTO GetReservationDTO(Rezervare reservation, Excursie exc, LocalTime t1, LocalTime t2){
        String name = reservation.getName();
        String phoneNo = reservation.getPhone_number();
        Integer ticketsNo = reservation.getNumber_of_tickets();
        return new ReservationDTO(name,phoneNo,ticketsNo, exc, t1, t2);
    }
    public static UpdateDTO getUpdateDTO(List<Excursie> forSelector, List<Excursie> forMainPage){
        return new UpdateDTO(forSelector, forMainPage);
    }
}
