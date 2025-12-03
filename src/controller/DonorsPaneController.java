package controller;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.Doador;

public class DonorsPaneController {

    @FXML private AnchorPane anchorPane_main;
    @FXML private Button btn_delete_donor;
    @FXML private TableView<Doador> tableView;
    @FXML private TableColumn<Doador, String> col_name;
    @FXML private TableColumn<Doador, Long> col_phone;
    @FXML private TableColumn<Doador, Long> col_id; 

    private ControllerDoadores controllerDoadores;

    @FXML
    public void initialize() {
        try {
            FileInputStream fis = new FileInputStream("doadores.ser");
            ObjectInputStream ois = new ObjectInputStream(fis);
            controllerDoadores = (ControllerDoadores) ois.readObject();
            fis.close();
            ois.close();
        } catch (Exception e) {
            controllerDoadores = new ControllerDoadores();
        }

        if (controllerDoadores.getDoadores() != null) {
            ObservableList<Doador> data = FXCollections.observableArrayList(controllerDoadores.getDoadores());
            col_name.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getNome()));
            col_phone.setCellValueFactory(cell -> new SimpleLongProperty(cell.getValue().getTelefone()).asObject());

            col_id.setCellValueFactory(cell -> new SimpleLongProperty(cell.getValue().getId()).asObject());
            tableView.setItems(data);
        }
    }

    @FXML
    void handleDeleteDonor(MouseEvent event) {
        Doador doadorSelecionado = tableView.getSelectionModel().getSelectedItem();
        
        if (doadorSelecionado == null) {
            Alert alert = new Alert(AlertType.WARNING);
            alert.setContentText("Selecione um doador.");
            alert.showAndWait();
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Popup.fxml"));
            AnchorPane pane = (AnchorPane) loader.load();
            
            PopupController popup = loader.getController();
            popup.setText("Doador removido!");

            Stage newStage = new Stage();
            newStage.initOwner(((Node) event.getSource()).getScene().getWindow());
            newStage.initModality(Modality.WINDOW_MODAL);
            newStage.setScene(new Scene(pane));
            newStage.setResizable(false);
            newStage.showAndWait();
            
            controllerDoadores.removeDoador(doadorSelecionado);
            
            FileOutputStream fos = new FileOutputStream("doadores.ser");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(controllerDoadores);
            oos.close();
            fos.close();
            
            initialize();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}