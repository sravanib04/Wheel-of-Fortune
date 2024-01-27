import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;

import java.awt.event.ActionEvent;

public class CategorySelectionScreenController {
    GameState game;
    Client clientConnection;
    @FXML
    RadioButton celebrityCategory;
    @FXML
    RadioButton gameCategory;
    @FXML
    RadioButton carCategory;

    @FXML
    ToggleGroup categorySelectionGroup;

    public void passData(GameState game, Client clientConnection){
        this.game=game;
        this.clientConnection=clientConnection;
    }

    public void goToGuessScreen(){
        try {//go to start of game which is selecting a category and pass the game and clientConnection Objects to the next controller
            Stage stage = (Stage) celebrityCategory.getScene().getWindow();//just needed to get the stage of any element didn't have to be this specific category
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("GuessScreen.fxml"));

            Parent root=fxmlLoader.load();//must call this to load the fxml before getController() otherwise you'll have nullptr
            GuessScreenController controller=fxmlLoader.getController();
            controller.passData(game, clientConnection); //pass data to next scene

            Scene scene = new Scene(root, 800, 800);
            stage.setTitle("Client");
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error: Something went wrong in loading guess screen");
        }
    }

    @FXML
    public void onSelectCategory() {
        RadioButton selected = (RadioButton) categorySelectionGroup.getSelectedToggle();
        if (selected == celebrityCategory) {
            game.currCategory = "Celebrities";//TODO should i update word attempt here?
        } else if (selected == gameCategory) {
            game.currCategory = "Games";
        } else if (selected == carCategory) {
            game.currCategory = "Cars";
        }
        clientConnection.send(game);
        //must redefine the callback function so that only when the client receives the data from the server it moves onto the guess screen, otherwise it'll move onto the guess screen before the data loads
        clientConnection.setCallback(dataFromServer -> {
            Platform.runLater(() -> {
                game = (GameState) dataFromServer; //sets the client's gamestate to the gamestate updated from the server
//                if (game.currGuess.equals("")) {
//                    System.out.println("game is not updating");
//                } else {
//                    System.out.println("game.currGuess=" + game.currGuess);
//                }

                System.out.println("C: From server currentGuess =" + game.currGuess);
                //move directly to the guessScreen or let them change their mind?
                goToGuessScreen();
            });
        });
    }

    @FXML
    public void initialize(){

    }
}
