package manager;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class AudioManager {

    private static MediaPlayer musicPlayer;

    private static double musicVolume = 0.5;
    private static double soundVolume = 0.5;

    private static boolean musicMuted = false;
    private static boolean soundMuted = false;

    private static void log(String msg) {
        System.out.println("[AUDIO] " + msg);
    }

    // =========================
    // MUSIC
    // =========================

    public static void playMusic(String path) {

        var url = AudioManager.class.getResource(path);

        // 🔥 ВОТ ЭТИ СТРОКИ ДОБАВЬ

        log("PATH = " + path);

        log("URL = " + url);

        if (url == null) {
            System.out.println("❌ AUDIO NOT FOUND: " + path);
            return;
        }

        System.out.println("✔ AUDIO URL: " + url);

        stopMusic();
        try {
            Media media = new Media(url.toExternalForm());

            musicPlayer = new MediaPlayer(media);

            musicPlayer.setCycleCount(MediaPlayer.INDEFINITE);

            musicPlayer.setVolume(musicMuted ? 0.0 : musicVolume);

            musicPlayer.setOnReady(() -> log("✔ MUSIC READY"));

            musicPlayer.setOnError(() ->
                    log("❌ MEDIA ERROR: " + musicPlayer.getError())
            );

            musicPlayer.play();

        } catch (Exception e) {
            log("❌ EXCEPTION WHILE PLAYING MUSIC");
            e.printStackTrace();
        }
    }

    public static void stopMusic() {

        if (musicPlayer != null) {
            musicPlayer.stop();
            musicPlayer.dispose();
            musicPlayer = null;
        }
    }

    public static void setMusicVolume(double v) {

        musicVolume = v;

        if (musicPlayer != null && !musicMuted) {
            musicPlayer.setVolume(musicVolume);
        }
    }

    public static double getMusicVolume() {
        return musicVolume;
    }

    public static boolean toggleMusic() {

        musicMuted = !musicMuted;

        if (musicPlayer != null) {
            musicPlayer.setVolume(musicMuted ? 0.0 : musicVolume);
        }

        log("Music muted: " + musicMuted);

        return musicMuted;
    }

    // =========================
    // SOUND EFFECTS
    // =========================

    public static void playSound(String path) {

        if (soundMuted) return;

        var url = AudioManager.class.getResource(path);

        if (url == null) {
            log("❌ SOUND NOT FOUND: " + path);
            return;
        }

        try {
            Media media = new Media(url.toExternalForm());
            MediaPlayer player = new MediaPlayer(media);

            player.setVolume(soundVolume);

            player.setOnReady(() -> log("✔ SOUND READY"));

            player.setOnError(() ->
                    log("❌ SOUND ERROR: " + player.getError())
            );

            player.setOnEndOfMedia(() -> {
                player.dispose();
                log("✔ SOUND DISPOSED");
            });

            player.play();

        } catch (Exception e) {
            log("❌ EXCEPTION WHILE PLAYING SOUND");
            e.printStackTrace();
        }
    }

    public static void setSoundVolume(double v) {
        soundVolume = v;
    }

    public static double getSoundVolume() {
        return soundVolume;
    }

    public static boolean toggleSound() {
        soundMuted = !soundMuted;
        log("Sound muted: " + soundMuted);
        return soundMuted;
    }
}