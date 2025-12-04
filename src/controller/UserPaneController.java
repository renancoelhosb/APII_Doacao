package controller;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

public class UserPaneController {

    @FXML
    private AnchorPane anchorPane_expose;

    @FXML
    private AnchorPane anchorPane_main;

    @FXML
    private Button btn_donors;

    @FXML
    private Button btn_logout;

    @FXML
    private Button btn_receivers;

    @FXML
    private Button btn_requests;

    @FXML
    private Button btn_stock;

    @FXML
    private Button btn_users;

    @FXML
    private Label label_id;

    public void setLoggedInUser(String username) {
        label_id.setText(username);
    }

    @FXML
    void inicialize() {
        
        label_id.setText("Usuário: ");

    }

    @FXML
    void handleDonors(MouseEvent event) {
        AnchorPane a;
        try {
            a = (AnchorPane)FXMLLoader.load(getClass().getResource("/view/DonorsPane.fxml"));
            this.anchorPane_expose.getChildren().setAll(a);
        } catch (IOException ex) {
            System.out.println("Erro ao carregar FXML");
        }
    }

    @FXML
    void handleLogout(MouseEvent event) {
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
    void handleReceivers(MouseEvent event) {
        AnchorPane a;
        try {
            a = (AnchorPane)FXMLLoader.load(getClass().getResource("/view/ReceiversPane.fxml"));
            this.anchorPane_expose.getChildren().setAll(a);
        } catch (IOException ex) {
            ex.printStackTrace();
            System.out.println("Erro ao carregar FXML");
        }
    }

    @FXML
    void handleRequests(MouseEvent event) {
        AnchorPane a;
        try {
            a = (AnchorPane)FXMLLoader.load(getClass().getResource("/view/RequestsPane.fxml"));
            this.anchorPane_expose.getChildren().setAll(a);
        } catch (IOException ex) {
            ex.printStackTrace();
            System.out.println("Erro ao carregar FXML");
        }
    }

    @FXML
    void handleStock(MouseEvent event) {
        AnchorPane a;
        try {
            a = (AnchorPane)FXMLLoader.load(getClass().getResource("/view/StockPane.fxml"));
            this.anchorPane_expose.getChildren().setAll(a);
        } catch (IOException ex) {
            System.out.println("Erro ao carregar FXML");
        }
    }

    @FXML
    void handleUsers(MouseEvent event) {
        AnchorPane a;
        try {
            a = (AnchorPane)FXMLLoader.load(getClass().getResource("/view/UsersPane.fxml"));
            this.anchorPane_expose.getChildren().setAll(a);
        } catch (IOException ex) {
            System.out.println("Erro ao carregar FXML");
        }
    }

}
