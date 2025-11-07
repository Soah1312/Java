package farmersapp;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseService {

    private static final String DB_URL = "jdbc:h2:mem:farmersdb;DB_CLOSE_DELAY=-1";
    private static final String DB_USER = "sa";
    private static final String DB_PASSWORD = "";
    
    private static DatabaseService instance;
    private Connection connection;

    private DatabaseService() {
        try {
            connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            createTable();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static DatabaseService getInstance() {
        if (instance == null) {
            instance = new DatabaseService();
        }
        return instance;
    }

    private void createTable() throws SQLException {
        // Create farmers table
        String farmersSql = """
            CREATE TABLE IF NOT EXISTS farmers (
                id BIGINT AUTO_INCREMENT PRIMARY KEY,
                name VARCHAR(255) NOT NULL,
                phone VARCHAR(20),
                location VARCHAR(255)
            )
            """;
        
        // Create produce table with foreign key
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

    public void addProduce(Produce produce) throws SQLException {
        String sql = "INSERT INTO produce (produce_name, quantity, farmer_id) VALUES (?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, produce.getProduceName());
            pstmt.setBigDecimal(2, produce.getQuantity());
            pstmt.setLong(3, produce.getFarmerId());
            pstmt.executeUpdate();
        }
    }

    public List<Produce> getAllProduce() throws SQLException {
        List<Produce> produceList = new ArrayList<>();
        String sql = """
            SELECT p.id, p.produce_name, p.quantity, p.farmer_id, f.name as farmer_name, p.date_added
            FROM produce p
            LEFT JOIN farmers f ON p.farmer_id = f.id
            ORDER BY p.id DESC
            """;
        
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
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

    // Farmer CRUD operations
    public void addFarmer(Farmer farmer) throws SQLException {
        String sql = "INSERT INTO farmers (name, phone, location) VALUES (?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, farmer.getName());
            pstmt.setString(2, farmer.getPhone());
            pstmt.setString(3, farmer.getLocation());
            pstmt.executeUpdate();
        }
    }

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

    public void deleteFarmer(Long id) throws SQLException {
        String sql = "DELETE FROM farmers WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setLong(1, id);
            pstmt.executeUpdate();
        }
    }

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
