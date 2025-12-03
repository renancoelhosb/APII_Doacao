package controller;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import model.Doacao;
import model.Item;
import model.Receptor;

public class RequestsPaneController {

    @FXML
    private TableView<Receptor> tableView_receptores;

    @FXML
    private TableColumn<Receptor, String> col_name;

    @FXML
    private TableColumn<Receptor, Double> col_income;

    @FXML
    private TableColumn<Receptor, Long> col_phone;

    @FXML
    private TableColumn<Receptor, Long> col_id;

    @FXML
    private TableView<Item> tableView_itens;

    @FXML
    private TableColumn<Item, String> col_item_name;

    @FXML
    private TableColumn<Item, Integer> col_item_qtd;

    @FXML
    private Button btn_efetivar;

    @FXML
    private Button btn_cancelar;

    private ControllerDoacoes controllerDoacoes;
    private ControllerItems controllerItems;
    private Receptor receptorSelecionado;
    private Item itemSelecionado;

    @FXML
    public void initialize() {
        try {
            controllerDoacoes = rescueDoacoesController();
            controllerItems = rescueItemsController();

            // Configura tabela de receptores
            ArrayList<Receptor> listaReceptores = controllerDoacoes.getReceptores();
            ObservableList<Receptor> dataRec = FXCollections.observableArrayList(listaReceptores);

            col_name.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getNome()));
            col_income.setCellValueFactory(cell -> new javafx.beans.property.SimpleDoubleProperty(cell.getValue().getRenda()).asObject());
            col_phone.setCellValueFactory(cell -> new SimpleLongProperty(cell.getValue().getTelefone()).asObject());
            col_id.setCellValueFactory(cell -> new javafx.beans.property.SimpleLongProperty(cell.getValue().getId()).asObject());

            tableView_receptores.setItems(dataRec);

            // Listener para seleção de receptor
            tableView_receptores.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
                if (newSelection != null) {
                    receptorSelecionado = newSelection;
                    mostrarPedidosDoReceptor(newSelection);
                }
            });

            // Configura tabela de itens
            col_item_name.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getNome()));
            col_item_qtd.setCellValueFactory(cell -> new SimpleIntegerProperty(cell.getValue().getQtd()).asObject());

            // Listener para seleção de item
            tableView_itens.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
                itemSelecionado = newSelection;
                atualizarBotoes();
            });

            // Centralizar colunas
            col_name.setStyle("-fx-alignment: CENTER;");
            col_income.setStyle("-fx-alignment: CENTER;");
            col_phone.setStyle("-fx-alignment: CENTER;");
            col_id.setStyle("-fx-alignment: CENTER;");
            col_item_name.setStyle("-fx-alignment: CENTER;");
            col_item_qtd.setStyle("-fx-alignment: CENTER;");

            atualizarBotoes();

        } catch (IOException | ClassNotFoundException e) {
            controllerDoacoes = new ControllerDoacoes();
            controllerItems = new ControllerItems();
            e.printStackTrace();
        }
    }

    private void mostrarPedidosDoReceptor(Receptor r) {
        ArrayList<Doacao> doacoes = controllerDoacoes.getDoacao(r);
        ArrayList<Item> itensPedidos = new ArrayList<>();
        
        for (Doacao d : doacoes) {
            itensPedidos.add(d.getItem());
        }

        ObservableList<Item> dataItens = FXCollections.observableArrayList(itensPedidos);
        tableView_itens.setItems(dataItens);
    }

    private void atualizarBotoes() {
        boolean itemSelecionadoValido = itemSelecionado != null && receptorSelecionado != null;
        btn_efetivar.setDisable(!itemSelecionadoValido);
        btn_cancelar.setDisable(!itemSelecionadoValido);
    }

    @FXML
    void handleConcludeRequest(MouseEvent event) {
        if (receptorSelecionado == null || itemSelecionado == null) {
            mostrarAlerta("Seleção inválida", "Selecione um receptor e um item para efetivar a doação.", AlertType.WARNING);
            return;
        }

        // Busca o item no estoque pelo nome
        Item itemEstoqueTemp = null;
        for (Item item : controllerItems.getItens()) {
            if (item.getNome().equals(itemSelecionado.getNome())) {
                itemEstoqueTemp = item;
                break;
            }
        }
        final Item itemEstoque = itemEstoqueTemp;
        
        if (itemEstoque == null) {
            mostrarAlerta("Item não encontrado", "O item solicitado não foi encontrado no estoque.", AlertType.ERROR);
            return;
        }

        // Verifica quantidade disponível
        int qtdSolicitada = itemSelecionado.getQtd();
        int qtdEstoque = itemEstoque.getQtd();

        if (qtdEstoque < qtdSolicitada) {
            mostrarAlerta("Estoque insuficiente", 
                String.format("Quantidade solicitada: %d\nQuantidade em estoque: %d\n\nNão há estoque suficiente para efetivar esta doação.", 
                qtdSolicitada, qtdEstoque), 
                AlertType.ERROR);
            return;
        }

        // Confirmação
        Alert confirmacao = new Alert(AlertType.CONFIRMATION);
        confirmacao.setTitle("Confirmar doação");
        confirmacao.setHeaderText("Efetivar doação");
        confirmacao.setContentText(String.format(
            "Receptor: %s\nItem: %s\nQuantidade: %d\n\nConfirma a efetivação desta doação?",
            receptorSelecionado.getNome(),
            itemSelecionado.getNome(),
            qtdSolicitada
        ));

        confirmacao.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    // NOVO: Remove quantidade usando FIFO (primeiro a vencer)
                    itemEstoque.removerQuantidade(qtdSolicitada);
                    
                    // NOVO: Limpa vencimentos com quantidade zero
                    itemEstoque.limparVencimentosZerados();
                    
                    // Remove a solicitação
                    controllerDoacoes.concludeRequest(receptorSelecionado, itemSelecionado);
                    
                    // Salva alterações
                    saveItemsController(controllerItems);
                    saveDoacoesController(controllerDoacoes);
                    
                    mostrarAlerta("Sucesso", "Doação efetivada com sucesso!\nItens retirados dos lotes mais próximos do vencimento.", AlertType.INFORMATION);
                    
                    // Atualiza a interface
                    atualizarInterface();
                    
                } catch (IOException e) {
                    mostrarAlerta("Erro ao salvar", "Não foi possível salvar as alterações.", AlertType.ERROR);
                    e.printStackTrace();
                }
            }
        });
    }

    @FXML
    void handleCancelRequest(MouseEvent event) {
        if (receptorSelecionado == null || itemSelecionado == null) {
            mostrarAlerta("Seleção inválida", "Selecione um receptor e um item para cancelar a solicitação.", AlertType.WARNING);
            return;
        }

        // Confirmação
        Alert confirmacao = new Alert(AlertType.CONFIRMATION);
        confirmacao.setTitle("Cancelar solicitação");
        confirmacao.setHeaderText("Cancelar solicitação de doação");
        confirmacao.setContentText(String.format(
            "Receptor: %s\nItem: %s\nQuantidade: %d\n\nConfirma o cancelamento desta solicitação?",
            receptorSelecionado.getNome(),
            itemSelecionado.getNome(),
            itemSelecionado.getQtd()
        ));

        confirmacao.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    // Remove a solicitação
                    controllerDoacoes.removeDoacao(receptorSelecionado, itemSelecionado);
                    
                    // Salva alterações
                    saveDoacoesController(controllerDoacoes);
                    
                    mostrarAlerta("Sucesso", "Solicitação cancelada com sucesso!", AlertType.INFORMATION);
                    
                    // Atualiza a interface
                    atualizarInterface();
                    
                } catch (IOException e) {
                    mostrarAlerta("Erro ao salvar", "Não foi possível salvar as alterações.", AlertType.ERROR);
                    e.printStackTrace();
                }
            }
        });
    }

    private void atualizarInterface() {
        // Recarrega a lista de receptores
        ArrayList<Receptor> listaReceptores = controllerDoacoes.getReceptores();
        ObservableList<Receptor> dataRec = FXCollections.observableArrayList(listaReceptores);
        tableView_receptores.setItems(dataRec);

        // Se ainda há um receptor selecionado, atualiza seus pedidos
        if (receptorSelecionado != null) {
            boolean receptorAindaTemPedidos = controllerDoacoes.receptorTemPedidos(receptorSelecionado);
            if (receptorAindaTemPedidos) {
                mostrarPedidosDoReceptor(receptorSelecionado);
            } else {
                tableView_itens.getItems().clear();
                receptorSelecionado = null;
                itemSelecionado = null;
            }
        }

        atualizarBotoes();
    }

    private void mostrarAlerta(String titulo, String mensagem, AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }

    private ControllerDoacoes rescueDoacoesController() throws IOException, ClassNotFoundException {
        try (FileInputStream fis = new FileInputStream("doacoes.ser");
             ObjectInputStream ois = new ObjectInputStream(fis)) {
            return (ControllerDoacoes) ois.readObject();
        }
    }

    private ControllerItems rescueItemsController() throws IOException, ClassNotFoundException {
        try (FileInputStream fis = new FileInputStream("stock.ser");
             ObjectInputStream ois = new ObjectInputStream(fis)) {
            return (ControllerItems) ois.readObject();
        }
    }

    private void saveDoacoesController(ControllerDoacoes controller) throws IOException {
        try (FileOutputStream fos = new FileOutputStream("doacoes.ser");
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(controller);
        }
    }

    private void saveItemsController(ControllerItems controller) throws IOException {
        try (FileOutputStream fos = new FileOutputStream("stock.ser");
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(controller);
        }
    }
}