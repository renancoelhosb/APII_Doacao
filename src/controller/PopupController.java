package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class PopupController {

    @FXML
    private Label label_text;

    public void setText(String text) {
        if (label_text != null) {
            label_text.setText(text);
        }
    }
}