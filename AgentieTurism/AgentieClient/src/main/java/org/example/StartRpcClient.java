package org.example;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.stage.Stage;
import org.example.controllers.LoginController;
import org.example.controllers.MainPageController;
import org.example.controllers.ReservationController;
import org.example.controllers.SelectorController;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class StartRpcClient extends Application {
    private static final int defaultAgentiePort = 55555;
    private static final String defaultServer = "localhost";

    public static void main(String[] args) {
        launch();
    }
    @Override
    public void start(Stage stage) throws IOException {
        Properties props=new Properties();
        try {
            props.load(StartRpcClient.class.getResourceAsStream("/agentclient.properties"));
        } catch (IOException e) {
            System.out.println("Cannot find bd.config "+e);
            return;
        }
        String serverIP = props.getProperty("agent.server.host", defaultServer);
        int serverPort = defaultAgentiePort;
        try {
            serverPort = Integer.parseInt(props.getProperty("agent.server.port"));
        } catch (NumberFormatException ex) {
            System.err.println("Wrong port number " + ex.getMessage());
            System.out.println("Using default port: " + defaultAgentiePort);
        }
        System.out.println("Using server IP " + serverIP);
        System.out.println("Using server port " + serverPort);

        IServices server = new AgentieServicesRpcProxy(serverIP,serverPort);
        LoginController loginController = new LoginController();
        loginController.setService(server);
//        MainPageController.service = server;
//        ReservationController.service = server;
        loginController.start(new Stage());


    }
}
