package Backend;

import java.io.*;
import java.net.*;
import java.util.*;

public class TCPServer {
    private static final int PORT = 12345;
    private ServerSocket serverSocket;
    private List<CLientHandler> clients = new ArrayList<>();

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
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // отправка сообщения всем клиентам
    public void broadcast(String message){
        for(CLientHandler client : clients){
            client.sendMessage(message);
        }
    }

    // удаление клиента
    public void removeClient(CLientHandler client){
        clients.remove(client);
        System.out.println("Client disconnected");
    }
    // тест
    public static void main(String[] args) {
        new TCPServer().start();
    }
}
