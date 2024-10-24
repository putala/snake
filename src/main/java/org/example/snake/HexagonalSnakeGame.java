package org.example.snake;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.stage.Stage;
import org.kordamp.bootstrapfx.BootstrapFX;

import javafx.animation.AnimationTimer;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;


import java.util.ArrayList;
import java.util.List;
import java.util.Random;




public class HexagonalSnakeGame extends Application {
    private static final int HEX_SIZE = 30;
    private static final int WIDTH = 10; // Szerokość planszy
    private static final int HEIGHT = 10; // Wysokość planszy
    private List<Polygon> snake = new ArrayList<>();
    private double snakeX = 300, snakeY = 300; // Początkowe współrzędne węża
    private Pane root;
    private boolean isOddRow; // Flaga wskazująca, czy rząd jest nieparzysty

    @Override
    public void start(Stage primaryStage) {
        root = new Pane();
        Scene scene = new Scene(root, 600, 600);

        // Tworzenie planszy w układzie heksagonalnym
        for (int row = 0; row < HEIGHT; row++) {
            for (int col = 0; col < WIDTH; col++) {
                double xOffset = (col * 1.5 * HEX_SIZE);
                double yOffset = (row * Math.sqrt(3) * HEX_SIZE) + (col % 2 == 0 ? 0 : HEX_SIZE * Math.sqrt(3) / 2);

                Polygon hexagon = createHexagon(xOffset, yOffset);
                root.getChildren().add(hexagon);
            }
        }

        // Inicjalizacja węża
        Polygon snakePart = createHexagon(snakeX, snakeY);
        snakePart.setFill(Color.GREEN);
        snake.add(snakePart);
        root.getChildren().add(snakePart);

        // Obsługa klawiszy Q, E, R, S, D, F do ruchu węża
        scene.setOnKeyPressed(event -> {
            KeyCode key = event.getCode();
            isOddRow = ((snakeY / (Math.sqrt(3) * HEX_SIZE)) % 2 != 0); // Czy wąż jest w nieparzystym rzędzie
            switch (key) {
                case Q -> moveSnake(isOddRow ? -HEX_SIZE * 1.5 : -HEX_SIZE * 3 / 2, -Math.sqrt(3) * HEX_SIZE / 2); // Lewy górny
                case E -> moveSnake(isOddRow ? HEX_SIZE * 1.5 : HEX_SIZE * 3 / 2, -Math.sqrt(3) * HEX_SIZE / 2); // Prawy górny
                case W -> moveSnake(0, -3 * HEX_SIZE / 2); // Prawy
                case D -> moveSnake(isOddRow ? HEX_SIZE * 1.5 : HEX_SIZE * 3 / 2, Math.sqrt(3) * HEX_SIZE / 2); // Prawy dolny
                case A -> moveSnake(isOddRow ? -HEX_SIZE * 1.5 : -HEX_SIZE * 3 / 2, Math.sqrt(3) * HEX_SIZE / 2); // Lewy dolny
                case S -> moveSnake(0, 3 * HEX_SIZE / 2); // Lewy
            }
        });

        primaryStage.setTitle("Hexagonal Snake");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // Funkcja tworząca sześciokąt
    private Polygon createHexagon(double x, double y) {
        Polygon hexagon = new Polygon();
        for (int i = 0; i < 6; i++) {
            double angle = Math.toRadians(60 * i);
            double xPoint = x + HEX_SIZE * Math.cos(angle);
            double yPoint = y + HEX_SIZE * Math.sin(angle);
            hexagon.getPoints().addAll(xPoint, yPoint);
        }
        hexagon.setStroke(Color.BLACK);
        hexagon.setFill(Color.LIGHTGRAY);
        return hexagon;
    }

    // Funkcja do przesuwania węża
    private void moveSnake(double deltaX, double deltaY) {
        snakeX += deltaX;
        snakeY += deltaY;
        Polygon snakePart = createHexagon(snakeX, snakeY);
        snakePart.setFill(Color.GREEN);
        snake.add(snakePart);
        root.getChildren().add(snakePart);

        // Usuwamy najstarszą część węża, aby symulować poruszanie się
        if (snake.size() > 5) { // Limit długości węża
            Polygon tail = snake.remove(0);
            root.getChildren().remove(tail);
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}

