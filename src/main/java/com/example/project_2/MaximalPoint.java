package com.example.project_2;

/*
 * Name: Kamil Roginski
 * Project: CMSC 315 Programming Project 2 – Maximal Points
 * Date: 7 APR 2025
 * Description: Immutable class representing a 2D point. It implements the Comparable interface
 *              by comparing x coordinates and provides a method to test if another point lies below
 *              and to the left of this point.
 */

public final class MaximalPoint implements Comparable<MaximalPoint> {
    private final double x;
    private final double y;

    public MaximalPoint(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    /**
     * Returns true if THIS point is strictly below and to the left of the OTHER point.
     * Equivalently: this.x < other.x AND this.y < other.y
     */
    public boolean isBelowAndLeft(MaximalPoint other) {
        return (this.x < other.x) && (this.y > other.y);
    }

    /**
     * Compare primarily by x ascending.
     * If x is the same, compare y descending
     * so the higher point (bigger y) comes first.
     */
    @Override
    public int compareTo(MaximalPoint other) {
        int xCompare = Double.compare(this.x, other.x);
        if (xCompare != 0) {
            return xCompare;  // sort by x ascending
        }
        // If same x, sort by y descending
        return Double.compare(this.y, other.y);
    }

    @Override
    public String toString() {
        return "(" + x + ", " + y + ")";
    }
}
