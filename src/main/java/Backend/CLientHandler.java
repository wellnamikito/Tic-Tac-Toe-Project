package Backend;

import java.io.*;
import java.net.Socket;

public class CLientHandler implements Runnable {

    private Socket socket;
    private int selectedBoardSize = 3;

    private TCPServer server;

    private BufferedReader in;
    private PrintWriter out;

    private GameSession session;
    private String nickname;

    private boolean readySent = false;
    private boolean nickSet = false;

    public CLientHandler(Socket socket, TCPServer server){
        this.socket = socket;
        this.server = server;

        try{
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);
        } catch(IOException e){
            e.printStackTrace();
        }
    }

    public int getSelectedBoardSize() {
        return selectedBoardSize;
    }

    @Override
    public void run() {

        String message;

        try {

            while ((message = in.readLine()) != null) {

                System.out.println("Received: " + message);

                if (message.startsWith("NICK")) {
                    nickname = message.substring(5);
                    nickSet = true;
                    sendMessage("NICK_OK");
                    continue;
                }

                if (message.startsWith("MODE")) {

                    try {
                        int size = Integer.parseInt(message.substring(5));

                        if (size == 3 || size == 9) {
                            selectedBoardSize = size;
                            sendMessage("MODE_OK");
                        } else {
                            sendMessage("INVALID_MODE");
                        }

                    } catch (Exception e) {
                        sendMessage("INVALID_MODE");
                    }

                    continue;
                }

                if (message.equals("READY")) {

                    // ❗ FIX: нельзя в очередь без ника
                    if (!nickSet) {
                        sendMessage("NO_NICK");
                        continue;
                    }

                    if (!readySent) {
                        readySent = true;
                        server.addWaitingPlayer(this);
                    }

                    continue;
                }

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

    public void setSession(GameSession session){
        this.session = session;
    }

    public String getNickname() {
        return nickname;
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