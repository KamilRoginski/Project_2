package com.example.project_2;

import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.input.MouseButton;
import javafx.scene.paint.Color;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import java.util.ArrayList;
import java.util.Collections;


/*
 * Name: Kamil Roginski
 * Project: CMSC 315 Programming Project 2 – Maximal Points
 * Date: 7 APR 2025
 * Description: A custom JavaFX Pane that displays points and computes the set of maximal points.
 *              Left mouse clicks add points, right mouse clicks remove points, and the maximal points
 *              are connected by red lines. (Changed from black because they are more visible)
 */

public class MaximalPointPane extends Pane {
    private ArrayList<MaximalPoint> points;
    private ArrayList<Circle> pointCircles;
    private final double POINT_RADIUS = 5.0;

    public MaximalPointPane(ArrayList<MaximalPoint> initialPoints) {
        this.points = new ArrayList<>(initialPoints);
        this.pointCircles = new ArrayList<>();
        setPrefSize(500, 500);
        drawPoints();
        computeAndDrawMaximalPoints();

        // Placed so that "getHeight()" doesnt go out of bounds
        layoutBoundsProperty().addListener((obs, oldBounds, newBounds) -> {
                    computeAndDrawMaximalPoints();});

        // Handle mouse clicks
        setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (event.getButton() == MouseButton.PRIMARY) {
                    // Left click: add point
                    addPoint(event.getX(), event.getY());
                } else if (event.getButton() == MouseButton.SECONDARY) {
                    // Right click: remove point if within tolerance
                    removePoint(event.getX(), event.getY());
                }
            }
        });
    }

    // Adds a point at the specified coordinates and updates the display.
    private void addPoint(double x, double y) {
        MaximalPoint newPoint = new MaximalPoint(x, y);
        points.add(newPoint);
        Circle circle = new Circle(x, y, POINT_RADIUS, Color.BLACK);
        pointCircles.add(circle);
        getChildren().add(circle);
        computeAndDrawMaximalPoints();
    }

    // Removes a point if the click is within a set tolerance.
    private void removePoint(double x, double y) {
        final double TOLERANCE = 10;
        MaximalPoint toRemove = null;
        Circle circleToRemove = null;
        for (int i = 0; i < points.size(); i++) {
            MaximalPoint p = points.get(i);
            if (Math.abs(p.getX() - x) <= TOLERANCE && Math.abs(p.getY() - y) <= TOLERANCE) {
                toRemove = p;
                circleToRemove = pointCircles.get(i);
                break;
            }
        }
        if (toRemove != null) {
            points.remove(toRemove);
            pointCircles.remove(circleToRemove);
            getChildren().remove(circleToRemove);
            computeAndDrawMaximalPoints();
        }
    }

    // Redraws all points.
    private void drawPoints() {
        getChildren().clear();
        pointCircles.clear();
        for (MaximalPoint p : points) {
            Circle circle = new Circle(p.getX(), p.getY(), POINT_RADIUS, Color.BLACK);
            pointCircles.add(circle);
            getChildren().add(circle);
        }
    }

    // Implemented to remove multiple print statements upon testing
    private boolean hasPrintedDebug = false;

    // Computes the maximal points and draws red lines connecting them.
    private void computeAndDrawMaximalPoints() {
        // Remove previously drawn lines
        getChildren().removeIf(node -> node instanceof Line);

        ArrayList<MaximalPoint> maximalPoints = findMaximalPoints();
        Collections.sort(maximalPoints);

        // Temp Test
        // Created to verify Maximal points are found.
        // Ran into issues where it would only read 2 Maximal Points

        if (!hasPrintedDebug) {
            System.out.println("Maximal points found:");
            for (MaximalPoint mp : maximalPoints) {
                System.out.println(mp);
            }
            hasPrintedDebug = true;
        }

        // If no maximal points, nothing to draw
        if (maximalPoints.isEmpty()) {
            return;
        }

        // Made for where vertical line meets X-axis
        double paneHeight = getLayoutBounds().getHeight();

        // 1) Draw from left edge to the first maximal point
        MaximalPoint first = maximalPoints.get(0);
        Line lineToFirst = new Line(
                0,                 // left edge
                first.getY(),      // same Y as first maximal point
                first.getX(),
                first.getY()
        );
        lineToFirst.setStroke(Color.RED);
        getChildren().add(lineToFirst);

        // 2) Draw the “staircase” among consecutive maximal points
        for (int i = 0; i < maximalPoints.size() - 1; i++) {
            MaximalPoint p1 = maximalPoints.get(i);
            MaximalPoint p2 = maximalPoints.get(i + 1);

            // Horizontal segment
            Line horizontal = new Line(p1.getX(), p1.getY(), p2.getX(), p1.getY());
            horizontal.setStroke(Color.RED);
            getChildren().add(horizontal);

            // Vertical segment
            Line vertical = new Line(p2.getX(), p1.getY(), p2.getX(), p2.getY());
            vertical.setStroke(Color.RED);
            getChildren().add(vertical);
        }

        // 3) Draw from the last maximal point down to the bottom edge
        MaximalPoint last = maximalPoints.get(maximalPoints.size() - 1);
        Line lineFromLast = new Line(
                last.getX(),
                last.getY(),
                last.getX(),
                paneHeight         // bottom edge
        );
        lineFromLast.setStroke(Color.RED);
        getChildren().add(lineFromLast);
    }

    // Determines the set of maximal points using the isBelowAndLeft method.
    private ArrayList<MaximalPoint> findMaximalPoints() {
        ArrayList<MaximalPoint> maximal = new ArrayList<>();
        for (MaximalPoint p : points) {
            boolean isMaximal = true;
            for (MaximalPoint q : points) {
                if (p != q && p.isBelowAndLeft(q)) {
                    isMaximal = false;
                    break;
                }
            }
            if (isMaximal) {
                maximal.add(p);
            }
        }
        return maximal;
    }
}
