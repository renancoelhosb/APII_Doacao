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
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.Receptor;

public class ReceiversPaneController {

    @FXML private AnchorPane anchorPane_main;
    @FXML private Button btn_deleteReceiver;
    @FXML private TableView<Receptor> tableView;
    @FXML private TableColumn<Receptor, String> col_name;
    @FXML private TableColumn<Receptor, Integer> col_phone;
    @FXML private TableColumn<Receptor, Long> col_id;
    @FXML private TableColumn<Receptor, Double> col_income;

    private ControllerReceivers controllerReceivers;

    @FXML
    public void initialize() {
        try {
            FileInputStream fis = new FileInputStream("receivers.ser");
            ObjectInputStream ois = new ObjectInputStream(fis);
            controllerReceivers = (ControllerReceivers) ois.readObject();
            fis.close();
            ois.close();
        } catch (Exception e) {
            controllerReceivers = new ControllerReceivers();
        }

        if (controllerReceivers.getReceivers() != null) {
            ObservableList<Receptor> data = FXCollections.observableArrayList(controllerReceivers.getReceivers());
            col_income.setCellValueFactory(new PropertyValueFactory<>("renda"));
            col_name.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getNome()));
            col_phone.setCellValueFactory(cell -> new SimpleIntegerProperty(cell.getValue().getTelefone()).asObject());

            col_id.setCellValueFactory(cell -> new SimpleLongProperty(cell.getValue().getId()).asObject());
            tableView.setItems(data);
        }
    }

    @FXML
    void handleDeleteReceiver(MouseEvent event) {
        Receptor receptorSelecionado = tableView.getSelectionModel().getSelectedItem();
        
        if (receptorSelecionado == null) {
            Alert alert = new Alert(AlertType.WARNING);
            alert.setContentText("Selecione um receptor.");
            alert.showAndWait();
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Popup.fxml"));
            AnchorPane pane = (AnchorPane) loader.load();
            
            PopupController popup = loader.getController();
            popup.setText("Receptor removido!");
            
            Stage newStage = new Stage();
            newStage.initOwner(((Node) event.getSource()).getScene().getWindow());
            newStage.initModality(Modality.WINDOW_MODAL);
            newStage.setScene(new Scene(pane));
            newStage.setResizable(false);
            newStage.showAndWait();
            
            controllerReceivers.removeReceptor(receptorSelecionado);
            
            FileOutputStream fos = new FileOutputStream("receivers.ser");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(controllerReceivers);
            oos.close();
            fos.close();
            
            initialize();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}