import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.function.Consumer;

public class Server{
    int port;
    int count = 1;
    ArrayList<ClientThread> clientList = new ArrayList<ClientThread>();
    ClientListenerThread server;//why do we have to create ClientListenerThread can't we just extend Thread on Server?
    private Consumer<Serializable> callback;//why do we want callback instead of just defining a function?
    private boolean isRunning;
    private ServerSocket serverSocket;

    Server(Consumer<Serializable> call, int port){//TODO again figure out what this does
        callback = call;//adds a message to the list view
        this.port = port;
        server = new ClientListenerThread();
        server.start();
    }

    public void close() {
        isRunning = false;
        try {
            if(serverSocket != null && !serverSocket.isClosed()) {//TODO: we don't actually use the global serverSocket so i don't think this will work properly?
                serverSocket.close();
            }
            for (ClientThread clientThread : clientList) {
                if (clientThread != null) {
                    clientThread.close();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public class ClientListenerThread extends Thread{

        //gets the clientList and adds them into arraylist to keep track of them
        public void run() {

            try(ServerSocket serverSocket = new ServerSocket(port)){
                System.out.println("Server is waiting for a client!");

                while(true) {
                    ClientThread c = new ClientThread(serverSocket.accept(), count);
                    callback.accept("client has connected to server: " + "client #" + count);
                    clientList.add(c);
                    c.start();
                    count++;
                }
            }//end of try
            catch(Exception e) {
                callback.accept("Server socket did not launch");
            }
        }//end of while
    }


    class ClientThread extends Thread{//connects to the Client from the server side
        Socket connection;
        String name;
        int count;
        GameLogic gameProcess;
        ObjectInputStream in;
        ObjectOutputStream out;
        GameState game;

        ClientThread(Socket s, int count){
            this.connection = s;
            this.count = count;
            game=new GameState();
            gameProcess=new GameLogic();
        }

        public void setUserName(String name){
            this.name=name;
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

        public void close() {
            try {
                if(connection != null && !connection.isClosed()) {
                    connection.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        public void run(){//treat as main method
            try {
                in = new ObjectInputStream(connection.getInputStream());
                out = new ObjectOutputStream(connection.getOutputStream());
                connection.setTcpNoDelay(true);
            }
            catch(Exception e) {
                System.out.println("Streams not open or new game could not be created");
            }

            try{
                this.out.writeObject(game);//sends game to client
//                System.out.println("Check:Sent initial game to client");
            } catch (IOException e){
                System.out.println("Error: Could not send initial game to client");
            }

            while(true) {
                try {
                    game = (GameState) in.readObject();
                    callback.accept("Server received data from client "+count);
                    gameProcess.updateGameState(game);//an exception???
                    this.out.writeObject(game);//updates the game info

//                    System.out.println("------------Client"+count+" Game before writing to client------------");
//                    printGameInfo(game);
                    callback.accept("Server sent data to client "+count);
                }
                catch(Exception e) {
                    e.printStackTrace();
                    callback.accept("Client: " + count + " has disconnected");
                    clientList.remove(this);
                    break;
                }
            }
        }//end of run


    }//end of client thread
}






