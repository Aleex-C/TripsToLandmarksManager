package org.example;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.example.domain.Agentie;
import org.example.domain.Excursie;
import org.example.domain.Rezervare;
import org.example.repository.AgentieRepository;
import org.example.repository.ExcursieRepository;
import org.example.repository.IRepository;
import org.example.repository.RezervareRepository;
import org.example.services.Service;

import java.io.FileReader;
import java.io.IOException;
import java.time.LocalTime;
import java.util.Properties;

public class Main extends Application {
    public static void main(String[] args) {
        launch();
    }
    @Override
    public void start(Stage stage) throws IOException{
        Properties props=new Properties();
        try {
            props.load(new FileReader("bd.properties"));
        } catch (IOException e) {
            System.out.println("Cannot find bd.config "+e);
        }
        ExcursieRepository excursieRepository = new ExcursieRepository(props);
        AgentieRepository agentieRepository = new AgentieRepository(props);
        RezervareRepository rezervareRepository = new RezervareRepository(props);
        Service service = new Service(agentieRepository, rezervareRepository, excursieRepository);
        LoginController loginController = new LoginController();
        LoginController.service = service;
        loginController.start(new Stage());
    }
}