package controller;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;

import model.Item;
import model.Vencimento;

public class StockPaneController {

    @FXML
    private AnchorPane anchorPane_main;

    @FXML
    private Button btn_register_product;

    @FXML
    private TableView<Item> table_items;

    @FXML
    private TableColumn<Item, Void> col_due;

    @FXML
    private TableColumn<Item, String> col_item;

    @FXML
    private TableColumn<Item, Integer> col_qtd;

    ControllerItems controllerItems;
    
    public void initialize() {
        try {
            controllerItems = rescueItemsController();

            // popula tabela se houver itens
            if (controllerItems != null && controllerItems.getItens() != null) {
                ObservableList<Item> data = FXCollections.observableArrayList(controllerItems.getItens());

                // usando lambdas para garantir compatibilidade com nomes dos getters
                col_item.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getNome()));
                col_qtd.setCellValueFactory(cell -> new SimpleIntegerProperty(cell.getValue().getQtd()).asObject());
                col_due.setCellFactory(new Callback<TableColumn<Item, Void>, TableCell<Item, Void>>() {
                    @Override
                    public TableCell<Item, Void> call(TableColumn<Item, Void> param) {
                        return new TableCell<Item, Void>() {
                            private final Button btn = new Button("Ver detalhes");
                            
                            {
                                btn.setStyle("-fx-padding: 5; -fx-font-size: 11;");
                                btn.setOnAction(event -> {
                                    Item item = getTableView().getItems().get(getIndex());
                                    openDuePopup(item);
                                });
                                setAlignment(Pos.CENTER);
                            }

                            @Override
                            protected void updateItem(Void item, boolean empty) {
                                super.updateItem(item, empty);
                                setGraphic(empty ? null : btn);
                            }
                        };
                    }
                });

                table_items.setItems(data);
            }

        } catch (IOException | ClassNotFoundException e) {
            controllerItems = new ControllerItems();
        }   
    }

    @FXML
    void handleRegisterProduct(MouseEvent event) {
        AnchorPane pane;
        Stage newStage;
        Stage currentStage;
        NewProductWindowController controller;   //// Not used but kept for consistency
        FXMLLoader loader;
        try {
            currentStage = (Stage)((Node) event.getSource()).getScene().getWindow();
            loader = new FXMLLoader(getClass().getResource("/view/NewProductWindow.fxml"));
            pane = (AnchorPane) loader.load();
            controller = loader.getController();
            newStage = new Stage();
            newStage.initOwner(currentStage);
            newStage.initModality(Modality.WINDOW_MODAL);
            newStage.setScene(new Scene(pane));
            newStage.setTitle("Novo Produto - DoAção");
            newStage.setResizable(false);
            newStage.showAndWait();
            if (controller.isItemCreatedSucessfully()) {
                controllerItems.adicionarItem(controller.getProductName());
                saveItemsController(controllerItems);
                initialize();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private ControllerItems rescueItemsController() throws IOException, ClassNotFoundException {
        ControllerItems controllerItems;
        FileInputStream flow = new FileInputStream("stock.ser");
        ObjectInputStream readControllerItems = new ObjectInputStream(flow);
        controllerItems = (ControllerItems)readControllerItems.readObject();
        flow.close();
        readControllerItems.close();
        return controllerItems;
    }

    private void saveItemsController(ControllerItems controllerItems) throws IOException {
        FileOutputStream fos = new FileOutputStream("stock.ser");
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(controllerItems);
        oos.close();
        fos.close();
    }

    private void openDuePopup(Item item) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/DueDetailsPopup.fxml"));
            AnchorPane pane = (AnchorPane) loader.load();
            DueDetailsPopupController popupCtrl = loader.getController();
            
            // passa o item para o popup exibir os detalhes
            popupCtrl.setItem(item);

            Stage currentStage = (Stage) table_items.getScene().getWindow();
            Stage popupStage = new Stage();
            popupStage.initOwner(currentStage);
            popupStage.initModality(Modality.WINDOW_MODAL);
            popupStage.setScene(new Scene(pane));
            popupStage.setTitle("Detalhes de Vencimento - " + item.getNome());
            popupStage.setResizable(false);
            popupStage.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
