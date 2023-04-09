package org.example.controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.example.AgentieException;
import org.example.IServices;
import org.example.domain.Excursie;

import java.net.URL;
import java.time.LocalTime;
import java.util.ResourceBundle;

public class ReservationController {
    private IServices service;

    public void setService(IServices service) {
        this.service = service;
    }

    private Excursie slectedExcursie;

    public void setSlectedExcursie(Excursie slectedExcursie) {
        this.slectedExcursie = slectedExcursie;
    }

    @FXML
    public TextField nameField;
    @FXML
    public TextField phoneNo;
    @FXML
    public TextField ticketsNo;
    @FXML
    public Button addReservation;
    private FreakingTime frkTime;

    public void setFrkTime(LocalTime t1, LocalTime t2) {
        this.frkTime = new FreakingTime(t1, t2);
    }

    @FXML
    public void onAddReservationClick(){
        Stage thisStage = (Stage) addReservation.getScene().getWindow();
        String numePerson = nameField.getText();
        String phoneNoPerson = phoneNo.getText();
        Integer ticketsNoPerson = Integer.valueOf(ticketsNo.getText());
        try {
            service.addReservation(numePerson, phoneNoPerson, ticketsNoPerson, slectedExcursie, frkTime.getT1(), frkTime.getT2());
        } catch (AgentieException e) {
            e.printStackTrace();
        }
//        .setAvailable_tickets(slectedExcursie.getAvailable_tickets()-ticketsNoPerson);
//        service.(slectedExcursie.getId(), slectedExcursie);
        thisStage.close();
    }
}
class FreakingTime{
    LocalTime t1;
    LocalTime t2;

    public FreakingTime(LocalTime t1, LocalTime t2) {
        this.t1 = t1;
        this.t2 = t2;
    }

    public LocalTime getT1() {
        return t1;
    }

    public void setT1(LocalTime t1) {
        this.t1 = t1;
    }

    public LocalTime getT2() {
        return t2;
    }

    public void setT2(LocalTime t2) {
        this.t2 = t2;
    }
}
