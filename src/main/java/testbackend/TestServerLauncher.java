package testbackend;

import Backend.TCPServer;

public class TestServerLauncher {

    public static void main(String[] args) {

        TCPServer server = new TCPServer();

        server.start();
    }
}