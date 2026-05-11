package app;

import config.UIConfig;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.stage.Stage;
import manager.AudioManager;
import manager.ScreenManager;
import view.MainMenuView;

public class Main extends Application {

    private static final double RATIO =
            UIConfig.BASE_WIDTH / UIConfig.BASE_HEIGHT;

    private boolean updating = false;
    private boolean pending = false;

    @Override
    public void start(Stage stage) {

        MainMenuView view = new MainMenuView();
        Scene scene = view.createScene(stage);

        stage.setTitle("Tic Tac Toe");
        stage.setScene(scene);

        ScreenManager.setStage(stage);

        stage.setWidth(UIConfig.BASE_WIDTH);
        stage.setHeight(UIConfig.BASE_HEIGHT);

        stage.setMinWidth(960);
        stage.setMinHeight(540);

        final double[] lastW = {stage.getWidth()};
        final double[] lastH = {stage.getHeight()};

        stage.widthProperty().addListener((obs, oldVal, newVal) ->
                requestResize(stage, lastW, lastH));

        stage.heightProperty().addListener((obs, oldVal, newVal) ->
                requestResize(stage, lastW, lastH));

        stage.show();

        // fullscreen запускается ПОСЛЕ show (важно)
        Platform.runLater(ScreenManager::startFullscreen);

        // музыка один раз
        AudioManager.playMusic("/audio/fff.mp3");
    }

    private void requestResize(Stage stage, double[] lastW, double[] lastH) {

        if (updating) return;
        if (pending) return;

        pending = true;

        Platform.runLater(() -> {
            pending = false;
            applyResize(stage, lastW, lastH);
        });
    }

    private void applyResize(Stage stage, double[] lastW, double[] lastH) {

        updating = true;

        double w = stage.getWidth();
        double h = stage.getHeight();

        double dw = Math.abs(w - lastW[0]);
        double dh = Math.abs(h - lastH[0]);

        if (dw > dh) {
            stage.setHeight(w / RATIO);
        } else {
            stage.setWidth(h * RATIO);
        }

        lastW[0] = stage.getWidth();
        lastH[0] = stage.getHeight();

        updating = false;
    }

    public static void main(String[] args) {
        launch();
    }
}