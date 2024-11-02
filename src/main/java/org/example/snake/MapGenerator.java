

package org.example.snake;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MapGenerator {

    private static final int NUM_ISLANDS = 5; // Liczba wewnętrznych wysp
    private static final int MIN_ISLAND_RADIUS = 30; // Minimalny promień wyspy
    private static final int MAX_ISLAND_RADIUS = 80; // Maksymalny promień wyspy
    private static final int MIN_SIDES = 8; // Minimalna liczba krawędzi
    private static final int MAX_SIDES = 25; // Maksymalna liczba krawędzi
    private final Random random = new Random();
    private static final int MIN_DISTANCE_FROM_CENTER = 150; // Minimalna odległość od środka
    private final List<Polygon> islands = new ArrayList<>(); // Lista wysp




    public void generateMap(Pane root, int width, int height) {
        // Ustawienie tła na brązowe (ląd)
        root.setStyle("-fx-background-color: brown;");

        // Dodanie nieregularnej wyspy w środku mapy jako obszar wody
        createWaterIsland(root, width, height);

        // Generowanie dodatkowych wysp wewnątrz mapy
        for (int i = 0; i < NUM_ISLANDS; i++) {
            createPolygonalIsland(root, width, height);
        }
    }

    private void createWaterIsland(Pane root, int width, int height) {
        Polygon waterIsland = new Polygon();
        int islandRadius = (Math.min(width, height) / 2); // Promień centralnej wyspy (wody)

        int sides = random.nextInt(2*MAX_SIDES) + 2*MAX_SIDES; // Więcej krawędzi dla płynności krawędzi
        double centerX = width / 2.0;
        double centerY = height / 2.0;

        // Tworzenie punktów dla wyspy w środku mapy
        for (int i = 0; i < sides; i++) {
            double angle = 2 * Math.PI * i / sides;
            double radius = islandRadius * (1 + random.nextDouble() * 0.1); // Losowość w promieniu dla nieregularności
            double x = centerX + Math.cos(angle) * radius;
            double y = centerY + Math.sin(angle) * radius;
            waterIsland.getPoints().addAll(x, y);
        }

        // Ustawienie koloru wyspy (wody) na błękitny
        waterIsland.setFill(Color.LIGHTBLUE);

        // Dodanie wody jako centralnej wyspy do planszy
        root.getChildren().add(waterIsland);
    }

    private void createPolygonalIsland(Pane root, int width, int height) {
        // Losowanie pozycji środka wyspy z minimalnym odstępem od środka
        double centerX;
        double centerY;
        do {
            centerX = random.nextInt(width - 2 * MAX_ISLAND_RADIUS) + MAX_ISLAND_RADIUS;
            centerY = random.nextInt(height - 2 * MAX_ISLAND_RADIUS) + MAX_ISLAND_RADIUS;
        } while (Math.hypot(centerX - width / 2.0, centerY - height / 2.0) < MIN_DISTANCE_FROM_CENTER);

        // Losowanie rozmiaru wyspy
        int islandRadius = random.nextInt(MAX_ISLAND_RADIUS - MIN_ISLAND_RADIUS + 1) + MIN_ISLAND_RADIUS;

        // Losowa liczba krawędzi dla wielokąta
        int sides = random.nextInt(MAX_SIDES - MIN_SIDES + 1) + MIN_SIDES;

        // Tworzenie punktów dla wielokąta
        Polygon island = new Polygon();
        for (int i = 0; i < sides; i++) {
            double angle = 2 * Math.PI * i / sides;
            double radius = islandRadius * (0.7 + random.nextDouble() * 0.3); // Wprowadzenie losowości w promieniu
            double x = centerX + Math.cos(angle) * radius;
            double y = centerY + Math.sin(angle) * radius;
            island.getPoints().addAll(x, y);
        }

        // Ustawienie koloru wyspy na brązowy
        island.setFill(Color.BROWN);


        // Dodanie wyspy do planszy i do listy wysp
        root.getChildren().add(island);
        islands.add(island);
    }

    public boolean checkCollisionWithSnake(Circle snakeHead) {
        for (Polygon island : islands) {
            if (island.contains(snakeHead.getCenterX(), snakeHead.getCenterY())) {
                return true; // Kolizja
            }
        }
        return false; // Brak kolizji
    }

}







