import javafx.application.Application;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.sql.*;

public class SimpleProduceApp extends Application {
    private static final String JDBC_URL = "jdbc:h2:./data/produce_db;AUTO_SERVER=TRUE";
    private static final String JDBC_USER = "sa";
    private static final String JDBC_PASS = "";

    private TableView<Produce> table;
    private ObservableList<Produce> data;
    private GridPane form;

    @Override
    public void start(Stage stage) throws Exception {
        ensureSchema();

        BorderPane root = new BorderPane();

        Button dashboardBtn = new Button("Dashboard");
        HBox top = new HBox(10, new Label("Farmers Produce Tracker"), dashboardBtn);
        top.setPadding(new Insets(10));
        root.setTop(top);

        table = new TableView<>();
        TableColumn<Produce, String> nameCol = new TableColumn<>("Produce");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<Produce, Number> kgCol = new TableColumn<>("Kg");
        kgCol.setCellValueFactory(c -> new SimpleDoubleProperty(c.getValue().getKg()));

        TableColumn<Produce, Number> priceCol = new TableColumn<>("Price");
        priceCol.setCellValueFactory(c -> new SimpleDoubleProperty(c.getValue().getPrice()));

        TableColumn<Produce, Number> totalCol = new TableColumn<>("Total");
        totalCol.setCellValueFactory(c -> new SimpleDoubleProperty(c.getValue().getTotal()));

        table.getColumns().add(nameCol);
        table.getColumns().add(kgCol);
        table.getColumns().add(priceCol);
        table.getColumns().add(totalCol);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);

        data = FXCollections.observableArrayList();
        table.setItems(data);
        root.setCenter(table);

        form = new GridPane();
        form.setPadding(new Insets(12));
        form.setHgap(10);
        form.setVgap(8);

        TextField nameField = new TextField();
        TextField kgField = new TextField();
        TextField priceField = new TextField();
        Button addBtn = new Button("Add");

        form.addRow(0, new Label("Produce:"), nameField);
        form.addRow(1, new Label("Kg:"), kgField);
        form.addRow(2, new Label("Price:"), priceField);
        form.add(addBtn, 1, 3);

        addBtn.setOnAction(e -> {
            String name = nameField.getText().trim();
            if (name.isEmpty()) {
                showAlert(Alert.AlertType.WARNING, "Validation", "Produce name is required");
                return;
            }
            double kg;
            double price;
            try {
                kg = Double.parseDouble(kgField.getText().trim());
                price = Double.parseDouble(priceField.getText().trim());
            } catch (NumberFormatException ex) {
                showAlert(Alert.AlertType.WARNING, "Validation", "Kg and Price must be numbers");
                return;
            }
            try {
                long id = insertProduce(name, kg, price);
                data.add(new Produce(id, name, kg, price));
                nameField.clear();
                kgField.clear();
                priceField.clear();
            } catch (Exception ex) {
                showAlert(Alert.AlertType.ERROR, "DB Error", ex.getMessage());
            }
        });

        form.setVisible(false);
        form.setManaged(false);
        dashboardBtn.setOnAction(e -> {
            boolean showing = form.isVisible();
            form.setVisible(!showing);
            form.setManaged(!showing);
            if (!showing) {
                table.requestFocus();
            }
        });

        root.setBottom(form);

        loadData();

        Scene scene = new Scene(root, 800, 500);
        stage.setTitle("Produce Dashboard");
        stage.setScene(scene);
        stage.show();
    }

    private void loadData() {
        data.clear();
        try (Connection conn = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASS);
             PreparedStatement ps = conn.prepareStatement("SELECT id, name, kg, price FROM produce ORDER BY id")) {
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    data.add(new Produce(
                            rs.getLong("id"),
                            rs.getString("name"),
                            rs.getDouble("kg"),
                            rs.getDouble("price")
                    ));
                }
            }
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "DB Error", e.getMessage());
        }
    }

    private void ensureSchema() throws SQLException {
        try (Connection conn = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASS);
             Statement st = conn.createStatement()) {
            st.executeUpdate(
                    "CREATE TABLE IF NOT EXISTS produce (" +
                            "id IDENTITY PRIMARY KEY, " +
                            "name VARCHAR(255) NOT NULL, " +
                            "kg DOUBLE NOT NULL, " +
                            "price DOUBLE NOT NULL, " +
                            "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP)"
            );
        }
    }

    private long insertProduce(String name, double kg, double price) throws SQLException {
        try (Connection conn = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASS);
             PreparedStatement ps = conn.prepareStatement(
                     "INSERT INTO produce(name, kg, price) VALUES(?,?,?)",
                     Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, name);
            ps.setDouble(2, kg);
            ps.setDouble(3, price);
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) return keys.getLong(1);
            }
        }
        return -1L;
    }

    private void showAlert(Alert.AlertType type, String title, String msg) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
