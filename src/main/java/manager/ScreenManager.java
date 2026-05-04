package manager;

import javafx.stage.Stage;

public class ScreenManager {

    private static Stage stage;

    public static void setStage(Stage s) {
        stage = s;
    }

    public static boolean toggleFullscreen() {

        if (stage == null)
            return false;

        boolean newState = !stage.isFullScreen();

        stage.setFullScreen(newState);

        return newState;
    }
}