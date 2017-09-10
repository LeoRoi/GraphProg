package application;

import com.fxgraph.cells.Connection;
import com.fxgraph.cells.RectangleCell;
import com.fxgraph.graph.*;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import com.fxgraph.layout.base.Layout;
import com.fxgraph.layout.random.RegularLayout;
import utils.AlertBox;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Main extends Application {

    private Insets PADDING_INSETS = new Insets(15, 0, 0, 0);
    private double SPACING_SETTING = 20D;
    private double MAX_WIDTH_TEXT_FIELD = 250D;
    private double MAX_WIDTH_BUTTON = 100D;
    private double MAX_HEIGHT_BUTTON = 40D;

    // main ui elements
    VBox root;
    Graph graph;

    // arrow prompts
    HBox arrowPromptsHbox;
    TextField textField1ArrowPrompts;
    Button connectButton;
    TextField textField2ArrowPrompts;
    TextField textField3ArrowPrompts;

    // delete arrow prompts
    HBox arrowDeletePromptsHbox;
    TextField textField1DeleteArrowPrompts;
    Button deleteButton;
    TextField textField2DeleteArrowPrompts;

    // save prompts
    private HBox savePromptsHbox;
    private TextField textField1SavePrompts;
    private Button saveButton;
    private Label labelSavePrompts;

    // read prompts
    private HBox readPromptsHbox;
    private TextField textField1ReadPrompts;
    private Button readButton;

    // random prompts
    private HBox randomPromptsHbox;
    private TextField textField1RandomPrompts;
    private Button randomButton;


    @Override
    public void start(Stage primaryStage) {
        graph = new Graph();

        // add Prompts
        addPrompts();

        // add canvas
        Scene scene = new Scene(root, 1024, 768);
        scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());

        primaryStage.setScene(scene);
        primaryStage.show();

        addGraphComponents();

        Layout layout = new RegularLayout(graph);
        layout.execute();
    }

    private void addPrompts() {

        // set root
        root = new VBox();
        root.setPadding(new Insets(15, 15, 15, 15));
        root.setSpacing(SPACING_SETTING);
        root.getChildren().add(graph.getScrollPane());

//        // create arrow prompts
        createArrowPrompts();
        setArrowPromptsListeners();
//
//        // delete arrow prompts
        createDeleteArrowPrompts();
        setDeleteArrowPromptsListeners();
//
//        // save prompts
        createSavePrompts();
        setSavePromptsListeners();

        // read prompts
        createReadPrompts();
        setReadPromptsListeners();

        // randomPrompts
        createRandomPrompts();
        setRandomPromptsListeners();

    }


    private void createArrowPrompts() {
        arrowPromptsHbox = new HBox();
        arrowPromptsHbox.setPadding(PADDING_INSETS);
        arrowPromptsHbox.setSpacing(SPACING_SETTING);

        textField1ArrowPrompts = new TextField();
        textField1ArrowPrompts.setMaxWidth(MAX_WIDTH_TEXT_FIELD);
        textField1ArrowPrompts.setPromptText("Choose source cell");

        connectButton = new Button();
        connectButton.setMaxWidth(MAX_WIDTH_BUTTON);
        connectButton.setMaxHeight(MAX_HEIGHT_BUTTON);
        connectButton.setText("Connect");

        textField2ArrowPrompts = new TextField();
        textField2ArrowPrompts.setMaxWidth(MAX_WIDTH_TEXT_FIELD);
        textField2ArrowPrompts.setPromptText("Choose target cell");

        textField3ArrowPrompts = new TextField();
        textField3ArrowPrompts.setMaxWidth(MAX_WIDTH_TEXT_FIELD + 200);
        textField3ArrowPrompts.setPromptText("Choose the sign. 3 letters");

        arrowPromptsHbox.getChildren().addAll(textField1ArrowPrompts, connectButton, textField2ArrowPrompts, textField3ArrowPrompts);
        root.getChildren().addAll(arrowPromptsHbox);
    }

    private void setArrowPromptsListeners() {
        connectButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                // take text from fields
                String field1 = textField1ArrowPrompts.getText().trim();
                String field2 = textField2ArrowPrompts.getText().trim();
                String field3 = textField3ArrowPrompts.getText().trim();
                // get cells
                Model model = graph.getModel();
                RectangleCell rectangleCell1 = null;
                RectangleCell rectangleCell2 = null;
                if (field3.length() > 3) {
                    AlertBox.display("Input error", "The sign should not be more than 3 letters");
                } else {
                    // find cell 1
                    rectangleCell1 = (RectangleCell) model.getCell(field1);
                    // find cell 2
                    rectangleCell2 = (RectangleCell) model.getCell(field2);

                }
                // if cells were found
                boolean found = false;
                if (rectangleCell1 != null && rectangleCell2 != null) {
                    // check if cells are already connected
                    List<Connection> connections = model.getAllConnections();
                    for (Connection connection : connections) {
                        String presumedSource = rectangleCell1.getCellId();
                        String presumedTarget = rectangleCell2.getCellId();
                        String source = connection.getSource().getCellId();
                        String target = connection.getTarget().getCellId();
                        if ((presumedSource.equals(source) && presumedTarget.equals(target))
                                || (presumedTarget.equals(source) && presumedSource.equals(target))) {
                            found = true;
                        }
                    }
                }
                // add cells
                if (!found) {
                    graph.beginUpdate();
                    model.addEdge(rectangleCell1.getCellId(), rectangleCell2.getCellId(), field3);
                    graph.endUpdate();
                } else {
                    AlertBox.display("", "There is no such cell or they are already connected");
                }
            }
        });
    }

    private void createDeleteArrowPrompts() {
        arrowDeletePromptsHbox = new HBox();
        arrowDeletePromptsHbox.setPadding(PADDING_INSETS);
        arrowDeletePromptsHbox.setSpacing(SPACING_SETTING);

        textField1DeleteArrowPrompts = new TextField();
        textField1DeleteArrowPrompts.setMaxWidth(MAX_WIDTH_TEXT_FIELD);
        textField1DeleteArrowPrompts.setPromptText("Choose source cell");

        deleteButton = new Button();
        deleteButton.setMaxWidth(MAX_WIDTH_BUTTON);
        deleteButton.setMaxHeight(MAX_HEIGHT_BUTTON);
        deleteButton.setText("Delete");

        textField2DeleteArrowPrompts = new TextField();
        textField2DeleteArrowPrompts.setMaxWidth(MAX_WIDTH_TEXT_FIELD);
        textField2DeleteArrowPrompts.setPromptText("Choose target cell");

        arrowDeletePromptsHbox.getChildren().addAll(textField1DeleteArrowPrompts, deleteButton, textField2DeleteArrowPrompts);
        root.getChildren().addAll(arrowDeletePromptsHbox);
    }

    private void setDeleteArrowPromptsListeners() {
        Model model = graph.getModel();
        deleteButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                // take text from fields
                String field1 = textField1DeleteArrowPrompts.getText().trim();
                String field2 = textField2DeleteArrowPrompts.getText().trim();

                int counter = 0;
                if (!field1.equals("") || !field2.equals("")) {
                    // find edges
                    List<Connection> arrows = model.getAllConnections();
                    for (Connection arrow : arrows) {
                        if (arrow.getSource().getCellId().equals(field1) && arrow.getTarget().getCellId().equals(field2)) {
                            model.getRemovedConnections().add(arrow);
                            counter++;
                        }
                    }
                    graph.beginUpdate();
                    graph.endUpdate();
                } else if (counter == 0) {
                    AlertBox.display("Error", "Try again");
                }
            }
        });
    }

    private void createSavePrompts() {
        savePromptsHbox = new HBox();
        savePromptsHbox.setPadding(PADDING_INSETS);
        savePromptsHbox.setSpacing(SPACING_SETTING);

        textField1SavePrompts = new TextField();
        textField1SavePrompts.setMaxWidth(MAX_WIDTH_TEXT_FIELD);
        textField1SavePrompts.setPromptText("Name of the graph");

        saveButton = new Button();
        saveButton.setMaxWidth(MAX_WIDTH_BUTTON);
        saveButton.setMaxHeight(MAX_HEIGHT_BUTTON);
        saveButton.setText("Save");

        labelSavePrompts = new Label();
        labelSavePrompts.setMaxWidth(MAX_WIDTH_TEXT_FIELD);

        savePromptsHbox.getChildren().addAll(textField1SavePrompts, saveButton, labelSavePrompts);
        root.getChildren().addAll(savePromptsHbox);
    }

    private void setSavePromptsListeners() {
        saveButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                String field1 = textField1SavePrompts.getText().trim();
                if (!field1.equals("") && field1.length() < 4) {
                    int result = graph.saveGraph(field1);
                    if (result == 0) {
                        labelSavePrompts.setText("Name of the graph: " + field1);
                    }
                } else {
                    AlertBox.display("Error", "Provide the name for the graph max 3 letters");
                }
            }
        });
    }

    private void createReadPrompts() {
        readPromptsHbox = new HBox();
        readPromptsHbox.setPadding(PADDING_INSETS);
        readPromptsHbox.setSpacing(SPACING_SETTING);

        textField1ReadPrompts = new TextField();
        textField1ReadPrompts.setMaxWidth(MAX_WIDTH_TEXT_FIELD);
        textField1ReadPrompts.setPromptText("Name of the graph");

        readButton = new Button();
        readButton.setMaxWidth(MAX_WIDTH_BUTTON);
        readButton.setMaxHeight(MAX_HEIGHT_BUTTON);
        readButton.setText("Load");

        readPromptsHbox.getChildren().addAll(textField1ReadPrompts, readButton);
        root.getChildren().addAll(readPromptsHbox);
    }

    private void setReadPromptsListeners() {
        readButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                String field1 = textField1ReadPrompts.getText().trim();
                if (!field1.equals("")) {
                    Structure result = graph.loadGraph(field1);
                    if (result != null) {
                        // clear old graph
                        graph.clearUI();
                        // display graph
                        Model model = graph.getModel();
                        List<String> cellIds = result.getListOfCellIds();
                        List<Location> locations = result.getListOfLocations();
                        List<EdgeWrapper> edgeWrappers = result.getListOfEdges();
                        if (cellIds.size() != 0) {
                            for (int i = 0; i < cellIds.size(); i++) {
                                graph.beginUpdate();
                                model.addCell(cellIds.get(i), TypeOfCell.RECTANGLE, locations.get(i).getX(), locations.get(i).getY());
                                graph.endUpdate();
                            }
                        }

                        if (edgeWrappers.size() != 0) {
                            for (int i = 0; i < edgeWrappers.size(); i++) {
                                EdgeWrapper edgeWrapper = edgeWrappers.get(i);
                                graph.beginUpdate();
                                model.addEdge(edgeWrapper.getSource(), edgeWrapper.getTarget(), edgeWrapper.getText());
                                graph.endUpdate();
                            }
                        }
                    }
                } else {
                    AlertBox.display("Error", "Provide the name for the graph max 3 letters");
                }
            }
        });
    }


    private void createRandomPrompts() {
        randomPromptsHbox = new HBox();
        randomPromptsHbox.setPadding(PADDING_INSETS);
        randomPromptsHbox.setSpacing(SPACING_SETTING);

        textField1RandomPrompts = new TextField();
        textField1RandomPrompts.setMinWidth(MAX_WIDTH_TEXT_FIELD + 30);
        textField1RandomPrompts.setPromptText("Number of cells and arrows max 40 min 2");

        randomButton = new Button();
        randomButton.setMaxWidth(MAX_WIDTH_BUTTON);
        randomButton.setMaxHeight(MAX_HEIGHT_BUTTON);
        randomButton.setText("Randomize");

        randomPromptsHbox.getChildren().addAll(textField1RandomPrompts, randomButton);
        root.getChildren().addAll(randomPromptsHbox);
    }

    private void setRandomPromptsListeners() {
        randomButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                Model model = graph.getModel();
                int cells = 5;
                try {
                    String one = textField1RandomPrompts.getText().trim();
                    if (!one.equals("")) {
                        cells = Integer.parseInt(textField1RandomPrompts.getText().trim());
                    }
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                    AlertBox.display("Error", "Please use whole numbers. Doing default randomizing");
                }

                if (cells < 41 && cells > 1) {
                    Random rnd = new Random();
                    graph.clearUI();
                    List<RectangleCell> listOfCells = new ArrayList<>();
                    for (int i = 0; i < cells; i++) {
                        // add cells and arrows
                        graph.beginUpdate();
                        double x = rnd.nextDouble() * 500;
                        double y = rnd.nextDouble() * 300;
                        RectangleCell cell = model.addCell(String.valueOf(i), TypeOfCell.RECTANGLE, x, y);
                        listOfCells.add(cell);
                        graph.endUpdate();
                    }

                    for (int i = 0; i < listOfCells.size(); i=i+2) {
                        if (i+2 <= listOfCells.size()) {
                            graph.beginUpdate();
                            model.addEdge(listOfCells.get(i).getCellId(), listOfCells.get(i+1).getCellId(), "");
                            graph.endUpdate();
                        }

                    }


                } else {
                    AlertBox.display("Error", "Max number of cells is 5. Number of arrows should not exceed number of cells");
                }

            }
        });

    }

    private void addGraphComponents() {

        Model model = graph.getModel();

        graph.beginUpdate();

        model.addCell("A", TypeOfCell.RECTANGLE, null, null);
        model.addCell("B", TypeOfCell.RECTANGLE, null, null);
        model.addCell("C", TypeOfCell.RECTANGLE, null, null);
        model.addCell("D", TypeOfCell.RECTANGLE, null, null);
        model.addCell("E", TypeOfCell.RECTANGLE, null, null);
        model.addCell("F", TypeOfCell.RECTANGLE, null, null);
        model.addCell("G", TypeOfCell.RECTANGLE, null, null);

        graph.endUpdate();

        graph.getScrollPane().setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (mouseEvent.getButton().equals(MouseButton.PRIMARY)) {
                    if (mouseEvent.getClickCount() == 2) {
                        System.out.println("Double clicked");
                        // get cell's name
                        AlertBox alertBox = new AlertBox();
                        alertBox.displayCellNameDialog();
                        String cellName = alertBox.getCellName();
                        // prevent duplicated cells' names
                        int counter = 0;
                        if (cellName != null) {
                            for (Cell cell : model.getAllCells()) {
                                if (cellName.equals(cell.getCellId())) {
                                    counter++;
                                }
                            }
                            if (cellName != null && counter == 0) {
                                graph.beginUpdate();
                                model.addCell(cellName, TypeOfCell.RECTANGLE, mouseEvent.getX() - 15, mouseEvent.getY() - 15);
                                graph.endUpdate();
                            } else {
                                AlertBox.display("Error", "Cell already exists");
                            }
                        }
                    }
                }
            }
        });
    }

    public static void main(String[] args) {
        launch(args);
    }

}



