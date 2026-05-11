package view.panels;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import manager.AudioManager;
import manager.ScreenManager;

public class SettingsPanel extends StackPane {

    public SettingsPanel() {

        // ================= BACKDROP =================
        setPrefSize(1368, 768);
        getStyleClass().add("settings-overlay");

        // ================= WINDOW =================
        VBox window = new VBox();
        window.getStyleClass().add("settings-window");

        window.setPrefWidth(720);
        window.setMaxWidth(720);

        window.setPadding(new Insets(28));
        window.setSpacing(20);
        window.setAlignment(Pos.TOP_CENTER);

        StackPane.setAlignment(window, Pos.CENTER);

        // ================= CLOSE BUTTON =================
        Button close = new Button("✕");
        close.getStyleClass().add("settings-close");

        close.setOnAction(e -> this.setVisible(false));

        HBox top = new HBox(close);
        top.setAlignment(Pos.TOP_RIGHT);

        // ================= TITLE =================
        Label title = new Label("НАСТРОЙКИ");
        title.getStyleClass().add("settings-title");

        // ================= GRID =================
        GridPane grid = new GridPane();

        grid.setHgap(30);
        grid.setVgap(18);

        ColumnConstraints c1 = new ColumnConstraints();
        c1.setPercentWidth(45);

        ColumnConstraints c2 = new ColumnConstraints();
        c2.setPercentWidth(55);

        grid.getColumnConstraints().addAll(c1, c2);

        int row = 0;

        // =====================================================
        // RESOLUTION
        // =====================================================

        grid.add(sectionLabel("Разрешение экрана"), 0, row);

        ComboBox<String> resolution = new ComboBox<>();

        resolution.getItems().addAll(
                "1920x1080",
                "1600x900",
                "1280x720"
        );

        resolution.setValue("1920x1080");

        resolution.getStyleClass().add("settings-combo");

        resolution.setPrefWidth(260);

        grid.add(resolution, 1, row++);

        grid.add(new Separator(), 0, row++, 2, 1);

        // =====================================================
        // FULLSCREEN
        // =====================================================

        grid.add(sectionLabel("Режим экрана"), 0, row);

        ToggleButton fullscreen = new ToggleButton("Fullscreen");

        fullscreen.getStyleClass().add("settings-button");

        fullscreen.setPrefWidth(260);

        fullscreen.setOnAction(e -> {

            boolean state = ScreenManager.toggleFullscreen();

            if (state) {
                fullscreen.setText("Fullscreen");
            } else {
                fullscreen.setText("Windowed");
            }

        });

        grid.add(fullscreen, 1, row++);

        grid.add(new Separator(), 0, row++, 2, 1);

        // =====================================================
        // MUSIC
        // =====================================================

        grid.add(sectionLabel("Музыка"), 0, row);

        ToggleButton musicOn = new ToggleButton("Вкл");
        ToggleButton musicOff = new ToggleButton("Выкл");

        musicOn.getStyleClass().add("settings-button");
        musicOff.getStyleClass().add("settings-button");

        musicOn.setSelected(true);

        HBox musicButtons = new HBox(12, musicOn, musicOff);
        musicButtons.setAlignment(Pos.CENTER_LEFT);

        Slider musicSlider = slider(
                AudioManager.getMusicVolume(),
                v -> AudioManager.setMusicVolume(v)
        );

        musicSlider.setPrefWidth(320);

        musicOn.setOnAction(e -> {
            musicOn.setSelected(true);
            musicOff.setSelected(false);
            AudioManager.setMusicVolume(musicSlider.getValue());
        });

        musicOff.setOnAction(e -> {
            musicOff.setSelected(true);
            musicOn.setSelected(false);
            AudioManager.setMusicVolume(0);
        });

        VBox musicBox = new VBox(8, musicButtons, musicSlider);
        musicBox.setAlignment(Pos.CENTER_LEFT);

        grid.add(musicBox, 1, row++);

        grid.add(new Separator(), 0, row++, 2, 1);

        // =====================================================
        // SOUND
        // =====================================================

        grid.add(sectionLabel("Звук"), 0, row);

        ToggleButton soundOn = new ToggleButton("Вкл");
        ToggleButton soundOff = new ToggleButton("Выкл");

        soundOn.getStyleClass().add("settings-button");
        soundOff.getStyleClass().add("settings-button");

        soundOn.setSelected(true);

        HBox soundButtons = new HBox(12, soundOn, soundOff);
        soundButtons.setAlignment(Pos.CENTER_LEFT);

        Slider soundSlider = slider(
                AudioManager.getSoundVolume(),
                v -> AudioManager.setSoundVolume(v)
        );

        soundSlider.setPrefWidth(320);

        soundOn.setOnAction(e -> {
            soundOn.setSelected(true);
            soundOff.setSelected(false);
            AudioManager.setSoundVolume(soundSlider.getValue());
        });

        soundOff.setOnAction(e -> {
            soundOff.setSelected(true);
            soundOn.setSelected(false);
            AudioManager.setSoundVolume(0);
        });

        VBox soundBox = new VBox(8, soundButtons, soundSlider);
        soundBox.setAlignment(Pos.CENTER_LEFT);

        grid.add(soundBox, 1, row++);

        // ================= BUILD =================

        window.getChildren().addAll(
                top,
                title,
                grid
        );

        getChildren().add(window);

        // ================= CSS =================

        getStylesheets().add(
                getClass()
                        .getResource("/css/Settings.css")
                        .toExternalForm()
        );
    }

    // =====================================================
    // LABEL
    // =====================================================

    private Label sectionLabel(String text) {

        Label label = new Label(text);

        label.getStyleClass().add("settings-section");

        label.setAlignment(Pos.CENTER_LEFT);

        label.setMaxWidth(Double.MAX_VALUE);

        return label;
    }

    // =====================================================
    // SLIDER
    // =====================================================

    private Slider slider(
            double value,
            java.util.function.DoubleConsumer onChange
    ) {

        Slider slider = new Slider(0, 1, value);

        slider.getStyleClass().add("settings-slider");

        slider.valueProperty().addListener((obs, oldVal, newVal) ->
                onChange.accept(newVal.doubleValue())
        );

        return slider;
    }
}