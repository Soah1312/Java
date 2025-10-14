package com.farmstock;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws IOException {
        SceneManager.initialize(primaryStage);
        SceneManager.switchScene("/com/farmstock/view/main-view.fxml", "FarmStock Manager");
    }

    public static void main(String[] args) {
        launch(args);
    }
}
