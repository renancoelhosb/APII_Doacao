package controller;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

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
import javafx.scene.control.CheckBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.Item;
import model.Receptor;

public class ReceivePaneController {

    @FXML
    private AnchorPane anchorPane_main;

    @FXML
    private Button brn_receive;

    @FXML
    private Button btn_cancelReceipt;

    @FXML
    private CheckBox cbox_iAgree;

    @FXML
    private ListView<Item> listView_items;

    @FXML
    private TextField textField_identification;

    @FXML
    private TextField textField_income;

    @FXML
    private TextField textField_name;

    @FXML
    private TextField textField_numberPhone;

    @FXML
    private ControllerReceivers controllerReceivers;

    @FXML
    private ControllerItems controllerItems;

    @FXML
    private TextField textField_qtd;

    @FXML
    void handleGoBackToMainPane(MouseEvent event) {
        AnchorPane pane;
        Scene scene;
        try {
            pane = (AnchorPane)FXMLLoader.load(getClass().getResource("/view/MainPane.fxml"));
            scene = this.anchorPane_main.getScene();
            scene.setRoot(pane);
        } catch (IOException e) {
            e.printStackTrace();
            mostrarErro("Erro ao voltar para a tela principal");
        }
    }

    @FXML
    void handleGoToConfirmationReceiveWindow(MouseEvent event) {
        if (!validarCampos()) {
            return;
        }
        
        Item itemSelecionado = listView_items.getSelectionModel().getSelectedItem();
        if (itemSelecionado == null) {
            Alert alert = new Alert(AlertType.WARNING);
            alert.setTitle("Nenhum item selecionado");
            alert.setHeaderText("Selecione um item");
            alert.setContentText("Por favor, selecione um item da lista para receber.");
            alert.showAndWait();
            return;
        }
        
        if (!cbox_iAgree.isSelected()) {
            Alert alert = new Alert(AlertType.WARNING);
            alert.setTitle("Termos não aceitos");
            alert.setHeaderText("Aceite os termos de doação");
            alert.setContentText("Você precisa estar de acordo com os termos de doação para continuar.");
            alert.showAndWait();
            return;
        }
        
        try {
            if (controllerReceivers.getReceptor(Integer.parseInt(textField_identification.getText().trim())) == null) {
                String nome = textField_name.getText().trim();
                int identificacao = Integer.parseInt(textField_identification.getText().trim());
                int telefone = Integer.parseInt(textField_numberPhone.getText().trim());
                double renda = Double.parseDouble(textField_income.getText().trim());
                controllerReceivers.addReceptor(nome, identificacao, telefone, renda);
                saveReceiversController(controllerReceivers);
            }

            // obter quantidade do TextField (ajuste conforme seus campos)
            int quantidade;
            try {
                quantidade = Integer.parseInt(textField_qtd.getText());
                if (quantidade <= 0) {
                    Alert alert = new Alert(AlertType.ERROR);
                    alert.setTitle("Quantidade inválida");
                    alert.setHeaderText(null);
                    alert.setContentText("Digite uma quantidade válida.");
                    alert.showAndWait();
                    return;
                }
            } catch (NumberFormatException e) {
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Quantidade inválida");
                alert.setHeaderText(null);
                alert.setContentText("Digite uma quantidade válida.");
                alert.showAndWait();
                return;
            }

            

            AnchorPane pane;
            Stage newStage;
            Stage currentStage = (Stage)((Node) event.getSource()).getScene().getWindow();
            
            pane = (AnchorPane)FXMLLoader.load(getClass().getResource("/view/ConfirmationReceiveWindow.fxml"));
            newStage = new Stage();
            newStage.initOwner(currentStage);
            newStage.initModality(Modality.WINDOW_MODAL);
            newStage.setScene(new Scene(pane));
            newStage.setTitle("Confirmação - DoAção");
            newStage.setResizable(false);
            newStage.showAndWait();
            
            handleGoBackToMainPane(event);

        } catch (NumberFormatException e) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Erro de formato");
            alert.setHeaderText("Formato inválido");
            alert.setContentText("Por favor, verifique se todos os campos numéricos estão preenchidos corretamente.");
            alert.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
            mostrarErro("Erro ao abrir janela de confirmação");
        }
    }
    
    @FXML
    public void initialize() {
        try {
            controllerReceivers = rescueReceiversController();

        } catch (IOException | ClassNotFoundException e) {
            controllerReceivers = new ControllerReceivers();
        }
        
        try {
            controllerItems = rescueItemsController();
            ArrayList<Item> lista = controllerItems.getItens();
            ObservableList<Item> dataItems = FXCollections.observableArrayList(lista);
            listView_items.setItems(dataItems);

            // opcional: customizar exibição dos itens no ListView
            listView_items.setCellFactory(lv -> new javafx.scene.control.ListCell<Item>() {
                @Override
                protected void updateItem(Item item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setText(null);
                    } else {
                        setText(item.getNome()); // ou qualquer outro campo que queira exibir
                    }
                }
            });

        } catch (IOException | ClassNotFoundException e) {
            controllerItems = new ControllerItems();
        }
    }

    @FXML
    void handleCheckId(KeyEvent event) {
        String id_ = textField_identification.getText().trim();
        int id = Integer.parseInt(id_);
        if (id_.length() >= 11 && controllerReceivers.getReceptor(id) != null) {
            textField_name.setText(controllerReceivers.getReceptor(id).getNome());
            textField_numberPhone.setText(String.valueOf(controllerReceivers.getReceptor(id).getTelefone()));
            textField_income.setText(String.valueOf(controllerReceivers.getReceptor(id).getRenda()));
        }
    }
    
    private void validarFormulario() {
        boolean camposPreenchidos = !textField_name.getText().trim().isEmpty() &&
                                   !textField_identification.getText().trim().isEmpty() &&
                                   !textField_numberPhone.getText().trim().isEmpty() &&
                                   !textField_income.getText().trim().isEmpty();
        
        boolean itemSelecionado = listView_items.getSelectionModel().getSelectedItem() != null;
        boolean termosAceitos = cbox_iAgree.isSelected();
        
        brn_receive.setDisable(!(camposPreenchidos && itemSelecionado && termosAceitos));
    }
    
    private boolean validarCampos() {
        if (textField_name.getText().trim().isEmpty() ||
            textField_identification.getText().trim().isEmpty() ||
            textField_numberPhone.getText().trim().isEmpty() ||
            textField_income.getText().trim().isEmpty()) {
            
            Alert alert = new Alert(AlertType.WARNING);
            alert.setTitle("Campos obrigatórios");
            alert.setHeaderText("Preencha todos os campos");
            alert.setContentText("Todos os campos são obrigatórios para realizar a solicitação.");
            alert.showAndWait();
            return false;
        }
        
        try {
            Integer.parseInt(textField_identification.getText().trim());
            Integer.parseInt(textField_numberPhone.getText().trim());
            Double.parseDouble(textField_income.getText().trim());
        } catch (NumberFormatException e) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Formato inválido");
            alert.setHeaderText("Campos numéricos inválidos");
            alert.setContentText("CPF/CNPJ, Telefone e Renda devem ser números válidos.");
            alert.showAndWait();
            return false;
        }
        
        return true;
    }
    
    private void mostrarErro(String mensagem) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Erro");
        alert.setHeaderText("Ocorreu um erro");
        alert.setContentText(mensagem);
        alert.showAndWait();
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

    private ControllerItems rescueItemsController() throws IOException, ClassNotFoundException {
        FileInputStream flow = new FileInputStream("stock.ser");
        ObjectInputStream readControllerItems = new ObjectInputStream(flow);
        controllerItems = (ControllerItems)readControllerItems.readObject();
        flow.close();
        readControllerItems.close();
        return controllerItems;
    }

    
}