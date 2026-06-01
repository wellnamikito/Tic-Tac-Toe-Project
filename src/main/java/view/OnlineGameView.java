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

    public OnlineGameView(TCPClient client, int size) {
        this.client = client;
        this.size = size;
    }

    public Scene createScene() {

        gameOver = false;
        myTurn = false;

        StackPane root = new StackPane();
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);

        cells = new Button[size][size];
        board = new char[size][size];

        // 🔥 ПРАВИЛЬНЫЙ HANDSHAKE
        client.send("NICK player");
        client.send("MODE " + size);
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
        return new Scene(root, 800, 800);
    }

    public void handleServer(String msg) {

        if (gameOver) return;

        // ================= ROLE =================
        if (msg.startsWith("ROLE")) {
            mySymbol = msg.contains("X") ? 'X' : 'O';
            return;
        }

        // ================= STATE =================
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

        // ================= TURN =================
        if (msg.equals("TURN YOUR")) {
            myTurn = true;
            return;
        }

        if (msg.equals("TURN WAIT")) {
            myTurn = false;
            return;
        }

        // ================= END =================
        if (msg.equals("WIN")) end("Победа");
        if (msg.equals("LOSE")) end("Поражение");
        if (msg.equals("DRAW")) end("Ничья");
        if (msg.equals("OPPONENT_LEFT")) end("Соперник вышел");
    }

    private void end(String text) {

        gameOver = true;
        myTurn = false;

        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText(text);
            alert.show();

            Stage stage = ScreenManager.getStage();
            stage.setScene(new GameView().createScene());
        });

        client.close();
    }
}