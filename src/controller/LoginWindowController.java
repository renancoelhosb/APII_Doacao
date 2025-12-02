package controller;

import java.io.FileInputStream;
import java.io.ObjectInputStream;

import javax.swing.JOptionPane;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import model.Usuario;

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

    private String authenticatedUser;

    public boolean isAuthenticated() {
        return authenticated;
    }

    public String getAuthenticatedUser() {
        return authenticatedUser;
    }

    private ControllerUsers rescueUserController() {
        try {
            ControllerUsers controllerUsers;
            FileInputStream flow = new FileInputStream("users.ser");
            ObjectInputStream readControllerUsers = new ObjectInputStream(flow);
            controllerUsers = (ControllerUsers)readControllerUsers.readObject();
            flow.close();
            readControllerUsers.close();
            return controllerUsers;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
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
        ControllerUsers controllerUsers = rescueUserController();
        Usuario user = controllerUsers.getUsuario(username);

        if (user != null && password.equals(user.getSenha())) {
            authenticated = true;
            authenticatedUser = username;
            handleGoBackToMainPane(event);
        } else {
            JOptionPane.showMessageDialog(
            null,
            user.getSenha(),
            "Erro",
            JOptionPane.ERROR_MESSAGE
            );
            authenticated = false;
        }
    }

}
