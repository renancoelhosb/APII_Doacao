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

    @FXML private Button btn_cancel;
    @FXML private Button btn_registerProduct;
    @FXML private TextField textField_product_name;

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
        try {
            Stage currentStage = (Stage)((Node) event.getSource()).getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Popup.fxml"));
            AnchorPane pane = (AnchorPane) loader.load();
            
            PopupController popup = loader.getController();
            popup.setText("Produto cadastrado!");

            Stage newStage = new Stage();
            newStage.initOwner(currentStage);
            newStage.initModality(Modality.WINDOW_MODAL);
            newStage.setScene(new Scene(pane));
            newStage.setTitle("Popup - DoAção");
            newStage.setResizable(false);
            newStage.showAndWait();
            
            itemCreatedSucessfully = true;
            currentStage.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}