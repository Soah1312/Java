package com.farmstock.controller;

import com.farmstock.SceneManager;
import javafx.event.ActionEvent;

import java.io.IOException;

public class MainController {

    public void handleFarmerView(ActionEvent event) throws IOException {
        SceneManager.switchScene("/com/farmstock/view/farmer-view.fxml", "Farmer Dashboard");
    }

    public void handleBuyerView(ActionEvent event) throws IOException {
        SceneManager.switchScene("/com/farmstock/view/buyer-view.fxml", "Buyer Dashboard");
    }
}
