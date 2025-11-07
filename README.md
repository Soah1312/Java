# Farmers Produce Tracker (JavaFX Desktop App)

A pure JavaFX desktop application for tracking farmer produce with H2 in-memory database.

## How It Works

**Tech Stack**:
- **JavaFX** - Desktop GUI framework (no web server)
- **H2 Database** - In-memory SQL database with JDBC
- **Pure Java** - No Spring Boot or heavy frameworks

**Flow**:
1. JavaFX UI → `MainController` → `DatabaseService` → H2 Database (JDBC)
2. Database responds via JDBC → Service layer → Controller → UI updates

**Features**:
- Add produce with name, quantity, and farmer name
- View all produce in a table
- Refresh data in real-time
- Lightweight and fast

## How to Run

**Requirements**: Java 17+

**Windows**:
```powershell
cd D:\Coding\Projects\java\javaproj\Java
.\gradlew.bat run
```

**Mac/Linux**:
```bash
cd /path/to/Java
./gradlew run
```

A desktop window will open with the Farmers Produce Tracker application.

## First Time Setup

The first run will download JavaFX libraries automatically. This may take a few minutes.

