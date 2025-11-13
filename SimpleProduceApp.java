import javafx.application.Application; // JavaFX app entry point, basically the main UI class
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

import java.sql.*; // Using H2 through plain JDBC because it's simple

public class SimpleProduceApp extends Application {
    // DB connection info. Local file DB next to the app. No password. Keeping it simple.
    private static final String JDBC_URL = "jdbc:h2:./data/produce_db;AUTO_SERVER=TRUE";
    private static final String JDBC_USER = "sa";
    private static final String JDBC_PASS = "";

    // Table shows what I already have. Data holds the rows. Form is the add section.
    private TableView<Produce> table;
    private ObservableList<Produce> data;
    private GridPane form;

    @Override
    public void start(Stage stage) throws Exception {
        // Make sure the table exists in the DB before anything else
        ensureSchema();

        BorderPane root = new BorderPane(); // This is the main layout: top/center/bottom

        Button dashboardBtn = new Button("Dashboard"); // This toggles the add form on/off
        HBox top = new HBox(10, new Label("Farmers Produce Tracker"), dashboardBtn);
        top.setPadding(new Insets(10));
        root.setTop(top);

        table = new TableView<>(); // The table where I show produce
        TableColumn<Produce, String> nameCol = new TableColumn<>("Produce");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<Produce, Number> kgCol = new TableColumn<>("Kg");
        kgCol.setCellValueFactory(c -> new SimpleDoubleProperty(c.getValue().getKg()));

        TableColumn<Produce, Number> priceCol = new TableColumn<>("Price");
        priceCol.setCellValueFactory(c -> new SimpleDoubleProperty(c.getValue().getPrice()));

        
        table.getColumns().add(nameCol);
        table.getColumns().add(kgCol);
        table.getColumns().add(priceCol);
        
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN); // auto-fit columns

        data = FXCollections.observableArrayList(); // in-memory list backing the table
        table.setItems(data); // hook the list to the table
        root.setCenter(table);

        form = new GridPane(); // This is the add form
        form.setPadding(new Insets(12));
        form.setHgap(10);
        form.setVgap(8);

        TextField nameField = new TextField();
        TextField kgField = new TextField();
        TextField priceField = new TextField();
        Button addBtn = new Button("Add"); // when I click this, it writes to DB and shows in table

        form.addRow(0, new Label("Produce:"), nameField);
        form.addRow(1, new Label("Kg:"), kgField);
        form.addRow(2, new Label("Price:"), priceField);
        form.add(addBtn, 1, 3);

        addBtn.setOnAction(e -> { // save a new row when I hit Add
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

        form.setVisible(false); // start hidden because I want it clean
        form.setManaged(false); // not managed so it doesn't take space
        dashboardBtn.setOnAction(e -> { // clicking dashboard shows/hides the form like a switch
            boolean showing = form.isVisible();
            form.setVisible(!showing);
            form.setManaged(!showing);
            if (!showing) {
                table.requestFocus();
            }
        });

        root.setBottom(form);

        loadData(); // pull whatever is in the DB into the table

        Scene scene = new Scene(root, 800, 500); // decent size window, nothing fancy
        stage.setTitle("Produce Dashboard");
        stage.setScene(scene);
        stage.show();
    }

    private void loadData() { // read all rows from DB and fill the table
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

    private void ensureSchema() throws SQLException { // make the table if it doesn't exist
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

    private long insertProduce(String name, double kg, double price) throws SQLException { // insert one row and give back its id
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

    private void showAlert(Alert.AlertType type, String title, String msg) { // quick popup to tell me stuff
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }

    public static void main(String[] args) { // start the JavaFX app, standard thing
        launch(args);
    }
}
