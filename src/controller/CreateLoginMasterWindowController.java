package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import javax.swing.JOptionPane;

public class CreateLoginMasterWindowController {

    @FXML
    private AnchorPane anchorPane_main;

    @FXML
    private Button btn_create_master_user;

    @FXML
    private PasswordField textField_password;

    @FXML
    private TextField textField_user;

    private boolean createdMasterUser = false;

    public String getUsername() {
        return textField_user.getText();
    }

    public String getPassword() {
        return textField_password.getText();
    }

    @FXML
    void handleCreateMasterUser(MouseEvent event) {

        if (getUsername().isEmpty() || getPassword().isEmpty()) {
            JOptionPane.showMessageDialog(
            null,
            "Preencha os campos",
            "Erro",
            JOptionPane.ERROR_MESSAGE
            );
        } else {
    
            JOptionPane.showMessageDialog(
            null,
            "Usuário mestre criado com sucesso!",
            "Sucesso",
            JOptionPane.INFORMATION_MESSAGE
            );
            
            this.createdMasterUser = true;


            Stage stage = (Stage) anchorPane_main.getScene().getWindow();
            stage.close();
        }
    }

    public boolean isCreatedMasterUser() {
        return createdMasterUser;
    }

}