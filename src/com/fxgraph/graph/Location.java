package com.fxgraph.graph;

import java.io.Serializable;

/**
 * X & Y coordinates for save & load sake
 */
public class Location implements Serializable {
    double x;
    double y;

    public Location(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }
}
