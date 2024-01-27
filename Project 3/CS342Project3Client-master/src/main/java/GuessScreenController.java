import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import java.util.ArrayList;
import java.util.HashMap;

public class GuessScreenController {
    GameState game;
    Client clientConnection;

    ArrayList<TextField> letterBoxes;

    @FXML
    VBox keyboardWrapper;

    @FXML
    FlowPane lettersDisplayWrapper;
    HashMap<Character, Button> letterKeyMap;

    @FXML
    BorderPane guessScreenContainer;

    @FXML
    Label msgDisplay;

    public void passData(GameState game, Client clientConnection){
        this.game=game;
        this.clientConnection=clientConnection;
        if(clientConnection==null){
            System.out.println("clientConnection==null");
        }
        createWordsDisplay(game.currGuess);
        createKeyboard();
    }
    //HELPER FUNCTIONS

    /**
     *  dynamically creates the beginnning blank display for the words (the white boxes)
     * @param template Ex: "________ ___"
     */
    public void createWordsDisplay(String template){
        System.out.println("Template is "+template);
        HBox word=new HBox();
        for(int i=0; i<template.length(); i++){
            TextField newLetterBox=new TextField();
            newLetterBox.setEditable(false);
            newLetterBox.setId("letterBox");
            if(template.charAt(i)==' '){
                lettersDisplayWrapper.getChildren().add(word);
                word=new HBox();
                newLetterBox.setVisible(false);
//                lettersDisplayWrapper.getChildren().add(word);
            }
            else{
                word.getChildren().add(newLetterBox);
            }
            letterBoxes.add(newLetterBox);
        }
        lettersDisplayWrapper.getChildren().add(word);
    }
    /**
     * updates the display word to reflect the user's guesses
     * @param currGuess the string representing what letters the user has guessed already
     *
     * Ex: In the beginning currGuess="_____ ______" but as more letters are guessed, currGuess="K__l_ ______"
     */
    public void updateWordsDisplay(String currGuess){//must have called createWordDisplay() before this
        for(int i=0; i<currGuess.length();i++){
            if(currGuess.charAt(i)!='_' && currGuess.charAt(i)!=' '){
                letterBoxes.get(i).setText(currGuess.charAt(i)+"");
            }
        }

    }

    /**
     * dynamically creates the keyboard (disable buttons for letters already guessed)
     */
    public void createKeyboard(){
        String qwertyRow1="QWERTYUIOP";
        String qwertyRow2="ASDFGHJKL";
        String qwertyRow3="ZXCVBNM";

        HBox row1=new HBox();
        HBox row2=new HBox();
        HBox row3=new HBox();

        for(int i=0; i<qwertyRow1.length(); i++){
            Button newKey=new Button(qwertyRow1.charAt(i)+"");
            HBox.setMargin(newKey, new Insets(5));
            newKey.setId("key");
            newKey.setOnAction(new EventHandler<javafx.event.ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    guessALetter(actionEvent);
                }
            });
            row1.getChildren().add(newKey);
            letterKeyMap.put(qwertyRow1.charAt(i), newKey);
        }
        for(int i=0; i<qwertyRow2.length(); i++){
            Button newKey=new Button(qwertyRow2.charAt(i)+"");
            HBox.setMargin(newKey, new Insets(5));newKey.setOnAction(new EventHandler<javafx.event.ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    guessALetter(actionEvent);
                }
            });
            newKey.setId("key");
            row2.getChildren().add(newKey);
            letterKeyMap.put(qwertyRow2.charAt(i), newKey);
        }
        for(int i=0; i<qwertyRow3.length(); i++){
            Button newKey=new Button(qwertyRow3.charAt(i)+"");
            HBox.setMargin(newKey, new Insets(5));newKey.setOnAction(new EventHandler<javafx.event.ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    guessALetter(actionEvent);
                }
            });
            newKey.setId("key");
            row3.getChildren().add(newKey);
            letterKeyMap.put(qwertyRow3.charAt(i), newKey);
        }
        keyboardWrapper.getChildren().addAll(row1, row2, row3);
        VBox.setMargin(row1, new Insets(5));

        //Use buttons
        //put into array for easy access?
    }

    /**
     * used to show messages to the user (ex: "G is not in the word, 3 Guesses Left")
     * @param msg
     */
    void showMessage(String msg){
        msgDisplay.setText(msg);
        msgDisplay.setVisible(true);
    }

    /**
     * hides the message (Ex: take away "G is not in the word, 3 Guesses Left")
     */
    void hideMessage(){
        msgDisplay.setVisible(false);
    }

    public void updateScreen(GameState game){
        //update the game to the server's response?
    }

    //METHODS THAT DIRECTLY RESPOND TO BUTTON PRESSES!
    @FXML
    public void guessALetter(ActionEvent e){//anytime one of the letter keys are pressed
        TextField letterGuessedBox=(TextField) (e.getSource());
        char letterGuessed=letterGuessedBox.getText().toString().charAt(0);
        letterBoxes.get(letterGuessed-'a').setDisable(true);
        //do we clear everything else in game, how does the server know what info we need them to react to?
        //should whenever the server gets the game object, after it uses the info, set that field back to default values?
        game.letterGuessed=letterGuessed;
        clientConnection.send(game);
        updateScreen(game);
    }

    @FXML
    public void showSolvePopup(){//when solve is pressed

    }

    @FXML
    public void hideSolvePopup(){//usually when you've guessed incorrectly a word but you still have more guesses or pressed X

    }

    @FXML
    public void solveWord(){//when next is pressed in the solve pop up

    }

    /**
     * first thing that the controller does (kinda like constructor)
     */
    @FXML
    void initialize(){
        letterBoxes=new ArrayList<>();
        letterKeyMap=new HashMap<>();
        //reupdate callBack
    }
}
