package com.javarush.island.apalinskiy;

import com.javarush.island.apalinskiy.map.Map;
import com.javarush.island.apalinskiy.simulation.SimulationInitializer;
import com.javarush.island.apalinskiy.simulation.SimulationManager;
import com.javarush.island.apalinskiy.statistics.SimulationStatistics;
import com.javarush.island.apalinskiy.util.MapUtils;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.concurrent.Phaser;

public class Runner extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        // Загрузка FXML
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/main.fxml"));

        // Настройка сцены
        Scene scene = new Scene(root, 600, 400);
        stage.setTitle("Island Simulation");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
