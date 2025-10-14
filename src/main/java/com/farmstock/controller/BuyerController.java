package com.farmstock.controller;

import com.farmstock.SceneManager;
import com.farmstock.model.Product;
import com.farmstock.service.ProductService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class BuyerController {

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

    @FXML
    private TextField searchField;

    private final ProductService productService = new ProductService();
    private final ObservableList<Product> masterData = FXCollections.observableArrayList();
    private final FilteredList<Product> filteredData = new FilteredList<>(masterData, p -> true);
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MMM dd, yyyy");

    @FXML
    private void initialize() {
        configureTable();
        SortedList<Product> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(productTable.comparatorProperty());
        productTable.setItems(sortedData);
        searchField.textProperty().addListener((obs, oldValue, newValue) -> applyFilter(newValue));
        loadProducts();
    }

    @FXML
    private void handleRefresh() {
        loadProducts();
    }

    @FXML
    private void handleBack() {
        try {
            SceneManager.switchScene("/com/farmstock/view/main-view.fxml", "FarmStock Manager");
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Navigation Error", "Unable to return to main menu: " + e.getMessage());
        }
    }

    private void loadProducts() {
        try {
            masterData.setAll(productService.fetchAllProducts());
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to load products: " + e.getMessage());
        }
    }

    private void applyFilter(String query) {
        if (query == null || query.isBlank()) {
            filteredData.setPredicate(product -> true);
            return;
        }
        String lowerQuery = query.toLowerCase(Locale.ROOT).trim();
        filteredData.setPredicate(product ->
                product.getName().toLowerCase(Locale.ROOT).contains(lowerQuery)
                        || product.getCategory().toLowerCase(Locale.ROOT).contains(lowerQuery)
        );
    }

    private void configureTable() {
        idColumn.setCellValueFactory(cellData -> cellData.getValue().idProperty());
        nameColumn.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        categoryColumn.setCellValueFactory(cellData -> cellData.getValue().categoryProperty());
        priceColumn.setCellValueFactory(cellData -> cellData.getValue().priceProperty());
        quantityColumn.setCellValueFactory(cellData -> cellData.getValue().quantityProperty());
        dateColumn.setCellValueFactory(cellData -> cellData.getValue().dateAddedProperty());

        priceColumn.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(Number item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(String.format(Locale.US, "%.2f", item.doubleValue()));
                }
            }
        });

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

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
