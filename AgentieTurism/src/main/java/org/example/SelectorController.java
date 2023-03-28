package org.example;

import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import io.github.palexdev.materialfx.controls.MFXTableColumn;
import io.github.palexdev.materialfx.controls.MFXTableView;
import io.github.palexdev.materialfx.controls.cell.MFXTableRowCell;
import io.github.palexdev.materialfx.css.themes.MFXThemeManager;
import io.github.palexdev.materialfx.css.themes.Themes;
import io.github.palexdev.materialfx.filter.StringFilter;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.example.domain.Excursie;
import org.example.services.Service;

import java.io.IOException;
import java.net.URL;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.ResourceBundle;

public class SelectorController implements Initializable {
    public static Service service;
    private ObservableList<String> landmarksList;
    @FXML
    MFXButton nextBtn;
    @FXML
    MFXTableView allTable;
    @FXML
    TextField startTime;
    @FXML
    TextField endTime;
    @FXML
    MFXComboBox <String> landmarks;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setupCombo();
        setupTable();
    }
    public void setupCombo(){
         List<String> landmarkStrings = service.findAllLandmarks();
         landmarksList = FXCollections.observableArrayList(landmarkStrings);
         landmarks.setItems(landmarksList);
    }
    @FXML
    public void onNextButtonClick() throws IOException {
        Stage thisStage = (Stage) nextBtn.getScene().getWindow();
        String landmark = landmarks.getSelectedItem();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        LocalTime t1 = LocalTime.parse(startTime.getText(), formatter);
        LocalTime t2 = LocalTime.parse(endTime.getText(), formatter);
        thisStage.hide();
        MainPageController mainPageController = new MainPageController();
        MainPageController.excursieList = service.findByLandmarkAndInterval(landmark, t1, t2);
        MainPageController.service = service;
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("/main_page.fxml"));
        Stage main_stage = new Stage();
        Scene scene = new Scene(loader.load());

        MFXThemeManager.addOn(scene, Themes.DEFAULT, Themes.DEFAULT);
        main_stage.setScene(scene);
        main_stage.show();
        main_stage.setOnCloseRequest(e->thisStage.show());
        main_stage.setOnHidden(e->thisStage.show());
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

        allTable.getTableColumns().addAll(transportCompanyColumn, priceColumn, departureTimeColumn, ticketsLeftColumn);
        allTable.getFilters().addAll(new StringFilter<>("Transport Company", Excursie::getTransport_company));
        List<Excursie> excursieList = (List<Excursie>) service.getAllExcursies();
        ObservableList<Excursie> excursieObservableList = FXCollections.observableArrayList(excursieList);
        allTable.setItems(excursieObservableList);
    }
}
