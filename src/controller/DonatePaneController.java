package controller;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

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
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import model.Doador;
import model.Item;
import model.Vencimento;

public class DonatePaneController {

    @FXML private AnchorPane anchorPane_main;
    @FXML private Button btn_cancelDonate;
    @FXML private Button btn_donate;
    @FXML private TextField tf_cpf;
    @FXML private TextField tf_nome;
    @FXML private TextField tf_telefone;
    @FXML private TextField tf_validade;
    @FXML private TextField tf_qtd;
    @FXML private ListView<Item> listView_items;
    @FXML private CheckBox cb_termos;

    private ControllerItems controllerItems;
    private ControllerDoadores controllerDoadores;

    @FXML
    public void initialize() {
        
        Label placeholder = new Label("Nenhum produto cadastrado no estoque.\nO Admin precisa cadastrar tipos de produtos primeiro.");
        placeholder.setStyle("-fx-text-fill: red; -fx-font-weight: bold; -fx-text-alignment: center; -fx-wrap-text: true;");
        listView_items.setPlaceholder(placeholder);

        try {

            controllerItems = rescueItemsController();
            if (controllerItems == null || controllerItems.getItens().isEmpty()) {
                controllerItems.adicionarItem("Arroz");
                controllerItems.adicionarItem("Feijão");
                controllerItems.adicionarItem("Macarrão (500g)");
                controllerItems.adicionarItem("Açúcar");
                controllerItems.adicionarItem("Café");
                controllerItems.adicionarItem("Leite (1L)");
                controllerItems.adicionarItem("Óleo de soja (900ml)");
                controllerItems.adicionarItem("Farinha de trigo");
                controllerItems.adicionarItem("Sal");
                controllerItems.adicionarItem("Fubá");
                saveItemsController(controllerItems);
            }

            controllerDoadores = rescueDoadoresController();
            if (controllerDoadores == null) controllerDoadores = new ControllerDoadores();

            updateListView();
            setupMasks();

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void updateListView() {
        if (controllerItems != null && controllerItems.getItens() != null) {
            ObservableList<Item> data = FXCollections.observableArrayList(controllerItems.getItens());
            listView_items.setItems(data);
            
           
            listView_items.setCellFactory(new Callback<ListView<Item>, ListCell<Item>>() {
                @Override
                public ListCell<Item> call(ListView<Item> param) {
                    return new ListCell<Item>() {
                        @Override
                        protected void updateItem(Item item, boolean empty) {
                            super.updateItem(item, empty);
                            if (empty || item == null) {
                                setText(null);
                            } else {
                                setText(item.getNome() + " (Em estoque: " + item.getQtd() + ")");
                            }
                        }
                    };
                }
            });
        }
    }

    @FXML
    void handleGoToConfirmationDonateWindow(MouseEvent event) {
        if (!validateInputs()) return;

        try {
            
            String docRaw = tf_cpf.getText().replaceAll("[^0-9]", "");
            long identificacao = Long.parseLong(docRaw);
            
            long telefone = Long.parseLong(tf_telefone.getText().replaceAll("[^0-9]", ""));
            int qtd = Integer.parseInt(tf_qtd.getText());
            
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            LocalDate validade = LocalDate.parse(tf_validade.getText(), formatter);


            if (validade.isBefore(LocalDate.now())) {
                showAlert("Data Inválida", "O produto já está vencido.");
                return;
            }

   
            if (controllerDoadores.getDoador(identificacao) == null) {
                controllerDoadores.addDoador(tf_nome.getText(), identificacao, telefone);
            }

            controllerDoadores.getDoador(identificacao).registrarDoacao();
            saveDoadoresController(controllerDoadores);


            Item itemSelecionado = listView_items.getSelectionModel().getSelectedItem();
            

            itemSelecionado.setQtd(itemSelecionado.getQtd() + qtd);
            
           
            itemSelecionado.getVencimentos().add(new Vencimento(qtd, validade));
            
            saveItemsController(controllerItems);

            showConfirmation(event);
            handleGoBackToMainPane(event);

        } catch (NumberFormatException e) {
            e.printStackTrace();
            showAlert("Erro numérico", "Verifique CPF e Telefone. Apenas números.");
        } catch (DateTimeParseException e) {
            showAlert("Data Inválida", "Use o formato dd/MM/yyyy (ex: 31/12/2025).");
        } catch (Exception e) {
            e.printStackTrace(); 
            showAlert("Erro", "Erro desconhecido ao processar doação.");
        }
    }

    private boolean validateInputs() {
        if (tf_nome.getText().isEmpty() || tf_cpf.getText().isEmpty() || tf_validade.getText().isEmpty()) {
            showAlert("Campos Vazios", "Preencha todos os campos.");
            return false;
        }
        if (!cb_termos.isSelected()) {
            showAlert("Termos", "Você deve aceitar os termos.");
            return false;
        }
        if (listView_items.getSelectionModel().getSelectedItem() == null) {
            showAlert("Item", "Selecione um item da lista.");
            return false;
        }
        
        String doc = tf_cpf.getText().replaceAll("[^0-9]", "");

        if (doc.length() < 11 || doc.length() > 14) {
            showAlert("Documento Inválido", "CPF/CNPJ deve ter entre 11 e 14 dígitos.");
            return false;
        }
        return true;
    }

    private void setupMasks() {

        tf_validade.setOnKeyTyped(e -> {
            String text = tf_validade.getText();
            if (text.length() == 2 || text.length() == 5) {
                tf_validade.appendText("/");
            }
            if (text.length() > 9) e.consume();
        });

        tf_telefone.textProperty().addListener((obs, oldV, newV) -> {
            if (!newV.matches("\\d*")) tf_telefone.setText(newV.replaceAll("[^\\d]", ""));
        });
    }

    @FXML
    void handleGoBackToMainPane(MouseEvent event) {
        try {
            AnchorPane pane = (AnchorPane)FXMLLoader.load(getClass().getResource("/view/MainPane.fxml"));
            this.anchorPane_main.getScene().setRoot(pane);
        } catch (IOException e) { e.printStackTrace(); }
    }

    private void showConfirmation(MouseEvent event) throws IOException {
        Stage currentStage = (Stage)((Node) event.getSource()).getScene().getWindow();
        AnchorPane pane = (AnchorPane)FXMLLoader.load(getClass().getResource("/view/ConfirmationDonateWindow.fxml"));
        Stage newStage = new Stage();
        newStage.initOwner(currentStage);
        newStage.initModality(Modality.WINDOW_MODAL);
        newStage.setScene(new Scene(pane));
        newStage.setResizable(false);
        newStage.showAndWait();
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
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

    private ControllerDoadores rescueDoadoresController() throws IOException, ClassNotFoundException {
        try (FileInputStream fis = new FileInputStream("doadores.ser");
             ObjectInputStream ois = new ObjectInputStream(fis)) {
            return (ControllerDoadores)ois.readObject();
        } catch (IOException e) { return new ControllerDoadores(); }
    }

    private void saveDoadoresController(ControllerDoadores c) throws IOException {
        try (FileOutputStream fos = new FileOutputStream("doadores.ser");
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(c);
        }
    }

    @FXML
    void handleCheckCPFRegister(KeyEvent event) {
        // Remove tudo que não é número
        String cpfRaw = tf_cpf.getText().replaceAll("[^0-9]", "");
        
        if (cpfRaw.length() < 11) {
            return;
        }
        
        try {
            long cpfNumber = Long.parseLong(cpfRaw);
            Doador doador = controllerDoadores.getDoador(cpfNumber); // remove o cast para (int)
            
            if (doador != null) {
                tf_nome.setText(doador.getNome());
                tf_telefone.setText(String.valueOf(doador.getTelefone()));
            } else {
                // Limpa campos se não encontrar doador
                tf_nome.clear();
                tf_telefone.clear();
            }
        } catch (NumberFormatException e) {
            // Ignora se ainda estiver digitando ou valor inválido
        }
    }
}