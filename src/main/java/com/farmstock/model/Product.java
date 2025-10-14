package com.farmstock.model;

import javafx.beans.property.*;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Product {

    private final IntegerProperty id = new SimpleIntegerProperty();
    private final StringProperty name = new SimpleStringProperty();
    private final StringProperty category = new SimpleStringProperty();
    private final DoubleProperty price = new SimpleDoubleProperty();
    private final IntegerProperty quantity = new SimpleIntegerProperty();
    private final ObjectProperty<LocalDate> dateAdded = new SimpleObjectProperty<>();

    public Product() {
    }

    public Product(int id, String name, String category, double price, int quantity, LocalDate dateAdded) {
        this.id.set(id);
        this.name.set(name);
        this.category.set(category);
        this.price.set(price);
        this.quantity.set(quantity);
        this.dateAdded.set(dateAdded);
    }

    public Product(String name, String category, BigDecimal price, int quantity) {
        this.name.set(name);
        this.category.set(category);
        this.price.set(price.doubleValue());
        this.quantity.set(quantity);
    }

    public IntegerProperty idProperty() {
        return id;
    }

    public int getId() {
        return id.get();
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public StringProperty nameProperty() {
        return name;
    }

    public String getName() {
        return name.get();
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public StringProperty categoryProperty() {
        return category;
    }

    public String getCategory() {
        return category.get();
    }

    public void setCategory(String category) {
        this.category.set(category);
    }

    public DoubleProperty priceProperty() {
        return price;
    }

    public double getPrice() {
        return price.get();
    }

    public void setPrice(double price) {
        this.price.set(price);
    }

    public IntegerProperty quantityProperty() {
        return quantity;
    }

    public int getQuantity() {
        return quantity.get();
    }

    public void setQuantity(int quantity) {
        this.quantity.set(quantity);
    }

    public ObjectProperty<LocalDate> dateAddedProperty() {
        return dateAdded;
    }

    public LocalDate getDateAdded() {
        return dateAdded.get();
    }

    public void setDateAdded(LocalDate dateAdded) {
        this.dateAdded.set(dateAdded);
    }
}
