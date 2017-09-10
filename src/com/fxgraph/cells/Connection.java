package com.fxgraph.cells;

import com.fxgraph.graph.Cell;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;

/**
 * connection representation
 */
public class Connection extends Group {

    protected Cell source;
    protected Cell target;

    //weight
    Label label;
    String text;

    //edge
    Arrow line;

    //start, finish, weight
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

        label = new Label(text);
        label.setFont(new Font(12));
        getChildren().add(label);
        // Node 1
        double position1X = source.layoutXProperty().doubleValue();
        double position1Y = source.layoutYProperty().doubleValue();
        //Node 2
        double position2X = target.layoutXProperty().doubleValue();
        double position2Y = target.layoutYProperty().doubleValue();
        // End position
        double endPosX = (position1X + position2X) * 0.6;// 2.0;
        double endPosY = (position1Y + position2Y) * 0.6;// 2.0;
        label.relocate(endPosX, endPosY);
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