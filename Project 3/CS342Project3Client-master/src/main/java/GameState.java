import java.io.Serializable;
import java.util.ArrayList;

public class GameState implements Serializable {//only fields and no methods
    String username;
    String currCategory;
    int guessesLeft;
    String currGuess; //Ex="K_L__ ______"
    ArrayList<Integer> wordAttemptInCategory;;//Index Key: 0=celebrity category attempts, 1=games category attempts, 2=cars category attempts
    ArrayList<Boolean> wonCategory;//Index Key: 0=celebrity category attempts, 1=games category attempts, 2=cars category attempts
    char letterGuessed;//letters the user has already guessed
    String guessedWord;//if they guess a word, it'll go here
//    String request="";//when the server sending something, note when
    GameState(){
        username="";
        guessesLeft=0;
        currGuess="";
        guessedWord="";
        wordAttemptInCategory=new ArrayList<>();
        wordAttemptInCategory.add(0);
        wordAttemptInCategory.add(0);
        wordAttemptInCategory.add(0);
        wonCategory=new ArrayList<>();
        wonCategory.add(false);
        wonCategory.add(false);
        wonCategory.add(false);
    }
}
