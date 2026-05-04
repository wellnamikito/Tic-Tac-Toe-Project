package Backend;

import java.io.*;
import java.net.*;
import java.util.*;

public class TCPServer {
    private static final int PORT = 12345;
    private ServerSocket serverSocket;
    private List<CLientHandler> clients = new ArrayList<>();
    private List<CLientHandler> waitingPlayers = new ArrayList<>();

    public void start(){
        try{
            serverSocket = new ServerSocket(PORT);
            System.out.println("Server started on port " + PORT);

            while(true){
                Socket clientSocket = serverSocket.accept();
                System.out.println("New client connected " + clientSocket);

                // 🔴 проверка лимита
                if (clients.size() >= 2) {
                    System.out.println("Connection rejected: too many players");
                    clientSocket.close();
                    continue;

                }

                CLientHandler client = new CLientHandler(clientSocket, this);
                clients.add(client);
                new Thread(client).start();
                waitingPlayers.add(client);
                // если 2 игрока — создаём сессию
                if (waitingPlayers.size() >= 2) {
                    CLientHandler p1 = waitingPlayers.remove(0);
                    CLientHandler p2 = waitingPlayers.remove(0);

                    new GameSession(p1, p2);

                    System.out.println("Game session started!");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // отправка сообщения всем клиентам (может пригодиться для дебага)
    public void broadcast(String message){
        for(CLientHandler client : clients){
            client.sendMessage(message);
        }
    }

    // удаление клиента
    public void removeClient(CLientHandler client){
        clients.remove(client);
        waitingPlayers.remove(client);
        System.out.println("Client disconnected");
    }
    // тест
    public static void main(String[] args) {
        new TCPServer().start();
    }
}
