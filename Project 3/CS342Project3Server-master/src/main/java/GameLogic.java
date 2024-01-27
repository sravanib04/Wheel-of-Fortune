import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GameLogic {
    private String selectedCategory;
    private ArrayList<String> celebritiesList;
    private ArrayList<String> gamesList;
    private ArrayList <String> carsList;
    ArrayList<Integer> wordAttemptInCategory;//Index Key: 0=celebrity category attempts, 1=games category attempts, 2=cars category attempts
    private String wordSelected;
    private int guessesLeft;

    //word options from category

    /**
     * Constructor
     */
    public GameLogic(){
        //add the words into the category lists
        celebritiesList = new ArrayList<>(List.of("Kim Kardashian", "Shawn Mendes", "Charlie Puth", "Taylor Swift", "Kendall Jenner", "Taehyung", "Timothee Chalamet", "Chris Evans", "Harry Styles", "Tuba"));
        gamesList = new ArrayList<>(List.of("Monopoly", "Uno", "Scrabble", "Eight ball", "Poker dice", "Call of Duty", "Grand Theft Auto", "Bingo", "Pinball", "Bank craps"));
        carsList = new ArrayList<>(List.of("Audi", "Rolls Royce", "BMW", "Honda", "Kia", "Toyota", "Tesla", "Chevrolet", "Ferrari", "Alfa Romeo"));
        selectedCategory="";
        wordAttemptInCategory=new ArrayList<>(3);
        wordAttemptInCategory.add(0);
        wordAttemptInCategory.add(0);
        wordAttemptInCategory.add(0);
        guessesLeft=0;
        wordSelected="";
    }

    //

    /**
     * returns the mystery version of the word
     *
     * @param word
     * @return word without the letters
     *
     * Ex: Monopoly="________"
     */
    public String getGuessString(String word){
        String guessStr="";
        for(int i=0; i<word.length(); i++){
            if(word.charAt(i)==' '){
                guessStr+=" ";
            }
            else{
                guessStr+="_";
            }
        }
        return guessStr;
    }

    /**
     * gets the current state of the game and then update it to its next state based on the info we get
     * @param gameState
     */
    public void updateGameState(GameState gameState){
        System.out.println("in updateGameState");
        if(wordSelected.isBlank()){//the user is starting a new round
            if(gameState.currCategory.equals("Celebrities")){
                selectedCategory="Celebrities";
                wordSelected=celebritiesList.get(gameState.wordAttemptInCategory.get(0));
            }
            else if(gameState.currCategory.equals("Games")){
                selectedCategory="Games";
                wordSelected=celebritiesList.get(gameState.wordAttemptInCategory.get(1));
            }
            else if(gameState.currCategory.equals("Cars")){
                selectedCategory="Cars";
                wordSelected=celebritiesList.get(gameState.wordAttemptInCategory.get(1));
            }
            gameState.currGuess=getGuessString(wordSelected);
            System.out.println("Updated game info");
        }
    }

    /**
     * sets the category
     * @param selectedCategory
     */
    public void setSelectedCategory(String selectedCategory){
        this.selectedCategory=selectedCategory;
    }

    /**
     * @return selected category
     */
    public String getSelectedCategory() {
        return selectedCategory;
    }

    /**
     * shuffle the selected category's words
     * @return
     */
    public void shuffleCategory(){
        if(selectedCategory.equalsIgnoreCase("celebrities")) {
            Collections.shuffle(celebritiesList);
        }
        else if(selectedCategory.equalsIgnoreCase("games")) {
            Collections.shuffle(gamesList);
        }
        else if(selectedCategory.equalsIgnoreCase("cars")) {
            Collections.shuffle(carsList);
        }
    }

    /**
     * sets to the word the user is guessing to the next word in the
     * category List (prevents getting the same word twice in a row)
     *
     * will go to the beginning of the list when it reaches the end of the list
     */
    public void setWord(){
        if(selectedCategory.equalsIgnoreCase("celebrities")) {
            int currentIndex = celebritiesList.indexOf(wordSelected);
            if(currentIndex == celebritiesList.size()-1) {
                wordSelected = celebritiesList.get(0);
            }
            else {
                wordSelected = celebritiesList.get(currentIndex + 1);
            }
        }
        if(selectedCategory.equalsIgnoreCase("games")) {
            int currentIndex = gamesList.indexOf(wordSelected);
            if(currentIndex == gamesList.size()-1) {
                wordSelected = gamesList.get(0);
            }
            else {
                wordSelected = gamesList.get(currentIndex + 1);
            }
        }
        if(selectedCategory.equalsIgnoreCase("cars")) {
            int currentIndex = carsList.indexOf(wordSelected);
            if(currentIndex == carsList.size()-1) {
                wordSelected = carsList.get(0);
            }
            else {
                wordSelected = carsList.get(currentIndex + 1);
            }
        }
    }

    /**
     * sets the word the user is guessing to the specified word
     * @param word
     */
    public void setWord(String word){
        wordSelected = word;
    }

    /**
     * checks if the letter the user is guessing is in the word
     * @param letter guess
     * @return the indices where the letter occurs or null if the letter is not found in the word
     */
    public ArrayList<Integer> isValidGuess(char letter){
        ArrayList<Integer> indices = new ArrayList<>();
        for(int i=0; i < wordSelected.length(); i++) {
            if (wordSelected.charAt(i) == letter) {
                indices.add(i);
            }
        }
        if(indices.isEmpty()) {
            return null;
        }
        return indices;
    }

    /**
     * checks if the word the user is guessing is correct
     * @param word guess
     * @return true=correct word, false=incorrect word
     */
    public boolean isValidGuess(String word){
        if(wordSelected.equalsIgnoreCase(word)) {
            return true;
        }
        return false;
    }

    /**
     * resets everything to play another round w/ a different word selected
     */
    public void resetRound(){
        wordSelected = null;
        guessesLeft = 6;
    }

    private int getCategoryIndex(String category) {
        if (category.equalsIgnoreCase("celebrities")) {
            return 0;
        } else if (category.equalsIgnoreCase("games")) {
            return 1;
        } else if (category.equalsIgnoreCase("cars")) {
            return 2;
        } else {
            return -1;
        }
    }


    /**
     * checks if the game should be over
     * @param wins
     * @return 0=game not over, 1=(LOST)game over because they failed 3 words in a category, 2=(WIN) game over because they've guessed a word in each category
     */
    public int gameOver(int wins){
       int selectedCategoryIndex = getCategoryIndex(selectedCategory);
       if(wordAttemptInCategory.get(selectedCategoryIndex) == 3) {
           return 1;
       }
       else if(wins == 3) {
           return 2;
       }
       else {
           return 0;
       }
    }
}
