import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;

public class ServerLogScreenController {
    Server serverConnection;
    int port;
    @FXML
    ListView<String> serverLog;


    void passData(int port){
        this.port=port;
        createServer();
    }
    public void createServer() {
        serverConnection = new Server(data -> {
            Platform.runLater(() -> {
                serverLog.getItems().add(data.toString());
            });
        },
                port);
//        System.out.println("Check: Finished creating server");

    }

    @FXML
    public void closeServer() {
        //debugging
//        if(serverConnection!=null){
//            System.out.println("Check2: Server is alive");
//        }
//        else{
//            System.out.println("Check2: Server is not alive");
//        }
        if(serverConnection != null) {
            serverConnection.close();
            serverLog.getItems().add("The server is closed.");
        }
    }

    @FXML
    void initialize(){

    }

}
