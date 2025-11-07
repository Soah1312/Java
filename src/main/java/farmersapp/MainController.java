package farmersapp;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.math.BigDecimal;
import java.sql.SQLException;

public class MainController {

    // Farmer fields
    @FXML private TextField farmerNameField;
    @FXML private TextField farmerPhoneField;
    @FXML private TextField farmerLocationField;
    @FXML private Label farmerStatusLabel;
    @FXML private TableView<Farmer> farmersTable;
    @FXML private TableColumn<Farmer, Long> farmerIdColumn;
    @FXML private TableColumn<Farmer, String> farmerNameColumn;
    @FXML private TableColumn<Farmer, String> farmerPhoneColumn;
    @FXML private TableColumn<Farmer, String> farmerLocationColumn;
    
    // Produce fields
    @FXML private TextField produceNameField;
    @FXML private TextField quantityField;
    @FXML private ComboBox<Farmer> farmerComboBox;
    @FXML private Label statusLabel;
    @FXML private TableView<Produce> produceTable;
    @FXML private TableColumn<Produce, Long> idColumn;
    @FXML private TableColumn<Produce, String> produceColumn;
    @FXML private TableColumn<Produce, BigDecimal> quantityColumn;
    @FXML private TableColumn<Produce, String> farmerColumn;
    @FXML private TableColumn<Produce, String> dateColumn;
    
    private DatabaseService databaseService;
    private ObservableList<Produce> produceList;
    private ObservableList<Farmer> farmerList;

    @FXML
    public void initialize() {
        databaseService = DatabaseService.getInstance();
        
        // Initialize farmer table columns
        farmerIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        farmerNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        farmerPhoneColumn.setCellValueFactory(new PropertyValueFactory<>("phone"));
        farmerLocationColumn.setCellValueFactory(new PropertyValueFactory<>("location"));
        
        // Initialize produce table columns
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        produceColumn.setCellValueFactory(new PropertyValueFactory<>("produceName"));
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        farmerColumn.setCellValueFactory(new PropertyValueFactory<>("farmerName"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("dateAdded"));
        
        // Load data
        loadFarmerData();
        loadProduceData();
        populateFarmerComboBox();
    }

    @FXML
    private void handleAddFarmer() {
        try {
            String name = farmerNameField.getText().trim();
            String phone = farmerPhoneField.getText().trim();
            String location = farmerLocationField.getText().trim();
            
            if (name.isEmpty()) {
                farmerStatusLabel.setText("WARNING: Farmer name is required!");
                farmerStatusLabel.setStyle("-fx-text-fill: red;");
                return;
            }
            
            Farmer farmer = new Farmer();
            farmer.setName(name);
            farmer.setPhone(phone);
            farmer.setLocation(location);
            
            databaseService.addFarmer(farmer);
            
            farmerStatusLabel.setText("SUCCESS: Farmer added successfully!");
            farmerStatusLabel.setStyle("-fx-text-fill: green;");
            
            // Clear fields
            farmerNameField.clear();
            farmerPhoneField.clear();
            farmerLocationField.clear();
            
            // Refresh data
            loadFarmerData();
            populateFarmerComboBox();
            
        } catch (SQLException e) {
            farmerStatusLabel.setText("ERROR: Database Error: " + e.getMessage());
            farmerStatusLabel.setStyle("-fx-text-fill: red;");
        }
    }

    @FXML
    private void handleAddProduce() {
        try {
            String produceName = produceNameField.getText().trim();
            String quantityStr = quantityField.getText().trim();
            Farmer selectedFarmer = farmerComboBox.getValue();
            
            if (produceName.isEmpty() || quantityStr.isEmpty() || selectedFarmer == null) {
                statusLabel.setText("WARNING: All fields are required!");
                statusLabel.setStyle("-fx-text-fill: red;");
                return;
            }
            
            BigDecimal quantity = new BigDecimal(quantityStr);
            
            Produce produce = new Produce();
            produce.setProduceName(produceName);
            produce.setQuantity(quantity);
            produce.setFarmerId(selectedFarmer.getId());
            
            databaseService.addProduce(produce);
            
            statusLabel.setText("SUCCESS: Produce added successfully!");
            statusLabel.setStyle("-fx-text-fill: green;");
            
            // Clear fields
            produceNameField.clear();
            quantityField.clear();
            farmerComboBox.setValue(null);
            
            // Refresh table
            loadProduceData();
            
        } catch (NumberFormatException e) {
            statusLabel.setText("WARNING: Quantity must be a valid number!");
            statusLabel.setStyle("-fx-text-fill: red;");
        } catch (SQLException e) {
            statusLabel.setText("ERROR: Database Error: " + e.getMessage());
            statusLabel.setStyle("-fx-text-fill: red;");
        } catch (Exception e) {
            statusLabel.setText("ERROR: " + e.getMessage());
            statusLabel.setStyle("-fx-text-fill: red;");
        }
    }

    @FXML
    private void handleRefresh() {
        loadProduceData();
        statusLabel.setText("Produce data refreshed!");
        statusLabel.setStyle("-fx-text-fill: blue;");
    }

    @FXML
    private void handleRefreshFarmers() {
        loadFarmerData();
        farmerStatusLabel.setText("Farmer data refreshed!");
        farmerStatusLabel.setStyle("-fx-text-fill: blue;");
    }

    private void loadProduceData() {
        try {
            produceList = FXCollections.observableArrayList(databaseService.getAllProduce());
            produceTable.setItems(produceList);
        } catch (SQLException e) {
            statusLabel.setText("ERROR: Error loading data: " + e.getMessage());
            statusLabel.setStyle("-fx-text-fill: red;");
        }
    }

    private void loadFarmerData() {
        try {
            farmerList = FXCollections.observableArrayList(databaseService.getAllFarmers());
            farmersTable.setItems(farmerList);
        } catch (SQLException e) {
            farmerStatusLabel.setText("ERROR: Error loading farmers: " + e.getMessage());
            farmerStatusLabel.setStyle("-fx-text-fill: red;");
        }
    }

    private void populateFarmerComboBox() {
        try {
            ObservableList<Farmer> farmers = FXCollections.observableArrayList(databaseService.getAllFarmers());
            farmerComboBox.setItems(farmers);
        } catch (SQLException e) {
            statusLabel.setText("ERROR: Error loading farmers: " + e.getMessage());
            statusLabel.setStyle("-fx-text-fill: red;");
        }
    }
}
