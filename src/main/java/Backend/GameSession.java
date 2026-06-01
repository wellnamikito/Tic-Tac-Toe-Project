package Backend;

/**
 * GameSession — управление игровой сессией между двумя игроками.
 *
 * 📌 Отвечает за:
 * - игровую логику
 * - обработку ходов
 * - проверку победы/ничьи
 * - синхронизацию состояния между игроками
 * - отправку событий клиентам
 */
public class GameSession {

    private CLientHandler player1;
    private CLientHandler player2;

    private char[][] board;
    private int size;

    private boolean isPlayer1Turn = true;
    private boolean gameOver = false;

    /**
     * Создаёт игровую сессию.
     *
     * @param player1 первый игрок
     * @param player2 второй игрок
     * @param size размер поля (3 или 9)
     */
    public GameSession(CLientHandler player1,
                       CLientHandler player2,
                       int size) {

        this.player1 = player1;
        this.player2 = player2;

        if (size != 3 && size != 9) {
            throw new IllegalArgumentException("Only 3x3 and 9x9 boards are supported");
        }

        this.size = size;

        board = new char[size][size];

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                board[i][j] = ' ';
            }
        }

        player1.setSession(this);
        player2.setSession(this);
    }

    // =========================
    // START GAME (вызывается сервером)
    // =========================

    public void startGame() {

        player1.sendMessage("START");
        player2.sendMessage("START");

        player1.sendMessage("ROLE X");
        player2.sendMessage("ROLE O");

        player1.sendMessage("SIZE " + size);
        player2.sendMessage("SIZE " + size);

        sendTurn();
        sendState();
    }

    // =========================
    // HANDLE MESSAGES
    // =========================

    public void handleMessage(CLientHandler sender,
                              String message) {

        if (message.equals("RESTART")) {
            restartGame();
            return;
        }

        // =========================
        // CHAT ALL
        // =========================
        if (message.startsWith("CHAT_ALL")) {

            String text = message.substring(9);

            String formatted =
                    "[Общий] [" + sender.getNickname() + "]: " + text;

            player1.sendMessage(formatted);
            player2.sendMessage(formatted);

            return;
        }

        // =========================
        // PRIVATE CHAT
        // =========================
        if (message.startsWith("CHAT_PRIVATE")) {

            String[] parts = message.split(" ", 3);
            if (parts.length < 3) return;

            String targetNick = parts[1];
            String text = parts[2];

            CLientHandler target = null;

            if (player1.getNickname().equals(targetNick)) target = player1;
            if (player2.getNickname().equals(targetNick)) target = player2;

            if (target == null) {
                sender.sendMessage("USER_NOT_FOUND");
                return;
            }

            target.sendMessage("[ЛС] [" + sender.getNickname() + "]: " + text);
            sender.sendMessage("[ВЫ -> " + targetNick + "]: " + text);

            return;
        }

        if (gameOver) return;

        // =========================
        // MOVE
        // =========================
        if (!message.startsWith("MOVE")) return;

        String[] parts = message.split(" ");

        if (parts.length < 3) {
            sender.sendMessage("INVALID");
            return;
        }

        int x;
        int y;

        try {
            x = Integer.parseInt(parts[1]);
            y = Integer.parseInt(parts[2]);
        } catch (Exception e) {
            sender.sendMessage("INVALID");
            return;
        }

        if (x < 0 || x >= size || y < 0 || y >= size) {
            sender.sendMessage("INVALID");
            return;
        }

        if (board[x][y] != ' ') {
            sender.sendMessage("INVALID");
            return;
        }

        if (sender == player1 && !isPlayer1Turn ||
                sender == player2 && isPlayer1Turn) {

            sender.sendMessage("NOT YOUR TURN");
            return;
        }

        if (sender == player1) {
            board[x][y] = 'X';
            isPlayer1Turn = false;
        } else {
            board[x][y] = 'O';
            isPlayer1Turn = true;
        }

        sendState();

        if (checkWin(x, y)) {
            sender.sendMessage("WIN");
            getOpponent(sender).sendMessage("LOSE");
            gameOver = true;
            return;
        }

        if (isDraw()) {
            player1.sendMessage("DRAW");
            player2.sendMessage("DRAW");
            gameOver = true;
            return;
        }

        sendTurn();
    }

    // =========================
    // GAME LOGIC
    // =========================

    private boolean checkWin(int x, int y) {

        char symbol = board[x][y];

        boolean win = true;

        for (int j = 0; j < size; j++)
            if (board[x][j] != symbol) win = false;

        if (win) return true;

        win = true;
        for (int i = 0; i < size; i++)
            if (board[i][y] != symbol) win = false;

        if (win) return true;

        if (x == y) {
            win = true;
            for (int i = 0; i < size; i++)
                if (board[i][i] != symbol) win = false;

            if (win) return true;
        }

        if (x + y == size - 1) {
            win = true;
            for (int i = 0; i < size; i++)
                if (board[i][size - 1 - i] != symbol) win = false;

            if (win) return true;
        }

        return false;
    }

    private boolean isDraw() {

        for (int i = 0; i < size; i++)
            for (int j = 0; j < size; j++)
                if (board[i][j] == ' ')
                    return false;

        return true;
    }

    // =========================
    // RESTART
    // =========================

    private void restartGame() {

        for (int i = 0; i < size; i++)
            for (int j = 0; j < size; j++)
                board[i][j] = ' ';

        gameOver = false;
        isPlayer1Turn = true;

        player1.sendMessage("RESTART");
        player2.sendMessage("RESTART");

        sendState();
        sendTurn();
    }

    // =========================
    // NETWORK
    // =========================

    private void sendState() {

        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < size; i++)
            for (int j = 0; j < size; j++)
                sb.append(board[i][j] == ' ' ? '_' : board[i][j]);

        String state = "STATE " + size + " " + sb;

        player1.sendMessage(state);
        player2.sendMessage(state);
    }

    private void sendTurn() {

        if (isPlayer1Turn) {
            player1.sendMessage("TURN YOUR");
            player2.sendMessage("TURN WAIT");
        } else {
            player2.sendMessage("TURN YOUR");
            player1.sendMessage("TURN WAIT");
        }
    }

    // =========================
    // DISCONNECT
    // =========================

    public void playerDisconnected(CLientHandler player) {

        gameOver = true;

        CLientHandler opponent = getOpponent(player);

        if (opponent != null) {
            opponent.sendMessage("OPPONENT_LEFT");
        }
    }

    private CLientHandler getOpponent(CLientHandler player) {
        return (player == player1) ? player2 : player1;
    }
}