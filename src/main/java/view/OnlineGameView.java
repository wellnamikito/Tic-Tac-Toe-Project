package view;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import manager.ScreenManager;
import manager.AudioManager;
import network.TCPClient;

public class OnlineGameView {

    private Button[][] cells;
    private char[][] board;
    private int size;
    private boolean myTurn = false;
    private boolean gameOver = false;
    private TCPClient client;
    private char mySymbol = 'X';

    // Счёт
    private int playerScore = 0;
    private int opponentScore = 0;
    private Label playerScoreLabel;
    private Label opponentScoreLabel;
    private Label turnLabel;

    // Компоненты для чата
    private TextArea chatArea;
    private TextField chatInput;
    private ToggleButton chatAllButton;
    private ToggleButton chatPrivateButton;
    private boolean isChatAll = true;
    private String opponentNickname = "СОПЕРНИК";
    private VBox chatPanel;
    private Button toggleChatButton;
    private boolean isChatExpanded = true;

    // Компоненты для перезапуска
    private BorderPane root;
    private GridPane grid;
    private StackPane overlayPane;
    private ScrollPane scrollPane;

    public OnlineGameView(TCPClient client, int size) {
        this.client = client;
        this.size = size;
        AudioManager.checkSoundFiles();
    }

    public Scene createScene() {

        gameOver = false;
        myTurn = false;

        root = new BorderPane();
        root.getStyleClass().add("game-board-root");

        // =========================================================
        // ВЕРХНЯЯ ПАНЕЛЬ (игроки, счёт)
        // =========================================================
        HBox topPanel = new HBox(30);
        topPanel.setAlignment(Pos.CENTER);
        topPanel.setStyle("-fx-padding: 20; -fx-background-color: rgba(139, 90, 43, 0.3);");

        // Панель игрока
        VBox playerPanel = new VBox(8);
        playerPanel.setAlignment(Pos.CENTER);

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

        Label playerName = new Label("ВЫ");
        playerName.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #5C3317;");

        playerScoreLabel = new Label("0");
        playerScoreLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #e74c3c;");

        playerPanel.getChildren().addAll(playerAvatar, playerName, playerScoreLabel);

        // Разделитель VS
        VBox vsPanel = new VBox(5);
        vsPanel.setAlignment(Pos.CENTER);

        Label vsText = new Label("VS");
        vsText.setStyle("-fx-text-fill: #8B5A2B; -fx-font-size: 28px; -fx-font-weight: bold;");

        Label onlineIcon = new Label("🌐");
        onlineIcon.setStyle("-fx-font-size: 24px;");

        vsPanel.getChildren().addAll(vsText, onlineIcon);

        // Панель соперника
        VBox opponentPanel = new VBox(8);
        opponentPanel.setAlignment(Pos.CENTER);

        Label opponentAvatar = new Label("👥");
        opponentAvatar.setStyle(
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

        Label opponentName = new Label("СОПЕРНИК");
        opponentName.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #5C3317;");

        opponentScoreLabel = new Label("0");
        opponentScoreLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #3498db;");

        opponentPanel.getChildren().addAll(opponentAvatar, opponentName, opponentScoreLabel);

        topPanel.getChildren().addAll(playerPanel, vsPanel, opponentPanel);

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

        turnLabel = new Label("Ожидание подключения...");
        turnLabel.setStyle(
                "-fx-text-fill: white;" +
                        "-fx-font-size: 16px;" +
                        "-fx-font-weight: bold;"
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
                        "-fx-background-radius: 30;"
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
            showConfirmDialog("Выход", "Вы уверены?", "Вы действительно хотите выйти из игры?", "❓");
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
        // ИГРОВОЕ ПОЛЕ
        // =========================================================
        grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(5);
        grid.setVgap(5);
        grid.setPadding(new Insets(20));

        initGameBoard();

        scrollPane = new ScrollPane();
        scrollPane.setContent(grid);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);
        scrollPane.setPannable(true);
        scrollPane.setStyle(
                "-fx-background: transparent;" +
                        "-fx-background-color: transparent;" +
                        "-fx-border-color: transparent;" +
                        "-fx-padding: 0;"
        );
        scrollPane.getStyleClass().add("custom-scroll-pane");

        // =========================================================
        // ПАНЕЛЬ ЧАТА
        // =========================================================
        createChatPanel();

        VBox chatButtonContainer = new VBox();
        chatButtonContainer.setAlignment(Pos.TOP_RIGHT);
        chatButtonContainer.setPadding(new Insets(0, 20, 0, 0));

        toggleChatButton = new Button("📨");
        toggleChatButton.setStyle(
                "-fx-background-color: #8B5A2B;" +
                        "-fx-text-fill: white;" +
                        "-fx-font-size: 20px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-background-radius: 15;" +
                        "-fx-padding: 8 12;" +
                        "-fx-cursor: hand;" +
                        "-fx-min-width: 45;" +
                        "-fx-min-height: 45;"
        );
        toggleChatButton.setOnAction(e -> toggleChat());

        chatButtonContainer.getChildren().add(toggleChatButton);

        VBox chatContainer = new VBox(10);
        chatContainer.setAlignment(Pos.TOP_RIGHT);
        chatContainer.getChildren().addAll(chatButtonContainer, chatPanel);

        // =========================================================
        // СБОРКА
        // =========================================================
        HBox mainLayout = new HBox(20);
        mainLayout.setAlignment(Pos.CENTER);
        mainLayout.setPadding(new Insets(20));
        mainLayout.getChildren().addAll(scrollPane, chatContainer);
        HBox.setHgrow(scrollPane, Priority.ALWAYS);

        overlayPane = new StackPane();
        overlayPane.setAlignment(Pos.CENTER);
        overlayPane.setVisible(false);

        StackPane gameArea = new StackPane();
        gameArea.getChildren().addAll(mainLayout, overlayPane);
        gameArea.setAlignment(Pos.CENTER);

        root.setTop(topContainer);
        root.setCenter(gameArea);
        root.setBottom(bottomPanel);

        BorderPane.setMargin(topContainer, new Insets(10, 20, 10, 20));
        BorderPane.setMargin(bottomPanel, new Insets(0, 20, 20, 20));

        Scene scene = new Scene(root, 900, 800);
        scene.getStylesheets().add(getClass().getResource("/css/game.css").toExternalForm());

        // Отправляем данные на сервер
        client.send("NICK player");
        client.send("MODE " + size);
        client.send("READY");

        ScreenManager.restoreFullscreen();

        return scene;
    }

    // =========================================================
    // СОЗДАНИЕ ПАНЕЛИ ЧАТА
    // =========================================================
    private void createChatPanel() {
        chatPanel = new VBox(10);
        chatPanel.setAlignment(Pos.CENTER);
        chatPanel.setStyle(
                "-fx-background-color: rgba(255, 218, 185, 0.95);" +
                        "-fx-background-radius: 15;" +
                        "-fx-border-color: #8B5A2B;" +
                        "-fx-border-width: 2;" +
                        "-fx-border-radius: 15;" +
                        "-fx-padding: 10;" +
                        "-fx-min-width: 280;" +
                        "-fx-max-width: 280;" +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 10, 0.1, 0, 5);"
        );

        Label chatTitle = new Label("💬 ЧАТ");
        chatTitle.setStyle(
                "-fx-text-fill: #5C3317;" +
                        "-fx-font-size: 18px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-font-family: 'Segoe UI';"
        );

        chatArea = new TextArea();
        chatArea.setEditable(false);
        chatArea.setWrapText(true);
        chatArea.setStyle(
                "-fx-background-color: #FFF8E7;" +
                        "-fx-border-color: #8B5A2B;" +
                        "-fx-border-radius: 10;" +
                        "-fx-background-radius: 10;" +
                        "-fx-font-size: 12px;" +
                        "-fx-font-family: 'Segoe UI';"
        );
        chatArea.setPrefHeight(300);
        chatArea.setText("💬 Добро пожаловать в чат!\n");

        HBox chatModeBox = new HBox(10);
        chatModeBox.setAlignment(Pos.CENTER);

        chatAllButton = new ToggleButton("🌐 Общий");
        chatPrivateButton = new ToggleButton("🔒 Личный");

        chatAllButton.setSelected(true);

        String buttonStyle =
                "-fx-background-color: #8B5A2B;" +
                        "-fx-text-fill: white;" +
                        "-fx-font-size: 12px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-background-radius: 10;" +
                        "-fx-padding: 5 10;" +
                        "-fx-cursor: hand;";

        chatAllButton.setStyle(buttonStyle);
        chatPrivateButton.setStyle(buttonStyle);

        chatAllButton.setOnAction(e -> {
            chatAllButton.setSelected(true);
            chatPrivateButton.setSelected(false);
            isChatAll = true;
            chatInput.setPromptText("Введите сообщение для всех...");
        });

        chatPrivateButton.setOnAction(e -> {
            chatPrivateButton.setSelected(true);
            chatAllButton.setSelected(false);
            isChatAll = false;
            chatInput.setPromptText("Введите сообщение для соперника...");
        });

        chatModeBox.getChildren().addAll(chatAllButton, chatPrivateButton);

        chatInput = new TextField();
        chatInput.setPromptText("Введите сообщение...");
        chatInput.setStyle(
                "-fx-background-color: #FFF8E7;" +
                        "-fx-border-color: #8B5A2B;" +
                        "-fx-border-radius: 10;" +
                        "-fx-background-radius: 10;" +
                        "-fx-padding: 8;" +
                        "-fx-font-size: 12px;"
        );

        chatInput.setOnAction(e -> sendChatMessage());

        Button sendButton = new Button("📤 Отправить");
        sendButton.setStyle(buttonStyle);
        sendButton.setOnAction(e -> sendChatMessage());

        HBox inputBox = new HBox(10);
        inputBox.getChildren().addAll(chatInput, sendButton);

        chatPanel.getChildren().addAll(chatTitle, chatArea, chatModeBox, inputBox);
    }

    // =========================================================
    // СВОРАЧИВАНИЕ/РАЗВОРАЧИВАНИЕ ЧАТА
    // =========================================================
    private void toggleChat() {
        if (isChatExpanded) {
            chatPanel.setVisible(false);
            chatPanel.setManaged(false);
            toggleChatButton.setText("📩");
            isChatExpanded = false;
        } else {
            chatPanel.setVisible(true);
            chatPanel.setManaged(true);
            toggleChatButton.setText("📨");
            isChatExpanded = true;
        }
    }

    // =========================================================
    // ОТПРАВКА СООБЩЕНИЯ В ЧАТ
    // =========================================================
    private void sendChatMessage() {
        String message = chatInput.getText().trim();
        if (message.isEmpty()) return;

        AudioManager.playMessageSound();

        if (isChatAll) {
            client.send("CHAT_ALL " + message);
            addChatMessage("[ВЫ (общий)]: " + message);
        } else {
            client.send("CHAT_PRIVATE " + opponentNickname + " " + message);
            addChatMessage("[ВЫ -> " + opponentNickname + "]: " + message);
        }

        chatInput.clear();
    }

    // =========================================================
    // ДОБАВЛЕНИЕ СООБЩЕНИЯ В ЧАТ
    // =========================================================
    private void addChatMessage(String message) {
        Platform.runLater(() -> {
            chatArea.appendText(message + "\n");
            chatArea.setScrollTop(Double.MAX_VALUE);
            if (!message.startsWith("[ВЫ")) {
                AudioManager.playMessageSound();
            }
        });
    }

    // =========================================================
    // ИНИЦИАЛИЗАЦИЯ ИГРОВОГО ПОЛЯ (ЛОГИКА ИЗ РАБОЧЕЙ ВЕРСИИ)
    // =========================================================
    private void initGameBoard() {
        grid.getChildren().clear();

        cells = new Button[size][size];
        board = new char[size][size];

        for (int r = 0; r < size; r++) {
            for (int c = 0; c < size; c++) {
                // ВАЖНО: инициализируем '_' как в рабочей версии
                board[r][c] = '_';

                Button cell = new Button();
                if (size == 3) {
                    cell.setPrefSize(100, 100);
                } else {
                    cell.setPrefSize(70, 70);
                }
                cell.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-background-color: #f0f0f0; -fx-border-color: #8B5A2B; -fx-border-width: 2;");

                int row = r;
                int col = c;

                cell.setOnAction(e -> {
                    // ЛОГИКА ИЗ РАБОЧЕЙ ВЕРСИИ
                    if (gameOver) return;
                    if (!myTurn) return;

                    // ПРОВЕРКА из рабочей версии: клетка пуста если символ '_'
                    if (board[row][col] != '_') {
                        return;
                    }

                    AudioManager.playMoveSound();
                    client.send("MOVE " + row + " " + col);

                    myTurn = false;
                    updateTurnLabel();
                });

                cells[r][c] = cell;
                grid.add(cell, c, r);
            }
        }

        gameOver = false;
    }

    // =========================================================
    // ОКНО ПОДТВЕРЖДЕНИЯ
    // =========================================================
    private void showConfirmDialog(String title, String header, String message, String emoji) {
        VBox dialogBox = new VBox(15);
        dialogBox.setAlignment(Pos.CENTER);
        dialogBox.setStyle(
                "-fx-background-color: #FFDAB9;" +
                        "-fx-background-radius: 20;" +
                        "-fx-border-color: #8B5A2B;" +
                        "-fx-border-width: 2;" +
                        "-fx-border-radius: 20;" +
                        "-fx-padding: 25 35;" +
                        "-fx-min-width: 280;" +
                        "-fx-min-height: 200;" +
                        "-fx-max-width: 280;" +
                        "-fx-max-height: 200;" +
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

        Label messageLabel = new Label(message);
        messageLabel.setStyle(
                "-fx-text-fill: #5C3317;" +
                        "-fx-font-size: 12px;" +
                        "-fx-font-family: 'Segoe UI';" +
                        "-fx-wrap-text: true;" +
                        "-fx-text-alignment: center;"
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
                    stage.setScene(new GameView().createScene());
                }
            });
            client.close();
        });

        noButton.setOnAction(e -> {
            overlayPane.setVisible(false);
        });

        buttonBox.getChildren().addAll(yesButton, noButton);
        dialogBox.getChildren().addAll(emojiLabel, titleLabel, headerLabel, messageLabel, buttonBox);

        overlayPane.getChildren().clear();
        overlayPane.getChildren().add(dialogBox);
        overlayPane.setVisible(true);
    }

    // =========================================================
    // ОКНО РЕЗУЛЬТАТА
    // =========================================================
    private void showResultDialog(String title, String message, String emoji) {
        gameOver = true;
        myTurn = false;

        Platform.runLater(() -> {
            if (title.equals("ПОБЕДА!")) {
                AudioManager.playWinSound();
            } else if (title.equals("ПОРАЖЕНИЕ!")) {
                AudioManager.playLoseSound();
            } else if (title.equals("НИЧЬЯ!")) {
                AudioManager.playDrawSound();
            } else if (title.equals("СОПЕРНИК ВЫШЕЛ")) {
                AudioManager.playMessageSound();
            }
        });

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

        Button exitButton = new Button("Выйти в меню");
        exitButton.setStyle(
                "-fx-background-color: #8B5A2B;" +
                        "-fx-text-fill: white;" +
                        "-fx-font-size: 12px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-background-radius: 12;" +
                        "-fx-padding: 6 20;" +
                        "-fx-cursor: hand;"
        );
        exitButton.setOnMouseEntered(e -> exitButton.setStyle(
                "-fx-background-color: #A0522D;" +
                        "-fx-text-fill: white;" +
                        "-fx-font-size: 12px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-background-radius: 12;" +
                        "-fx-padding: 6 20;" +
                        "-fx-cursor: hand;"
        ));
        exitButton.setOnMouseExited(e -> exitButton.setStyle(
                "-fx-background-color: #8B5A2B;" +
                        "-fx-text-fill: white;" +
                        "-fx-font-size: 12px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-background-radius: 12;" +
                        "-fx-padding: 6 20;" +
                        "-fx-cursor: hand;"
        ));

        exitButton.setOnAction(e -> {
            overlayPane.setVisible(false);
            Platform.runLater(() -> {
                Stage stage = ScreenManager.getStage();
                if (stage != null) {
                    stage.setScene(new GameView().createScene());
                }
            });
            client.close();
        });

        buttonBox.getChildren().addAll(exitButton);
        dialogBox.getChildren().addAll(emojiLabel, titleLabel, messageLabel, buttonBox);

        overlayPane.getChildren().clear();
        overlayPane.getChildren().add(dialogBox);
        overlayPane.setVisible(true);
    }

    // =========================================================
    // ОБНОВЛЕНИЕ СЧЁТА
    // =========================================================
    private void updateScores() {
        Platform.runLater(() -> {
            playerScoreLabel.setText(String.valueOf(playerScore));
            opponentScoreLabel.setText(String.valueOf(opponentScore));
        });
    }

    // =========================================================
    // ОБНОВЛЕНИЕ ТЕКСТА ХОДА
    // =========================================================
    private void updateTurnLabel() {
        Platform.runLater(() -> {
            if (myTurn && !gameOver) {
                turnLabel.setText("🔥 ВАШ ХОД! 🔥");
                turnLabel.setStyle(
                        "-fx-text-fill: #FFD700;" +
                                "-fx-font-size: 18px;" +
                                "-fx-font-weight: bold;" +
                                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.5), 5, 0.1, 0, 2);"
                );
            } else if (!gameOver) {
                turnLabel.setText("⏳ ХОД СОПЕРНИКА... ⏳");
                turnLabel.setStyle(
                        "-fx-text-fill: #FFFFFF;" +
                                "-fx-font-size: 16px;" +
                                "-fx-font-weight: bold;"
                );
            }
        });
    }

    // =========================================================
    // ОБНОВЛЕНИЕ ДОСКИ (ЛОГИКА ИЗ РАБОЧЕЙ ВЕРСИИ)
    // =========================================================
    private void updateBoard(String data) {
        Platform.runLater(() -> {
            for (int r = 0; r < size; r++) {
                for (int c = 0; c < size; c++) {
                    int index = r * size + c;
                    if (index >= data.length()) return;
                    char ch = data.charAt(index);
                    // СОХРАНЯЕМ ТО, ЧТО ПРИСЛАЛ СЕРВЕР (как в рабочей версии)
                    board[r][c] = ch;

                    if (ch == '_') {
                        cells[r][c].setText("");
                        cells[r][c].setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-background-color: #f0f0f0; -fx-border-color: #8B5A2B; -fx-border-width: 2;");
                    } else {
                        cells[r][c].setText(String.valueOf(ch));
                        if (ch == 'X') {
                            cells[r][c].setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: red; -fx-background-color: #ffe0e0; -fx-border-color: #8B5A2B; -fx-border-width: 2;");
                        } else if (ch == 'O') {
                            cells[r][c].setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: blue; -fx-background-color: #e0e0ff; -fx-border-color: #8B5A2B; -fx-border-width: 2;");
                        }
                    }
                }
            }
        });
    }

    // =========================================================
    // ОБРАБОТКА СООБЩЕНИЙ ОТ СЕРВЕРА
    // =========================================================
    public void handleServer(String msg) {
        System.out.println("OnlineGameView received: " + msg);

        if (msg.startsWith("[Общий]") || msg.startsWith("[ЛС]") || msg.startsWith("[ВЫ ->")) {
            addChatMessage(msg);
            return;
        }

        if (msg.equals("START")) {
            gameOver = false;
            Platform.runLater(() -> {
                turnLabel.setText("Игра началась!");
            });
            return;
        }

        if (msg.startsWith("ROLE")) {
            mySymbol = msg.contains("X") ? 'X' : 'O';
            System.out.println("My symbol: " + mySymbol);
            return;
        }

        if (msg.startsWith("STATE")) {
            String[] parts = msg.split(" ");
            if (parts.length >= 3) {
                updateBoard(parts[2]);
            }
            return;
        }

        if (msg.equals("TURN YOUR")) {
            myTurn = true;
            updateTurnLabel();
            return;
        }

        if (msg.equals("TURN WAIT")) {
            myTurn = false;
            updateTurnLabel();
            return;
        }

        if (msg.equals("WIN")) {
            playerScore++;
            updateScores();
            showResultDialog("ПОБЕДА!", "Вы выиграли игру! 🎉", "🏆");
            client.close();
            return;
        }

        if (msg.equals("LOSE")) {
            opponentScore++;
            updateScores();
            showResultDialog("ПОРАЖЕНИЕ!", "Вы проиграли... 💀", "😞");
            client.close();
            return;
        }

        if (msg.equals("DRAW")) {
            showResultDialog("НИЧЬЯ!", "Ничья! 🤝", "🤝");
            client.close();
            return;
        }

        if (msg.equals("OPPONENT_LEFT")) {
            showResultDialog("СОПЕРНИК ВЫШЕЛ", "Соперник покинул игру", "👋");
            client.close();
            return;
        }

        if (msg.equals("NOT YOUR TURN")) {
            myTurn = false;
            updateTurnLabel();
            Platform.runLater(() -> {
                turnLabel.setText("❌ СЕЙЧАС НЕ ВАШ ХОД! ❌");
            });
            return;
        }

        if (msg.equals("INVALID")) {
            myTurn = true;
            updateTurnLabel();
            Platform.runLater(() -> {
                turnLabel.setText("❌ НЕВЕРНЫЙ ХОД! Попробуйте другую клетку ❌");
            });
            return;
        }
    }
}