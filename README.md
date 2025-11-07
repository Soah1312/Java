# Farmers Produce Tracker

A lightweight JavaFX desktop application for managing farmer produce inventory. Built with pure Java and JavaFX, this app uses an H2 in-memory database for data storage. The application provides a simple GUI to add, view, and track produce entries including product names, quantities, and farmer details. Data flows through a clean architecture: JavaFX UI → Controller → DatabaseService → H2 Database via JDBC. Features include real-time data refresh, table views, and form-based data entry. No web server required—just a standalone desktop application that's fast and efficient.

## Requirements

- Java 17 or higher

## How to Run

Navigate to the project directory and execute:

```powershell
cd Java
.\gradlew.bat run
```

The desktop application window will launch automatically.

