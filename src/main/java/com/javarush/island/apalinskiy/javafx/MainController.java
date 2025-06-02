package com.javarush.island.apalinskiy.javafx;

import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class MainController {
    @FXML
    private Button startButton;  // fx:id должен совпадать с FXML!

    @FXML
    private void initialize() {
        startButton.setOnAction(event -> {
            System.out.println("Button clicked!");
        });
    }
}
