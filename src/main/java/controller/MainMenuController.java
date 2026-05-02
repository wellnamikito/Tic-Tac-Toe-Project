package controller;

import javafx.stage.Stage;
import view.GameView;

/**
 * Controller управляет переключением экранов
 */
public class MainMenuController {

    private Stage stage;

    public MainMenuController(Stage stage) {
        this.stage = stage;
    }

    // кнопка "Играть"
    public void onPlay() {

        GameView gameView = new GameView();

        // меняем сцену (переход на игру)
        stage.setScene(gameView.createScene());
    }

    public void onRules() {
        System.out.println("Rules screen (позже добавим)");
    }

    public void onSettings() {
        System.out.println("Settings screen (позже добавим)");
    }

    public void onShop() {
        System.out.println("Shop screen (позже добавим)");
    }
}