
package org.example.snake;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import org.kordamp.bootstrapfx.BootstrapFX;

import javafx.animation.AnimationTimer;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;
import java.util.Random;

import static javafx.scene.input.KeyCode.*;


public class SnakeGame extends Application {

    private static final int TILE_SIZE = 20;
    private static final int WIDTH = 600;
    private static final int HEIGHT = 400;

    private ArrayList<Rectangle> snake;
    private Circle food;
    private int direction = RIGHT.getCode();
//    KeyCode direction = KeyCode.RIGHT.getCode();
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
            if (event.getCode() == UP && direction != DOWN.getCode()) {
                direction = UP.getCode();
            } else if (event.getCode() == DOWN && direction != UP.getCode()) {
                direction = DOWN.getCode();
            } else if (event.getCode() == LEFT && direction != RIGHT.getCode()) {
                direction = LEFT.getCode();
            } else if (event.getCode() == RIGHT && direction != LEFT.getCode()) {
                direction = RIGHT.getCode();
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

        Rectangle head = snake.getFirst();
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

        if (newHead.getX() == food.getCenterX() - TILE_SIZE / 2 && newHead.getY() == food.getCenterY() - TILE_SIZE / 2) {
            // Wąż zjadł jedzenie, spawn nowego jedzenia
            spawnFood((Pane) head.getParent());
        } else {
            // Usuwamy ostatni segment ogona
            Rectangle tail = snake.removeLast();
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

//        food = new Rectangle(x, y, TILE_SIZE, TILE_SIZE);
//        food.setFill(Color.RED);
//        pane.getChildren().add(food);

        // Tworzymy jedzenie jako koła (Circle) z promieniem równym połowie TILE_SIZE
        food = new Circle(x + TILE_SIZE / 2, y + TILE_SIZE / 2, TILE_SIZE / 2);
        food.setFill(Color.RED); // Ustawienie koloru jedzenia
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