package com.fxgraph.graph;

import java.io.Serializable;
import java.util.List;

public class Structure implements Serializable {
    List<String> listOfCellIds;
    List<EdgeWrapper> listOfEdges;
    List<Location> listOfLocations;

    public Structure(List<String> listOfCellIds, List<Location> listOfLocations, List<EdgeWrapper> listOfEdges) {
        this.listOfCellIds = listOfCellIds;
        this.listOfLocations = listOfLocations;
        this.listOfEdges = listOfEdges;
    }

    public List<String> getListOfCellIds() {
        return listOfCellIds;
    }

    public List<Location> getListOfLocations() {
        return listOfLocations;
    }

    public List<EdgeWrapper> getListOfEdges() {
        return listOfEdges;
    }
}
