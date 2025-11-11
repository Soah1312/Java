package farmersapp;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

// this handles all the database stuff
// spent like 3 days figuring out h2 database lol
public class DatabaseService {

    // connection info for the database
    private static final String DB_URL = "jdbc:h2:mem:farmersdb;DB_CLOSE_DELAY=-1"; // in-memory database
    private static final String DB_USER = "sa";
    private static final String DB_PASSWORD = ""; // no password needed for h2
    
    private static DatabaseService instance; // singleton pattern we learned last week
    private Connection connection;

    // private constructor so you can't make new instances
    private DatabaseService() {
        try {
            // connect to database
            connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            createTable(); // make the tables when we first connect
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // this makes sure there's only one database connection
    public static DatabaseService getInstance() {
        if (instance == null) {
            instance = new DatabaseService();
        }
        return instance;
    }

    // create the tables in the database
    private void createTable() throws SQLException {
        // farmers table
        String farmersSql = """
            CREATE TABLE IF NOT EXISTS farmers (
                id BIGINT AUTO_INCREMENT PRIMARY KEY,
                name VARCHAR(255) NOT NULL,
                phone VARCHAR(20),
                location VARCHAR(255)
            )
            """;
        
        // produce table with foreign key to farmers
        String produceSql = """
            CREATE TABLE IF NOT EXISTS produce (
                id BIGINT AUTO_INCREMENT PRIMARY KEY,
                produce_name VARCHAR(255) NOT NULL,
                quantity DECIMAL(10,2) NOT NULL,
                farmer_id BIGINT,
                date_added TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                CONSTRAINT produce_farmer_fk FOREIGN KEY(farmer_id) REFERENCES farmers(id)
            )
            """;
        
        try (Statement stmt = connection.createStatement()) {
            stmt.execute(farmersSql);
            stmt.execute(produceSql);
        }
    }

    // add new produce to database
    public void addProduce(Produce produce) throws SQLException {
        String sql = "INSERT INTO produce (produce_name, quantity, farmer_id) VALUES (?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, produce.getProduceName());
            pstmt.setBigDecimal(2, produce.getQuantity());
            pstmt.setLong(3, produce.getFarmerId());
            pstmt.executeUpdate(); // run the insert query
        }
    }

    // get all produce from database
    public List<Produce> getAllProduce() throws SQLException {
        List<Produce> produceList = new ArrayList<>();
        // using join to get farmer name too
        String sql = """
            SELECT p.id, p.produce_name, p.quantity, p.farmer_id, f.name as farmer_name, p.date_added
            FROM produce p
            LEFT JOIN farmers f ON p.farmer_id = f.id
            ORDER BY p.id DESC
            """;
        
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            // loop through all results and make produce objects
            while (rs.next()) {
                Produce produce = new Produce(
                    rs.getLong("id"),
                    rs.getString("produce_name"),
                    rs.getBigDecimal("quantity"),
                    rs.getLong("farmer_id"),
                    rs.getString("farmer_name"),
                    rs.getTimestamp("date_added").toString()
                );
                produceList.add(produce);
            }
        }
        return produceList;
    }

    // add new farmer to database
    public void addFarmer(Farmer farmer) throws SQLException {
        String sql = "INSERT INTO farmers (name, phone, location) VALUES (?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, farmer.getName());
            pstmt.setString(2, farmer.getPhone());
            pstmt.setString(3, farmer.getLocation());
            pstmt.executeUpdate();
        }
    }

    // get all farmers from database
    public List<Farmer> getAllFarmers() throws SQLException {
        List<Farmer> farmerList = new ArrayList<>();
        String sql = "SELECT * FROM farmers ORDER BY name ASC";
        
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Farmer farmer = new Farmer(
                    rs.getLong("id"),
                    rs.getString("name"),
                    rs.getString("phone"),
                    rs.getString("location")
                );
                farmerList.add(farmer);
            }
        }
        return farmerList;
    }

    // close database connection when done
    public void close() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
