package view;

import Backend.Difficulty;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import manager.ScreenManager;

import java.util.Random;

public class BotGameView {

    private Button[][] cells;
    private char[][] board;
    private int size;
    private boolean gameOver = false;

    // Сложность
    private final Difficulty difficulty;
    private final Random random = new Random();

    // Счёт
    private int playerScore = 0;
    private int botScore = 0;
    private Label playerScoreLabel;
    private Label botScoreLabel;
    private Label turnLabel;

    // Компоненты для перезапуска
    private BorderPane root;
    private GridPane grid;
    private StackPane overlayPane;
    private ScrollPane scrollPane;

    public BotGameView(Difficulty difficulty) {
        this.difficulty = difficulty;
    }

    public Scene createScene(int size) {

        this.size = size;

        root = new BorderPane();
        root.getStyleClass().add("game-board-root");

        // =========================================================
        // ВЕРХНЯЯ ПАНЕЛЬ (игроки, счёт)
        // =========================================================
        HBox topPanel = new HBox(30);
        topPanel.setAlignment(Pos.CENTER);
        topPanel.getStyleClass().add("top-panel");

        // Панель игрока
        VBox playerPanel = new VBox(8);
        playerPanel.setAlignment(Pos.CENTER);
        playerPanel.getStyleClass().add("player-panel");

        Label playerAvatar = new Label("👤");
        playerAvatar.setStyle(
                "-fx-font-size: 36px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: #e74c3c;" +
                        "-fx-background-color: rgba(231, 76, 60, 0.2);" +
                        "-fx-background-radius: 30;" +
                        "-fx-padding: 8;" +
                        "-fx-min-width: 50;" +
                        "-fx-min-height: 50;" +
                        "-fx-alignment: center;"
        );

        Label playerName = new Label("ИГРОК");
        playerName.getStyleClass().add("player-name");

        playerScoreLabel = new Label("0");
        playerScoreLabel.getStyleClass().add("score-text");

        playerPanel.getChildren().addAll(playerAvatar, playerName, playerScoreLabel);

        // Разделитель VS
        VBox vsPanel = new VBox(5);
        vsPanel.setAlignment(Pos.CENTER);

        Label vsText = new Label("VS");
        vsText.getStyleClass().add("vs-text");

        Label botIcon = new Label("🤖");
        botIcon.setStyle("-fx-font-size: 24px;");

        vsPanel.getChildren().addAll(vsText, botIcon);

        // Панель бота
        VBox botPanel = new VBox(8);
        botPanel.setAlignment(Pos.CENTER);
        botPanel.getStyleClass().add("player-panel");

        Label botAvatar = new Label("🤖");
        botAvatar.setStyle(
                "-fx-font-size: 32px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: #3498db;" +
                        "-fx-background-color: rgba(52, 152, 219, 0.2);" +
                        "-fx-background-radius: 30;" +
                        "-fx-padding: 8;" +
                        "-fx-min-width: 50;" +
                        "-fx-min-height: 50;" +
                        "-fx-alignment: center;"
        );

        Label botName = new Label("БОТ");
        botName.getStyleClass().add("bot-name");

        botScoreLabel = new Label("0");
        botScoreLabel.getStyleClass().add("score-text");

        botPanel.getChildren().addAll(botAvatar, botName, botScoreLabel);

        topPanel.getChildren().addAll(playerPanel, vsPanel, botPanel);

        // =========================================================
        // НИЖНЯЯ ПАНЕЛЬ (информация о ходе)
        // =========================================================
        HBox bottomPanel = new HBox();
        bottomPanel.setAlignment(Pos.CENTER);
        bottomPanel.setStyle(
                "-fx-background-color: rgba(139, 90, 43, 0.7);" +
                        "-fx-background-radius: 15;" +
                        "-fx-padding: 10 20;" +
                        "-fx-min-height: 50;"
        );

        turnLabel = new Label("🔥 ВАШ ХОД! 🔥");
        turnLabel.setStyle(
                "-fx-text-fill: #FFD700;" +
                        "-fx-font-size: 18px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.5), 5, 0.1, 0, 2);"
        );
        bottomPanel.getChildren().add(turnLabel);

        // =========================================================
        // КНОПКА НАЗАД
        // =========================================================
        Button back = new Button("←");
        back.setStyle(
                "-fx-background-color: transparent;" +
                        "-fx-text-fill: #8B5A2B;" +
                        "-fx-font-size: 40px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-cursor: hand;" +
                        "-fx-padding: 14 20;" +
                        "-fx-background-radius: 30;" +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.15), 5, 0.1, 0, 3);"
        );

        back.setOnMouseEntered(e -> back.setStyle(
                "-fx-background-color: rgba(139, 90, 43, 0.25);" +
                        "-fx-text-fill: #8B5A2B;" +
                        "-fx-font-size: 40px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-cursor: hand;" +
                        "-fx-padding: 14 20;" +
                        "-fx-background-radius: 30;" +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.25), 8, 0.15, 0, 4);"
        ));

        back.setOnMouseExited(e -> back.setStyle(
                "-fx-background-color: transparent;" +
                        "-fx-text-fill: #8B5A2B;" +
                        "-fx-font-size: 40px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-cursor: hand;" +
                        "-fx-padding: 14 20;" +
                        "-fx-background-radius: 30;"
        ));

        back.setOnAction(e -> {
            showConfirmDialog("Выход", "Вы уверены?", "❓");
        });

        // =========================================================
        // КОНТЕЙНЕР ДЛЯ ВЕРХНЕЙ ЧАСТИ (кнопка поверх панели)
        // =========================================================
        StackPane topContainer = new StackPane();
        topContainer.getChildren().add(topPanel);
        topContainer.getChildren().add(back);
        StackPane.setAlignment(back, Pos.TOP_LEFT);
        StackPane.setMargin(back, new Insets(40, 0, 0, 85));

        // =========================================================
        // ИГРОВОЕ ПОЛЕ (с прокруткой)
        // =========================================================
        grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.getStyleClass().add("grid-pane");

        initGameBoard();

        // Создаём ScrollPane для прокрутки игрового поля
        scrollPane = new ScrollPane();
        scrollPane.setContent(grid);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);
        scrollPane.setPannable(true);
        scrollPane.setStyle(
                "-fx-background: transparent;" +
                        "-fx-background-color: transparent;" +
                        "-fx-border-color: transparent;" +
                        "-fx-padding: 0;" +
                        "-fx-background-insets: 0;" +
                        "-fx-border-insets: 0;"
        );

        // Стилизация полосы прокрутки
        scrollPane.getStylesheets().add(
                getClass().getResource("/css/game.css").toExternalForm()
        );
        scrollPane.getStyleClass().add("custom-scroll-pane");

        overlayPane = new StackPane();
        overlayPane.setAlignment(Pos.CENTER);
        overlayPane.setVisible(false);

        StackPane gameArea = new StackPane();
        gameArea.getChildren().addAll(scrollPane, overlayPane);
        gameArea.setAlignment(Pos.CENTER);

        // Собираем всё вместе
        root.setTop(topContainer);
        root.setCenter(gameArea);
        root.setBottom(bottomPanel);

        BorderPane.setMargin(topContainer, new Insets(10, 20, 10, 20));
        BorderPane.setMargin(bottomPanel, new Insets(0, 20, 20, 20));

        Scene scene = new Scene(root, 800, 800);
        scene.getStylesheets().add(getClass().getResource("/css/game.css").toExternalForm());

        // Восстанавливаем полноэкранный режим из настроек
        ScreenManager.restoreFullscreen();

        return scene;
    }

    // =========================================================
    // ОКНО ПОДТВЕРЖДЕНИЯ
    // =========================================================
    private void showConfirmDialog(String title, String header, String emoji) {
        VBox dialogBox = new VBox(15);
        dialogBox.setAlignment(Pos.CENTER);
        dialogBox.setStyle(
                "-fx-background-color: #FFDAB9;" +
                        "-fx-background-radius: 20;" +
                        "-fx-border-color: #8B5A2B;" +
                        "-fx-border-width: 2;" +
                        "-fx-border-radius: 20;" +
                        "-fx-padding: 25 35;" +
                        "-fx-min-width: 260;" +
                        "-fx-min-height: 180;" +
                        "-fx-max-width: 260;" +
                        "-fx-max-height: 180;" +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 15, 0.1, 0, 5);"
        );

        Label emojiLabel = new Label(emoji);
        emojiLabel.setStyle("-fx-font-size: 40px;");

        Label titleLabel = new Label(title);
        titleLabel.setStyle(
                "-fx-text-fill: #5C3317;" +
                        "-fx-font-size: 20px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-font-family: 'Segoe UI';"
        );

        Label headerLabel = new Label(header);
        headerLabel.setStyle(
                "-fx-text-fill: #5C3317;" +
                        "-fx-font-size: 16px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-font-family: 'Segoe UI';"
        );

        HBox buttonBox = new HBox(15);
        buttonBox.setAlignment(Pos.CENTER);

        Button yesButton = new Button("Да");
        yesButton.setStyle(
                "-fx-background-color: #8B5A2B;" +
                        "-fx-text-fill: white;" +
                        "-fx-font-size: 12px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-background-radius: 12;" +
                        "-fx-padding: 6 20;" +
                        "-fx-cursor: hand;"
        );
        yesButton.setOnMouseEntered(e -> yesButton.setStyle(
                "-fx-background-color: #A0522D;" +
                        "-fx-text-fill: white;" +
                        "-fx-font-size: 12px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-background-radius: 12;" +
                        "-fx-padding: 6 20;" +
                        "-fx-cursor: hand;"
        ));
        yesButton.setOnMouseExited(e -> yesButton.setStyle(
                "-fx-background-color: #8B5A2B;" +
                        "-fx-text-fill: white;" +
                        "-fx-font-size: 12px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-background-radius: 12;" +
                        "-fx-padding: 6 20;" +
                        "-fx-cursor: hand;"
        ));

        Button noButton = new Button("Нет");
        noButton.setStyle(
                "-fx-background-color: #8B5A2B;" +
                        "-fx-text-fill: white;" +
                        "-fx-font-size: 12px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-background-radius: 12;" +
                        "-fx-padding: 6 20;" +
                        "-fx-cursor: hand;"
        );
        noButton.setOnMouseEntered(e -> noButton.setStyle(
                "-fx-background-color: #A0522D;" +
                        "-fx-text-fill: white;" +
                        "-fx-font-size: 12px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-background-radius: 12;" +
                        "-fx-padding: 6 20;" +
                        "-fx-cursor: hand;"
        ));
        noButton.setOnMouseExited(e -> noButton.setStyle(
                "-fx-background-color: #8B5A2B;" +
                        "-fx-text-fill: white;" +
                        "-fx-font-size: 12px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-background-radius: 12;" +
                        "-fx-padding: 6 20;" +
                        "-fx-cursor: hand;"
        ));

        yesButton.setOnAction(e -> {
            overlayPane.setVisible(false);
            Platform.runLater(() -> {
                Stage stage = ScreenManager.getStage();
                if (stage != null) {
                    // НЕ выключаем полноэкранный режим!
                    stage.setScene(new GameView().createScene());
                }
            });
        });

        noButton.setOnAction(e -> {
            overlayPane.setVisible(false);
        });

        buttonBox.getChildren().addAll(yesButton, noButton);
        dialogBox.getChildren().addAll(emojiLabel, titleLabel, headerLabel, buttonBox);

        overlayPane.getChildren().clear();
        overlayPane.getChildren().add(dialogBox);
        overlayPane.setVisible(true);
    }

    // =========================================================
    // ОКНО РЕЗУЛЬТАТА
    // =========================================================
    private void showResultDialog(String title, String message, String emoji) {
        VBox dialogBox = new VBox(12);
        dialogBox.setAlignment(Pos.CENTER);
        dialogBox.setStyle(
                "-fx-background-color: #FFDAB9;" +
                        "-fx-background-radius: 20;" +
                        "-fx-border-color: #8B5A2B;" +
                        "-fx-border-width: 2;" +
                        "-fx-border-radius: 20;" +
                        "-fx-padding: 20 30;" +
                        "-fx-min-width: 260;" +
                        "-fx-min-height: 180;" +
                        "-fx-max-width: 260;" +
                        "-fx-max-height: 180;" +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 15, 0.1, 0, 5);"
        );

        Label emojiLabel = new Label(emoji);
        emojiLabel.setStyle("-fx-font-size: 36px;");

        Label titleLabel = new Label(title);
        titleLabel.setStyle(
                "-fx-text-fill: #5C3317;" +
                        "-fx-font-size: 18px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-font-family: 'Segoe UI';"
        );

        Label messageLabel = new Label(message);
        messageLabel.setStyle(
                "-fx-text-fill: #5C3317;" +
                        "-fx-font-size: 12px;" +
                        "-fx-font-family: 'Segoe UI';"
        );

        HBox buttonBox = new HBox(12);
        buttonBox.setAlignment(Pos.CENTER);

        Button continueButton = new Button("Дальше");
        continueButton.setStyle(
                "-fx-background-color: #8B5A2B;" +
                        "-fx-text-fill: white;" +
                        "-fx-font-size: 12px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-background-radius: 12;" +
                        "-fx-padding: 6 15;" +
                        "-fx-cursor: hand;"
        );
        continueButton.setOnMouseEntered(e -> continueButton.setStyle(
                "-fx-background-color: #A0522D;" +
                        "-fx-text-fill: white;" +
                        "-fx-font-size: 12px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-background-radius: 12;" +
                        "-fx-padding: 6 15;" +
                        "-fx-cursor: hand;"
        ));
        continueButton.setOnMouseExited(e -> continueButton.setStyle(
                "-fx-background-color: #8B5A2B;" +
                        "-fx-text-fill: white;" +
                        "-fx-font-size: 12px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-background-radius: 12;" +
                        "-fx-padding: 6 15;" +
                        "-fx-cursor: hand;"
        ));

        Button exitButton = new Button("Выйти");
        exitButton.setStyle(
                "-fx-background-color: #8B5A2B;" +
                        "-fx-text-fill: white;" +
                        "-fx-font-size: 12px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-background-radius: 12;" +
                        "-fx-padding: 6 15;" +
                        "-fx-cursor: hand;"
        );
        exitButton.setOnMouseEntered(e -> exitButton.setStyle(
                "-fx-background-color: #A0522D;" +
                        "-fx-text-fill: white;" +
                        "-fx-font-size: 12px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-background-radius: 12;" +
                        "-fx-padding: 6 15;" +
                        "-fx-cursor: hand;"
        ));
        exitButton.setOnMouseExited(e -> exitButton.setStyle(
                "-fx-background-color: #8B5A2B;" +
                        "-fx-text-fill: white;" +
                        "-fx-font-size: 12px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-background-radius: 12;" +
                        "-fx-padding: 6 15;" +
                        "-fx-cursor: hand;"
        ));

        continueButton.setOnAction(e -> {
            overlayPane.setVisible(false);
            restartRound();
        });

        exitButton.setOnAction(e -> {
            overlayPane.setVisible(false);
            showConfirmDialog("Выход", "Вы уверены?", "❓");
        });

        buttonBox.getChildren().addAll(continueButton, exitButton);
        dialogBox.getChildren().addAll(emojiLabel, titleLabel, messageLabel, buttonBox);

        overlayPane.getChildren().clear();
        overlayPane.getChildren().add(dialogBox);
        overlayPane.setVisible(true);
    }

    // =========================================================
    // ОБНОВЛЕНИЕ ТЕКСТА ХОДА
    // =========================================================
    private void updateTurnLabel(boolean isPlayerTurn) {
        Platform.runLater(() -> {
            if (isPlayerTurn) {
                turnLabel.setText("🔥 ВАШ ХОД! 🔥");
                turnLabel.setStyle(
                        "-fx-text-fill: #FFD700;" +
                                "-fx-font-size: 18px;" +
                                "-fx-font-weight: bold;" +
                                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.5), 5, 0.1, 0, 2);"
                );
            } else {
                turnLabel.setText("🤖 ХОД БОТА... 🤖");
                turnLabel.setStyle(
                        "-fx-text-fill: #FFFFFF;" +
                                "-fx-font-size: 16px;" +
                                "-fx-font-weight: bold;"
                );
            }
        });
    }

    // =========================================================
    // ИНИЦИАЛИЗАЦИЯ ИГРОВОГО ПОЛЯ
    // =========================================================
    private void initGameBoard() {
        grid.getChildren().clear();

        cells = new Button[size][size];
        board = new char[size][size];

        for (int r = 0; r < size; r++) {
            for (int c = 0; c < size; c++) {

                board[r][c] = ' ';

                Button cell = new Button();
                cell.setPrefSize(100, 100);
                cell.getStyleClass().add("cell");

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

        gameOver = false;
        updateTurnLabel(true);
    }

    private void restartRound() {
        for (int r = 0; r < size; r++) {
            for (int c = 0; c < size; c++) {
                board[r][c] = ' ';
                cells[r][c].setText("");
                cells[r][c].setDisable(false);
                cells[r][c].getStyleClass().removeAll("cell-x", "cell-o");
                cells[r][c].getStyleClass().add("cell");
            }
        }
        gameOver = false;
        updateTurnLabel(true);
    }

    private void updateScores() {
        playerScoreLabel.setText(String.valueOf(playerScore));
        botScoreLabel.setText(String.valueOf(botScore));
    }

    private void playerMove(int r, int c) {

        board[r][c] = 'X';
        cells[r][c].setText("X");
        cells[r][c].getStyleClass().add("cell-x");
        cells[r][c].getStyleClass().remove("cell-o");

        if (checkWin('X')) {
            playerScore++;
            updateScores();
            gameOver = true;
            lockBoard();
            showResultDialog("ПОБЕДА!", "Ты выиграл!", "🏆");
            return;
        }

        if (isDraw()) {
            gameOver = true;
            lockBoard();
            showResultDialog("НИЧЬЯ!", "Ничья!", "🤝");
            return;
        }

        updateTurnLabel(false);
        botMove();
    }

    // =========================================================
    // BOT MOVE С ПОДДЕРЖКОЙ СЛОЖНОСТИ
    // =========================================================
    private void botMove() {

        // Проверка на сложность Easy - случайные ходы
        if (difficulty == Difficulty.EASY) {
            java.util.ArrayList<int[]> emptyCells = new java.util.ArrayList<>();
            for (int r = 0; r < size; r++) {
                for (int c = 0; c < size; c++) {
                    if (board[r][c] == ' ') {
                        emptyCells.add(new int[]{r, c});
                    }
                }
            }

            if (!emptyCells.isEmpty()) {
                int[] randomMove = emptyCells.get(random.nextInt(emptyCells.size()));
                makeMove(randomMove[0], randomMove[1], 'O');
            }
            return;
        }

        // Для MEDIUM и HARD - умные ходы
        int[] winMove = findWinningMove('O');
        if (winMove != null) {
            makeMove(winMove[0], winMove[1], 'O');
            return;
        }

        int[] blockMove = findWinningMove('X');
        if (blockMove != null) {
            makeMove(blockMove[0], blockMove[1], 'O');
            return;
        }

        if (difficulty == Difficulty.HARD) {
            int center = size / 2;
            if (board[center][center] == ' ') {
                makeMove(center, center, 'O');
                return;
            }

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
        }

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

    private boolean isDraw() {
        for (int r = 0; r < size; r++) {
            for (int c = 0; c < size; c++) {
                if (board[r][c] == ' ') return false;
            }
        }
        return true;
    }

    private void lockBoard() {
        for (int r = 0; r < size; r++) {
            for (int c = 0; c < size; c++) {
                cells[r][c].setDisable(true);
            }
        }
    }

    private void makeMove(int r, int c, char symbol) {
        board[r][c] = symbol;
        cells[r][c].setText(String.valueOf(symbol));

        if (symbol == 'X') {
            cells[r][c].getStyleClass().add("cell-x");
            cells[r][c].getStyleClass().remove("cell-o");
        } else if (symbol == 'O') {
            cells[r][c].getStyleClass().add("cell-o");
            cells[r][c].getStyleClass().remove("cell-x");
        }

        if (checkWin(symbol)) {
            if (symbol == 'O') {
                botScore++;
                updateScores();
                gameOver = true;
                lockBoard();
                showResultDialog("ПОРАЖЕНИЕ!", "Бот выиграл!", "🤖");
            } else {
                playerScore++;
                updateScores();
                gameOver = true;
                lockBoard();
                showResultDialog("ПОБЕДА!", "Ты выиграл!", "🏆");
            }
            return;
        }

        if (isDraw()) {
            gameOver = true;
            lockBoard();
            showResultDialog("НИЧЬЯ!", "Ничья!", "🤝");
            return;
        }

        updateTurnLabel(true);
    }
}