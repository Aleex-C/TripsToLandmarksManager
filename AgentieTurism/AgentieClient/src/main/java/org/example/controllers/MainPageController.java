package org.example.controllers;

import io.github.palexdev.materialfx.controls.MFXTableColumn;
import io.github.palexdev.materialfx.controls.MFXTableView;
import io.github.palexdev.materialfx.controls.cell.MFXTableRowCell;
import io.github.palexdev.materialfx.filter.StringFilter;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.example.AgentieException;
import org.example.IObserver;
import org.example.IServices;
import org.example.StartRpcClient;
import org.example.domain.Excursie;
import org.example.domain.Rezervare;

import java.io.IOException;
import java.net.URL;
import java.time.LocalTime;
import java.util.Comparator;
import java.util.List;
import java.util.ResourceBundle;

public class MainPageController {
    private IServices service;

    public void setService(IServices service) {
        this.service = service;
        init();
    }

    @FXML
    MFXTableView<Excursie> excursieMFXTableView;
    ObservableList<Excursie> excursieObservableList;
    private List<Excursie> excursieList;

    public void setExcursieList(List<Excursie> excursieList) {
        this.excursieList = excursieList;
    }

    public void setInformations(String landmark, LocalTime t1, LocalTime t2) {
        this.informations = new Wrapper(landmark, t1, t2);
    }

    private  Wrapper informations;

    public void init() {
        setupTable();
    }
    private void setupTable(){
        MFXTableColumn<Excursie> transportCompanyColumn = new MFXTableColumn<>("Transport Company", true, Comparator.comparing(Excursie::getTransport_company));
        MFXTableColumn<Excursie> priceColumn = new MFXTableColumn<>("Price", true, Comparator.comparing(Excursie::getPrice));
        MFXTableColumn<Excursie> departureTimeColumn = new MFXTableColumn<>("Departure at", true, Comparator.comparing(Excursie::getDeparture_time));
        MFXTableColumn<Excursie> ticketsLeftColumn = new MFXTableColumn<>("Available tickets", true, Comparator.comparing(Excursie::getAvailable_tickets));
//        MFXTableColumn<Excursie> reservationColum = new MFXTableColumn<>("Reservation column");
//        reservationColum.setRowCellFactory();
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

        /***
         * @TODO:
         *          - disable when not available tickets
         *            - A FAST UN-CSS'ed page of reservation
         *          COLOR RED WHEN OUT OF TICKETS
         */
        transportCompanyColumn.setAlignment(Pos.CENTER_RIGHT);
        priceColumn.setAlignment(Pos.CENTER_RIGHT);
        departureTimeColumn.setAlignment(Pos.CENTER_RIGHT);
        ticketsLeftColumn.setAlignment(Pos.CENTER_RIGHT);

        excursieMFXTableView.getTableColumns().addAll(transportCompanyColumn, priceColumn, departureTimeColumn, ticketsLeftColumn);
        excursieMFXTableView.getFilters().addAll(new StringFilter<>("Transport Company", Excursie::getTransport_company));
        excursieObservableList = FXCollections.observableArrayList(excursieList);
        excursieMFXTableView.setItems(excursieObservableList);
    }
    @FXML
    public void itemSelected() throws IOException{
        if(excursieMFXTableView.getSelectionModel().getSelectedValue()!=null){
            FXMLLoader loader = new FXMLLoader(StartRpcClient.class.getResource("/reservation.fxml"));
            Parent root = loader.load();
            ReservationController reservationController = loader.getController();
            Excursie exc = excursieMFXTableView.getSelectionModel().getSelectedValue();
            reservationController.setFrkTime(informations.getT1(), informations.getT2());
            reservationController.setService(service);
            reservationController.setSlectedExcursie(exc);
            Stage reservationStage = new Stage();
            Scene scene = new Scene(root);
//            MFXThemeManager.addOn(scene, Themes.DEFAULT, Themes.DEFAULT);
            reservationStage.setScene(scene);
            reservationStage.show();
//            reservationStage.setOnCloseRequest(e -> refresh());
        }

    }
    public void refresh(List<Excursie> exList) throws AgentieException {
//        service.findByLandmarkAndInterval(informations.getLandmark(), informations.getT1(), informations.getT2())
//        excursieObservableList.clear();
//        excursieObservableList.addAll(service.findByLandmarkAndInterval(informations.getLandmark(), informations.getT1(), informations.getT2()));
        excursieObservableList = FXCollections.observableArrayList(exList);
        excursieMFXTableView.setItems(excursieObservableList);
    }
}
class Wrapper{
    private String landmark;
    private LocalTime t1;

    public String getLandmark() {
        return landmark;
    }

    public LocalTime getT1() {
        return t1;
    }

    public LocalTime getT2() {
        return t2;
    }

    private LocalTime t2;

    public Wrapper(String landmark, LocalTime t1, LocalTime t2) {
        this.landmark = landmark;
        this.t1 = t1;
        this.t2 = t2;
    }
}
