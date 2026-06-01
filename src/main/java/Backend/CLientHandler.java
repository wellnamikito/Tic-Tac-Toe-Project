package Backend;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * CLientHandler — обработчик одного подключённого клиента.
 *
 * <p>📌 Отвечает за:
 * <ul>
 *   <li>приём сообщений от клиента (TCP)</li>
 *   <li>отправку сообщений клиенту</li>
 *   <li>обработку ника пользователя</li>
 *   <li>передачу игровых сообщений в GameSession</li>
 *   <li>обработку отключения клиента</li>
 * </ul>
 *
 * <p>📡 Протокол сообщений от клиента:
 * <ul>
 *   <li>NICK name — установка ника</li>
 *   <li>MOVE x y — ход в игре</li>
 *   <li>CHAT_ALL text — сообщение в общий чат</li>
 *   <li>CHAT_PRIVATE nick text — личное сообщение</li>
 *   <li>RESTART — перезапуск игры</li>
 * </ul>
 *
 * <p>📡 Сообщения от сервера:
 * <ul>
 *   <li>NICK_OK — ник успешно установлен</li>
 *   <li>STATE — состояние игрового поля</li>
 *   <li>TURN YOUR / TURN WAIT — управление ходом</li>
 *   <li>WIN / LOSE / DRAW — результат игры</li>
 *   <li>INVALID — некорректный ход</li>
 *   <li>NOT YOUR TURN — попытка ходить вне очереди</li>
 *   <li>OPPONENT_LEFT — соперник отключился</li>
 * </ul>
 */
public class CLientHandler implements Runnable {

    /**
     * TCP-соединение с клиентом.
     */
    private Socket socket;

    private int selectedBoardSize = 3;

    /**
     * Ссылка на основной сервер.
     */
    private TCPServer server;

    /**
     * Поток входящих сообщений от клиента.
     */
    private BufferedReader in;

    /**
     * Поток исходящих сообщений клиенту.
     */
    private PrintWriter out;

    /**
     * Игровая сессия, к которой подключён клиент.
     */
    private GameSession session;

    /**
     * Никнейм игрока.
     */
    private String nickname;

    /**
     * Создаёт обработчик клиента и инициализирует I/O потоки.
     *
     * @param socket TCP-соединение клиента
     * @param server ссылка на сервер
     */
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

    /**
     * @return выбранный пользователем размер поля
     */
    public int getSelectedBoardSize() {
        return selectedBoardSize;
    }

    /**
     * Основной цикл обработки сообщений клиента.
     *
     * <p>📌 Логика:
     * <ul>
     *   <li>читает сообщения из сокета</li>
     *   <li>обрабатывает установку ника</li>
     *   <li>передаёт игровые команды в GameSession</li>
     *   <li>обрабатывает отключение клиента</li>
     * </ul>
     */
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

                    System.out.println("Nickname set: " + nickname);

                    continue;
                }

                // =========================
                // MODE SELECTION (3 or 9)
                // =========================
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

                // =========================
                // READY (enter queue)
                // =========================
                if (message.equals("READY")) {

                    if (session == null) {
                        server.addWaitingPlayer(this);
                    }

                    continue;
                }

                // =========================
                // GAME MESSAGES
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

    /**
     * Отправляет сообщение клиенту.
     *
     * @param message текст сообщения
     */
    public void sendMessage(String message){
        out.println(message);
    }

    /**
     * Устанавливает игровую сессию для клиента.
     *
     * @param session активная GameSession
     */
    public void setSession(GameSession session){
        this.session = session;
    }

    /**
     * @return никнейм игрока
     */
    public String getNickname() {
        return nickname;
    }

    /**
     * Устанавливает никнейм игрока.
     *
     * @param nickname имя пользователя
     */
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    /**
     * Закрывает соединение с клиентом и удаляет его с сервера.
     */
    private void closeConnection(){
        try{
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        server.removeClient(this);
    }
}