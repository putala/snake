package org.example.snake;



import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Slither extends Application {

    private static final int BLOCK_SIZE = 20;
    private static final int WIDTH = 600;
    private static final int HEIGHT = 600;

    private final List<Circle> snake = new ArrayList<>();
    private double direction = 0; // Kąt w radianach (0 = w prawo)
    private final double rotationSpeed = Math.toRadians(5); // Prędkość obrotu w radianach (5 stopni na każdą zmianę)

    private Circle food; // Okrągłe "jedzenie"
    private final Random random = new Random();
    private boolean gameOver = false;

    private static final int IGNORE_SEGMENTS_COUNT = 10; // Liczba segmentów, które ignorujemy przy sprawdzaniu kolizji

    @Override
    public void start(Stage stage) {
        Pane root = new Pane();
        Scene scene = new Scene(root, WIDTH, HEIGHT);

        // Głowa węża jako kółko
        Circle snakeHead = new Circle(BLOCK_SIZE / 2, Color.GREEN);
        snake.add(snakeHead);
        root.getChildren().add(snakeHead);

        // Dodajemy "jedzenie" na planszy
        food = new Circle(BLOCK_SIZE / 2, Color.RED);
        placeFood(root);

        // Animacja ruchu węża
        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(100), e -> {
            if (!gameOver) {
                moveSnake(root);
                checkForFood(root); // Sprawdzamy, czy wąż zjadł jedzenie
                checkForCollisions(); // Sprawdzamy, czy wąż uderzył w siebie lub krawędź
            }
        }));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();

        // Obsługa klawiszy
        scene.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.A) {
                // Obrót w lewo (zmniejszamy kąt)
                direction -= rotationSpeed;
            } else if (event.getCode() == KeyCode.D) {
                // Obrót w prawo (zwiększamy kąt)
                direction += rotationSpeed;
            }
        });

        // Ustawienia okna
        stage.setTitle("Slither");
        stage.setScene(scene);
        stage.show();
    }

    private void moveSnake(Pane root) {
        // Pobieramy głowę węża
        Circle head = snake.getFirst();

        // Obliczamy nowe współrzędne na podstawie aktualnego kierunku (kąta)
        // Mniejsza prędkość ruchu (przesuwamy się o mniejsze odległości)
        double moveSpeed = 5;
        double deltaX = Math.cos(direction) * moveSpeed;
        double deltaY = Math.sin(direction) * moveSpeed;

        // Nowa pozycja dla nowego segmentu (głowy)
        double newHeadX = head.getCenterX() + deltaX;
        double newHeadY = head.getCenterY() + deltaY;

        // Przesuwamy wszystkie segmenty węża
        for (int i = snake.size() - 1; i > 0; i--) {
            Circle previous = snake.get(i - 1);
            Circle current = snake.get(i);
            current.setCenterX(previous.getCenterX());
            current.setCenterY(previous.getCenterY());
        }

        // Aktualizujemy pozycję głowy węża
        head.setCenterX(newHeadX);
        head.setCenterY(newHeadY);

        // Sprawdzamy, czy wąż wyszedł poza ekran i kończymy grę
        if (newHeadX < 0 || newHeadX >= WIDTH || newHeadY < 0 || newHeadY >= HEIGHT) {
            gameOver = true;
            System.out.println("Game Over! Wąż uderzył w krawędź.");
        }
    }

    private void placeFood(Pane root) {
        // Losujemy pozycję "jedzenia" w obrębie planszy
        double x = random.nextInt(WIDTH / BLOCK_SIZE) * BLOCK_SIZE + BLOCK_SIZE / 2;
        double y = random.nextInt(HEIGHT / BLOCK_SIZE) * BLOCK_SIZE + BLOCK_SIZE / 2;

        // Ustawiamy pozycję jedzenia
        food.setCenterX(x);
        food.setCenterY(y);

        // Dodajemy jedzenie do planszy, jeśli nie jest jeszcze dodane
        if (!root.getChildren().contains(food)) {
            root.getChildren().add(food);
        }
    }

    private void checkForFood(Pane root) {
        // Pobieramy głowę węża
        Circle head = snake.getFirst();

        // Sprawdzamy, czy odległość między głową węża a jedzeniem jest mniejsza niż ich promienie
        if (Math.hypot(head.getCenterX() - food.getCenterX(), head.getCenterY() - food.getCenterY()) < BLOCK_SIZE) {
            // Zjedzono jedzenie - losujemy nowe miejsce dla jedzenia
            placeFood(root);

            // Dodajemy 5 nowych segmentów do węża
            for (int i = 0; i < 5; i++) {
                Circle newSegment = new Circle(BLOCK_SIZE / 2, Color.GREEN);
                Circle lastSegment = snake.getLast();
                newSegment.setCenterX(lastSegment.getCenterX());
                newSegment.setCenterY(lastSegment.getCenterY());
                snake.add(newSegment);
                root.getChildren().add(newSegment);
            }
        }
    }

    private void checkForCollisions() {
        // Pobieramy głowę węża
        Circle head = snake.getFirst();

        // Sprawdzamy kolizję z ciałem węża, zaczynając od 20. segmentu
        for (int i = IGNORE_SEGMENTS_COUNT; i < snake.size(); i++) {
            Circle segment = snake.get(i);
            if (Math.hypot(head.getCenterX() - segment.getCenterX(), head.getCenterY() - segment.getCenterY()) < BLOCK_SIZE) {
                gameOver = true;
                System.out.println("Game Over! Wąż uderzył w swoje ciało.");
                break;
            }
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}










