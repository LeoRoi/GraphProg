package com.fxgraph.graph;

import java.util.ArrayList;
import java.util.List;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;

/**
 * node representation
 * with coordinates & name
 */
public class Cell extends StackPane {

    String cellId;

    List<Cell> children = new ArrayList<>();
    List<Cell> parents = new ArrayList<>();

    double x, y;

    Node view;

    public Cell(String cellId) {
        this.cellId = cellId;
    }

    public void addCellChild(Cell cell) {
        children.add(cell);
    }

    public List<Cell> getCellChildren() {
        return children;
    }

    public void addCellParent(Cell cell) {
        parents.add(cell);
    }

    public List<Cell> getCellParents() {
        return parents;
    }

    public void removeCellChild(Cell cell) {
        children.remove(cell);
    }

    public void setView(Node view, String id) {

        this.view = view;
        if (id != null) {
            Text text = new Text(id);
            getChildren().addAll(view, text);

            //how to automize???
            if(text.getText().length() == 1) setStyle("-fx-font-size: 30pt");
            if(text.getText().length() == 2) setStyle("-fx-font-size: 18pt");
            if(text.getText().length() == 3) setStyle("-fx-font-size: 14pt");
            //setAlignment(Pos.BOTTOM_CENTER);
        }
        else {
            getChildren().add(view);
        }
    }

    public Node getView() {
        return this.view;
    }

    public String getCellId() {
        return cellId;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }
}