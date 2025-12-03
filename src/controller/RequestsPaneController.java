package controller;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import model.Doacao;
import model.Item;
import model.Receptor;

public class RequestsPaneController {

    @FXML private AnchorPane anchorPane_main;
    @FXML private Button btn_cancel_request;
    @FXML private Button btn_conclude;

    @FXML private TableView<Receptor> tableView_receptores;
    @FXML private TableColumn<Receptor, String> col_name;
    @FXML private TableColumn<Receptor, Integer> col_phone;
    @FXML private TableColumn<Receptor, Long> col_id; 
    @FXML private TableColumn<Receptor, Double> col_income;

    @FXML private TableView<Item> tableView_pedido;
    @FXML private TableColumn<Item, String> col_item;
    @FXML private TableColumn<Item, Integer> col_qtd;

    private ControllerDoacoes controllerDoacoes;
    private ControllerItems controllerItems;

    @FXML
    public void initialize() {
        try {
            controllerDoacoes = rescueDoacoesController();
            if (controllerDoacoes == null) controllerDoacoes = new ControllerDoacoes();
            
            col_name.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getNome()));
            col_income.setCellValueFactory(cell -> new SimpleDoubleProperty(cell.getValue().getRenda()).asObject());
            col_phone.setCellValueFactory(cell -> new SimpleIntegerProperty(cell.getValue().getTelefone()).asObject());
            col_id.setCellValueFactory(cell -> new SimpleLongProperty(cell.getValue().getId()).asObject());

            ArrayList<Receptor> listaReceptores = controllerDoacoes.getReceptores();
           
            ObservableList<Receptor> dataRec = FXCollections.observableArrayList(listaReceptores);
            tableView_receptores.setItems(dataRec);

            tableView_receptores.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
                if (newSelection != null) {
                    mostrarPedidoDoReceptor(newSelection);
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void mostrarPedidoDoReceptor(Receptor r) {
        ArrayList<Doacao> doacoes = controllerDoacoes.getDoacao(r);
        ArrayList<Item> itensPedidos = new ArrayList<>();
        for (Doacao d : doacoes) {
            itensPedidos.add(d.getItem());
        }

        ObservableList<Item> dataItens = FXCollections.observableArrayList(itensPedidos);
        col_item.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getNome()));
        col_qtd.setCellValueFactory(cell -> new SimpleIntegerProperty(cell.getValue().getQtd()).asObject());
        
        tableView_pedido.setItems(dataItens);
    }

    @FXML
    void handleConcludeRequest(MouseEvent event) {
        Receptor rec = tableView_receptores.getSelectionModel().getSelectedItem();
        Item item = tableView_pedido.getSelectionModel().getSelectedItem();

        if (rec != null && item != null) {
            boolean removeu = item.removerDoEstoque(1); 
            if (removeu) {
                controllerDoacoes.removeDoacao(rec, item);
                try {
                    saveDoacoesController(controllerDoacoes);
                    saveItemsController(controllerItems); 
                    initialize(); 

                    tableView_pedido.getItems().clear();
                    showAlert("Sucesso", "Doação efetivada e estoque atualizado.");
                } catch (IOException e) { e.printStackTrace(); }
            } else {
                showAlert("Erro", "Estoque insuficiente para efetivar.");
            }
        } else {
            showAlert("Seleção", "Selecione um Receptor e um Item.");
        }
    }

    @FXML
    void handleCancelRequest(MouseEvent event) {
        Receptor rec = tableView_receptores.getSelectionModel().getSelectedItem();
        Item item = tableView_pedido.getSelectionModel().getSelectedItem();

        if (rec != null && item != null) {
            controllerDoacoes.removeDoacao(rec, item);
            try {
                saveDoacoesController(controllerDoacoes);
                initialize();
                tableView_pedido.getItems().clear();
                showAlert("Cancelado", "Solicitação cancelada.");
            } catch (IOException e) { e.printStackTrace(); }
        }
    }

    private void showAlert(String title, String msg) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
   
    private ControllerDoacoes rescueDoacoesController() throws IOException, ClassNotFoundException {
        try (FileInputStream fis = new FileInputStream("doacoes.ser");
             ObjectInputStream ois = new ObjectInputStream(fis)) {
            return (ControllerDoacoes)ois.readObject();
        } catch (IOException e) { return new ControllerDoacoes(); }
    }
    
    private void saveDoacoesController(ControllerDoacoes c) throws IOException {
        try (FileOutputStream fos = new FileOutputStream("doacoes.ser");
                ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(c);
        }
    }

    private ControllerItems rescueItemsController() throws IOException, ClassNotFoundException {
        try (FileInputStream fis = new FileInputStream("stock.ser");
             ObjectInputStream ois = new ObjectInputStream(fis)) {
            return (ControllerItems)ois.readObject();
        } catch (IOException e) { return new ControllerItems(); }
    }

    private void saveItemsController(ControllerItems c) throws IOException {
        try (FileOutputStream fos = new FileOutputStream("stock.ser");
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(c);
        }
    }
}