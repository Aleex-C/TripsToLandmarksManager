package org.example;

import io.github.palexdev.materialfx.controls.MFXTableColumn;
import io.github.palexdev.materialfx.controls.MFXTableView;
import io.github.palexdev.materialfx.controls.cell.MFXTableRowCell;
import io.github.palexdev.materialfx.css.themes.MFXThemeManager;
import io.github.palexdev.materialfx.css.themes.Themes;
import io.github.palexdev.materialfx.filter.StringFilter;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.example.domain.Excursie;
import org.example.services.Service;

import java.io.IOException;
import java.net.URL;
import java.util.Comparator;
import java.util.List;
import java.util.ResourceBundle;

public class MainPageController implements Initializable {
    public static Service service;
    @FXML
    MFXTableView<Excursie> excursieMFXTableView;
    public static List<Excursie> excursieList;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
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
        ObservableList<Excursie> excursieObservableList = FXCollections.observableArrayList(excursieList);
        excursieMFXTableView.setItems(excursieObservableList);
    }
    @FXML
    public void itemSelected() throws IOException{
        if(excursieMFXTableView.getSelectionModel().getSelectedValue()!=null){
            Excursie exc = excursieMFXTableView.getSelectionModel().getSelectedValue();
            ReservationController reservationController = new ReservationController();
            reservationController.service = service;
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("/reservation.fxml"));
            Stage reservationStage = new Stage();
            Scene scene = new Scene(loader.load());
//            MFXThemeManager.addOn(scene, Themes.DEFAULT, Themes.DEFAULT);
            reservationStage.setScene(scene);
            reservationStage.show();
        }
    }
}
