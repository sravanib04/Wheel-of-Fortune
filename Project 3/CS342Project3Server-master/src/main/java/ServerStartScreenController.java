import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.ServerSocket;
import java.security.spec.ECField;

public class ServerStartScreenController {
    Server serverConnection;

    @FXML
    TextField portInput;

    @FXML
    ListView<String> serverLog;

    @FXML
    Button createServerBtn;


    //HELPER FUNCTIONS
    public void goToServerLogScreen(ActionEvent event) {
        if (event.getSource() == createServerBtn) {
            try {
                Stage stage = (Stage) createServerBtn.getScene().getWindow();
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("ServerLogScreen.fxml"));
                Parent root=fxmlLoader.load();
                ServerLogScreenController controller= fxmlLoader.getController();
                controller.passData(Integer.parseInt(portInput.getText().toString()));
                Scene scene = new Scene(root, 800, 800);
                stage.setTitle("Server");
                stage.setScene(scene);
                stage.show();
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Error: Something went wrong in loading server log screen");
            }

        }

    }
}