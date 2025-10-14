package com.farmstock.repository;

import com.farmstock.db.DBConnection;
import com.farmstock.model.Product;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ProductRepository {

    private static final String INSERT_PRODUCT = "INSERT INTO products (name, category, price, quantity) VALUES (?, ?, ?, ?)";
    private static final String SELECT_ALL = "SELECT id, name, category, price, quantity, date_added FROM products ORDER BY date_added DESC, id DESC";

    public void addProduct(Product product) throws SQLException {
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(INSERT_PRODUCT)) {
            statement.setString(1, product.getName());
            statement.setString(2, product.getCategory());
            statement.setBigDecimal(3, BigDecimal.valueOf(product.getPrice()));
            statement.setInt(4, product.getQuantity());
            statement.executeUpdate();
        }
    }

    public List<Product> findAll() throws SQLException {
        List<Product> products = new ArrayList<>();
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(SELECT_ALL);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                products.add(mapRow(resultSet));
            }
        }
        return products;
    }

    private Product mapRow(ResultSet resultSet) throws SQLException {
        int id = resultSet.getInt("id");
        String name = resultSet.getString("name");
        String category = resultSet.getString("category");
        double price = resultSet.getBigDecimal("price").doubleValue();
        int quantity = resultSet.getInt("quantity");
        Date date = resultSet.getDate("date_added");
        LocalDate dateAdded = date != null ? date.toLocalDate() : null;
        return new Product(id, name, category, price, quantity, dateAdded);
    }
}
