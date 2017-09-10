package com.fxgraph.graph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fxgraph.cells.*;
import com.sun.istack.internal.Nullable;
import utils.AlertBox;

/**
 * data representation of cells and their connections
 */
public class Model {
    Cell graphParent;

    List<Cell> allCells;
    List<Cell> addedCells;
    List<Cell> removedCells;

    List<Connection> allConnections;
    List<Connection> addedConnections;
    List<Connection> removedConnections;

    //map of all the existing cells
    // <id = key, cell = value>
    Map<String, Cell> cellMap;

    public Model() {
        graphParent = new Cell("_ROOT_");
        // clear model, create lists
        clear();
    }

    public void clear() {
        allCells = new ArrayList<>();
        addedCells = new ArrayList<>();
        removedCells = new ArrayList<>();

        allConnections = new ArrayList<>();
        addedConnections = new ArrayList<>();
        removedConnections = new ArrayList<>();

        cellMap = new HashMap<>(); // <id,cell>
    }

    public void clearAddedLists() {
        addedCells.clear();
        addedConnections.clear();
    }

    public List<Cell> getAddedCells() {
        return addedCells;
    }

    public List<Cell> getRemovedCells() {
        return removedCells;
    }

    public List<Cell> getAllCells() {
        return allCells;
    }

    public List<Connection> getAddedConnections() {
        return addedConnections;
    }

    public List<Connection> getRemovedConnections() {
        return removedConnections;
    }

    public List<Connection> getAllConnections() {
        return allConnections;
    }

    public RectangleCell addCell(String id, TypeOfCell type, Double x, Double y) {
        switch (type) {
            case RECTANGLE:
                RectangleCell rectangleCell = new RectangleCell(id);
                if (x != null && y != null) {
                    rectangleCell.relocate(x, y);
                    rectangleCell.setX(x);
                    rectangleCell.setY(y);
                }
                addCell(rectangleCell);
                return rectangleCell;
            default:
                throw new UnsupportedOperationException("Unsupported type: " + type);
        }
    }

    private void addCell(Cell cell) {
        addedCells.add(cell);
        cellMap.put(cell.getCellId(), cell);
    }

    public void addEdge(String sourceId, String targetId, String text) {
        Cell sourceCell = cellMap.get(sourceId);
        Cell targetCell = cellMap.get(targetId);

        Connection connection = new Connection(sourceCell, targetCell, text);
        addedConnections.add(connection);
    }

    public int addEdgeS(String pSource, String pTarget, String weight) {
        Cell pSourceCell = cellMap.get(pSource);
        Cell pTargetCell = cellMap.get(pTarget);
        boolean edge;

        edge = conCheck(allConnections, pSource, pTarget);
        if(edge)
            return 0;

        edge = conCheck(addedConnections, pSource, pTarget);
        if(edge)
            return 0;

        Connection connection = new Connection(pSourceCell, pTargetCell, weight);
        addedConnections.add(connection);
        return 1;
    }

    private boolean conCheck(List<Connection> conList, String s, String e) {

        //source & target the same cell?
        if(s.equals(e))
            return true;

        //connection already exists?
        for (Connection temp : conList) {
            String source = temp.getSource().getCellId();
            String target = temp.getTarget().getCellId();

            if ((s.equals(source) && e.equals(target)))
                return true;
        }
        return false;
    }

    /**
     * Attach all cells which don't have a parent to graphParent
     *
     * @param cellList
     */
    public void attachOrphansToGraphParent(List<Cell> cellList) {

        for (Cell cell : cellList) {
            if (cell.getCellParents().size() == 0) {
                graphParent.addCellChild(cell);
            }
        }

    }

    /**
     * Remove the graphParent reference if it is set
     *
     * @param cellList
     */
    public void disconnectFromGraphParent(List<Cell> cellList) {

        for (Cell cell : cellList) {
            graphParent.removeCellChild(cell);
        }
    }

    public void merge() {

        // cells
        allCells.addAll(addedCells);
        allCells.removeAll(removedCells);

        addedCells.clear();
        removedCells.clear();

        // edges
        allConnections.addAll(addedConnections);
        allConnections.removeAll(removedConnections);

        addedConnections.clear();
        removedConnections.clear();

    }

    public Map<String, Cell> getCellMap() {
        return cellMap;
    }

    @Nullable
    public Cell getCell(String id) {
        return cellMap.get(id);
    }
}