package controller;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

import javafx.application.Platform;
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

    public void initialize() {
        try {
            FileInputStream users = new FileInputStream("users.ser");
            ObjectInputStream ois = new ObjectInputStream(users);
            ControllerUsers controllerUsers = (ControllerUsers) ois.readObject();
            users.close();
            ois.close();

        } catch (IOException | ClassNotFoundException e) {
            openLoginForMasterUserSetup();
        }
    }

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
                FXMLLoader userPaneLoader = new FXMLLoader(getClass().getResource("/view/UserPane.fxml"));
                AnchorPane userPane = (AnchorPane) userPaneLoader.load();
                UserPaneController userController = userPaneLoader.getController();
                
                userController.setLoggedInUser(loginController.getAuthenticatedUser());
                
                Scene scene = this.anchorPane_main.getScene();
                scene.setRoot(userPane);
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

    private void openLoginForMasterUserSetup() {
        if (this.anchorPane_main.getScene() == null) {
            Platform.runLater(this::openLoginForMasterUserSetup);
            return;
        }

        AnchorPane pane;
        Stage newStage;
        Stage currentStage;
        CreateLoginMasterWindowController loginMasterController;
        FXMLLoader loader;
        try {
            currentStage = (Stage) this.anchorPane_main.getScene().getWindow();
            loader = new FXMLLoader(getClass().getResource("/view/CreateLoginMasterWindow.fxml"));
            pane = (AnchorPane) loader.load();
            loginMasterController = loader.getController();
            newStage = new Stage();
            newStage.initOwner(currentStage);
            newStage.initModality(javafx.stage.Modality.WINDOW_MODAL);
            newStage.setScene(new javafx.scene.Scene(pane));
            newStage.setTitle("Configuração inicial - DoAção");
            newStage.setResizable(false);
            newStage.showAndWait();
            if (loginMasterController.isCreatedMasterUser()) {
                ControllerUsers controllerUsers = new ControllerUsers();
                controllerUsers.createUsuario(loginMasterController.getUsername(), loginMasterController.getPassword(), true);
                try {
                    FileOutputStream flow = new FileOutputStream("users.ser");
                    ObjectOutputStream writeFile = new ObjectOutputStream(flow);
                    writeFile.writeObject(controllerUsers);
                    writeFile.close();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
