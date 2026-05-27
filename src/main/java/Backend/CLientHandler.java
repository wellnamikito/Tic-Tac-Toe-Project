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
    private GameSession session;
    private String nickname;

    public CLientHandler(Socket socket, TCPServer server){
        this.socket = socket;
        this.server = server;

        try{
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);
        } catch(IOException e){ e.printStackTrace();}
    }

    @Override
    public void run() {

        String message;

        try {

            while ((message = in.readLine()) != null) {

                System.out.println("Received: " + message);

                // =========================
                // NICKNAME
                // =========================

                if (message.startsWith("NICK")) {

                    String nick = message.substring(5);

                    setNickname(nick);

                    sendMessage("NICK_OK");

                    System.out.println(
                            "Nickname set: " + nickname
                    );

                    continue;
                }

                // =========================
                // GAME SESSION
                // =========================

                if (session != null) {

                    session.handleMessage(this, message);
                }
            }

        } catch (Exception e) {

            System.out.println("Client lost connection");

        } finally {

            if (session != null) {
                session.playerDisconnected(this);
            }

            closeConnection();
        }
    }
    public void sendMessage(String message){
        out.println(message);
    }
    public void setSession(GameSession session){ this.session = session;}

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
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
