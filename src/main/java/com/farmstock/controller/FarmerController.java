package com.farmstock.controller;

import com.farmstock.SceneManager;
import com.farmstock.model.Product;
import com.farmstock.service.ProductService;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class FarmerController {

    @FXML
    private TextField nameField;

    @FXML
    private TextField categoryField;

    @FXML
    private TextField priceField;

    @FXML
    private TextField quantityField;

    @FXML
    private TableView<Product> productTable;

    @FXML
    private TableColumn<Product, Number> idColumn;

    @FXML
    private TableColumn<Product, String> nameColumn;

    @FXML
    private TableColumn<Product, String> categoryColumn;

    @FXML
    private TableColumn<Product, Number> priceColumn;

    @FXML
    private TableColumn<Product, Number> quantityColumn;

    @FXML
    private TableColumn<Product, LocalDate> dateColumn;

    private final ProductService productService = new ProductService();
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MMM dd, yyyy");

    @FXML
    private void initialize() {
        configureTable();
        refreshProducts();
    }

    @FXML
    private void handleAddProduct() {
        String name = nameField.getText();
        String category = categoryField.getText();
        String priceText = priceField.getText();
        String quantityText = quantityField.getText();

        if (name == null || name.isBlank() || category == null || category.isBlank()
            || priceText == null || priceText.isBlank() || quantityText == null || quantityText.isBlank()) {
            showAlert(Alert.AlertType.WARNING, "Validation Error", "Please fill out all fields before adding a product.");
            return;
        }

        BigDecimal price;
        int quantity;
        try {
            price = new BigDecimal(priceText);
            if (price.signum() < 0) {
                showAlert(Alert.AlertType.WARNING, "Validation Error", "Price must be a positive number.");
                return;
            }
        } catch (NumberFormatException ex) {
            showAlert(Alert.AlertType.ERROR, "Invalid Price", "Please enter a valid numeric price.");
            return;
        }

        try {
            quantity = Integer.parseInt(quantityText);
            if (quantity < 0) {
                showAlert(Alert.AlertType.WARNING, "Validation Error", "Quantity cannot be negative.");
                return;
            }
        } catch (NumberFormatException ex) {
            showAlert(Alert.AlertType.ERROR, "Invalid Quantity", "Please enter a valid integer quantity.");
            return;
        }

        Product product = new Product(name.trim(), category.trim(), price, quantity);
        try {
            productService.saveProduct(product);
            clearForm();
            refreshProducts();
            showAlert(Alert.AlertType.INFORMATION, "Success", "Product added successfully.");
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Database Error", "Unable to add product: " + e.getMessage());
        }
    }

    @FXML
    private void handleRefresh() {
        refreshProducts();
    }

    private void configureTable() {
        idColumn.setCellValueFactory(cellData -> cellData.getValue().idProperty());
        nameColumn.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        categoryColumn.setCellValueFactory(cellData -> cellData.getValue().categoryProperty());
        priceColumn.setCellValueFactory(cellData -> cellData.getValue().priceProperty());
        quantityColumn.setCellValueFactory(cellData -> cellData.getValue().quantityProperty());
        dateColumn.setCellValueFactory(cellData -> cellData.getValue().dateAddedProperty());
        dateColumn.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(dateFormatter.format(item));
                }
            }
        });
    }

    private void refreshProducts() {
        try {
            ObservableList<Product> products = productService.fetchAllProducts();
            productTable.setItems(products);
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to load products: " + e.getMessage());
        }
    }

    @FXML
    private void handleBack() {
        try {
            SceneManager.switchScene("/com/farmstock/view/main-view.fxml", "FarmStock Manager");
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Navigation Error", "Unable to return to main menu: " + e.getMessage());
        }
    }

    private void clearForm() {
        nameField.clear();
        categoryField.clear();
        priceField.clear();
        quantityField.clear();
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
