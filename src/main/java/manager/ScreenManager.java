package manager;

import config.UIConfig;
import javafx.stage.Stage;

public class ScreenManager {

    private static Stage stage;

    public static void setStage(Stage s) {
        stage = s;
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
        stage.setFullScreen(false);
        stage.setWidth(width);
        stage.setHeight(height);
        stage.centerOnScreen();
    }
}