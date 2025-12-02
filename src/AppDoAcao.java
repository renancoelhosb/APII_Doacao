import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class AppDoAcao extends Application {
    
   @Override
    public void start(Stage stage) {
        try {
            Parent root = FXMLLoader.load (getClass().getResource("/view/MainPane.fxml"));
            Scene scene = new Scene(root);
            stage.setTitle("Gestor de Doação de Alimentos");
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
    
}
