package view.panels;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import manager.AudioManager;
import manager.ScreenManager;

public class SettingsPanel extends StackPane {

    // Хранит текущее разрешение только в памяти (в рамках сессии)
    private static String CURRENT_RESOLUTION = "1920x1080";

    public SettingsPanel() {

        // ================= BACKDROP =================
        setPrefSize(1368, 768);
        setStyle("-fx-background-color: transparent;");
        getStyleClass().add("settings-overlay");
        setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);

        // ================= WINDOW (ФИКСИРОВАННЫЙ РАЗМЕР) =================
        VBox window = new VBox();
        window.getStyleClass().add("settings-window");

        window.setPrefWidth(1400);
        window.setMaxWidth(1400);
        window.setMinWidth(1400);

        window.setPrefHeight(800);
        window.setMaxHeight(800);
        window.setMinHeight(800);

        window.setPadding(new Insets(52));
        window.setSpacing(38);
        window.setAlignment(Pos.TOP_CENTER);

        // ================= CLOSE BUTTON =================
        Button closeButton = new Button("Х");
        closeButton.getStyleClass().add("settings-close");
        closeButton.setPrefSize(60, 60);
        closeButton.setCursor(javafx.scene.Cursor.HAND);
        closeButton.setOnAction(e -> this.setVisible(false));

        HBox topBox = new HBox();
        topBox.setAlignment(Pos.TOP_RIGHT);
        topBox.getChildren().add(closeButton);
        topBox.setTranslateY(20);

        // ================= TITLE =================
        Label title = new Label("НАСТРОЙКИ");
        title.getStyleClass().add("settings-title");
        title.setMaxWidth(Double.MAX_VALUE);
        title.setAlignment(Pos.CENTER);
        VBox.setMargin(title, new Insets(-150, 0, 0, 0));

        // ================= GRID =================
        GridPane grid = new GridPane();
        grid.setHgap(56);
        grid.setVgap(33);

        ColumnConstraints c1 = new ColumnConstraints();
        c1.setPercentWidth(45);

        ColumnConstraints c2 = new ColumnConstraints();
        c2.setPercentWidth(55);

        grid.getColumnConstraints().addAll(c1, c2);

        int row = 0;

        // =====================================================
        // RESOLUTION
        // =====================================================
        grid.add(createSectionLabel("Разрешение экрана"), 0, row);

        ComboBox<String> resolution = new ComboBox<>();
        resolution.getItems().addAll("1920x1080", "1600x900", "1280x720");

        // Устанавливаем последнее значение из памяти
        resolution.setValue(CURRENT_RESOLUTION);

        resolution.getStyleClass().add("settings-combo");
        resolution.setPrefWidth(484);
        resolution.setPrefHeight(62);

        // Сохраняем в памяти при выборе
        resolution.setOnAction(e -> {
            String selected = resolution.getValue();
            String[] parts = selected.split("x");
            int width = Integer.parseInt(parts[0]);
            int height = Integer.parseInt(parts[1]);

            // Сохраняем в статическое поле (только в сессии)
            CURRENT_RESOLUTION = selected;

            // Меняем размер окна
            ScreenManager.setWindowSize(width, height);

            // Центрируем панель
            Platform.runLater(() -> {
                window.setLayoutX((getWidth() - window.getPrefWidth()) / 2);
                window.setLayoutY((getHeight() - window.getPrefHeight()) / 2);
            });
        });

        grid.add(resolution, 1, row++);
        grid.add(createSeparator(), 0, row++, 2, 1);

        // =====================================================
        // FULLSCREEN
        // =====================================================
        grid.add(createSectionLabel("Режим экрана"), 0, row);

        ToggleButton fullscreen = new ToggleButton("Fullscreen");
        fullscreen.getStyleClass().add("settings-button");
        fullscreen.setPrefWidth(484);
        fullscreen.setPrefHeight(62);

        fullscreen.setOnAction(e -> {
            boolean state = ScreenManager.toggleFullscreen();
            fullscreen.setText(state ? "Fullscreen" : "Windowed");
            fullscreen.setSelected(state);
        });

        grid.add(fullscreen, 1, row++);
        grid.add(createSeparator(), 0, row++, 2, 1);

        // =====================================================
        // MUSIC
        // =====================================================
        grid.add(createSectionLabel("Музыка"), 0, row);

        ToggleButton musicOff = new ToggleButton("Выкл");
        ToggleButton musicOn = new ToggleButton("Вкл");

        musicOff.getStyleClass().add("settings-button");
        musicOn.getStyleClass().add("settings-button");
        musicOff.setPrefSize(182, 72);
        musicOn.setPrefSize(182, 72);
        musicOn.setSelected(true);

        HBox musicButtons = new HBox(290, musicOff, musicOn);
        musicButtons.setAlignment(Pos.CENTER_LEFT);

        Slider musicSlider = createSlider(
                AudioManager.getMusicVolume(),
                v -> AudioManager.setMusicVolume(v)
        );
        musicSlider.setPrefWidth(595);

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

        VBox musicBox = new VBox(14, musicButtons, musicSlider);
        musicBox.setAlignment(Pos.CENTER_LEFT);

        grid.add(musicBox, 1, row++);
        grid.add(createSeparator(), 0, row++, 2, 1);

        // =====================================================
        // SOUND
        // =====================================================
        grid.add(createSectionLabel("Звук"), 0, row);

        ToggleButton soundOff = new ToggleButton("Выкл");
        ToggleButton soundOn = new ToggleButton("Вкл");

        soundOff.getStyleClass().add("settings-button");
        soundOn.getStyleClass().add("settings-button");
        soundOff.setPrefSize(182, 72);
        soundOn.setPrefSize(182, 72);
        soundOn.setSelected(true);

        HBox soundButtons = new HBox(290, soundOff, soundOn);
        soundButtons.setAlignment(Pos.CENTER_LEFT);

        Slider soundSlider = createSlider(
                AudioManager.getSoundVolume(),
                v -> AudioManager.setSoundVolume(v)
        );
        soundSlider.setPrefWidth(595);

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

        VBox soundBox = new VBox(14, soundButtons, soundSlider);
        soundBox.setAlignment(Pos.CENTER_LEFT);

        grid.add(soundBox, 1, row++);

        // ================= BUILD =================
        window.getChildren().addAll(topBox, title, grid);
        getChildren().add(window);

        // ================= CSS =================
        getStylesheets().add(
                getClass()
                        .getResource("/css/Settings.css")
                        .toExternalForm()
        );

        // ================= ЦЕНТРИРОВАНИЕ =================
        widthProperty().addListener((obs, old, newVal) -> {
            window.setLayoutX((newVal.doubleValue() - window.getPrefWidth()) / 2);
        });
        heightProperty().addListener((obs, old, newVal) -> {
            window.setLayoutY((newVal.doubleValue() - window.getPrefHeight()) / 2);
        });

        Platform.runLater(() -> {
            window.setLayoutX((getWidth() - window.getPrefWidth()) / 2);
            window.setLayoutY((getHeight() - window.getPrefHeight()) / 2);
        });
    }

    private Label createSectionLabel(String text) {
        Label label = new Label(text);
        label.getStyleClass().add("settings-section");
        label.setAlignment(Pos.CENTER_LEFT);
        label.setMaxWidth(Double.MAX_VALUE);
        return label;
    }

    private Separator createSeparator() {
        Separator separator = new Separator();
        separator.setPrefHeight(1);
        return separator;
    }

    private Slider createSlider(double value, java.util.function.DoubleConsumer onChange) {
        Slider slider = new Slider(0, 1, value);
        slider.getStyleClass().add("settings-slider");
        slider.valueProperty().addListener((obs, oldVal, newVal) ->
                onChange.accept(newVal.doubleValue())
        );
        return slider;
    }
}