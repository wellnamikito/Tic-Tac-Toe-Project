package manager;

import config.UIConfig;
import javafx.stage.Stage;
import javafx.application.Platform;

public class ScreenManager {

    private static Stage stage;
    private static boolean fullscreenState = false; // Сохраняем состояние

    // Флаг программного изменения размера
    private static boolean changingResolution = false;

    public static void setStage(Stage s) {
        stage = s;
        // При установке Stage восстанавливаем сохранённое состояние
        if (stage != null && fullscreenState) {
            Platform.runLater(() -> stage.setFullScreen(true));
        }
    }

    public static Stage getStage() {
        return stage;
    }

    public static boolean isChangingResolution() {
        return changingResolution;
    }

    public static void startFullscreen() {
        if (stage == null) return;

        fullscreenState = true;
        Platform.runLater(() -> {
            stage.setFullScreen(true);
            System.out.println("Fullscreen enabled: " + stage.isFullScreen());
        });
    }

    public static boolean toggleFullscreen() {
        if (stage == null) return false;

        boolean newState = !stage.isFullScreen();
        fullscreenState = newState;
        stage.setFullScreen(newState);

        if (!newState) {
            stage.setWidth(UIConfig.BASE_WIDTH);
            stage.setHeight(UIConfig.BASE_HEIGHT);
            stage.centerOnScreen();
        }

        System.out.println("Fullscreen toggled to: " + newState);
        return newState;
    }

    public static void setWindowSize(double width, double height) {
        if (stage == null) return;

        changingResolution = true;

        Platform.runLater(() -> {
            try {
                if (stage.isFullScreen()) {
                    fullscreenState = false;
                    stage.setFullScreen(false);
                }
                stage.setWidth(width);
                stage.setHeight(height);
                stage.centerOnScreen();
            } finally {
                changingResolution = false;
            }
        });
    }

    public static void setFullscreen(boolean fullscreen) {
        if (stage == null) return;

        fullscreenState = fullscreen;
        Platform.runLater(() -> {
            if (fullscreen && !stage.isFullScreen()) {
                stage.setFullScreen(true);
                System.out.println("Fullscreen set to: true");
            } else if (!fullscreen && stage.isFullScreen()) {
                stage.setFullScreen(false);
                stage.setWidth(UIConfig.BASE_WIDTH);
                stage.setHeight(UIConfig.BASE_HEIGHT);
                stage.centerOnScreen();
                System.out.println("Fullscreen set to: false");
            }
        });
    }

    public static boolean isFullscreen() {
        return fullscreenState;
    }

    // Восстановление полноэкранного режима после смены сцены
    public static void restoreFullscreen() {
        if (stage == null) return;

        Platform.runLater(() -> {
            if (fullscreenState && !stage.isFullScreen()) {
                stage.setFullScreen(true);
                System.out.println("Fullscreen restored");
            }
        });
    }
}