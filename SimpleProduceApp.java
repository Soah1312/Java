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
        // Show login dialog first
        if (!showLoginDialog()) {
            stage.close();
            return;
        }

        // Make sure the table exists in the DB before anything else
        ensureSchema();

        BorderPane root = new BorderPane(); // This is the main layout: top/center/bottom

        Label farmerNameLabel = new Label("Farmer: Ram");
        farmerNameLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");
        Button dashboardBtn = new Button("Dashboard"); // This toggles the add form on/off
        HBox top = new HBox(10, farmerNameLabel, new Label("Farmers Produce Tracker"), dashboardBtn);
        top.setPadding(new Insets(10));
        root.setTop(top);

        table = new TableView<>(); // The table where I show produce
        TableColumn<Produce, String> nameCol = new TableColumn<>("Produce");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<Produce, Number> kgCol = new TableColumn<>("Kg");
        kgCol.setCellValueFactory(c -> new SimpleDoubleProperty(c.getValue().getKg()));

        TableColumn<Produce, Number> priceCol = new TableColumn<>("Price");
        priceCol.setCellValueFactory(c -> new SimpleDoubleProperty(c.getValue().getPrice()));

        TableColumn<Produce, Void> actionsCol = new TableColumn<>("Actions");
        actionsCol.setCellFactory(col -> new TableCell<>() {
            private final Button updateBtn = new Button("Update");
            private final Button deleteBtn = new Button("Delete");
            private final HBox buttons = new HBox(5, updateBtn, deleteBtn);

            {
                updateBtn.setOnAction(e -> {
                    Produce produce = getTableView().getItems().get(getIndex());
                    showUpdateDialog(produce);
                });

                deleteBtn.setOnAction(e -> {
                    Produce produce = getTableView().getItems().get(getIndex());
                    try {
                        deleteProduce(produce.getId());
                        data.remove(produce);
                    } catch (SQLException ex) {
                        showAlert(Alert.AlertType.ERROR, "DB Error", ex.getMessage());
                    }
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : buttons);
            }
        });
        
        table.getColumns().add(nameCol);
        table.getColumns().add(kgCol);
        table.getColumns().add(priceCol);
        table.getColumns().add(actionsCol);
        
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

    private void updateProduce(long id, String name, double kg, double price) throws SQLException { // update existing row
        try (Connection conn = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASS);
             PreparedStatement ps = conn.prepareStatement(
                     "UPDATE produce SET name=?, kg=?, price=? WHERE id=?")) {
            ps.setString(1, name);
            ps.setDouble(2, kg);
            ps.setDouble(3, price);
            ps.setLong(4, id);
            ps.executeUpdate();
        }
    }

    private void deleteProduce(long id) throws SQLException { // delete a row by id
        try (Connection conn = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASS);
             PreparedStatement ps = conn.prepareStatement("DELETE FROM produce WHERE id=?")) {
            ps.setLong(1, id);
            ps.executeUpdate();
        }
    }

    private void showUpdateDialog(Produce produce) { // popup dialog to edit existing produce
        Dialog<Produce> dialog = new Dialog<>();
        dialog.setTitle("Update Produce");
        dialog.setHeaderText("Edit " + produce.getName());

        ButtonType saveButtonType = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField nameField = new TextField(produce.getName());
        TextField kgField = new TextField(String.valueOf(produce.getKg()));
        TextField priceField = new TextField(String.valueOf(produce.getPrice()));

        grid.add(new Label("Produce:"), 0, 0);
        grid.add(nameField, 1, 0);
        grid.add(new Label("Kg:"), 0, 1);
        grid.add(kgField, 1, 1);
        grid.add(new Label("Price:"), 0, 2);
        grid.add(priceField, 1, 2);

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == saveButtonType) {
                try {
                    String name = nameField.getText().trim();
                    double kg = Double.parseDouble(kgField.getText().trim());
                    double price = Double.parseDouble(priceField.getText().trim());
                    
                    if (name.isEmpty()) {
                        showAlert(Alert.AlertType.WARNING, "Validation", "Produce name is required");
                        return null;
                    }
                    
                    updateProduce(produce.getId(), name, kg, price);
                    produce.setName(name);
                    produce.setKg(kg);
                    produce.setPrice(price);
                    table.refresh(); // refresh table to show updated values
                    return produce;
                } catch (NumberFormatException ex) {
                    showAlert(Alert.AlertType.WARNING, "Validation", "Kg and Price must be numbers");
                } catch (SQLException ex) {
                    showAlert(Alert.AlertType.ERROR, "DB Error", ex.getMessage());
                }
            }
            return null;
        });

        dialog.showAndWait();
    }

    private boolean showLoginDialog() { // login popup before app starts
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Login");
        dialog.setHeaderText("Farmers Produce Tracker Login");

        ButtonType loginButtonType = new ButtonType("Login", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(loginButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField usernameField = new TextField();
        usernameField.setPromptText("Username");
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");

        grid.add(new Label("Username:"), 0, 0);
        grid.add(usernameField, 1, 0);
        grid.add(new Label("Password:"), 0, 1);
        grid.add(passwordField, 1, 1);

        dialog.getDialogPane().setContent(grid);
        usernameField.requestFocus();

        while (true) {
            var result = dialog.showAndWait();
            if (result.isPresent() && result.get() == loginButtonType) {
                String username = usernameField.getText().trim();
                String password = passwordField.getText();

                if ("ram".equals(username) && "1234".equals(password)) {
                    return true; // login success
                } else {
                    showAlert(Alert.AlertType.ERROR, "Login Failed", "Invalid username or password");
                    usernameField.clear();
                    passwordField.clear();
                    usernameField.requestFocus();
                }
            } else {
                return false; // user cancelled
            }
        }
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
