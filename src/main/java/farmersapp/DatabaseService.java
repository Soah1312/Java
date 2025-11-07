package farmersapp;

import farmersapp.Produce;
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
        String sql = """
            CREATE TABLE IF NOT EXISTS produce (
                id BIGINT AUTO_INCREMENT PRIMARY KEY,
                produce_name VARCHAR(255) NOT NULL,
                quantity DECIMAL(19,4),
                farmer_name VARCHAR(255) NOT NULL,
                date_added VARCHAR(255)
            )
            """;
        try (Statement stmt = connection.createStatement()) {
            stmt.execute(sql);
        }
    }

    public void addProduce(Produce produce) throws SQLException {
        String sql = "INSERT INTO produce (produce_name, quantity, farmer_name, date_added) VALUES (?, ?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, produce.getProduceName());
            pstmt.setBigDecimal(2, produce.getQuantity());
            pstmt.setString(3, produce.getFarmerName());
            pstmt.setString(4, produce.getDateAdded());
            pstmt.executeUpdate();
        }
    }

    public List<Produce> getAllProduce() throws SQLException {
        List<Produce> produceList = new ArrayList<>();
        String sql = "SELECT * FROM produce ORDER BY id DESC";
        
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Produce produce = new Produce(
                    rs.getLong("id"),
                    rs.getString("produce_name"),
                    rs.getBigDecimal("quantity"),
                    rs.getString("farmer_name"),
                    rs.getString("date_added")
                );
                produceList.add(produce);
            }
        }
        return produceList;
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
