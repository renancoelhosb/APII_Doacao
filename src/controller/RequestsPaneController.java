package controller;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.SimpleDoubleProperty;
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
import model.Doacao;
import model.Doador;
import model.Item;
import model.Receptor;

public class RequestsPaneController {

    @FXML
    private AnchorPane anchorPane_main;

    @FXML
    private Button btn_cancel_request;

    @FXML
    private Button btn_conclude;

    @FXML
    private TableColumn<Receptor, Integer> col_id;

    @FXML
    private TableColumn<Receptor, Double> col_income;

    @FXML
    private TableColumn<Receptor, String> col_name;

    @FXML
    private TableColumn<Receptor, Integer> col_phone;

    @FXML
    private TableView<Item> tableView_pedido;

    @FXML
    private TableView<Receptor> tableView_receptores;

    ControllerDoacoes controllerDoacoes;

    void initialize() {
        try {
            this.controllerDoacoes = rescueDoacoesController();
            ArrayList<Receptor> lista = this.controllerDoacoes.getReceptores();
            ObservableList<Receptor> data = FXCollections.observableArrayList(lista);
            col_name.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getNome()));
            col_income.setCellValueFactory(cell -> new SimpleDoubleProperty(cell.getValue().getRenda()).asObject());
            col_phone.setCellValueFactory(cell -> new SimpleIntegerProperty(cell.getValue().getTelefone()).asObject());
            col_id.setCellValueFactory(cell -> new SimpleIntegerProperty(cell.getValue().getId()).asObject());

            tableView_receptores.setItems(data);

        } catch (IOException | ClassNotFoundException e) {
            this.controllerDoacoes = new ControllerDoacoes();
        }
    }

    @FXML
    void handleCancelRequest(MouseEvent event) {
        Receptor receptorSelecionado = tableView_receptores.getSelectionModel().getSelectedItem();
        Item itemSelecionado = tableView_pedido.getSelectionModel().getSelectedItem();
        
        if (receptorSelecionado == null || itemSelecionado == null) {
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
            this.controllerDoacoes.removeDoacao(receptorSelecionado, itemSelecionado);
            saveDoacoesController(this.controllerDoacoes);
            
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

            initialize();
            

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @FXML
    void handleConcludeRequest(MouseEvent event) {
        Receptor receptorSelecionado = tableView_receptores.getSelectionModel().getSelectedItem();
        Item itemSelecionado = tableView_pedido.getSelectionModel().getSelectedItem();
        
        if (receptorSelecionado == null || itemSelecionado == null) {
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
            this.controllerDoacoes.concludeRequest(receptorSelecionado, itemSelecionado);
            saveDoacoesController(this.controllerDoacoes);
            
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
            
            initialize();
            

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private ControllerDoacoes rescueDoacoesController() throws IOException, ClassNotFoundException {
        ControllerDoacoes controllerDoacoes;
        FileInputStream flow = new FileInputStream("doacoes.ser");
        ObjectInputStream readControllerDoacoes = new ObjectInputStream(flow);
        controllerDoacoes = (ControllerDoacoes)readControllerDoacoes.readObject();
        flow.close();
        readControllerDoacoes.close();
        return controllerDoacoes;
    }

    private void saveDoacoesController(ControllerDoacoes controllerDoacoes) throws IOException {
        FileOutputStream fos = new FileOutputStream("doacoes.ser");
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(controllerDoacoes);
        oos.close();
        fos.close();
    }

}
