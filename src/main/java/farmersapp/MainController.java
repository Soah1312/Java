package farmersapp;

import farmersapp.Produce;
import farmersapp.DatabaseService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class MainController {

    @FXML private TextField produceNameField;
    @FXML private TextField quantityField;
    @FXML private TextField farmerNameField;
    @FXML private Label statusLabel;
    
    @FXML private TableView<Produce> produceTable;
    @FXML private TableColumn<Produce, Long> idColumn;
    @FXML private TableColumn<Produce, String> produceColumn;
    @FXML private TableColumn<Produce, BigDecimal> quantityColumn;
    @FXML private TableColumn<Produce, String> farmerColumn;
    @FXML private TableColumn<Produce, String> dateColumn;
    
    private DatabaseService databaseService;
    private ObservableList<Produce> produceList;

    @FXML
    public void initialize() {
        databaseService = DatabaseService.getInstance();
        
        // Initialize table columns
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        produceColumn.setCellValueFactory(new PropertyValueFactory<>("produceName"));
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        farmerColumn.setCellValueFactory(new PropertyValueFactory<>("farmerName"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("dateAdded"));
        
        // Load data
        loadProduceData();
    }

    @FXML
    private void handleAddProduce() {
        try {
            String produceName = produceNameField.getText().trim();
            String quantityStr = quantityField.getText().trim();
            String farmerName = farmerNameField.getText().trim();
            
            if (produceName.isEmpty() || quantityStr.isEmpty() || farmerName.isEmpty()) {
                statusLabel.setText("⚠️ All fields are required!");
                statusLabel.setStyle("-fx-text-fill: red;");
                return;
            }
            
            BigDecimal quantity = new BigDecimal(quantityStr);
            
            Produce produce = new Produce();
            produce.setProduceName(produceName);
            produce.setQuantity(quantity);
            produce.setFarmerName(farmerName);
            produce.setDateAdded(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            
            databaseService.addProduce(produce);
            
            statusLabel.setText("✅ Produce added successfully!");
            statusLabel.setStyle("-fx-text-fill: green;");
            
            // Clear fields
            produceNameField.clear();
            quantityField.clear();
            farmerNameField.clear();
            
            // Refresh table
            loadProduceData();
            
        } catch (NumberFormatException e) {
            statusLabel.setText("⚠️ Quantity must be a valid number!");
            statusLabel.setStyle("-fx-text-fill: red;");
        } catch (SQLException e) {
            statusLabel.setText("❌ Database Error: " + e.getMessage());
            statusLabel.setStyle("-fx-text-fill: red;");
        } catch (Exception e) {
            statusLabel.setText("❌ Error: " + e.getMessage());
            statusLabel.setStyle("-fx-text-fill: red;");
        }
    }

    @FXML
    private void handleRefresh() {
        loadProduceData();
        statusLabel.setText("🔄 Data refreshed!");
        statusLabel.setStyle("-fx-text-fill: blue;");
    }

    private void loadProduceData() {
        try {
            produceList = FXCollections.observableArrayList(databaseService.getAllProduce());
            produceTable.setItems(produceList);
        } catch (SQLException e) {
            statusLabel.setText("❌ Error loading data: " + e.getMessage());
            statusLabel.setStyle("-fx-text-fill: red;");
        }
    }
}
