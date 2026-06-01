package Backend;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class TCPServer {

    private ConfigManager config;
    private ServerSocket serverSocket;

    private final List<CLientHandler> clients = new ArrayList<>();

    private final List<CLientHandler> waiting3x3 = new ArrayList<>();
    private final List<CLientHandler> waiting9x9 = new ArrayList<>();

    public void start() {

        config = new ConfigManager();
        int port = config.getPort();

        try {

            serverSocket = new ServerSocket(port);

            System.out.println("=== TCP SERVER STARTED ===");
            System.out.println("Port: " + port);
            System.out.println("==========================");

            while (true) {

                Socket clientSocket = serverSocket.accept();

                System.out.println("New client: " + clientSocket.getInetAddress());

                if (clients.size() >= config.getMaxClients()) {
                    System.out.println("Server full");
                    clientSocket.close();
                    continue;
                }

                CLientHandler client = new CLientHandler(clientSocket, this);

                clients.add(client);
                new Thread(client).start();

                System.out.println("Client connected. Total: " + clients.size());
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public synchronized void addWaitingPlayer(CLientHandler client) {

        int boardSize = client.getSelectedBoardSize();

        if (boardSize == 3) {

            waiting3x3.add(client);

            client.sendMessage("WAITING_FOR_OPPONENT");

            System.out.println(
                    client.getNickname() + " entered 3x3 queue"
            );

            if (waiting3x3.size() >= 2) {

                CLientHandler p1 = waiting3x3.remove(0);
                CLientHandler p2 = waiting3x3.remove(0);

                GameSession session = new GameSession(p1, p2, 3);

                // ❗ FIX: старт только здесь
                session.startGame();

                System.out.println("3x3 session started");
            }

        } else {

            waiting9x9.add(client);

            client.sendMessage("WAITING_FOR_OPPONENT");

            System.out.println(
                    client.getNickname() + " entered 9x9 queue"
            );

            if (waiting9x9.size() >= 2) {

                CLientHandler p1 = waiting9x9.remove(0);
                CLientHandler p2 = waiting9x9.remove(0);

                GameSession session = new GameSession(p1, p2, 9);

                session.startGame();

                System.out.println("9x9 session started");
            }
        }
    }

    public synchronized void removeClient(CLientHandler client) {

        clients.remove(client);
        waiting3x3.remove(client);
        waiting9x9.remove(client);

        System.out.println("Client disconnected. Remaining: " + clients.size());
    }

    public void broadcast(String message) {
        for (CLientHandler c : clients) {
            c.sendMessage(message);
        }
    }

    public static void main(String[] args) {
        new TCPServer().start();
    }
}