package view.panels;

import config.UIConfig;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import manager.AudioManager;
import manager.ScreenManager;

public class SettingsPanel extends StackPane {

    private Pane layout;

    public SettingsPanel() {

        setPrefSize(1368, 549);

        layout = new Pane();
        layout.setPrefSize(1368, 549);

        // =========================
        // PANEL BACKGROUND
        // =========================

        ImageView panel = new ImageView(
                new Image(getClass().getResource("/images/settings_panel.png").toExternalForm())
        );

        panel.setFitWidth(1368);
        panel.setFitHeight(549);

        layout.getChildren().add(panel);

        // =========================
        // FULLSCREEN BUTTON
        // =========================

        Button fullscreenBtn = createToggleButton("Fullscreen");

        fullscreenBtn.setLayoutX(1050);
        fullscreenBtn.setLayoutY(110);

        fullscreenBtn.setOnAction(e -> {

            boolean state = ScreenManager.toggleFullscreen();

            if (state)
                fullscreenBtn.setText("Fullscreen ON");
            else
                fullscreenBtn.setText("Fullscreen OFF");
        });

        // =========================
        // MUSIC VOLUME SLIDER
        // =========================

        Slider musicSlider = new Slider();

        musicSlider.setMin(0);
        musicSlider.setMax(1);
        musicSlider.setValue(AudioManager.getMusicVolume());

        musicSlider.setPrefWidth(300);

        musicSlider.setLayoutX(700);
        musicSlider.setLayoutY(300);

        // MUSIC PERCENT TEXT
        Label musicPercent = new Label();
        musicPercent.setLayoutX(1010);
        musicPercent.setLayoutY(300);
        musicPercent.setStyle("-fx-text-fill: white; -fx-font-size: 18;");

        musicPercent.setText((int)(musicSlider.getValue() * 100) + "%");

        musicSlider.valueProperty().addListener((obs, oldVal, newVal) -> {

            AudioManager.setMusicVolume(newVal.doubleValue());

            int percent = (int)(newVal.doubleValue() * 100);
            musicPercent.setText(percent + "%");

        });


        // =========================
        // MUSIC MUTE BUTTON
        // =========================

        Button musicMute = createToggleButton("Music ON");

        musicMute.setLayoutX(1050);
        musicMute.setLayoutY(207);

        setButtonOnStyle(musicMute);

        musicMute.setOnAction(e -> {

            boolean muted = AudioManager.toggleMusic();

            if (muted) {
                musicMute.setText("Music OFF");
                setButtonOffStyle(musicMute);
            } else {
                musicMute.setText("Music ON");
                setButtonOnStyle(musicMute);
            }
        });

        // =========================
        // SOUND VOLUME SLIDER
        // =========================

        Slider soundSlider = new Slider();

        soundSlider.setMin(0);
        soundSlider.setMax(1);
        soundSlider.setValue(AudioManager.getSoundVolume());

        soundSlider.setPrefWidth(300);

        soundSlider.setLayoutX(700);
        soundSlider.setLayoutY(480);

        // SOUND PERCENT TEXT
        Label soundPercent = new Label();
        soundPercent.setLayoutX(1010);
        soundPercent.setLayoutY(480);
        soundPercent.setStyle("-fx-text-fill: white; -fx-font-size: 18;");

        soundPercent.setText((int)(soundSlider.getValue() * 100) + "%");

        soundSlider.valueProperty().addListener((obs, oldVal, newVal) -> {

            AudioManager.setSoundVolume(newVal.doubleValue());

            int percent = (int)(newVal.doubleValue() * 100);
            soundPercent.setText(percent + "%");

        });

        // =========================
        // SOUND MUTE BUTTON
        // =========================

        Button soundMute = createToggleButton("Sound ON");

        soundMute.setLayoutX(1050);
        soundMute.setLayoutY(387);

        setButtonOnStyle(soundMute);

        soundMute.setOnAction(e -> {

            boolean muted = AudioManager.toggleSound();

            if (muted) {
                soundMute.setText("Sound OFF");
                setButtonOffStyle(soundMute);
            } else {
                soundMute.setText("Sound ON");
                setButtonOnStyle(soundMute);
            }
        });

        layout.getChildren().addAll(
                fullscreenBtn,
                musicSlider,
                musicPercent,
                musicMute,
                soundSlider,
                soundPercent,
                soundMute
        );

        getChildren().add(layout);
        setPadding(new Insets(0));
    }

    // =========================
    // BUTTON STYLE
    // =========================
    private void setButtonOnStyle(Button b) {

        b.setStyle("""
            -fx-background-color: #2ecc71;
            -fx-text-fill: white;
            -fx-font-size: 16;
            -fx-background-radius: 8;
            """);
    }

    private void setButtonOffStyle(Button b) {

        b.setStyle("""
            -fx-background-color: #3c3c3c;
            -fx-text-fill: white;
            -fx-font-size: 16;
            -fx-background-radius: 8;
            """);
    }

    private Button createToggleButton(String text) {

        Button b = new Button(text);

        b.setPrefWidth(160);
        b.setPrefHeight(40);

        // базовый стиль (OFF по умолчанию)
        setButtonOffStyle(b);

        b.setOnMouseEntered(e -> {
            b.setStyle(b.getStyle() + "-fx-cursor: hand;");
        });

        b.setOnMouseExited(e -> {
            b.setStyle(b.getStyle().replace("-fx-cursor: hand;", ""));
        });

        return b;
    }
}