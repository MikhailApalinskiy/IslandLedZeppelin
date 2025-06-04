package com.javarush.island.apalinskiy.javafx;

import com.javarush.island.apalinskiy.creatures.animals.Animal;
import com.javarush.island.apalinskiy.creatures.plants.AbstractPlant;
import com.javarush.island.apalinskiy.map.Cell;
import com.javarush.island.apalinskiy.map.Map;
import com.javarush.island.apalinskiy.simulation.SimulationInitializer;
import com.javarush.island.apalinskiy.simulation.SimulationManager;
import com.javarush.island.apalinskiy.statistics.SimulationStatistics;
import com.javarush.island.apalinskiy.util.MapUtils;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Bounds;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.scene.transform.Scale;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.Phaser;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * The final frontier of simulation control.
 * <p>
 * Handles rendering, user input, and phased updates.
 * <p>
 * There is no god beyond this class.
 */
public class Controller {

    private final Map map = new Map();
    public VBox statsPanel;
    public BorderPane root;
    private Cell[][] modelMap;
    private Pane[][] cellViews;
    private double lastMouseX, lastMouseY;
    private SimulationManager simulationManager;
    private Phaser phaser;
    private ScheduledExecutorService stepExecutor;
    private final AtomicBoolean running = new AtomicBoolean(false);
    private final Scale scale = new Scale(1, 1, 0, 0);
    private Label[][] emojiLabels;

    @FXML
    private TextArea statsTextArea;

    @FXML
    private ScrollPane scrollPane;

    @FXML
    private GridPane gridPane;

    @FXML
    public void initialize() {
        Group zoomGroup = new Group();
        zoomGroup.getChildren().add(gridPane);
        scrollPane.setContent(zoomGroup);
        gridPane.getTransforms().add(scale);
        scrollPane.setPannable(false);
        scrollPane.setFitToWidth(false);
        scrollPane.setFitToHeight(false);
        scrollPane.addEventFilter(ScrollEvent.SCROLL, event -> {
            if (event.isControlDown()) {
                double zoomFactor = (event.getDeltaY() > 0) ? 1.1 : 0.9;
                scale.setX(scale.getX() * zoomFactor);
                scale.setY(scale.getY() * zoomFactor);
                event.consume();
            }
        });
        generateGrid();
        gridPane.setOnMousePressed(event -> {
            if (event.isPrimaryButtonDown()) {
                lastMouseX = event.getSceneX();
                lastMouseY = event.getSceneY();
            }
        });
        gridPane.setOnMouseDragged(event -> {
            if (event.isPrimaryButtonDown()) {
                double deltaX = lastMouseX - event.getSceneX();
                double deltaY = lastMouseY - event.getSceneY();
                Node content = scrollPane.getContent();
                Bounds bounds = content.getBoundsInLocal();
                scrollPane.setHvalue(clamp(scrollPane.getHvalue() + deltaX / bounds.getWidth()));
                scrollPane.setVvalue(clamp(scrollPane.getVvalue() + deltaY / bounds.getHeight()));
                lastMouseX = event.getSceneX();
                lastMouseY = event.getSceneY();
            }
        });
    }

    @FXML
    private void startSimulation() {
        Platform.runLater(() -> {
            if (running.get()) updateMapView();
        });
        running.set(true);
        phaser = new Phaser(1);
        SimulationInitializer initializer = new SimulationInitializer(map);
        initializer.randomlyPopulateAnimals();
        initializer.randomlyPopulatePlants();
        simulationManager = new SimulationManager(map, phaser);
        simulationManager.run();
        MapUtils.init(map);
        stepExecutor = Executors.newSingleThreadScheduledExecutor();
        stepExecutor.scheduleWithFixedDelay(() -> {
            if (!running.get()) {
                return;
            }
            phaser.arriveAndAwaitAdvance();
            phaser.arriveAndAwaitAdvance();
            Platform.runLater(() -> {
                updateMapView();
                updateStats(SimulationStatistics.getStatisticsText());
            });
            phaser.arriveAndAwaitAdvance();
        }, 0, 1, TimeUnit.SECONDS);
        System.out.println("Simulation start");
    }


    private void generateGrid() {
        map.initializeMap();
        modelMap = map.getMap();
        int columns = map.getLength();
        int rows = map.getWidth();
        double cellSize = 40.0;
        cellViews = new Pane[rows][columns];
        emojiLabels = new Label[rows][columns];
        for (int y = 0; y < rows; y++) {
            for (int x = 0; x < columns; x++) {
                Pane cellView = new Pane();
                cellView.setPrefSize(cellSize, cellSize);
                cellView.setStyle("-fx-border-color: lightgray; -fx-background-color: white;");
                Label emojiLabel = new Label();
                emojiLabel.setFont(Font.font(18));
                emojiLabel.setMouseTransparent(true);
                emojiLabel.setAlignment(Pos.CENTER);
                emojiLabel.setTextAlignment(TextAlignment.CENTER);
                emojiLabel.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
                cellView.getChildren().add(emojiLabel);
                gridPane.add(cellView, x, y);
                cellViews[y][x] = cellView;
                emojiLabels[y][x] = emojiLabel;
            }
        }
    }

    public void updateStats(String stats) {
        statsTextArea.setText(stats);
    }

    @FXML
    private void handleStop() {
        running.set(false);
        simulationManager.shutdown();
        if (stepExecutor != null) {
            stepExecutor.shutdownNow();
        }
        System.out.println("Simulation stop");
        Platform.exit();
        System.exit(0);
    }

    private double clamp(double value) {
        return Math.max(0.0, Math.min(1.0, value));
    }

    /**
     * Updates the visual state of the entire grid based on the current contents of each cell.
     * <p>
     * Applies background color changes based on plant presence, and displays up to 15 unique animal emojis per cell.
     * <p>
     * ⚠️ Performance Warning:
     * <ul>
     *   <li>May cause significant lag, especially at simulation startup with a large number of animals.</li>
     *   <li>Re-rendering emoji labels and recalculating font sizes across all cells is computationally expensive.</li>
     *   <li>No definitive solution to this bottleneck has yet been implemented.</li>
     * </ul>
     * <p>
     * 💡 Potential optimizations:
     * <ul>
     *   <li>Cache emoji strings per cell and update only on meaningful changes.</li>
     *   <li>Skip rendering for visually unchanged cells between ticks.</li>
     *   <li>Use {@code Canvas} or low-level drawing APIs instead of {@code Label} elements for faster rendering.</li>
     * </ul>
     */
    private void updateMapView() {
        for (int y = 0; y < cellViews.length; y++) {
            for (int x = 0; x < cellViews[0].length; x++) {
                Pane pane = cellViews[y][x];
                Cell cell = modelMap[x][y];
                Label label = emojiLabels[y][x];
                List<Animal> animals = cell.getAnimals();
                List<AbstractPlant> plants = cell.getPlants();
                String bgColor = plants.isEmpty() ? "white" : "lightgreen";
                pane.setStyle("-fx-border-color: lightgray; -fx-background-color: " + bgColor + ";");
                if (!animals.isEmpty()) {
                    Set<String> emojis = new LinkedHashSet<>();
                    for (Animal animal : animals) {
                        if (emojis.size() >= 15) break;
                        emojis.add(animal.getEmoji());
                    }
                    StringBuilder sb = new StringBuilder();
                    emojis.forEach(sb::append);
                    int emojiCount = sb.length();
                    int fontSize = switch (emojiCount) {
                        case 1 -> 18;
                        case 2, 3 -> 16;
                        case 4, 5 -> 14;
                        case 6, 7 -> 12;
                        case 8, 9, 10 -> 10;
                        default -> 8;
                    };
                    label.setText(sb.toString());
                    label.setFont(Font.font(fontSize));
                } else {
                    label.setText("");
                }
            }
        }
    }
}
