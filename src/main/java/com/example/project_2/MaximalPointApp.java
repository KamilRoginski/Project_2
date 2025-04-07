package com.example.project_2;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.util.ArrayList;
import java.io.File;
import java.util.Scanner;
import java.io.FileNotFoundException;

/*
 * Name: Kamil Roginski
 * Project: CMSC 315 Programming Project 2 – Maximal Points
 * Date: 7 APR 2025
 * Description: Main application class that reads the initial set of points from a file
 *              ("points.txt"), creates the custom pane, and sets up the JavaFX scene.
 */

public class MaximalPointApp extends Application {
    @Override
    public void start(Stage primaryStage) {
        ArrayList<MaximalPoint> initialPoints = readPointsFromFile("points.txt");
        MaximalPointPane pane = new MaximalPointPane(initialPoints);

        Scene scene = new Scene(pane, 500, 500);
        primaryStage.setTitle("Maximal Points");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // Reads points from a file where each line (ignoring blank lines) has two double values.
    private ArrayList<MaximalPoint> readPointsFromFile(String filename) {
        ArrayList<MaximalPoint> points = new ArrayList<>();
        try {
            Scanner scanner = new Scanner(new File(filename));
            while (scanner.hasNext()) {
                if (scanner.hasNextDouble()) {
                    double x = scanner.nextDouble();
                    if (scanner.hasNextDouble()) {
                        double y = scanner.nextDouble();
                        points.add(new MaximalPoint(x, y));
                    }
                } else {
                    scanner.next();
                }
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            System.out.println("File " + filename + " not found.");
        }
        return points;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
