package app;

import config.UIConfig;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.stage.Stage;
import manager.AudioManager;
import manager.ScreenManager;
import view.MainMenuView;
import view.panels.SettingsPanel;

public class Main extends Application {

    private static final double RATIO =
            UIConfig.BASE_WIDTH / UIConfig.BASE_HEIGHT;

    private boolean updating = false;
    private boolean pending = false;
    private boolean lockAspectRatio = true;

    @Override
    public void start(Stage stage) {

        SettingsPanel.setStage(stage);

        MainMenuView view = new MainMenuView();
        Scene scene = view.createScene(stage);

        stage.setTitle("Tic Tac Toe");
        stage.setScene(scene);

        ScreenManager.setStage(stage);

        stage.setWidth(UIConfig.BASE_WIDTH);
        stage.setHeight(UIConfig.BASE_HEIGHT);

        stage.setMinWidth(960);
        stage.setMinHeight(540);

        stage.setIconified(false);
        stage.setAlwaysOnTop(false);
        stage.requestFocus();

        final double[] lastW = {stage.getWidth()};
        final double[] lastH = {stage.getHeight()};

        stage.widthProperty().addListener((obs, oldVal, newVal) ->
                requestResize(stage, lastW, lastH));

        stage.heightProperty().addListener((obs, oldVal, newVal) ->
                requestResize(stage, lastW, lastH));

        stage.showingProperty().addListener((obs, wasShowing, isShowing) -> {
            if (isShowing) {
                stage.toFront();
                stage.requestFocus();
            }
        });

        stage.show();

        Platform.runLater(() -> {
            ScreenManager.startFullscreen();
            stage.toFront();
            stage.requestFocus();
        });

        AudioManager.playMusic("/audio/fff.wav");
    }

    private void requestResize(Stage stage, double[] lastW, double[] lastH) {

        if (ScreenManager.isChangingResolution()) return;
        if (updating || pending) return;

        pending = true;

        Platform.runLater(() -> {
            pending = false;
            applyResize(stage, lastW, lastH);
        });
    }

    private void applyResize(Stage stage, double[] lastW, double[] lastH) {

        if (!lockAspectRatio) return; // <<< ВАЖНО

        if (ScreenManager.isChangingResolution()) return;

        updating = true;

        try {
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

        } finally {
            updating = false;
        }
    }

    public static void main(String[] args) {
        launch();
    }


}