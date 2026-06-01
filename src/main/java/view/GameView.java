package view;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.geometry.Pos;

import manager.ScreenManager;
import network.TCPClient;

public class GameView {

    private VBox menu;
    private Text title;

    private TCPClient client;

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

        scene.getStylesheets().add(
                getClass().getResource("/css/game.css").toExternalForm()
        );

        return scene;
    }

    // =========================
    // MENU
    // =========================
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
        bot.setOnAction(e -> showSizeMenu(true));

        back.setOnAction(e -> {

            Stage stage = ScreenManager.getStage();
            MainMenuView menuView = new MainMenuView();

            stage.setScene(menuView.createScene(stage));
        });

        menu.getChildren().addAll(title, player, bot, back);
    }

    // =========================
    // SIZE MENU
    // =========================
    private void showSizeMenu(boolean botMode) {

        menu.getChildren().clear();

        title.setText(botMode ? "ИГРА ПРОТИВ БОТА" : "ИГРА ПРОТИВ ИГРОКА");

        Button size3 = new Button("3 x 3");
        Button size9 = new Button("9 x 9");
        Button back = new Button("Назад");

        size3.getStyleClass().add("game-button");
        size9.getStyleClass().add("game-button");
        back.getStyleClass().add("game-button");

        size3.setOnAction(e -> startGame(botMode, 3));
        size9.setOnAction(e -> startGame(botMode, 9));

        back.setOnAction(e -> showOpponentMenu());

        menu.getChildren().addAll(title, size3, size9, back);
    }

    // =========================
    // START GAME
    // =========================
    private void startGame(boolean botMode, int size) {

        Stage stage = ScreenManager.getStage();

        // =========================
        // BOT MODE
        // =========================
        if (botMode) {

            BotGameView gameView = new BotGameView();
            stage.setScene(gameView.createScene(size));
            return;
        }

        // =========================
        // ONLINE MODE
        // =========================

        // создаём клиент ОДИН РАЗ
        client = new TCPClient("localhost", 12345, msg -> {
            System.out.println("SERVER: " + msg);
        });

        client.send("MODE " + size);
        client.send("READY");

        // ⚠ ВАЖНО:
        // OnlineGameView ДОЛЖЕН иметь конструктор:
        // OnlineGameView(TCPClient client, int size)

        OnlineGameView[] holder = new OnlineGameView[1];

        holder[0] = new OnlineGameView(client, size);

        client = new TCPClient("localhost", 12345, msg -> {
            holder[0].handleServer(msg);
        });

        holder[0] = new OnlineGameView(client, size);

        stage.setScene(holder[0].createScene());
    }
}