package com.fxgraph.layout.random;

import java.util.List;
import java.util.Random;

import com.fxgraph.graph.Cell;
import com.fxgraph.graph.Graph;
import com.fxgraph.layout.base.Layout;

public class RegularLayout extends Layout {

    Graph graph;

    Random rnd = new Random();

    public RegularLayout(Graph graph) {

        this.graph = graph;

    }

    public void execute() {

        List<Cell> cells = graph.getModel().getAllCells();

        for (Cell cell : cells) {

            double x = rnd.nextDouble() * 300;
            double y = rnd.nextDouble() * 300;
            cell.setX(x);
            cell.setY(y);
            cell.relocate(x, y);

        }

    }

}