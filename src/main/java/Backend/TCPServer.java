package Backend;

import java.io.*;
import java.net.*;
import java.util.*;

/**
 * TCPServer — основной серверный класс приложения.
 *
 * 📌 Отвечает за:
 * - приём TCP-подключений
 * - управление клиентами
 * - очередь ожидания игроков
 * - создание игровых сессий
 *
 * 📌 Логика:
 * Клиенты подключаются → выбирают MODE → отправляют READY →
 * попадают в очередь → сервер создаёт GameSession.
 */
public class TCPServer {

    private ConfigManager config;

    private ServerSocket serverSocket;

    private List<CLientHandler> clients = new ArrayList<>();

    /**
     * Очередь игроков, готовых к игре
     */
    private final List<CLientHandler> waitingPlayers = new ArrayList<>();

    public void start() {

        config = new ConfigManager();
        int port = config.getPort();

        try {
            serverSocket = new ServerSocket(port);

            System.out.println("=== TCP SERVER STARTED ===");
            System.out.println("Port: " + port);
            System.out.println("Max Clients: " + config.getMaxClients());
            System.out.println("==========================");

            while (true) {

                Socket clientSocket = serverSocket.accept();

                System.out.println("New client: " + clientSocket.getInetAddress());

                if (clients.size() >= config.getMaxClients()) {
                    System.out.println("Server full, connection rejected");
                    clientSocket.close();
                    continue;
                }

                CLientHandler client = new CLientHandler(clientSocket, this);

                clients.add(client);
                new Thread(client).start();

                System.out.println("Client connected. Total: " + clients.size());
            }

        } catch (IOException e) {
            System.err.println("Server error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Добавление игрока в очередь после READY
     */
    public synchronized void addWaitingPlayer(CLientHandler client) {

        waitingPlayers.add(client);

        System.out.println("Player added to queue: " + client.getNickname());

        if (waitingPlayers.size() >= 2) {

            CLientHandler p1 = waitingPlayers.remove(0);
            CLientHandler p2 = waitingPlayers.remove(0);

            int boardSize = p1.getSelectedBoardSize();

            GameSession session = new GameSession(p1, p2, boardSize);

            System.out.println("Game session started (" + boardSize + "x" + boardSize + ")");
        }
    }

    /**
     * Удаление клиента
     */
    public void removeClient(CLientHandler client) {

        clients.remove(client);
        waitingPlayers.remove(client);

        System.out.println("Client disconnected. Remaining: " + clients.size());
    }

    /**
     * Broadcast всем (если понадобится)
     */
    public void broadcast(String message) {
        for (CLientHandler c : clients) {
            c.sendMessage(message);
        }
    }

    public static void main(String[] args) {
        new TCPServer().start();
    }
}