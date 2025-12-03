package controller;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Optional;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextInputDialog;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import model.Usuario;

public class UsersPaneController {

    @FXML private AnchorPane anchorPane_main;
    @FXML private Button btn_change_password;
    @FXML private Button btn_create;
    @FXML private Button btn_delete;
    @FXML private ListView<String> listview_users;

    private ControllerUsers controllerUsers;

    @FXML
    public void initialize() {
        loadUsers();
    }

    private void loadUsers() {
        try {
            FileInputStream fis = new FileInputStream("users.ser");
            ObjectInputStream ois = new ObjectInputStream(fis);
            controllerUsers = (ControllerUsers) ois.readObject();
            fis.close();
            ois.close();
        } catch (Exception e) {
            controllerUsers = new ControllerUsers();
        }
        updateListView();
    }

    private void updateListView() {
        if (controllerUsers.getUsuarios() != null) {
            ArrayList<String> userNames = new ArrayList<>();
            for (Usuario u : controllerUsers.getUsuarios()) {
                userNames.add(u.getUsuario() + (u.isMaster() ? " (ADMIN)" : ""));
            }
            ObservableList<String> data = FXCollections.observableArrayList(userNames);
            listview_users.setItems(data);
        }
    }

    @FXML
    void handleCreateUser(MouseEvent event) {
        TextInputDialog dialogUser = new TextInputDialog();
        dialogUser.setContentText("Nome de usuário:");
        Optional<String> resultUser = dialogUser.showAndWait();

        if (resultUser.isPresent() && !resultUser.get().trim().isEmpty()) {
            TextInputDialog dialogPass = new TextInputDialog();
            dialogPass.setContentText("Senha:");
            Optional<String> resultPass = dialogPass.showAndWait();

            if (resultPass.isPresent()) {
                boolean sucesso = controllerUsers.createUsuario(resultUser.get(), resultPass.get(), false);
                if (sucesso) {
                    saveAndRefresh("Usuário criado!");
                } else {
                    showAlert("Erro", "Usuário já existe.");
                }
            }
        }
    }

    @FXML
    void handleDeleteUser(MouseEvent event) {
        String selected = listview_users.getSelectionModel().getSelectedItem();
        if (selected == null) return;

        String username = selected.replace(" (ADMIN)", "");
        Usuario u = controllerUsers.getUsuario(username);
        if (u != null && u.isMaster()) {
            showAlert("Erro", "Não pode apagar Master.");
            return;
        }

        if (controllerUsers.removeUsuario(username)) {
            saveAndRefresh("Usuário removido.");
        }
    }

    @FXML
    void handleChangePassword(MouseEvent event) {
        String selected = listview_users.getSelectionModel().getSelectedItem();
        if (selected == null) return;
        String username = selected.replace(" (ADMIN)", "");
        
        TextInputDialog dialog = new TextInputDialog();
        dialog.setContentText("Nova senha:");
        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()) {
            Usuario u = controllerUsers.getUsuario(username);
            if (u != null) {
                u.setSenha(result.get());
                saveAndRefresh("Senha alterada.");
            }
        }
    }

    private void saveAndRefresh(String msg) {
        try {
            FileOutputStream fos = new FileOutputStream("users.ser");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(controllerUsers);
            oos.close();
            fos.close();
            updateListView();
            showAlert("Sucesso", msg);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setContentText(content);
        alert.showAndWait();
    }
}