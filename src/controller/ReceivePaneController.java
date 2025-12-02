package controller;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ReceivePaneController {

    @FXML
    private AnchorPane anchorPane_main;

    @FXML
    private Button brn_receive;

    @FXML
    private Button btn_cancelReceipt;

    @FXML
    private CheckBox cbox_iAgree;

    @FXML
    private ListView<?> listView_items;

    @FXML
    private TextField textField_identification;

    @FXML
    private TextField textField_income;

    @FXML
    private TextField textField_name;

    @FXML
    private TextField textField_numberPhone;

    @FXML
    void handleGoBackToMainPane(MouseEvent event) {
        AnchorPane pane;
        Scene scene;
        try {
            pane = (AnchorPane)FXMLLoader.load(getClass().getResource("/view/MainPane.fxml"));
            scene = this.anchorPane_main.getScene();
            scene.setRoot(pane);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void handleGoToConfirmationReceiveWindow(MouseEvent event) {
        AnchorPane pane;
        Stage newStage;
        Stage currentStage;
        try {
            currentStage = (Stage)((Node) event.getSource()).getScene().getWindow();
            pane = (AnchorPane)FXMLLoader.load(getClass().getResource("/view/ConfirmationReceiveWindow.fxml"));
            newStage = new Stage();
            newStage.initOwner(currentStage);
            newStage.initModality(Modality.WINDOW_MODAL);
            newStage.setScene(new Scene(pane));
            newStage.setTitle("Confirmação - DoAção");
            newStage.setResizable(false);
            newStage.showAndWait();
            handleGoBackToMainPane(event);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
