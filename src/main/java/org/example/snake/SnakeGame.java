package org.example.snake;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.kordamp.bootstrapfx.BootstrapFX;

import javafx.application.Application;
import javafx.animation.AnimationTimer;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import java.io.IOException;






public class SnakeGame extends Application {

    private static final int TILE_SIZE = 20;
    private static final int WIDTH = 600;
    private static final int HEIGHT = 400;

    private ArrayList<Rectangle> snake;
    private Rectangle food;
    private int direction = KeyCode.RIGHT.getCode();
    private boolean gameOver = false;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        Pane pane = new Pane();
        snake = new ArrayList<>();
        Rectangle head = new Rectangle(TILE_SIZE, TILE_SIZE, Color.GREEN);
        snake.add(head);
        pane.getChildren().add(head);

        spawnFood(pane);

        Scene scene = new Scene(pane, WIDTH, HEIGHT);
        primaryStage.setTitle("Snake Game");
        primaryStage.setScene(scene);
        primaryStage.show();

        scene.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.UP && direction != KeyCode.DOWN.getCode()) {
                direction = KeyCode.UP.getCode();
            } else if (event.getCode() == KeyCode.DOWN && direction != KeyCode.UP.getCode()) {
                direction = KeyCode.DOWN.getCode();
            } else if (event.getCode() == KeyCode.LEFT && direction != KeyCode.RIGHT.getCode()) {
                direction = KeyCode.LEFT.getCode();
            } else if (event.getCode() == KeyCode.RIGHT && direction != KeyCode.LEFT.getCode()) {
                direction = KeyCode.RIGHT.getCode();
            }
        });

        AnimationTimer timer = new AnimationTimer() {
            long lastUpdate = 0;

            @Override
            public void handle(long now) {
                if (now - lastUpdate >= 100_000_000) { // 100 ms
                    moveSnake();
                    lastUpdate = now;
                }
            }
        };
        timer.start();
    }

    private void moveSnake() {
        if (gameOver) return;

        Rectangle head = snake.get(0);
        int newX = (int) head.getX();
        int newY = (int) head.getY();

        switch (direction) {
            case 37: // LEFT
                newX -= TILE_SIZE;
                break;
            case 38: // UP
                newY -= TILE_SIZE;
                break;
            case 39: // RIGHT
                newX += TILE_SIZE;
                break;
            case 40: // DOWN
                newY += TILE_SIZE;
                break;
        }

        if (newX < 0 || newX >= WIDTH || newY < 0 || newY >= HEIGHT || isColliding(newX, newY)) {
            gameOver = true;
            System.out.println("Game Over!");
            return;
        }

        Rectangle newHead = new Rectangle(TILE_SIZE, TILE_SIZE, Color.GREEN);
        newHead.setX(newX);
        newHead.setY(newY);
        snake.add(0, newHead);
        ((Pane) head.getParent()).getChildren().add(newHead);

        if (newHead.getBoundsInParent().intersects(food.getBoundsInParent())) {
            spawnFood((Pane) head.getParent());
        } else {
            Rectangle tail = snake.remove(snake.size() - 1);
            ((Pane) head.getParent()).getChildren().remove(tail);
        }
    }

    private void spawnFood(Pane pane) {
        // Jeśli istnieje jedzenie, usuń je
        if (food != null) {
            pane.getChildren().remove(food);
        }

        Random rand = new Random();
        int x = rand.nextInt(WIDTH / TILE_SIZE) * TILE_SIZE;
        int y = rand.nextInt(HEIGHT / TILE_SIZE) * TILE_SIZE;

        food = new Rectangle(x, y, TILE_SIZE, TILE_SIZE);
        food.setFill(Color.RED);
        pane.getChildren().add(food);
    }

    private boolean isColliding(int x, int y) {
        for (Rectangle segment : snake) {
            if (segment.getX() == x && segment.getY() == y) {
                return true;
            }
        }
        return false;
    }
}