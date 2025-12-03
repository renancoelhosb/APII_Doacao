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
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.Receptor;

public class ReceiversPaneController {

    @FXML
    private AnchorPane anchorPane_main;

    @FXML
    private Button btn_deleteReceiver;

    @FXML
    private TableView<Receptor> tableView;

    @FXML
    private TableColumn<Receptor, String> col_name;

    @FXML
    private TableColumn<Receptor, Integer> col_phone;

    @FXML
    private TableColumn<Receptor, Integer> col_id;

    @FXML
    private TableColumn<Receptor, Double> col_income;

    private ControllerReceivers controllerReceivers;

    @FXML
    public void initialize() {
        try {
            controllerReceivers = rescueReceiversController();

            // popula tabela se houver itens
            if (controllerReceivers != null && controllerReceivers.getReceivers() != null) {
                ObservableList<Receptor> data = FXCollections.observableArrayList(controllerReceivers.getReceivers());

                col_income.setCellValueFactory(new PropertyValueFactory<>("renda"));
                // usando lambdas para garantir compatibilidade com nomes dos getters
                col_name.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getNome()));
                col_phone.setCellValueFactory(cell -> new SimpleIntegerProperty(cell.getValue().getTelefone()).asObject());
                col_id.setCellValueFactory(cell -> new SimpleIntegerProperty(cell.getValue().getId()).asObject());
                

                tableView.setItems(data);
            }

        } catch (IOException | ClassNotFoundException e) {
            controllerReceivers = new ControllerReceivers();
        }
    }

    @FXML
    void handleDeleteReceiver(MouseEvent event) {
        Receptor receptorSelecionado = tableView.getSelectionModel().getSelectedItem();
        
        if (receptorSelecionado == null) {
            Alert alert = new Alert(AlertType.WARNING);
            alert.setTitle("Nenhuma seleção");
            alert.setHeaderText("Nenhum receptor selecionado");
            alert.setContentText("Por favor, selecione um receptor na tabela.");
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
            
            this.controllerReceivers.removeReceptor(receptorSelecionado);
            saveReceiversController(this.controllerReceivers);
            initialize();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private ControllerReceivers rescueReceiversController() throws IOException, ClassNotFoundException {
        ControllerReceivers controllerUsers;
        FileInputStream flow = new FileInputStream("receivers.ser");
        ObjectInputStream readControllerReceivers = new ObjectInputStream(flow);
        controllerUsers = (ControllerReceivers)readControllerReceivers.readObject();
        flow.close();
        readControllerReceivers.close();
        return controllerUsers;
    }

    private void saveReceiversController(ControllerReceivers controllerReceivers) throws IOException {
        FileOutputStream fos = new FileOutputStream("receivers.ser");
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(controllerReceivers);
        oos.close();
        fos.close();
    }
}