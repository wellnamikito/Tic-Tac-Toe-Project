package view;

import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import manager.ScreenManager;
import javafx.application.Platform;
import javafx.animation.PauseTransition;
import javafx.util.Duration;

public class BotGameView {

    private Button[][] cells;
    private char[][] board;
    private int size;

    private boolean gameOver = false;

    public Scene createScene(int size) {

        this.size = size;

        BorderPane root = new BorderPane();
        GridPane grid = new GridPane();

        grid.setAlignment(Pos.CENTER);

        cells = new Button[size][size];
        board = new char[size][size];

        for (int r = 0; r < size; r++) {
            for (int c = 0; c < size; c++) {

                board[r][c] = ' ';

                Button cell = new Button();
                cell.setPrefSize(80, 80);

                int rr = r;
                int cc = c;

                cell.setOnAction(e -> {

                    if (gameOver) return;
                    if (board[rr][cc] != ' ') return;

                    playerMove(rr, cc);
                });

                cells[r][c] = cell;
                grid.add(cell, c, r);
            }
        }

        Button back = new Button("Назад");

        back.setOnAction(e -> {
            Stage stage = ScreenManager.getStage();
            stage.setScene(new GameView().createScene());
        });

        root.setCenter(grid);
        root.setBottom(back);

        BorderPane.setAlignment(back, Pos.CENTER);

        return new Scene(root, 1920, 1080);
    }

    // =========================
    // PLAYER MOVE
    // =========================
    private void playerMove(int r, int c) {

        board[r][c] = 'X';
        cells[r][c].setText("X");

        if (checkWin('X')) {
            endGame("Победа", "Ты выиграл 🎉");
            return;
        }

        if (isDraw()) {
            endGame("Ничья", "Ничья 🤝");
            return;
        }

        botMove();
    }

    // =========================
    // BOT MOVE (SIMPLE + SAFE)
    // =========================
    private void botMove() {

        // 1. попытка выиграть
        int[] winMove = findWinningMove('O');
        if (winMove != null) {
            makeMove(winMove[0], winMove[1], 'O');
            return;
        }

        // 2. блокировка игрока
        int[] blockMove = findWinningMove('X');
        if (blockMove != null) {
            makeMove(blockMove[0], blockMove[1], 'O');
            return;
        }

        // 3. центр
        int center = size / 2;
        if (board[center][center] == ' ') {
            makeMove(center, center, 'O');
            return;
        }

        // 4. углы
        int[][] corners = {
                {0, 0},
                {0, size - 1},
                {size - 1, 0},
                {size - 1, size - 1}
        };

        for (int[] c : corners) {
            if (board[c[0]][c[1]] == ' ') {
                makeMove(c[0], c[1], 'O');
                return;
            }
        }

        // 5. любой ход
        for (int r = 0; r < size; r++) {
            for (int c = 0; c < size; c++) {
                if (board[r][c] == ' ') {
                    makeMove(r, c, 'O');
                    return;
                }
            }
        }
    }
    private int[] findWinningMove(char symbol) {

        for (int r = 0; r < size; r++) {
            for (int c = 0; c < size; c++) {

                if (board[r][c] == ' ') {

                    board[r][c] = symbol;

                    boolean win = checkWin(symbol);

                    board[r][c] = ' ';

                    if (win) {
                        return new int[]{r, c};
                    }
                }
            }
        }

        return null;
    }
    // =========================
    // WIN CHECK
    // =========================
    private boolean checkWin(char symbol) {

        for (int i = 0; i < size; i++) {

            boolean row = true;
            boolean col = true;

            for (int j = 0; j < size; j++) {

                if (board[i][j] != symbol) row = false;
                if (board[j][i] != symbol) col = false;
            }

            if (row || col) return true;
        }

        boolean d1 = true;
        boolean d2 = true;

        for (int i = 0; i < size; i++) {

            if (board[i][i] != symbol) d1 = false;
            if (board[i][size - i - 1] != symbol) d2 = false;
        }

        return d1 || d2;
    }

    // =========================
    // DRAW
    // =========================
    private boolean isDraw() {

        for (int r = 0; r < size; r++) {
            for (int c = 0; c < size; c++) {
                if (board[r][c] == ' ') return false;
            }
        }

        return true;
    }
    private void makeMove(int r, int c, char symbol) {

        board[r][c] = symbol;
        cells[r][c].setText(String.valueOf(symbol));

        if (checkWin(symbol)) {

            if (symbol == 'O') {
                endGame("Поражение", "Бот выиграл 🤖");
            } else {
                endGame("Победа", "Ты выиграл 🎉");
            }
        }

        if (isDraw()) {
            endGame("Ничья", "Ничья 🤝");
        }
    }
    // =========================
    // END GAME (UI SAFE)
    // =========================
    private void endGame(String title, String text) {

        gameOver = true;

        for (int r = 0; r < size; r++) {
            for (int c = 0; c < size; c++) {
                cells[r][c].setDisable(true);
            }
        }

        Platform.runLater(() -> {

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle(title);
            alert.setHeaderText(null);
            alert.setContentText(text);
            alert.show();
        });

        // ⬇️ возврат в меню через 1.5 секунды
        PauseTransition pause = new PauseTransition(Duration.seconds(1.5));

        pause.setOnFinished(e -> {

            Stage stage = ScreenManager.getStage();
            stage.setScene(new GameView().createScene());
        });

        pause.play();
    }
}