package view;

import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import manager.ScreenManager;
import network.TCPClient;

public class OnlineGameView {

    private Button[][] cells;
    private char[][] board;
    private int size;

    private boolean myTurn = false;
    private boolean gameOver = false;

    private TCPClient client;
    private char mySymbol = 'X';

    // =========================
    // ✅ ВОТ ЭТОГО КОНСТРУКТОРА ТЕБЕ НЕ ХВАТАЛО
    // =========================
    public OnlineGameView(TCPClient client, int size) {
        this.client = client;
        this.size = size;
    }

    public Scene createScene() {

        this.gameOver = false;
        this.myTurn = false;

        StackPane root = new StackPane();
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);

        cells = new Button[size][size];
        board = new char[size][size];

        // НЕ СОЗДАЁМ TCPClient ЗАНОВО!
        client.send("READY");

        for (int r = 0; r < size; r++) {
            for (int c = 0; c < size; c++) {

                board[r][c] = ' ';

                Button btn = new Button();
                btn.setPrefSize(80, 80);

                int rr = r;
                int cc = c;

                btn.setOnAction(e -> {

                    if (gameOver) return;
                    if (!myTurn) return;
                    if (board[rr][cc] != ' ') return;

                    client.send("MOVE " + rr + " " + cc);
                });

                cells[r][c] = btn;
                grid.add(btn, c, r);
            }
        }

        root.getChildren().add(grid);

        return new Scene(root, 1920, 1080);
    }

    // =========================
    // SERVER HANDLER
    // =========================
    public void handleServer(String msg) {

        if (gameOver) return;

        if (msg.startsWith("ROLE")) {
            mySymbol = msg.contains("X") ? 'X' : 'O';
            return;
        }

        if (msg.startsWith("STATE")) {

            String[] parts = msg.split(" ");
            if (parts.length < 3) return;

            String data = parts[2];
            java.util.concurrent.atomic.AtomicInteger i =
                    new java.util.concurrent.atomic.AtomicInteger(0);

            Platform.runLater(() -> {

                for (int r = 0; r < size; r++) {
                    for (int c = 0; c < size; c++) {

                        int index = i.getAndIncrement();

                        if (index >= data.length()) return;

                        char ch = data.charAt(index);
                        board[r][c] = ch;

                        cells[r][c].setText(ch == '_' ? "" : String.valueOf(ch));
                    }
                }
            });

            return;
        }

        if (msg.equals("TURN YOUR")) {
            myTurn = true;
            return;
        }

        if (msg.equals("TURN WAIT")) {
            myTurn = false;
            return;
        }

        if (msg.equals("WIN")) end("Победа");
        else if (msg.equals("LOSE")) end("Поражение");
        else if (msg.equals("DRAW")) end("Ничья");
        else if (msg.equals("OPPONENT_LEFT")) end("Соперник вышел");
    }

    private void end(String text) {

        gameOver = true;
        myTurn = false;

        Platform.runLater(() -> {

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Игра окончена");
            alert.setHeaderText(null);
            alert.setContentText(text);
            alert.show();

            Stage stage = ScreenManager.getStage();
            stage.setScene(new GameView().createScene());
        });

        if (client != null) {
            client.close();
        }
    }
}