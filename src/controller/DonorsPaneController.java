package controller;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javafx.beans.property.SimpleIntegerProperty;
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

    @FXML
    private AnchorPane anchorPane_main;

    @FXML
    private Button btn_delete_donor;

    @FXML
    private TableView<Doador> tableView;

    @FXML
    private TableColumn<Doador, String> col_name;

    @FXML
    private TableColumn<Doador, Integer> col_phone;

    @FXML
    private TableColumn<Doador, Integer> col_id;

    private ControllerDoadores controllerDoadores;

    @FXML
    public void initialize() {
        try {
            controllerDoadores = rescueDonorsController();

            // popula tabela se houver itens
            if (controllerDoadores != null && controllerDoadores.getDoadores() != null) {
                ObservableList<Doador> data = FXCollections.observableArrayList(controllerDoadores.getDoadores());

                // usando lambdas para garantir compatibilidade com nomes dos getters
                col_name.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getNome()));
                col_phone.setCellValueFactory(cell -> new SimpleIntegerProperty(cell.getValue().getTelefone()).asObject());
                col_id.setCellValueFactory(cell -> new SimpleIntegerProperty(cell.getValue().getId()).asObject());

                tableView.setItems(data);
            }

        } catch (IOException | ClassNotFoundException e) {
            controllerDoadores = new ControllerDoadores();
        }
    }

    @FXML
    void handleDeleteDonor(MouseEvent event) {
        Doador doadorSelecionado = tableView.getSelectionModel().getSelectedItem();
        
        if (doadorSelecionado == null) {
            Alert alert = new Alert(AlertType.WARNING);
            alert.setTitle("Nenhuma seleção");
            alert.setHeaderText("Nenhum doador selecionado");
            alert.setContentText("Por favor, selecione um doador na tabela.");
            alert.showAndWait();
            return;
        }

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
            newStage.setTitle("Confirmação - DoAção");
            newStage.setResizable(false);
            newStage.showAndWait();
            
            this.controllerDoadores.removeDoador(doadorSelecionado);
            saveDonorsController(this.controllerDoadores);
            initialize();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private ControllerDoadores rescueDonorsController() throws IOException, ClassNotFoundException {
        ControllerDoadores controllerUsers;
        FileInputStream flow = new FileInputStream("doadores.ser");
        ObjectInputStream readControllerDoadores = new ObjectInputStream(flow);
        controllerUsers = (ControllerDoadores)readControllerDoadores.readObject();
        flow.close();
        readControllerDoadores.close();
        return controllerUsers;
    }

    private void saveDonorsController(ControllerDoadores controllerDoadores) throws IOException {
        FileOutputStream fos = new FileOutputStream("doadores.ser");
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(controllerDoadores);
        oos.close();
        fos.close();
    }
}
