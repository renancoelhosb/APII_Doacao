package controller;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class StockPaneController {

    @FXML
    private AnchorPane anchorPane_main;

    @FXML
    private Button btn_register_product;

    @FXML
    private TableColumn<?, ?> col_due;

    @FXML
    private TableColumn<?, ?> col_item;

    @FXML
    private TableColumn<?, ?> col_qtd;

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

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
