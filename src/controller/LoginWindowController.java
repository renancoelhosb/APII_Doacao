package controller;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class LoginWindowController {

    @FXML
    private AnchorPane anchorPane_main;

    @FXML
    private Button btn_cancel;

    @FXML
    private Button btn_login;

    @FXML
    private PasswordField textField_password;

    @FXML
    private TextField textField_user;

    private boolean authenticated;

    public boolean isAuthenticated() {
        return authenticated;
    }

    @FXML
    void handleGoBackToMainPane(MouseEvent event) {
        Stage currentStage = (Stage)((Node) event.getSource()).getScene().getWindow();
        currentStage.close();
    }   

    @FXML
    void handleLogin(MouseEvent event) {
        String username = textField_user.getText();
        String password = textField_password.getText();

        if (username.isEmpty() && password.isEmpty()) {
            authenticated = true;
            handleGoBackToMainPane(event);
        } else {
            authenticated = false;
        }
    }

}
