package controller;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class RequestsPaneController {

    @FXML
    private AnchorPane anchorPane_main;

    @FXML
    private Button btn_cancel_request;

    @FXML
    private Button btn_conclude;

    @FXML
    private TableColumn<?, ?> col_id;

    @FXML
    private TableColumn<?, ?> col_income;

    @FXML
    private TableColumn<?, ?> col_item;

    @FXML
    private TableColumn<?, ?> col_name;

    @FXML
    private TableColumn<?, ?> col_phone;

    @FXML
    private TableColumn<?, ?> col_qtd;

    @FXML
    void handleCancelRequest(MouseEvent event) {
        // Implement cancel request logic here

        AnchorPane pane;
        Stage newStage;
        Stage currentStage;
        LoginWindowController controller;   //// Not used but kept for consistency
        FXMLLoader loader;
        try {
            currentStage = (Stage)((Node) event.getSource()).getScene().getWindow();
            loader = new FXMLLoader(getClass().getResource("/view/Popup.fxml"));
            pane = (AnchorPane) loader.load();
            controller = loader.getController();
            newStage = new Stage();
            newStage.initOwner(currentStage);
            newStage.initModality(Modality.WINDOW_MODAL);
            newStage.setScene(new Scene(pane));
            newStage.setTitle("Popup - DoAção");
            newStage.setResizable(false);
            newStage.showAndWait();
            currentStage.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @FXML
    void handleConcludeRequest(MouseEvent event) {
        // Implement conclude request logic here

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

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
