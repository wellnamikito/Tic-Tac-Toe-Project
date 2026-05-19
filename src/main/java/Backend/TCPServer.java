package Backend;

import java.io.*;
import java.net.*;
import java.util.*;

public class TCPServer {
    private ConfigManager config; // Менеджер настроек
    private ServerSocket serverSocket;
    private List<CLientHandler> clients = new ArrayList<>();
    private List<CLientHandler> waitingPlayers = new ArrayList<>();

    public void start() {
        // Инициализируем настройки перед запуском сервера
        config = new ConfigManager();
        int port = config.getPort();

        try {
            serverSocket = new ServerSocket(port);
            System.out.println("=== TCP SERVER STARTED ===");
            System.out.println("Port: " + port);
            System.out.println("Max Clients: " + config.getMaxClients());
            System.out.println("Game Board Size: " + config.getBoardSize() + "x" + config.getBoardSize());
            System.out.println("==========================");

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("New client attempting to connect: " + clientSocket.getInetAddress());

                // Проверка лимита клиентов из конфига
                if (clients.size() >= config.getMaxClients()) {
                    System.out.println("Connection rejected: Server is full (" + config.getMaxClients() + ")");
                    clientSocket.close();
                    continue;
                }

                CLientHandler client = new CLientHandler(clientSocket, this);
                clients.add(client);
                new Thread(client).start();

                waitingPlayers.add(client);
                System.out.println("Client connected. Total active clients: " + clients.size());

                // Если набралось 2 игрока — создаём сессию
                if (waitingPlayers.size() >= 2) {
                    CLientHandler p1 = waitingPlayers.remove(0);
                    CLientHandler p2 = waitingPlayers.remove(0);

                    // Передаем размер поля из конфигурации
                    int boardSize = config.getBoardSize();
                    new GameSession(p1, p2, boardSize);

                    System.out.println("Game session started with board size " + boardSize + "!");
                }
            }
        } catch (IOException e) {
            System.err.println("Critical server error: " + e.getMessage());
            e.printStackTrace();
        }
    }


     // Отправка сообщения всем подключенным клиентам.
    public void broadcast(String message) {
        for (CLientHandler client : clients) {
            client.sendMessage(message);
        }
    }

    // Удаление клиента из списков при отключении.

    public void removeClient(CLientHandler client) {
        clients.remove(client);
        waitingPlayers.remove(client);
        System.out.println("Client disconnected. Remaining clients: " + clients.size());
    }

    public static void main(String[] args) {
        new TCPServer().start();
    }
}
