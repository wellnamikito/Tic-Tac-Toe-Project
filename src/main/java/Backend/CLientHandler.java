package Backend;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class CLientHandler implements Runnable {
    private Socket socket;
    private TCPServer server;
    private BufferedReader in;
    private PrintWriter out;

    public CLientHandler(Socket socket, TCPServer server){
        this.socket = socket;
        this.server = server;

        try{
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);
        } catch(IOException e){ e.printStackTrace();}
    }

    @Override
    public void run(){
        String message;

        try{
            while((message = in.readLine()) != null){
                System.out.println("Received: " + message);

                // пока просто рассылаем всем
                server.broadcast(message);
            }
        } catch (Exception e) {
            System.out.println("Client lost connection");
        } finally {
            closeConnection();
        }
    }
    public void sendMessage(String message){
        out.println(message);
    }

    private void closeConnection(){
        try{
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        server.removeClient(this);
    }
}
