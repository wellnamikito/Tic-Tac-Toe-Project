package view;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import manager.ScreenManager;

public class GameView {

    private VBox menu;
    private Text title;

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

        Scene scene = new Scene(
                root,
                1920,
                1080
        );

        scene.getStylesheets().add(
                getClass()
                        .getResource("/css/game.css")
                        .toExternalForm()
        );

        return scene;
    }

    // =====================================
    // ВЫБОР СОПЕРНИКА
    // =====================================

    private void showOpponentMenu() {

        menu.getChildren().clear();

        title.setText("ВЫБЕРИТЕ СОПЕРНИКА");

        Button player =
                new Button("Игрок");

        Button bot =
                new Button("Бот");

        Button back =
                new Button("Назад");

        player.getStyleClass().add("game-button");
        bot.getStyleClass().add("game-button");
        back.getStyleClass().add("game-button");

        player.setOnAction(e ->
                showSizeMenu(false)
        );

        bot.setOnAction(e ->
                showSizeMenu(true)
        );

        back.setOnAction(e -> {

            Stage stage =
                    ScreenManager.getStage();

            MainMenuView menuView =
                    new MainMenuView();

            stage.setScene(
                    menuView.createScene(stage)
            );
        });

        menu.getChildren().addAll(
                title,
                player,
                bot,
                back
        );
    }

    // =====================================
    // ВЫБОР РАЗМЕРА ПОЛЯ
    // =====================================

    private void showSizeMenu(boolean botMode) {

        menu.getChildren().clear();

        if (botMode) {
            title.setText("ИГРА ПРОТИВ БОТА");
        } else {
            title.setText("ИГРА ПРОТИВ ИГРОКА");
        }

        Button size3 =
                new Button("3 x 3");

        Button size9 =
                new Button("9 x 9");

        Button back =
                new Button("Назад");

        size3.getStyleClass().add("game-button");
        size9.getStyleClass().add("game-button");
        back.getStyleClass().add("game-button");

        size3.setOnAction(e -> {

            Stage stage =
                    ScreenManager.getStage();

            if (botMode) {

                BotGameView gameView =
                        new BotGameView();

                stage.setScene(
                        gameView.createScene(3)
                );

            } else {

                System.out.println(
                        "Игрок 3x3"
                );

                // тут потом будет TCP:
                // MODE 3
                // READY
            }
        });

        size9.setOnAction(e -> {

            Stage stage =
                    ScreenManager.getStage();

            if (botMode) {

                BotGameView gameView =
                        new BotGameView();

                stage.setScene(
                        gameView.createScene(9)
                );

            } else {

                System.out.println(
                        "Игрок 9x9"
                );

                // тут потом будет TCP:
                // MODE 9
                // READY
            }
        });

        back.setOnAction(e ->
                showOpponentMenu()
        );

        menu.getChildren().addAll(
                title,
                size3,
                size9,
                back
        );
    }
}