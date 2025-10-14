package com.farmstock.service;

import com.farmstock.model.Product;
import com.farmstock.repository.ProductRepository;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.SQLException;

public class ProductService {

    private final ProductRepository productRepository = new ProductRepository();

    public ObservableList<Product> fetchAllProducts() throws SQLException {
        return FXCollections.observableArrayList(productRepository.findAll());
    }

    public void saveProduct(Product product) throws SQLException {
        productRepository.addProduct(product);
    }
}
