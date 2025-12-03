package controller;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class NewProductWindowController {

    @FXML
    private Button btn_cancel;

    @FXML
    private Button btn_registerProduct;

    @FXML
    private TextField textField_product_name;

    private boolean itemCreatedSucessfully = false;

    public String getProductName() {
        return textField_product_name.getText();
    }

    public boolean isItemCreatedSucessfully() {
        return itemCreatedSucessfully;
    }

    @FXML
    void handleGoBackToStockPane(MouseEvent event) {
        Stage currentStage = (Stage)((Node) event.getSource()).getScene().getWindow();
        currentStage.close();
    }

    @FXML
    void handleRegisterProduct(MouseEvent event) {
        // Implement product registration logic here

        AnchorPane pane;
        Stage newStage;
        Stage currentStage;
        FXMLLoader loader;
        try {
            currentStage = (Stage)((Node) event.getSource()).getScene().getWindow();
            loader = new FXMLLoader(getClass().getResource("/view/Popup.fxml"));
            pane = (AnchorPane) loader.load();
            newStage = new Stage();
            newStage.initOwner(currentStage);
            newStage.initModality(Modality.WINDOW_MODAL);
            newStage.setScene(new Scene(pane));
            newStage.setTitle("Popup - DoAção");
            newStage.setResizable(false);
            newStage.showAndWait();
            currentStage.close();
            itemCreatedSucessfully = true;

        } catch (IOException e) {
            e.printStackTrace();
        }
        
    }

}
