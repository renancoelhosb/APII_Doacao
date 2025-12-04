package controller;

import java.util.ArrayList;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import model.Item;
import model.Vencimento;

public class DueDetailsPopupController {

    @FXML
    private AnchorPane anchorPane_main;

    @FXML
    private Button btn_close_due_details;

    @FXML
    private TableColumn<Vencimento, String> col_date;

    @FXML
    private TableColumn<Vencimento, Integer> col_qtd;

    @FXML
    private TableView<Vencimento> tableView_due_details;

    private Item item;

    public void setItem(Item item) {
        this.item = item;
        
        ArrayList<Vencimento> vencimentosValidos = new ArrayList<>();
        for (Vencimento v : item.getVencimentos()) {
            if (v.getQtd() > 0) {
                vencimentosValidos.add(v);
            }
        }
        
        if (vencimentosValidos.isEmpty()) {
            Vencimento semVencimentos = new Vencimento(0, null);
            vencimentosValidos.add(semVencimentos);
        }
        
        ObservableList<Vencimento> data = FXCollections.observableArrayList(vencimentosValidos);
        tableView_due_details.setItems(data);
    }

    private void loadVencimentos() {
        if (item == null || item.getVencimentos() == null) {
            tableView_due_details.getItems().clear();
            return;
        }

        ArrayList<Vencimento> vencimentos = item.getVencimentos();
        ObservableList<Vencimento> data = FXCollections.observableArrayList(vencimentos);

        col_date.setCellValueFactory(cell -> {
            Vencimento v = cell.getValue();
            if (v == null) return new SimpleStringProperty("");
            // supondo que getVencimento() retorne java.time.LocalDate — trate null
            if (v.getVencimento() == null) return new SimpleStringProperty("");
            return new SimpleStringProperty(v.getVencimento().toString());
        });

        col_qtd.setCellValueFactory(cell -> {
            Vencimento v = cell.getValue();
            if (v == null) return new SimpleIntegerProperty(0).asObject();
            Integer qtd = v.getQtd();
            return new SimpleIntegerProperty(qtd == null ? 0 : qtd).asObject();
        });

        tableView_due_details.setItems(data);
    }


    @FXML
    void handleCloseDueDetails(ActionEvent event) {
        Stage stage = (Stage) anchorPane_main.getScene().getWindow();
        stage.close();
    }

}
