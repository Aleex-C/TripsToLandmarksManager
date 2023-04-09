package org.example.controllers;

import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXPasswordField;
import io.github.palexdev.materialfx.controls.MFXTextField;
import io.github.palexdev.materialfx.css.themes.MFXThemeManager;
import io.github.palexdev.materialfx.css.themes.Themes;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.example.AgentieException;
import org.example.IServices;
import org.example.StartRpcClient;
import org.example.domain.Agentie;

import java.io.IOException;

public class LoginController extends Application  {
    private IServices service;

    public void setService(IServices service) {
        this.service = service;
    }

    @FXML
    MFXButton xBtn;
    @FXML
    MFXButton loginButton;
    @FXML
    MFXTextField usernameField;
    @FXML
    MFXPasswordField passwordField;
    private static double xOffset = 0;
    private static double yOffset = 0;
    @FXML
    public void onClick() throws IOException{
        Stage thisStage = (Stage) xBtn.getScene().getWindow();
        thisStage.close();
    }
    @Override
    public void start(Stage stage) throws IOException {
        stage.initStyle(StageStyle.UNDECORATED);
        FXMLLoader fxmlLoader = new FXMLLoader(StartRpcClient.class.getResource("/login.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 314);
        LoginController lgnController = fxmlLoader.getController();
        lgnController.setService(service);
        MFXThemeManager.addOn(scene, Themes.DEFAULT, Themes.DEFAULT);
        stage.setTitle("Login");
        stage.setScene(scene);
        stage.setResizable(false);
        Parent root = fxmlLoader.getRoot();
        root.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                xOffset = stage.getX() - event.getScreenX();
                yOffset = stage.getY() - event.getScreenY();
            }
        });
        root.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                stage.setX(event.getScreenX() + xOffset);
                stage.setY(event.getScreenY() + yOffset);
            }
        });

        stage.show();
    }
    @FXML
    public void onLoginButtonClick() throws IOException, AgentieException {
            FXMLLoader loader = new FXMLLoader(StartRpcClient.class.getResource("/selector.fxml"));
            Parent root = loader.load();
            SelectorController selectorController = loader.getController();
            selectorController.setService(this.service);
            String username = usernameField.getText();
            String password = passwordField.getText();
//            SelectorController selectorController = new SelectorController();
            Agentie agentDTO = new Agentie("",username, password);
            try {
                Agentie agent = service.login(agentDTO, selectorController);
                if (agent != null){
                    Stage thisStage = (Stage) loginButton.getScene().getWindow();
                    thisStage.close();
                    selectorController.setCurrAgent(agent);
                    Stage login_stage = new Stage();
                    Scene scene = new Scene(root);
                    MFXThemeManager.addOn(scene, Themes.DEFAULT, Themes.DEFAULT);
                    login_stage.setScene(scene);
                    login_stage.setTitle(agent.toString());
                    login_stage.show();
                }
                else{
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error Dialog");
                    alert.setHeaderText("Something went wrong! The credentials don't match!");
                    alert.setContentText("No such user found!");
                    alert.showAndWait();
                }
            } catch (AgentieException e) {
                e.printStackTrace();
            }
    }
}
