package controller;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
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
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import model.Item;
import model.Receptor;

public class ReceivePaneController {

    @FXML private AnchorPane anchorPane_main;
    @FXML private Button brn_receive;
    @FXML private Button btn_cancelReceipt;
    @FXML private CheckBox cbox_iAgree;
    @FXML private ListView<Item> listView_items;
    @FXML private TextField textField_identification;
    @FXML private TextField textField_income;
    @FXML private TextField textField_name;
    @FXML private TextField textField_numberPhone;
    @FXML private TextField textField_qtd;

    private ControllerReceivers controllerReceivers;
    private ControllerItems controllerItems;
    private ControllerDoacoes controllerDoacoes;

    @FXML
    public void initialize() {
        try {
            FileInputStream fisRec = new FileInputStream("receivers.ser");
            ObjectInputStream oisRec = new ObjectInputStream(fisRec);
            controllerReceivers = (ControllerReceivers) oisRec.readObject();
            fisRec.close();
            oisRec.close();
        } catch (Exception e) {
            controllerReceivers = new ControllerReceivers();
        }

        try {
            FileInputStream fisItems = new FileInputStream("stock.ser");
            ObjectInputStream oisItems = new ObjectInputStream(fisItems);
            controllerItems = (ControllerItems) oisItems.readObject();
            fisItems.close();
            oisItems.close();
        } catch (Exception e) {
            controllerItems = new ControllerItems();
        }

        try {
            FileInputStream fisDoac = new FileInputStream("doacoes.ser");
            ObjectInputStream oisDoac = new ObjectInputStream(fisDoac);
            controllerDoacoes = (ControllerDoacoes) oisDoac.readObject();
            fisDoac.close();
            oisDoac.close();
        } catch (Exception e) {
            controllerDoacoes = new ControllerDoacoes();
        }

        ArrayList<Item> validItems = new ArrayList<>();
        if (controllerItems.getItens() != null) {
            for (Item i : controllerItems.getItens()) {
                if (i.getQtd() > 0) validItems.add(i);
            }
        }

        ObservableList<Item> data = FXCollections.observableArrayList(validItems);
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
                            setText(item.getNome() + " (Disp: " + item.getQtd() + ")");
                        }
                    }
                };
            }
        });
    }

    @FXML
    void handleGoToConfirmationReceiveWindow(MouseEvent event) {
        if (textField_name.getText().isEmpty() || textField_identification.getText().isEmpty() || 
            textField_numberPhone.getText().isEmpty() || textField_income.getText().isEmpty()) {
            showAlert(AlertType.WARNING, "Preencha todos os campos.");
            return;
        }

        if (!cbox_iAgree.isSelected()) {
            showAlert(AlertType.WARNING, "Aceite os termos.");
            return;
        }

        String cpfRaw = textField_identification.getText().replaceAll("[^0-9]", "");
        if (cpfRaw.length() != 11 && cpfRaw.length() != 14) {
            showAlert(AlertType.WARNING, "CPF (11) ou CNPJ (14) inválido.");
            return;
        }

        Item itemSelecionado = listView_items.getSelectionModel().getSelectedItem();
        if (itemSelecionado == null) {
            showAlert(AlertType.WARNING, "Selecione um item disponível.");
            return;
        }

        try {

            long id = Long.parseLong(cpfRaw);
            int tel = Integer.parseInt(textField_numberPhone.getText().replaceAll("[^0-9]", ""));
            double renda = Double.parseDouble(textField_income.getText().replace(",", "."));

            Receptor r = controllerReceivers.getReceptor(id);
            if (r == null) {
                controllerReceivers.addReceptor(textField_name.getText(), id, tel, renda);
                r = controllerReceivers.getReceptor(id);
            }

            controllerDoacoes.insertDoacao(r, itemSelecionado);

            saveController(controllerReceivers, "receivers.ser");
            saveController(controllerDoacoes, "doacoes.ser");

            showConfirmation(event);
            handleGoBackToMainPane(event);

        } catch (Exception e) {
            e.printStackTrace();
            showAlert(AlertType.ERROR, "Erro ao processar.");
        }
    }

    @FXML
    void handleCheckId(KeyEvent event) {
        try {
            String txt = textField_identification.getText().replaceAll("[^0-9]", "");
            if (!txt.isEmpty() && (txt.length() == 11 || txt.length() == 14)) {
                long id = Long.parseLong(txt);
                Receptor r = controllerReceivers.getReceptor(id);
                if (r != null) {
                    textField_name.setText(r.getNome());
                    textField_numberPhone.setText(String.valueOf(r.getTelefone()));
                    textField_income.setText(String.valueOf(r.getRenda()));
                }
            }
        } catch (Exception e) {}
    }

    @FXML
    void handleGoBackToMainPane(MouseEvent event) {
        try {
            AnchorPane pane = (AnchorPane)FXMLLoader.load(getClass().getResource("/view/MainPane.fxml"));
            this.anchorPane_main.getScene().setRoot(pane);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private void showAlert(AlertType type, String msg) {
        Alert alert = new Alert(type);
        alert.setContentText(msg);
        alert.showAndWait();
    }
    
    private void saveController(Object controller, String filename) throws IOException {
        FileOutputStream fos = new FileOutputStream(filename);
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(controller);
        oos.close();
        fos.close();
    }

    private void showConfirmation(MouseEvent event) throws IOException {
        Stage currentStage = (Stage)((Node) event.getSource()).getScene().getWindow();
        AnchorPane pane = (AnchorPane)FXMLLoader.load(getClass().getResource("/view/ConfirmationReceiveWindow.fxml"));
        Stage newStage = new Stage();
        newStage.initOwner(currentStage);
        newStage.initModality(Modality.WINDOW_MODAL);
        newStage.setScene(new Scene(pane));
        newStage.setResizable(false);
        newStage.showAndWait();
    }
}