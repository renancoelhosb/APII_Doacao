package controller;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.Node;

public class MainPaneController {

    @FXML
    private AnchorPane anchorPane_main;

    @FXML
    private Button btn_donate;

    @FXML
    private ImageView btn_user;

    @FXML
    private Button btn_receive;

    @FXML
    void handleMoveToDonatePane(MouseEvent event) {
        AnchorPane pane;
        Scene scene;
        try {
            pane = (AnchorPane)FXMLLoader.load(getClass().getResource("/view/DonatePane.fxml"));
            scene = this.anchorPane_main.getScene();
            scene.setRoot(pane);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void handleMoveToUserPane(MouseEvent event) {
        AnchorPane pane;
        Stage newStage;
        Stage currentStage;
        LoginWindowController loginController;
        FXMLLoader loader;
        try {
            currentStage = (Stage)((Node) event.getSource()).getScene().getWindow();
            loader = new FXMLLoader(getClass().getResource("/view/LoginWindow.fxml"));
            pane = (AnchorPane) loader.load();
            loginController = loader.getController();
            newStage = new Stage();
            newStage.initOwner(currentStage);
            newStage.initModality(Modality.WINDOW_MODAL);
            newStage.setScene(new Scene(pane));
            newStage.setTitle("Login - DoAção");
            newStage.setResizable(false);
            newStage.showAndWait();

            if (loginController.isAuthenticated()) {
                pane = (AnchorPane)FXMLLoader.load(getClass().getResource("/view/UserPane.fxml"));
                Scene scene = this.anchorPane_main.getScene();
                scene.setRoot(pane);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void handleMoveToReceivePane(MouseEvent event) {
        AnchorPane pane;
        Scene scene;
        try {
            pane = (AnchorPane)FXMLLoader.load(getClass().getResource("/view/ReceivePane.fxml"));
            scene = this.anchorPane_main.getScene();
            scene.setRoot(pane);
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
