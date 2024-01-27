import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class StartScreenController {
    @FXML
    Button joinServerBtn;
    @FXML
    public void goToJoinServerScreen(){ //pLay button
        try {
            Stage stage = (Stage) joinServerBtn.getScene().getWindow();
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("JoinServerScreen.fxml"));

            Scene scene = new Scene(fxmlLoader.load(), 800, 800);
            stage.setTitle("Client");
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error: Something went wrong in loading join server screen");
        }
    }

    void initialize(){
        //first things you want to happen when this screen is loaded
    }
}
