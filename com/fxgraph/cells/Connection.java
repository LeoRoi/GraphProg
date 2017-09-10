package com.fxgraph.cells;

import com.fxgraph.graph.Cell;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.shape.Line;

public class Connection extends Group {

    protected Cell source;
    protected Cell target;
    String text;

    Arrow line;
    Label label;

    public Connection(Cell source, Cell target, String text) {

        this.source = source;
        this.target = target;
        this.text = text;

        source.addCellChild(target);
        target.addCellParent(source);

        line = new Arrow();

        line.startXProperty().bind(source.layoutXProperty().add(source.getBoundsInParent().getWidth() / 2.0));
        line.startYProperty().bind(source.layoutYProperty().add(source.getBoundsInParent().getHeight() / 2.0));

        line.endXProperty().bind(target.layoutXProperty().add(target.getBoundsInParent().getWidth() / 2.0));
        line.endYProperty().bind(target.layoutYProperty().add(target.getBoundsInParent().getHeight() / 2.0));

        getChildren().add(line);
        if(text != null) {
            if (!text.equals("")) {
                label = new Label(text);
                getChildren().add(label);
                // Node 1
                double position1X = source.layoutXProperty().doubleValue();
                double position1Y = source.layoutYProperty().doubleValue();

                //Node 2
                double position2X = target.layoutXProperty().doubleValue();
                double position2Y = target.layoutYProperty().doubleValue();

                // Endposition
                double endpositionX =  (position1X + position2X) / 2.0;
                double endpositionY =  (position1Y + position2Y) / 2.0;
                label.relocate(endpositionX, endpositionY);
            }
        }

    }

    public Cell getSource() {
        return source;
    }

    public Cell getTarget() {
        return target;
    }

    public Label getLabel() {
        return label;
    }

    public String getText() {
        return text;
    }
}