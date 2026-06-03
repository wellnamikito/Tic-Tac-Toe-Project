package manager;

import config.UIConfig;
import javafx.stage.Stage;
import javafx.application.Platform;

public class ScreenManager {

    private static Stage stage;

    // Флаг программного изменения размера
    private static boolean changingResolution = false;

    public static void setStage(Stage s) {
        stage = s;
    }

    public static Stage getStage() {
        return stage;
    }

    public static boolean isChangingResolution() {
        return changingResolution;
    }

    public static void startFullscreen() {

        if (stage == null) return;

        stage.setFullScreen(true);
    }

    public static boolean toggleFullscreen() {

        if (stage == null)
            return false;

        boolean newState = !stage.isFullScreen();

        stage.setFullScreen(newState);

        if (!newState) {

            stage.setWidth(UIConfig.BASE_WIDTH);
            stage.setHeight(UIConfig.BASE_HEIGHT);

            stage.centerOnScreen();
        }

        return newState;
    }

    public static void setWindowSize(double width, double height) {

        if (stage == null) return;

        changingResolution = true;

        Platform.runLater(() -> {
            try {
                stage.setFullScreen(false);

                stage.setWidth(width);
                stage.setHeight(height);

                stage.centerOnScreen();
            } finally {
                changingResolution = false;
            }
        });
    }
    // =========================================================
    // ДОБАВЬТЕ ЭТОТ МЕТОД
    // =========================================================
    public static void setFullscreen(boolean fullscreen) {
        if (stage == null) return;

        if (fullscreen && !stage.isFullScreen()) {
            stage.setFullScreen(true);
        } else if (!fullscreen && stage.isFullScreen()) {
            stage.setFullScreen(false);
            stage.setWidth(UIConfig.BASE_WIDTH);
            stage.setHeight(UIConfig.BASE_HEIGHT);
            stage.centerOnScreen();
        }
    }

    // =========================================================
    // ДОБАВЬТЕ ЭТОТ МЕТОД (для проверки состояния)
    // =========================================================
    public static boolean isFullscreen() {
        return stage != null && stage.isFullScreen();
    }


}