package app;

import javafx.application.Application;
import javafx.stage.Stage;
import view.MainMenuView;

public class Main extends Application {

    @Override
    public void start(Stage stage) {

        MainMenuView menu = new MainMenuView();

        stage.setTitle("Tic Tac Toe");

        stage.setWidth(1280);
        stage.setHeight(720);

        stage.setMinWidth(960);
        stage.setMinHeight(540);

        stage.setScene(menu.createScene(stage));
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}