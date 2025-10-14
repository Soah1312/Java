# FarmStock Manager

FarmStock Manager is a JavaFX desktop application that enables farmers to publish their produce inventory and allows buyers to browse available stock in real time. The app connects directly to a Supabase-hosted PostgreSQL database using JDBC.

## Features
- **Farmer dashboard** to add new products and monitor existing inventory.
- **Buyer dashboard** to view and filter available products.
- **Live database sync** backed by Supabase PostgreSQL via JDBC.
- **Scene navigation** between farmer and buyer dashboards.
- **Responsive UI** built with FXML and styled through CSS.

## Project Structure
```
src/
  main/
    java/
      com/farmstock/
        Main.java
        SceneManager.java
      com/farmstock/controller/
        MainController.java
        FarmerController.java
        BuyerController.java
      com/farmstock/db/DBConnection.java
      com/farmstock/model/Product.java
      com/farmstock/repository/ProductRepository.java
      com/farmstock/service/ProductService.java
      module-info.java
    resources/
      com/farmstock/view/
        main-view.fxml
        farmer-view.fxml
        buyer-view.fxml
      com/farmstock/style/styles.css
```

## Prerequisites
- Java 17 or later
- Maven 3.8+
- Supabase PostgreSQL instance configured with the following schema:

```sql
CREATE TABLE products (
  id SERIAL PRIMARY KEY,
  name VARCHAR(100),
  category VARCHAR(50),
  price DECIMAL(10,2),
  quantity INT,
  date_added DATE DEFAULT CURRENT_DATE
);
```

## Configuration
The application reads Supabase credentials from environment variables. Set them before running the app:

- `SUPABASE_DB_URL` (e.g., `jdbc:postgresql://<project>.supabase.co:5432/postgres?sslmode=require`)
- `SUPABASE_DB_USER`
- `SUPABASE_DB_PASSWORD`

Alternatively, you can edit the default placeholders directly in `src/main/java/com/farmstock/db/DBConnection.java` (not recommended for production).

## Running the Application
1. Install project dependencies:
   ```bash
   mvn clean install
   ```
2. Launch the JavaFX application:
   ```bash
   mvn javafx:run
   ```

Both commands should be executed from the project root (`c:\Users\soahf\Desktop\JAVA`).

## Usage
- When the app starts, choose **Farmer Dashboard** or **Buyer Dashboard** from the main menu.
- Farmers can add new products by entering the required details and selecting **Add Product**. Use **Refresh** to reload data from Supabase or **Back** to return to the main menu.
- Buyers can search by product name or category using the search bar. Use **Refresh** to fetch the latest data or **Back** to return to the main menu.

## Optional Enhancements
Potential future improvements include:
- Authentication for farmers vs. buyers
- Editing or deleting inventory items
- Sorting and advanced filtering
- Exporting inventory data to CSV

## Troubleshooting
- **Database connection errors**: Ensure your Supabase credentials are correct and the instance allows connections from your IP.
- **SSL issues**: Confirm the JDBC URL includes `?sslmode=require`.
- **JavaFX not launching**: Verify you're using Java 17 or newer and that the `javafx:run` goal is executed with the provided Maven plugin.
