package co.edu.uptc.presenter;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import co.edu.uptc.model.Enterprise;
import co.edu.uptc.persistence.Persistence;

public class Presenter {

    private final int PORT = 1234;
    private ServerSocket serverSocket;
    private Enterprise enterprise;
    private Persistence persistence;
    private static String JSON_FILE = "data/HumanResources.json";

    public Presenter() throws IOException {
        serverSocket = new ServerSocket(PORT);
        persistence = new Persistence(JSON_FILE);
        enterprise = new Enterprise(persistence.loadTeams());
    }

    public void runServer() throws IOException {
        while (true) {
            Socket socket = serverSocket.accept();
            ClientThread clientThread = new ClientThread(socket, enterprise, persistence);
            clientThread.start();
        }
    }

    public static void main(String[] args) {
        try {
            Presenter presenter = new Presenter();
            System.out.println("Server running on port " + presenter.PORT);
            presenter.runServer();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
