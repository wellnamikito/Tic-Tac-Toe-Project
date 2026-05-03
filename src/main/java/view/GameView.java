package view;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

/**
 * Экран игры (пока заглушка)
 * Позже тут будет поле 3x3
 */
public class GameView {

    public Scene createScene() {

        Text title = new Text("Game Screen");

        Button backButton = new Button("Назад");

        VBox root = new VBox(20);
        root.setAlignment(Pos.CENTER);
        root.getChildren().addAll(title, backButton);

        return new Scene(root, 400, 500);
    }
}