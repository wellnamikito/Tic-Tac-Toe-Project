package network;

import java.io.*;
import java.net.Socket;
import java.util.function.Consumer;

public class TCPClient {

    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;

    private Consumer<String> messageHandler;

    public TCPClient(String host, int port, Consumer<String> handler) {

        try {
            this.socket = new Socket(host, port);
            this.out = new PrintWriter(socket.getOutputStream(), true);
            this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            this.messageHandler = handler;

            new Thread(this::listen).start();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void listen() {

        try {
            String msg;

            while ((msg = in.readLine()) != null) {
                messageHandler.accept(msg);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void send(String msg) {
        out.println(msg);
    }

    public void close() {
        try {
            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}