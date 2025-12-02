package controller;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;

public class MainWindowController {

    @FXML
    private AnchorPane anchorPane_main;

    public void initialize() {
        AnchorPane pane;
        try {
            pane = (AnchorPane)FXMLLoader.load(getClass().getResource("/view/MainPane.fxml"));
            this.anchorPane_main.getChildren().setAll(pane);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
