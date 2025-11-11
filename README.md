# farmers produce tracker# Farmers Produce Tracker (JavaFX + H2)



hey so this is my first real javafx project! basically its a desktop app to track produce from different farmers. been learning javafx and sql for like 2 weeks now and wanted to make something useful. the app uses h2 database which is cool cause it runs in memory so you dont need to install mysql or anything. you can add farmers with their info, then add produce and link it to a farmer. everything shows up in tables and you can refresh them. the ui is built with fxml which took me forever to understand but its actually pretty neat once you get it. overall pretty happy with how it turned out even tho the code is probably messy lolA modern Java 17+ JavaFX desktop application with normalized database schema for managing farmers and their produce. Features an intuitive UI with H2 in-memory database and clean layered architecture.



## how to run## Features



just cd into the Java folder and run this command:- **Farmer Management**: Add, view, and manage farmer records

- **Produce Tracking**: Link produce to farmers with foreign key relationships

```powershell- **Normalized Schema**: Proper one-to-many relationship (farmer → produce)

cd Java- **Interactive UI**: JavaFX-based desktop interface with tables and forms

.\gradlew.bat run- **H2 Database**: Embedded in-memory database (easily migrated to MySQL)

```- **Clean Architecture**: Layered design (Model, DAO, Controller, View)



it'll open a window with the app---



## what i learned## Requirements



- javafx is actually pretty cool for making desktop apps- **Java 17+** (JDK 17 or newer)

- h2 database is super easy to use for testing- **JavaFX SDK 21** (or 17+) - Download from [openjfx.io](https://openjfx.io)

- fxml separates ui from code which is nice- **Gradle** (included via Gradle Wrapper)

- prepared statements prevent sql injection (learned that the hard way)

- observablelist is needed for javafx tables---

- bigdecimal is better than double for money/quantity stuff

## Database Schema

## requirements

### Tables Created

needs java 17 or higher

#### 1. `farmers` Table

## how to useStores farmer information with contact details.



1. first add some farmers with their names and stuff| Column   | Type         | Constraints           | Description                    |

2. then you can add produce and pick which farmer it belongs to|----------|--------------|-----------------------|--------------------------------|

3. everything shows up in the tables| `id`     | BIGINT       | PRIMARY KEY, AUTO_INCREMENT | Unique farmer identifier |

4. hit refresh if stuff doesnt update| `name`   | VARCHAR(255) | NOT NULL              | Farmer's full name             |

| `phone`  | VARCHAR(20)  | NULL                  | Contact phone number           |

## the database| `location` | VARCHAR(255) | NULL                | Farm location/address          |



theres 2 tables:**Purpose**: Central registry of all farmers in the system.

- farmers table: stores farmer id, name, phone, location

- produce table: stores produce id, name, quantity, which farmer it belongs to, and when you added it#### 2. `produce` Table

Stores produce records linked to farmers via foreign key.

the produce table has a foreign key to the farmers table so you cant add produce without a farmer (learned about foreign keys last week)

| Column        | Type         | Constraints                          | Description                    |

## notes|---------------|--------------|--------------------------------------|--------------------------------|

| `id`          | BIGINT       | PRIMARY KEY, AUTO_INCREMENT          | Unique produce identifier      |

- data disappears when you close the app cause its in-memory| `produce_name`| VARCHAR(255) | NOT NULL                             | Name of the produce (e.g., Tomatoes) |

- probably has bugs but works for me| `quantity`    | DECIMAL(10,2)| NOT NULL                             | Quantity in kilograms          |

- maybe ill add delete buttons later idk| `farmer_id`   | BIGINT       | FOREIGN KEY → `farmers(id)`          | Reference to farmer who owns this produce |

| `date_added`  | TIMESTAMP    | DEFAULT CURRENT_TIMESTAMP            | Auto-generated timestamp       |

**Purpose**: Track produce inventory with proper farmer attribution.

**Relationship**: One farmer can have many produce records (one-to-many).

---

## How It Functions

### Application Workflow

1. **Startup**
   - Application initializes H2 in-memory database
   - Creates `farmers` and `produce` tables if they don't exist
   - Loads JavaFX UI with empty tables

2. **Adding Farmers**
   - User fills in farmer details (name required, phone/location optional)
   - Click "Add Farmer" button
   - Data validated and inserted into `farmers` table
   - Farmer table refreshes to show new entry
   - ComboBox in produce form updates with new farmer

3. **Adding Produce**
   - User selects existing farmer from ComboBox dropdown
   - Enters produce name and quantity
   - Click "Add Produce" button
   - System validates all fields (farmer selection required)
   - Inserts record into `produce` table with `farmer_id` foreign key
   - Produce table refreshes with JOIN query showing farmer name

4. **Viewing Data**
   - **Farmers Table**: Displays all farmers with ID, name, phone, location
   - **Produce Table**: Shows produce with ID, name, quantity, farmer name (via JOIN), date added
   - Both tables support refresh buttons to reload data

5. **Data Integrity**
   - Foreign key constraint ensures produce can only reference existing farmers
   - Prevents orphaned produce records
   - Database enforces referential integrity

---

## Running the Application

### Using Gradle (Recommended)

```bash
.\gradlew run
```

This automatically handles all dependencies and classpaths.

### Manual Java Command

```bash
java --module-path "C:\path\to\javafx-sdk-21\lib" `
     --add-modules javafx.controls,javafx.fxml `
     -cp build\libs\farmersapp-0.0.1-SNAPSHOT.jar farmersapp.FarmersAppApplication
```

Replace `C:\path\to\javafx-sdk-21\lib` with your actual JavaFX SDK path.

---

## Usage Guide

### Adding a Farmer

1. Locate the **"Add New Farmer"** section at the top
2. Enter farmer details:
   - **Name** (required)
   - **Phone** (optional)
   - **Location** (optional)
3. Click **"Add Farmer"**
4. Farmer appears in the "All Farmers" table below

### Adding Produce

1. Scroll to the **"Add New Produce"** section
2. Enter produce details:
   - **Produce Name** (e.g., Tomatoes, Potatoes)
   - **Quantity** in kg (e.g., 50.5)
   - **Select Farmer** from dropdown (must add farmers first!)
3. Click **"Add Produce"**
4. Produce appears in the "All Produce" table with farmer name

### Refreshing Data

- Click **"Refresh Farmers"** to reload farmer table
- Click **"Refresh"** (in produce section) to reload produce table

---

## Migrating to MySQL

See `MIGRATION_NOTES.md` for complete step-by-step instructions.

---

## Troubleshooting

### Issue: "Module javafx.controls not found"
**Solution**: Ensure JavaFX SDK path is correct in the run command.

### Issue: "No suitable driver found for jdbc:h2"
**Solution**: Use `.\gradlew run` to include all dependencies automatically.

### Issue: Can't scroll in the app
**Solution**: Fixed in latest version with ScrollPane wrapper.

### Issue: Foreign key constraint violation
**Solution**: Add farmers first before adding produce. Produce requires an existing farmer.

---

## Technical Stack

- **Language**: Java 17
- **UI Framework**: JavaFX 21
- **Database**: H2 (embedded, in-memory)
- **Build Tool**: Gradle 8.9
- **Architecture**: Layered MVC pattern

---

## Notes

- **In-Memory Database**: Data is lost when application closes
- **Scrollable UI**: ScrollPane enabled for long content
- **Validation**: Required fields enforced, numeric validation for quantity
- **Foreign Keys**: Enforces referential integrity between farmers and produce
