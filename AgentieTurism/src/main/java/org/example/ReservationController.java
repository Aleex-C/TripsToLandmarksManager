package org.example;

import io.github.palexdev.materialfx.css.themes.MFXThemeManager;
import io.github.palexdev.materialfx.css.themes.Themes;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.example.domain.Excursie;
import org.example.services.Service;

import java.net.URL;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class ReservationController implements Initializable {
    public static Service service;
    @FXML
    public TextField nameField;
    @FXML
    public TextField phoneNo;
    @FXML
    public TextField ticketsNo;
    @FXML
    public Button addReservation;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
    @FXML
    public void onAddReservationClick(){
        Stage thisStage = (Stage) addReservation.getScene().getWindow();
        String numePerson = nameField.getText();
        String phoneNoPerson = phoneNo.getText();
        Integer ticketsNoPerson = Integer.valueOf(ticketsNo.getText());
        service.addReservation(numePerson, phoneNoPerson, ticketsNoPerson);
        thisStage.hide();
    }
}
