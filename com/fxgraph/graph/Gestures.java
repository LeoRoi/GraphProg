package com.fxgraph.graph;

import com.fxgraph.cells.Connection;
import com.fxgraph.cells.RectangleCell;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;

import java.util.List;

public class Gestures {

    final DragContext dragContext = new DragContext();

    Graph graph;

    public Gestures(Graph graph) {
        this.graph = graph;
    }

    public void makeDeletable(final Node node) {

        node.setOnMousePressed(onMousePressedEventHandler);

    }

    public void makeDraggable(final Node node) {
        node.setOnMousePressed(onMousePressedEventHandler2);
        node.setOnMouseDragged(onMouseDraggedEventHandler);
        node.setOnMouseReleased(onMouseReleasedEventHandler);

    }


    EventHandler<MouseEvent> onMousePressedEventHandler = new EventHandler<MouseEvent>() {

        @Override
        public void handle(MouseEvent event) {

            Node node = (Node) event.getSource();

//            double scale = graph.getScale();
//
//            dragContext.x = node.getBoundsInParent().getMinX() * scale - event.getScreenX();
//            dragContext.y = node.getBoundsInParent().getMinY()  * scale - event.getScreenY();

            Model model = graph.getModel();
            // find and remove cell
            RectangleCell cell = (RectangleCell) node;
            model.getRemovedCells().add(cell);
            graph.beginUpdate();
            model.getCellMap().remove(cell.getCellId());
            graph.endUpdate();

            // find and remove edges
            List<Connection> arrows = model.getAllConnections();
            for (Connection arrow : arrows) {
                if (arrow.getSource().getCellId().equals(cell.getCellId())
                        || arrow.getTarget().getCellId().equals(cell.getCellId())) {
                    model.getRemovedConnections().add(arrow);
                }
            }
            graph.beginUpdate();
            graph.endUpdate();

        }
    };

    EventHandler<MouseEvent> onMousePressedEventHandler2 = new EventHandler<MouseEvent>() {

        @Override
        public void handle(MouseEvent event) {

            Node node = (Node) event.getSource();

            double scale = graph.getScale();

            dragContext.x = node.getBoundsInParent().getMinX() * scale - event.getScreenX();
            dragContext.y = node.getBoundsInParent().getMinY() * scale - event.getScreenY();

        }
    };

    EventHandler<MouseEvent> onMouseDraggedEventHandler = new EventHandler<MouseEvent>() {

        @Override
        public void handle(MouseEvent event) {

            Node node = (Node) event.getSource();

            double offsetX = event.getScreenX() + dragContext.x;
            double offsetY = event.getScreenY() + dragContext.y;

            // adjust the offset in case we are zoomed
            double scale = graph.getScale();

            offsetX /= scale;
            offsetY /= scale;

            node.relocate(offsetX, offsetY);

        }
    };

    EventHandler<MouseEvent> onMouseReleasedEventHandler = new EventHandler<MouseEvent>() {

        @Override
        public void handle(MouseEvent event) {

        }
    };


    class DragContext {
        double x;
        double y;

    }
}