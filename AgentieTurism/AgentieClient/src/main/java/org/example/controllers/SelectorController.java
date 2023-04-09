package org.example.controllers;

import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import io.github.palexdev.materialfx.controls.MFXTableColumn;
import io.github.palexdev.materialfx.controls.MFXTableView;
import io.github.palexdev.materialfx.controls.cell.MFXTableRowCell;
import io.github.palexdev.materialfx.css.themes.MFXThemeManager;
import io.github.palexdev.materialfx.css.themes.Themes;
import io.github.palexdev.materialfx.filter.StringFilter;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.example.AgentieException;
import org.example.IObserver;
import org.example.IServices;
import org.example.StartRpcClient;
import org.example.domain.Agentie;
import org.example.domain.Excursie;
import org.example.domain.Rezervare;

import java.io.IOException;
import java.net.URL;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class SelectorController implements IObserver {
    private IServices service;

    public void setService(IServices service) {
        this.service = service;
        init();
    }

    private Agentie currAgent;

    public void setCurrAgent(Agentie currAgent) {
        this.currAgent = currAgent;
    }

    private ObservableList<String> landmarksList;
    @FXML
    MFXButton nextBtn;
    MainPageController mainPageController;
    @FXML
    MFXTableView allTable;
    ObservableList<Excursie> excursieObservableList;
    @FXML
    TextField startTime;
    @FXML
    TextField endTime;
    @FXML
    MFXComboBox <String> landmarks;

    public void init() {
        try {
            setupCombo();
        } catch (AgentieException e) {
            throw new RuntimeException(e);
        }
        try {
            setupTable();
        } catch (AgentieException e) {
            throw new RuntimeException(e);
        }
    }
    public void setupCombo() throws AgentieException {
        Set<String> landmarkSet = service.findAllLandmarks().stream().collect(Collectors.toSet());
        List<String> landmarkStrings = new ArrayList<>(landmarkSet);
        landmarksList = FXCollections.observableArrayList(landmarkStrings);
        landmarks.setItems(landmarksList);
    }
    @FXML
    public void onNextButtonClick() throws IOException, AgentieException {
        Stage thisStage = (Stage) nextBtn.getScene().getWindow();
        String landmark = landmarks.getSelectedItem();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        LocalTime t1 = LocalTime.parse(startTime.getText(), formatter);
        LocalTime t2 = LocalTime.parse(endTime.getText(), formatter);
        FXMLLoader loader = new FXMLLoader(StartRpcClient.class.getResource("/main_page.fxml"));
        Scene scene = new Scene(loader.load());
        mainPageController = loader.getController();
        mainPageController.setExcursieList(service.findByLandmarkAndInterval(landmark, t1, t2));
        mainPageController.setInformations(landmark, t1, t2);
        mainPageController.setService(service);
//        thisStage.hide();

        Stage main_stage = new Stage();
        MFXThemeManager.addOn(scene, Themes.DEFAULT, Themes.DEFAULT);
        main_stage.setScene(scene);
        main_stage.setTitle(currAgent.toString());
        main_stage.show();
//        main_stage.setOnCloseRequest(e->thisStage.show());
//        main_stage.setOnHidden(e->thisStage.show());
    }
    private void setupTable() throws AgentieException {
        MFXTableColumn<Excursie> landmarkColumn = new MFXTableColumn<>("Landmark", true, Comparator.comparing(Excursie::getLandmark));
        MFXTableColumn<Excursie> transportCompanyColumn = new MFXTableColumn<>("Transport Company", true, Comparator.comparing(Excursie::getTransport_company));
        MFXTableColumn<Excursie> priceColumn = new MFXTableColumn<>("Price", true, Comparator.comparing(Excursie::getPrice));
        MFXTableColumn<Excursie> departureTimeColumn = new MFXTableColumn<>("Departure at", true, Comparator.comparing(Excursie::getDeparture_time));
        MFXTableColumn<Excursie> ticketsLeftColumn = new MFXTableColumn<>("Available tickets", true, Comparator.comparing(Excursie::getAvailable_tickets));
//        MFXTableColumn<Excursie> reservationColum = new MFXTableColumn<>("Reservation column");
//        reservationColum.setRowCellFactory();
        landmarkColumn.setRowCellFactory(excursie -> new MFXTableRowCell<>(Excursie::getLandmark){{
            setAlignment(Pos.CENTER_RIGHT);
        }});
        transportCompanyColumn.setRowCellFactory(excursie -> new MFXTableRowCell<>(Excursie::getTransport_company){{
            setAlignment(Pos.CENTER_RIGHT);
        }});
        priceColumn.setRowCellFactory(excursie -> new MFXTableRowCell<>(Excursie::getPrice){{
            setAlignment(Pos.CENTER_RIGHT);
        }});
        departureTimeColumn.setRowCellFactory(excursie -> new MFXTableRowCell<>(Excursie::getDeparture_time){{
            setAlignment(Pos.CENTER_RIGHT);
        }});
        ticketsLeftColumn.setRowCellFactory(excursie -> new MFXTableRowCell<>(Excursie::getAvailable_tickets){{
            setAlignment(Pos.CENTER_RIGHT);
        }});


        landmarkColumn.setAlignment(Pos.CENTER_RIGHT);
        transportCompanyColumn.setAlignment(Pos.CENTER_RIGHT);
        priceColumn.setAlignment(Pos.CENTER_RIGHT);
        departureTimeColumn.setAlignment(Pos.CENTER_RIGHT);
        ticketsLeftColumn.setAlignment(Pos.CENTER_RIGHT);

        allTable.getTableColumns().addAll(landmarkColumn,transportCompanyColumn, priceColumn, departureTimeColumn, ticketsLeftColumn);
        allTable.getFilters().addAll(new StringFilter<>("Transport Company", Excursie::getTransport_company));
        List<Excursie> excursieList = service.getAllExcursies();
        excursieObservableList = FXCollections.observableArrayList(excursieList);
        allTable.setItems(excursieObservableList);
    }

    public void refresh(List<Excursie> forSelector, List<Excursie> forMainPage) throws AgentieException {
//        List<Excursie> excursieList = service.getAllExcursies();
        excursieObservableList = FXCollections.observableArrayList(forSelector);
//        excursieObservableList.clear();
//        excursieObservableList.addAll(excursieList);
        allTable.setItems(excursieObservableList);
        mainPageController.refresh(forMainPage);
    }

    @Override
    public void update(List<Excursie> forSelector, List<Excursie> forMainPage) throws AgentieException {
        Platform.runLater(()->{
            try {
                refresh(forSelector, forMainPage);
            } catch (AgentieException e) {
                e.printStackTrace();
            }
        });
    }
}
