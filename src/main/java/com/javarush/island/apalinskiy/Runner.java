package com.javarush.island.apalinskiy;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Objects;

/**
 * Entry point for the JavaFX application.
 * <p>
 * Loads the main FXML layout and launches the island simulation UI.
 * <p>
 * To start the simulation, run the application via Maven:
 * <pre>{@code
 * mvn javafx:run
 * }</pre>
 */
public class Runner extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/fxml/main.fxml")));
        primaryStage.setTitle("Island simulation");
        primaryStage.setScene(new Scene(root));
        primaryStage.setMaximized(true);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
