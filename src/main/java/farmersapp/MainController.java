package farmersapp;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.math.BigDecimal;
import java.sql.SQLException;

// this is the controller class that handles all the button clicks and stuff
public class MainController {

    // all these @FXML things connect to the ui elements in the fxml file
    
    // farmer input fields
    @FXML private TextField farmerNameField;
    @FXML private TextField farmerPhoneField;
    @FXML private TextField farmerLocationField;
    @FXML private Label farmerStatusLabel; // shows messages like "farmer added successfully"
    
    // farmer table stuff
    @FXML private TableView<Farmer> farmersTable;
    @FXML private TableColumn<Farmer, Long> farmerIdColumn;
    @FXML private TableColumn<Farmer, String> farmerNameColumn;
    @FXML private TableColumn<Farmer, String> farmerPhoneColumn;
    @FXML private TableColumn<Farmer, String> farmerLocationColumn;
    
    // produce input fields
    @FXML private TextField produceNameField;
    @FXML private TextField quantityField;
    @FXML private ComboBox<Farmer> farmerComboBox; // dropdown to pick a farmer
    @FXML private Label statusLabel;
    
    // produce table stuff
    @FXML private TableView<Produce> produceTable;
    @FXML private TableColumn<Produce, Long> idColumn;
    @FXML private TableColumn<Produce, String> produceColumn;
    @FXML private TableColumn<Produce, BigDecimal> quantityColumn;
    @FXML private TableColumn<Produce, String> farmerColumn;
    @FXML private TableColumn<Produce, String> dateColumn;
    
    private DatabaseService databaseService; // this talks to the database
    private ObservableList<Produce> produceList; // list for the produce table
    private ObservableList<Farmer> farmerList; // list for the farmer table

    // this runs automatically when the app starts
    @FXML
    public void initialize() {
        databaseService = DatabaseService.getInstance();
        
        // connect table columns to the data fields
        // took me forever to figure out PropertyValueFactory lol
        farmerIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        farmerNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        farmerPhoneColumn.setCellValueFactory(new PropertyValueFactory<>("phone"));
        farmerLocationColumn.setCellValueFactory(new PropertyValueFactory<>("location"));
        
        // same thing for produce table
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        produceColumn.setCellValueFactory(new PropertyValueFactory<>("produceName"));
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        farmerColumn.setCellValueFactory(new PropertyValueFactory<>("farmerName"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("dateAdded"));
        
        // load all the data when app starts
        loadFarmerData();
        loadProduceData();
        populateFarmerComboBox();
    }

    // runs when you click "add farmer" button
    @FXML
    private void handleAddFarmer() {
        try {
            // get text from input fields
            String name = farmerNameField.getText().trim();
            String phone = farmerPhoneField.getText().trim();
            String location = farmerLocationField.getText().trim();
            
            // check if name is empty
            if (name.isEmpty()) {
                farmerStatusLabel.setText("you need to enter a name!");
                farmerStatusLabel.setStyle("-fx-text-fill: red;");
                return;
            }
            
            // make a new farmer object
            Farmer farmer = new Farmer();
            farmer.setName(name);
            farmer.setPhone(phone);
            farmer.setLocation(location);
            
            // save to database
            databaseService.addFarmer(farmer);
            
            farmerStatusLabel.setText("farmer added!");
            farmerStatusLabel.setStyle("-fx-text-fill: green;");
            
            // clear the input fields
            farmerNameField.clear();
            farmerPhoneField.clear();
            farmerLocationField.clear();
            
            // refresh the table and dropdown
            loadFarmerData();
            populateFarmerComboBox();
            
        } catch (SQLException e) {
            farmerStatusLabel.setText("database error: " + e.getMessage());
            farmerStatusLabel.setStyle("-fx-text-fill: red;");
        }
    }

    // runs when you click "add produce" button
    @FXML
    private void handleAddProduce() {
        try {
            // get input values
            String produceName = produceNameField.getText().trim();
            String quantityStr = quantityField.getText().trim();
            Farmer selectedFarmer = farmerComboBox.getValue();
            
            // make sure everything is filled in
            if (produceName.isEmpty() || quantityStr.isEmpty() || selectedFarmer == null) {
                statusLabel.setText("fill in all fields!");
                statusLabel.setStyle("-fx-text-fill: red;");
                return;
            }
            
            // convert quantity string to number
            BigDecimal quantity = new BigDecimal(quantityStr);
            
            // make new produce object
            Produce produce = new Produce();
            produce.setProduceName(produceName);
            produce.setQuantity(quantity);
            produce.setFarmerId(selectedFarmer.getId());
            
            // save to database
            databaseService.addProduce(produce);
            
            statusLabel.setText("produce added!");
            statusLabel.setStyle("-fx-text-fill: green;");
            
            // clear inputs
            produceNameField.clear();
            quantityField.clear();
            farmerComboBox.setValue(null);
            
            // refresh table
            loadProduceData();
            
        } catch (NumberFormatException e) {
            statusLabel.setText("quantity needs to be a number!");
            statusLabel.setStyle("-fx-text-fill: red;");
        } catch (SQLException e) {
            statusLabel.setText("database error: " + e.getMessage());
            statusLabel.setStyle("-fx-text-fill: red;");
        } catch (Exception e) {
            statusLabel.setText("error: " + e.getMessage());
            statusLabel.setStyle("-fx-text-fill: red;");
        }
    }

    // refresh produce table button
    @FXML
    private void handleRefresh() {
        loadProduceData();
        statusLabel.setText("refreshed!");
        statusLabel.setStyle("-fx-text-fill: blue;");
    }

    // refresh farmers table button
    @FXML
    private void handleRefreshFarmers() {
        loadFarmerData();
        farmerStatusLabel.setText("refreshed!");
        farmerStatusLabel.setStyle("-fx-text-fill: blue;");
    }

    // get all produce from database and show in table
    private void loadProduceData() {
        try {
            produceList = FXCollections.observableArrayList(databaseService.getAllProduce());
            produceTable.setItems(produceList);
        } catch (SQLException e) {
            statusLabel.setText("error loading data: " + e.getMessage());
            statusLabel.setStyle("-fx-text-fill: red;");
        }
    }

    // get all farmers from database and show in table
    private void loadFarmerData() {
        try {
            farmerList = FXCollections.observableArrayList(databaseService.getAllFarmers());
            farmersTable.setItems(farmerList);
        } catch (SQLException e) {
            farmerStatusLabel.setText("error loading farmers: " + e.getMessage());
            farmerStatusLabel.setStyle("-fx-text-fill: red;");
        }
    }

    // fill the dropdown with all farmers
    private void populateFarmerComboBox() {
        try {
            ObservableList<Farmer> farmers = FXCollections.observableArrayList(databaseService.getAllFarmers());
            farmerComboBox.setItems(farmers);
        } catch (SQLException e) {
            statusLabel.setText("error loading farmers: " + e.getMessage());
            statusLabel.setStyle("-fx-text-fill: red;");
        }
    }
}
