package farmersapp;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

// this is the main file that starts everything
public class FarmersAppApplication extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        // load the fxml file which has all the buttons and stuff
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/main.fxml"));
        Parent root = loader.load();

        // make a window that's 800x600 pixels
        Scene scene = new Scene(root, 800, 600);
        primaryStage.setTitle("Farmers Produce Tracker");
        primaryStage.setScene(scene);
        primaryStage.show(); // actually show the window
    }

    // this runs when you start the program
    public static void main(String[] args) {
        launch(args);
    }
}

