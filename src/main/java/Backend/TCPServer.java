package Backend;

import java.io.*;
import java.net.*;
import java.util.*;

/**
 * TCPServer — основной серверный класс приложения.
 *
 * <p>Отвечает за:
 * <ul>
 *   <li>приём TCP-подключений от клиентов</li>
 *   <li>управление подключёнными пользователями</li>
 *   <li>формирование игровых сессий между игроками</li>
 *   <li>рассылку сообщений всем клиентам</li>
 *   <li>контроль лимитов сервера (макс. число игроков)</li>
 * </ul>
 *
 * <p>Логика работы:
 * сервер принимает клиентов, добавляет их в очередь ожидания,
 * и автоматически создаёт GameSession, когда набираются 2 игрока.
 */
public class TCPServer {

    /**
     * Менеджер конфигурации сервера.
     * Используется для получения:
     * - порта сервера
     * - максимального числа клиентов
     * - размера игрового поля
     */
    private ConfigManager config;

    /**
     * Основной TCP-серверный сокет, принимающий подключения клиентов.
     */
    private ServerSocket serverSocket;

    /**
     * Список всех активных подключённых клиентов.
     */
    private List<CLientHandler> clients = new ArrayList<>();

    /**
     * Очередь игроков, ожидающих начала игры.
     * Когда набирается 2 игрока — создаётся GameSession.
     */
    private List<CLientHandler> waitingPlayers = new ArrayList<>();

    /**
     * Запуск TCP-сервера.
     *
     * <p>Выполняет:
     * <ul>
     *   <li>загрузку конфигурации</li>
     *   <li>запуск ServerSocket</li>
     *   <li>ожидание подключений клиентов</li>
     *   <li>создание игровых сессий при наличии 2 игроков</li>
     * </ul>
     *
     * <p>Работает в бесконечном цикле.
     */
    public void start() {
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

                // Проверка лимита клиентов
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

                // Если есть 2 игрока — создаём игровую сессию
                if (waitingPlayers.size() >= 2) {
                    CLientHandler p1 = waitingPlayers.remove(0);
                    CLientHandler p2 = waitingPlayers.remove(0);

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

    /**
     * Отправляет сообщение всем подключённым клиентам.
     *
     * @param message текст сообщения
     */
    public void broadcast(String message) {
        for (CLientHandler client : clients) {
            client.sendMessage(message);
        }
    }

    /**
     * Удаляет клиента из системы при отключении.
     *
     * @param client клиент, который отключился
     */
    public void removeClient(CLientHandler client) {
        clients.remove(client);
        waitingPlayers.remove(client);
        System.out.println("Client disconnected. Remaining clients: " + clients.size());
    }

    /**
     * Точка входа в серверное приложение.
     */
    public static void main(String[] args) {
        new TCPServer().start();
    }
}