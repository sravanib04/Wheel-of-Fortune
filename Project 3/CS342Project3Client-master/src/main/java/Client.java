import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.util.function.Consumer;



public class Client extends Thread{//connects to the ClientThread on the Server from Client Side
    Socket socketClient;

    ObjectOutputStream out;
    ObjectInputStream in;
    int port;

    private Consumer<Serializable> callback;

    /**
     *
     * @param call
     * @param port
     */
    Client(Consumer<Serializable> call, int port){
        callback = call;//
        this.port=port;
    }

    static void printGameInfo(GameState gameState){
        System.out.println("GameState Info:");
        System.out.println("username="+gameState.username);
        System.out.println("currCategory="+gameState.currCategory);
        System.out.println("guessesLeft="+gameState.guessesLeft);
        System.out.println("currGuess="+gameState.currGuess);
        System.out.print("wordAttemptInCategory:{");
        for(Integer i: gameState.wordAttemptInCategory){
            System.out.print(i+" ");
        }
        System.out.println("}");

        System.out.print("wonCategory:{");
        for(boolean i: gameState.wonCategory){
            System.out.print(i+" ");
        }
        System.out.println("}");

        System.out.println("letterGuessed: "+gameState.letterGuessed);
        System.out.println("guessedWord: "+gameState.guessedWord);
    }

    public void run() {
        //Set up the input/output, so we can get and send info from the server
        try {
            socketClient= new Socket("localHost",port);
            out = new ObjectOutputStream(socketClient.getOutputStream());
            in = new ObjectInputStream(socketClient.getInputStream());
            socketClient.setTcpNoDelay(true);
        }
        catch(Exception e) {
            e.printStackTrace();
            System.out.println("Error: 1 Problem trying to set up input/outputs");
        }

        while(true) {//commented out for debugging purpose
            try {
                //receive data from server
                GameState gameInfo = (GameState) in.readObject(); //readObject() is a blocking call
                System.out.println("------------Game Info After reading from server in client------------");
                printGameInfo(gameInfo);
                callback.accept(gameInfo);//Sp
            }
            catch(Exception e) {
                e.printStackTrace();
                System.out.println("Error: Problem with receiving data from server");
            }
        }

    }

    void setCallback(Consumer<Serializable> callback){
        this.callback=callback;
    }

    /**
     * sends data to the server
     * @param data
     */
    public void send(GameState data) {
        try {
            out.writeObject(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}

