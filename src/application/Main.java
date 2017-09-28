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
import utils.Dijkstra;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

/*
Created in IntelliJ IDEA 2017.1.4
so it may not run in other IDEs
 */

/**
 * Main class with {@link Application} for JavaFX
 * @autor Konstantin K. 559121
 * @autor Andrej L. 557966
 */
public class Main extends Application {

    private final Insets PADDING_INSETS = new Insets(15, 0, 0, 0);
    private final double SPACING_SETTING = 20D;
    private final double MAX_WIDTH_TEXT_FIELD = 250D;
    private final double MAX_WIDTH_BUTTON = 100D;
    private final double MAX_HEIGHT_BUTTON = 40D;

    // main ui elements
    VBox root;
    public Graph graph;

    //weightMenu
    HBox wMenu;
    TextField wInitial, wDistant;
    Button wButton;
    Label wLabel;

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
    private TextField textField1RandomPrompts, textField2RandomPrompts;
    private Button randomButton;

    /**
     * Method that gets resources and launch the main scene
     * @param primaryStage to display a scene
     */
    @Override
    public void start(Stage primaryStage) {
        graph = new Graph();

        // add Prompts
        addPrompts();

        // add canvas
        Scene scene = new Scene(root, 1024, 768);
        scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());

        primaryStage.setTitle("GraphProg");
        primaryStage.setScene(scene);
        primaryStage.show();

        addGraphComponents();

        //set random coordinates for default nodes
        Layout layout = new RegularLayout(graph);
        layout.execute();
    }

    /**
     * place control elements
     */
    private void addPrompts() {

        // set root
        root = new VBox();
        root.setPadding(new Insets(15, 15, 15, 15));
        root.setSpacing(SPACING_SETTING);
        root.getChildren().add(graph.getScrollPane());

        // create arrow prompts
        createArrowPrompts();
        setArrowPromptsListeners();

        // delete arrow prompts
        createDeleteArrowPrompts();
        setDeleteArrowPromptsListeners();

        // save prompts
        createSavePrompts();
        setSavePromptsListeners();

        // read prompts
        createReadPrompts();
        setReadPromptsListeners();

        // randomPrompts
        createRandomPrompts();
        setRandomPromptsListeners();

        //shortest path
        createWeightPrompts();
        weightListener();
    }

    /**
     * calculate shortest path
     */
    private void weightListener(){
        Model model = graph.getModel();
        wButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                String start = wInitial.getText().trim();
                String end = wDistant.getText().trim();

                if(cellCheck(model, start) && cellCheck(model, end)) {
                    List<Connection> c = model.getAllConnections();

                    //compose an array & copy all connections
                    Dijkstra.Edge[] tree = new Dijkstra.Edge[c.size()];
                    for (int i = 0; i < c.size(); i++)
                        tree[i] = new Dijkstra.Edge(c.get(i).getSource().getCellId(), c.get(i).getTarget().getCellId(), Integer.parseInt(c.get(i).getText()));

                    Dijkstra path = new Dijkstra(tree);
                    path.tree(start);
                    ByteArrayOutputStream output = new ByteArrayOutputStream();
                    System.setOut(new PrintStream(output));
                    path.printPath(end);

                    //update gui
                    if(path.ok) {
                        graph.beginUpdate();
                        wLabel.setText("Shortest path: " + output.toString());
                        //System.out.println("ByteStream: " + output.toString());
                        graph.endUpdate();
                    }
                }
                else {
                    wLabel.setVisible(false);
                    AlertBox.display("Input error", "Please check start and end nodes!");
                }
            }
        });
    }

    private void createWeightPrompts() {
        wMenu = new HBox();
        wMenu.setPadding(PADDING_INSETS);
        wMenu.setSpacing(SPACING_SETTING);

        wInitial = new TextField();
        wInitial.setMaxWidth(MAX_WIDTH_TEXT_FIELD);
        wInitial.setPromptText("Start node");

        wButton = new Button();
        wButton.setMaxWidth(MAX_WIDTH_BUTTON);
        wButton.setMaxHeight(MAX_HEIGHT_BUTTON);
        wButton.setText("Show path");

        wDistant = new TextField();
        wDistant.setMaxWidth(MAX_WIDTH_TEXT_FIELD);
        wDistant.setPromptText("Destination node");

        wLabel = new Label();
        wLabel.setMaxWidth(MAX_WIDTH_TEXT_FIELD);
        //wLabel.setText("Shortest path: ");

        wMenu.getChildren().addAll(wInitial, wButton, wDistant, wLabel);
        root.getChildren().addAll(wMenu);
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
        connectButton.setOnAction( e -> intCheck(textField3ArrowPrompts) );

        textField2ArrowPrompts = new TextField();
        textField2ArrowPrompts.setMaxWidth(MAX_WIDTH_TEXT_FIELD);
        textField2ArrowPrompts.setPromptText("Choose target cell");

        textField3ArrowPrompts = new TextField();
        textField3ArrowPrompts.setMaxWidth(MAX_WIDTH_TEXT_FIELD + 200);
        textField3ArrowPrompts.setPromptText("Path weight");

        arrowPromptsHbox.getChildren().addAll(textField1ArrowPrompts, connectButton, textField2ArrowPrompts, textField3ArrowPrompts);
        root.getChildren().addAll(arrowPromptsHbox);
    }

    private boolean intCheck(TextField input){
        try {
            int age = Integer.parseInt(input.getText());
            return true;
        }
        catch(NumberFormatException e){
            AlertBox.display("Input error", "Please enter a positive weight-integer!");
            return false;
        }
    }

    private void setArrowPromptsListeners() {
        connectButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {

                // connection dialog
                String field1 = textField1ArrowPrompts.getText().trim(); //from
                String field2 = textField2ArrowPrompts.getText().trim(); //to
                String field3 = textField3ArrowPrompts.getText().trim(); //weight

                // get cells
                Model model = graph.getModel();
                RectangleCell rect1 = null;
                RectangleCell rect2 = null;

                if (field1.length() > 3 || field2.length() > 3) {
                    AlertBox.display("Input error", "Maximal 3 letters allowed!");
                }
                else {
                    rect1 = (RectangleCell) model.getCell(field1);
                    rect2 = (RectangleCell) model.getCell(field2);
                }

                // input was correct
                if (rect1 != null && rect2 != null) {
                    int stat;

                    graph.beginUpdate();
                    stat = model.addEdgeS(rect1.getCellId(), rect2.getCellId(), field3);
                    if (stat == 1)
                        graph.endUpdate();
                    else
                        AlertBox.display("Trace error", "Cells not found or already connected!");
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
                    AlertBox.display("Input Error", "I don't get it!");
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
        textField1RandomPrompts.setPromptText("Number of cells [5, 10]");

        textField2RandomPrompts = new TextField();
        textField2RandomPrompts.setPromptText("Number of arrows");

        randomButton = new Button();
        randomButton.setMaxWidth(MAX_WIDTH_BUTTON);
        randomButton.setMaxHeight(MAX_HEIGHT_BUTTON);
        randomButton.setText("Randomize");

        randomPromptsHbox.getChildren().addAll(textField1RandomPrompts, textField2RandomPrompts, randomButton);
        root.getChildren().addAll(randomPromptsHbox);
    }

    private void setRandomPromptsListeners() {
        randomButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                Model model = graph.getModel();
                int cells = 4; //number of random cells
                int edges = 1; //& edges by default
                boolean error = false;

                //get user input
                try {
                    String one = textField1RandomPrompts.getText().trim();
                    String two = textField2RandomPrompts.getText().trim();
                    if (!one.equals("") && !two.equals("")) {
                        cells = Integer.parseInt(textField1RandomPrompts.getText().trim());
                        edges = Integer.parseInt(textField2RandomPrompts.getText().trim());
                    }
                }
                catch (NumberFormatException e) {
                    e.printStackTrace();
                    error = true;
                }

                if(cells == 1 || error)
                    AlertBox.display("Input Error", "U probably entered false numbers, so I take defaults!");

                //add & distribute cells
                if (cells < 11 && cells > 4) {
                    double a = root.getWidth() / 4;
                    double b = root.getHeight() / 4;
                    double r = 4 * b / 5;

                    graph.clearUI();
                    List<RectangleCell> listOfCells = new ArrayList<>();
                    for (int i = 0; i < cells; i++) {
                        // add cells
                        graph.beginUpdate();
                        double j = 2 * Math.PI * i / cells;
                        double x = (int) Math.round(a + r * Math.cos(j));
                        double y = (int) Math.round(b + r * Math.sin(j));
                        RectangleCell cell = model.addCell(String.valueOf(i), TypeOfCell.RECTANGLE, x, y);
                        listOfCells.add(cell);
                        graph.endUpdate();
                    }

                    //add connections
                    if (edges < cells) {
                        int counter = 0;
                        while (counter != edges) {
                            graph.beginUpdate();
                            int i = mathRand(0, listOfCells.size() - 1);
                            int j = mathRand(0, 9);
                            counter += model.addEdgeS(listOfCells.get(i).getCellId(), listOfCells.get( j % listOfCells.size() ).getCellId(), Integer.toString(j));
                            graph.endUpdate();
                        }
                    }
                    else
                        AlertBox.display("Input Error", "Number of arrows should be lesser than number of cells!");
                }
                else
                    AlertBox.display("Error", "Only numbers between 5 & 10 are allowed!");
            }
        });
    }

    /**
     * random number generator
     * @param min included
     * @param max included
     * @return number
     */
    static int mathRand(long min, long max) {
        return (int)(min + Math.round( Math.random() * (max - min) ));
    }

    private boolean cellCheck(Model model, String name) {
        for (Cell temp : model.getAllCells()) {
            if (name.equals(temp.getCellId()))
                return true;
        }
        return false;
    }

    private void addGraphComponents() {
        Model model = graph.getModel();
        graph.beginUpdate();

        //default nodes, appear at once
        model.addCell("A", TypeOfCell.RECTANGLE, null, null);
        model.addCell("B", TypeOfCell.RECTANGLE, null, null);

        /*
        model.addCell("C", TypeOfCell.RECTANGLE, null, null);
        model.addCell("D", TypeOfCell.RECTANGLE, null, null);
        model.addCell("E", TypeOfCell.RECTANGLE, null, null);
        model.addCell("F", TypeOfCell.RECTANGLE, null, null);
        model.addCell("G", TypeOfCell.RECTANGLE, null, null);
        model.addCell("H", TypeOfCell.RECTANGLE, null, null);
        model.addCell("I", TypeOfCell.RECTANGLE, null, null);
        */
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

                        // prevent duplicates
                        int counter = 0;
                        if (cellName != null) {

                            if (cellCheck(model, cellName))
                                counter++;

                            if (cellName != null && counter == 0) {
                                graph.beginUpdate();
                                model.addCell(cellName, TypeOfCell.RECTANGLE, mouseEvent.getX() - 15, mouseEvent.getY() - 15);
                                graph.endUpdate();
                            }
                            else {
                                AlertBox.display("Error", "There is already a cell with that name!");
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