package utils;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class AlertBox {

    private static String cellName;

    public AlertBox() {
    }

    public static void display(String title, String message) {
        Stage window = new Stage();

        //Block events to other windows
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle(title);
        window.setMinWidth(250);

        Label label = new Label();
        label.setText(message);
        Button closeButton = new Button("Close");
        closeButton.setOnAction(e -> window.close());

        VBox layout = new VBox(10);
        layout.getChildren().addAll(label, closeButton);
        layout.setAlignment(Pos.CENTER);

        //Display window and wait for it to be closed before returning
        Scene scene = new Scene(layout);
        window.setScene(scene);
        window.showAndWait();
    }

    public void displayCellNameDialog() {
        Stage window = new Stage();

        //Block events to other windows
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("New Title");
        window.setMinWidth(350);
        window.setMinHeight(100);

        TextField textField = new TextField();
        String promptText = "MAX 3 LETTERS. Choose the name of the cell";
        textField.setText(promptText);
        Button closeButton = new Button("Create");
        closeButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String textFieldText = textField.getText().trim();
                if(!textFieldText.toLowerCase().equals(promptText.toLowerCase().trim()) && textFieldText.toLowerCase().length() < 4 && !textFieldText.equals("")) {
                    cellName = textFieldText;
                    window.close();
                } else {
                    cellName = null;
                }

            }
        });

        VBox layout = new VBox(10);
        layout.getChildren().addAll(textField, closeButton);
        layout.setAlignment(Pos.CENTER);

        //Display window and wait for it to be closed before returning
        Scene scene = new Scene(layout);
        window.setScene(scene);
        window.showAndWait();
    }

    public String getCellName() {
        return cellName;
    }
}