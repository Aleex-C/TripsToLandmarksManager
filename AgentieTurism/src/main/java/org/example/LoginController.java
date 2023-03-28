package org.example;

import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.css.themes.MFXThemeManager;
import io.github.palexdev.materialfx.css.themes.Themes;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.example.services.Service;

import java.io.IOException;

public class LoginController extends Application {
    public static Service service;
    @FXML
    MFXButton xBtn;
    @FXML
    MFXButton loginButton;
    @FXML
    public void onClick() throws IOException{
        Stage thisStage = (Stage) xBtn.getScene().getWindow();
        thisStage.close();
    }
    @Override
    public void start(Stage stage) throws IOException {
        stage.initStyle(StageStyle.UNDECORATED);
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/login.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 314);
        MFXThemeManager.addOn(scene, Themes.DEFAULT, Themes.DEFAULT);
        stage.setTitle("Login");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }
    @FXML
    public void onLoginButtonClick() throws IOException{
            Stage thisStage = (Stage) loginButton.getScene().getWindow();
            thisStage.hide();
            SelectorController selectorController = new SelectorController();
            SelectorController.service = service;
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("/selector.fxml"));
            Stage login_stage = new Stage();
            Scene scene = new Scene(loader.load());

            MFXThemeManager.addOn(scene, Themes.DEFAULT, Themes.DEFAULT);
            login_stage.setScene(scene);
            login_stage.show();
            login_stage.setOnCloseRequest(e->thisStage.show());
            login_stage.setOnHidden(e->thisStage.show());


    }

}
