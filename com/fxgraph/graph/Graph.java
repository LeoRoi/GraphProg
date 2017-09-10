package com.fxgraph.graph;

import com.fxgraph.cells.Connection;
import javafx.scene.Group;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Pane;
import utils.AlertBox;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Graph {

    private Model model;

    private Group canvas;

    private CustomPane scrollPane;

    Gestures gestures;

    /**
     * the pane wrapper is necessary or else the scrollpane would always align
     * the top-most and left-most child to the top and left eg when you drag the
     * top child down, the entire scrollpane would move down
     */
    CanvasChild canvasChild;

    public Graph() {

        this.model = new Model();

        canvas = new Group();

        canvasChild = new CanvasChild();

        canvas.getChildren().add(canvasChild);

        gestures = new Gestures(this);

        scrollPane = new CustomPane(canvas);
        scrollPane.setMinViewportWidth(500);
        scrollPane.setMinViewportHeight(350);

        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);

    }

    public ScrollPane getScrollPane() {
        return this.scrollPane;
    }

    public Pane getCanvasChild() {
        return this.canvasChild;
    }

    public Model getModel() {
        return model;
    }

    public void beginUpdate() {
    }

    public void endUpdate() {

        // add components to graph pane
        getCanvasChild().getChildren().addAll(model.getAddedConnections());
        getCanvasChild().getChildren().addAll(model.getAddedCells());

        // remove components from graph pane
        getCanvasChild().getChildren().removeAll(model.getRemovedCells());
        getCanvasChild().getChildren().removeAll(model.getRemovedConnections());

        // enable dragging of cells
        for (Cell cell : model.getAddedCells()) {
            gestures.makeDeletable(cell);
        }

        // every cell must have a parent, if it doesn't, then the graphParent is
        // the parent
        getModel().attachOrphansToGraphParent(model.getAddedCells());

        // remove reference to graphParent
        getModel().disconnectFromGraphParent(model.getRemovedCells());

        // merge added & removed cells with all cells
        getModel().merge();
    }

    public double getScale() {
        return this.scrollPane.getScaleValue();
    }

    public int saveGraph(String name) {
            try {
                FileOutputStream fos = new FileOutputStream(name + ".ser");
                ObjectOutputStream oos = new ObjectOutputStream(fos);

                // get cell ids, connections and position
                List<Cell> cells = model.getAllCells();
                List<String> cellIds = new ArrayList<>();
                List<Location> locations = new ArrayList<>();
                List<Connection> connections = model.getAllConnections();
                List<EdgeWrapper> edgeWrappers = new ArrayList<>();
                for (int i = 0; i < model.getAllCells().size(); i++) {
                    Cell cell = cells.get(i);
                    String id = cell.getCellId();
                    cellIds.add(id);
                    double x = cell.getX();
                    double y = cell.getY();
                    locations.add(new Location(x, y));
                }

                for (int i = 0; i < model.getAllConnections().size(); i++) {
                    Connection connection = connections.get(i);
                    EdgeWrapper edgeWrapper = new EdgeWrapper(connection.getSource().getCellId(), connection.getTarget().getCellId(), connection.getText());
                    edgeWrappers.add(edgeWrapper);
                }

                // save
                Structure structure = new Structure(cellIds, locations, edgeWrappers);
                oos.writeObject(structure);
                oos.close();
                return 0;
            } catch (IOException e) {
                AlertBox.display("Error", "Sorry, we could not save the graph");
                e.printStackTrace();
                return -1;
            }
    }

    public static Structure loadGraph(String name) {
        Structure result = null;
        try {
            FileInputStream fis = new FileInputStream(name + ".ser");
            ObjectInputStream ois = new ObjectInputStream(fis);
            result = (Structure) ois.readObject();
            ois.close();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            AlertBox.display("Error", "Sorry, we could not load the graph");
        }
        return result;
    }

    public void clearUI() {
        beginUpdate();
        model.getRemovedCells().addAll(model.getAllCells());
        model.getRemovedConnections().addAll(model.getAllConnections());
        endUpdate();
        model.clear();

    }
}