package view;

import Backend.Difficulty;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import manager.ScreenManager;
import network.TCPClient;

import java.net.URL;

public class GameView {

    private VBox menu;
    private Text title;

    private TCPClient client;

    // Компоненты для кастомного окна
    private StackPane overlayPane;
    private BorderPane rootContainer;

    public Scene createScene() {

        StackPane root = new StackPane();
        root.getStyleClass().add("game-root");

        menu = new VBox(20);
        menu.setAlignment(Pos.CENTER);
        menu.getStyleClass().add("game-menu");

        title = new Text();
        title.getStyleClass().add("game-title");

        showOpponentMenu();

        root.getChildren().add(menu);

        Scene scene = new Scene(root, 1920, 1080);

        // Проверка на существование CSS файла
        URL cssUrl = getClass().getResource("/css/game.css");
        if (cssUrl != null) {
            scene.getStylesheets().add(cssUrl.toExternalForm());
        } else {
            System.err.println("Предупреждение: CSS файл не найден по пути /css/game.css");
        }

        // Восстанавливаем полноэкранный режим из настроек
        ScreenManager.restoreFullscreen();

        return scene;
    }

    // =========================================================
    // ПРИМЕНЕНИЕ ПОЛНОЭКРАННОГО РЕЖИМА (С УЧЁТОМ НАСТРОЕК)
    // =========================================================
    private void applyFullscreen(Stage stage) {
        if (stage == null) return;

        Platform.runLater(() -> {
            if (ScreenManager.isFullscreen() && !stage.isFullScreen()) {
                stage.setFullScreen(true);
                System.out.println("Fullscreen applied from settings");

                // Дополнительная проверка для macOS
                new Thread(() -> {
                    try {
                        Thread.sleep(100);
                        Platform.runLater(() -> {
                            if (!stage.isFullScreen()) {
                                stage.setFullScreen(true);
                                System.out.println("Fullscreen re-applied for macOS");
                            }
                        });
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }).start();
            }
        });
    }

    // =========================================================
    // КАСТОМНОЕ ОКНО ДЛЯ ВВОДА IP (БЕЗ ПОЛЯ ПОРТА)
    // =========================================================
    private void showCustomIpDialog(int gameSize) {
        // Применяем полноэкранный режим из настроек
        Stage stage = ScreenManager.getStage();
        applyFullscreen(stage);

        // Создаём затемняющий фон
        StackPane overlay = new StackPane();
        overlay.setStyle("-fx-background-color: rgba(0,0,0,0.7);");
        overlay.setAlignment(Pos.CENTER);

        // Создаём диалоговое окно
        VBox dialogBox = new VBox(15);
        dialogBox.setAlignment(Pos.CENTER);
        dialogBox.setStyle(
                "-fx-background-color: #FFDAB9;" +
                        "-fx-background-radius: 20;" +
                        "-fx-border-color: #8B5A2B;" +
                        "-fx-border-width: 2;" +
                        "-fx-border-radius: 20;" +
                        "-fx-padding: 25 35;" +
                        "-fx-min-width: 380;" +
                        "-fx-min-height: 250;" +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 15, 0.1, 0, 5);"
        );

        // Заголовок
        Label titleLabel = new Label("🌐 ПОДКЛЮЧЕНИЕ К СЕРВЕРУ");
        titleLabel.setStyle(
                "-fx-text-fill: #5C3317;" +
                        "-fx-font-size: 20px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-font-family: 'Segoe UI';"
        );

        // Подзаголовок
        Label subtitleLabel = new Label("Введите IP-адрес сервера");
        subtitleLabel.setStyle(
                "-fx-text-fill: #5C3317;" +
                        "-fx-font-size: 14px;" +
                        "-fx-font-family: 'Segoe UI';"
        );

        // Поле ввода IP
        TextField ipField = new TextField("localhost");
        ipField.setStyle(
                "-fx-background-color: #FFF8E7;" +
                        "-fx-border-color: #8B5A2B;" +
                        "-fx-border-radius: 10;" +
                        "-fx-background-radius: 10;" +
                        "-fx-padding: 10;" +
                        "-fx-font-size: 14px;" +
                        "-fx-font-family: 'Segoe UI';"
        );
        ipField.setPromptText("Введите IP адрес (например: 192.168.1.100)");

        // Кнопки
        HBox buttonBox = new HBox(15);
        buttonBox.setAlignment(Pos.CENTER);

        Button connectButton = new Button("🔌 ПОДКЛЮЧИТЬСЯ");
        connectButton.setStyle(
                "-fx-background-color: #8B5A2B;" +
                        "-fx-text-fill: white;" +
                        "-fx-font-size: 14px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-background-radius: 12;" +
                        "-fx-padding: 8 20;" +
                        "-fx-cursor: hand;"
        );
        connectButton.setOnMouseEntered(e -> connectButton.setStyle(
                "-fx-background-color: #A0522D;" +
                        "-fx-text-fill: white;" +
                        "-fx-font-size: 14px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-background-radius: 12;" +
                        "-fx-padding: 8 20;" +
                        "-fx-cursor: hand;"
        ));
        connectButton.setOnMouseExited(e -> connectButton.setStyle(
                "-fx-background-color: #8B5A2B;" +
                        "-fx-text-fill: white;" +
                        "-fx-font-size: 14px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-background-radius: 12;" +
                        "-fx-padding: 8 20;" +
                        "-fx-cursor: hand;"
        ));

        Button cancelButton = new Button("❌ ОТМЕНА");
        cancelButton.setStyle(
                "-fx-background-color: #8B5A2B;" +
                        "-fx-text-fill: white;" +
                        "-fx-font-size: 14px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-background-radius: 12;" +
                        "-fx-padding: 8 20;" +
                        "-fx-cursor: hand;"
        );
        cancelButton.setOnMouseEntered(e -> cancelButton.setStyle(
                "-fx-background-color: #A0522D;" +
                        "-fx-text-fill: white;" +
                        "-fx-font-size: 14px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-background-radius: 12;" +
                        "-fx-padding: 8 20;" +
                        "-fx-cursor: hand;"
        ));
        cancelButton.setOnMouseExited(e -> cancelButton.setStyle(
                "-fx-background-color: #8B5A2B;" +
                        "-fx-text-fill: white;" +
                        "-fx-font-size: 14px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-background-radius: 12;" +
                        "-fx-padding: 8 20;" +
                        "-fx-cursor: hand;"
        ));

        connectButton.setOnAction(e -> {
            String serverIp = ipField.getText().trim();
            if (serverIp.isEmpty()) {
                serverIp = "localhost";
            }

            // Удаляем overlay из корневой сцены
            StackPane root = (StackPane) menu.getParent();
            root.getChildren().remove(overlay);

            // Подключаемся к серверу
            connectToServer(serverIp, gameSize);
        });

        cancelButton.setOnAction(e -> {
            StackPane root = (StackPane) menu.getParent();
            root.getChildren().remove(overlay);
        });

        buttonBox.getChildren().addAll(connectButton, cancelButton);
        dialogBox.getChildren().addAll(titleLabel, subtitleLabel, ipField, buttonBox);
        overlay.getChildren().add(dialogBox);

        // Добавляем overlay на корневую сцену
        StackPane root = (StackPane) menu.getParent();
        root.getChildren().add(overlay);
    }

    // =========================================================
    // ПОДКЛЮЧЕНИЕ К СЕРВЕРУ
    // =========================================================
    private void connectToServer(String serverIp, int gameSize) {
        OnlineGameView[] holder = new OnlineGameView[1];

        client = new TCPClient(
                serverIp,
                12345,
                msg -> {
                    if (holder[0] != null) {
                        Platform.runLater(() -> holder[0].handleServer(msg));
                    }
                }
        );

        holder[0] = new OnlineGameView(client, gameSize);

        Stage stage = ScreenManager.getStage();
        Scene gameScene = holder[0].createScene();

        // Устанавливаем сцену и применяем полноэкранный режим из настроек
        stage.setScene(gameScene);
        applyFullscreen(stage);
    }

    // =========================================================
    // OPPONENT MENU
    // =========================================================

    private void showOpponentMenu() {

        menu.getChildren().clear();

        title.setText("ВЫБЕРИТЕ СОПЕРНИКА");

        Button player = new Button("Игрок");
        Button bot = new Button("Бот");
        Button back = new Button("Назад");

        player.getStyleClass().add("game-button");
        bot.getStyleClass().add("game-button");
        back.getStyleClass().add("game-button");

        player.setOnAction(e -> showSizeMenu(false));
        bot.setOnAction(e -> showDifficultyMenu());

        back.setOnAction(e -> {
            Stage stage = ScreenManager.getStage();
            MainMenuView menuView = new MainMenuView();
            stage.setScene(menuView.createScene(stage));
        });

        menu.getChildren().addAll(title, player, bot, back);
    }

    // =========================================================
    // DIFFICULTY MENU
    // =========================================================

    private void showDifficultyMenu() {

        menu.getChildren().clear();

        title.setText("СЛОЖНОСТЬ БОТА");

        Button easy = new Button("Лёгкий");
        Button medium = new Button("Средний");
        Button hard = new Button("Сложный");
        Button back = new Button("Назад");

        easy.getStyleClass().add("game-button");
        medium.getStyleClass().add("game-button");
        hard.getStyleClass().add("game-button");
        back.getStyleClass().add("game-button");

        easy.setOnAction(e -> showSizeMenu(true, Difficulty.EASY));
        medium.setOnAction(e -> showSizeMenu(true, Difficulty.MEDIUM));
        hard.setOnAction(e -> showSizeMenu(true, Difficulty.HARD));

        back.setOnAction(e -> showOpponentMenu());

        menu.getChildren().addAll(title, easy, medium, hard, back);
    }

    // =========================================================
    // SIZE MENU
    // =========================================================

    private void showSizeMenu(boolean botMode) {
        showSizeMenu(botMode, Difficulty.MEDIUM);
    }

    private void showSizeMenu(boolean botMode, Difficulty difficulty) {

        menu.getChildren().clear();

        title.setText(
                botMode ? "ИГРА ПРОТИВ БОТА" : "ИГРА ПРОТИВ ИГРОКА"
        );

        Button size3 = new Button("3 x 3");
        Button size9 = new Button("9 x 9");
        Button back = new Button("Назад");

        size3.getStyleClass().add("game-button");
        size9.getStyleClass().add("game-button");
        back.getStyleClass().add("game-button");

        size3.setOnAction(e -> startGame(botMode, 3, difficulty));
        size9.setOnAction(e -> startGame(botMode, 9, difficulty));

        back.setOnAction(e -> {
            if (botMode) {
                showDifficultyMenu();
            } else {
                showOpponentMenu();
            }
        });

        menu.getChildren().addAll(title, size3, size9, back);
    }

    // =========================================================
    // START GAME
    // =========================================================

    private void startGame(
            boolean botMode,
            int size,
            Difficulty difficulty
    ) {

        Stage stage = ScreenManager.getStage();

        // =========================================================
        // BOT GAME
        // =========================================================

        if (botMode) {

            BotGameView gameView = new BotGameView(difficulty);

            Scene gameScene = gameView.createScene(size);

            // Устанавливаем сцену и применяем полноэкранный режим из настроек
            stage.setScene(gameScene);
            applyFullscreen(stage);
            return;
        }

        // =========================================================
        // ONLINE GAME (с кастомным окном ввода IP)
        // =========================================================

        // Показываем кастомное окно для ввода IP
        showCustomIpDialog(size);
    }
}