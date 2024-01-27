import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.w3c.dom.Text;

public class JoinServerScreenController {
    GameState game;
    Client clientConnection;

    @FXML
    TextField nameField;
    @FXML
    TextField portField;
    @FXML
    Button startBtn;


    @FXML
    public void connectToServer(){//start button
        //try to start client connection
        try{
            clientConnection = new Client(dataFromServer->{
                Platform.runLater(()->{
                    game=(GameState) dataFromServer; //sets the client's gamestate to the gamestate updated from the server

                });
            }, Integer.parseInt(portField.getText()));
            clientConnection.start();
        }
        catch (Exception e){
            e.printStackTrace();
            System.out.println("Error: Something went wrong in creating a client connection");
        }

        try {//go to start of game which is selecting a category and pass the game and clientConnection Objects to the next controller
            Stage stage = (Stage) startBtn.getScene().getWindow();
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("CategorySelectionScreen.fxml"));
            Parent root=fxmlLoader.load();//must call this to load the fxml before getController() otherwise you'll have nullptr
            CategorySelectionScreenController controller=fxmlLoader.getController();

            controller.passData(game, clientConnection); //pass data to next scene

            Scene scene = new Scene(root, 800, 800);
            stage.setTitle("Client");
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error: Something went wrong in loading join server screen");
        }
    }

    /**
     * first function called when you get to a fxml layout (kinda like constructors, but it also has access to all the FXML stuff)
     */
    @FXML
    void initialize(){
        game=new GameState();
    }
}
