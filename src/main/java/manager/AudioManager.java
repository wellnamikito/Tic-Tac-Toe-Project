package manager;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class AudioManager {

    private static MediaPlayer musicPlayer;

    private static double musicVolume = 0.0;
    private static double soundVolume = 0.5;

    private static boolean musicMuted = false;
    private static boolean soundMuted = false;

    // =========================
    // ЗВУКОВЫЕ ЭФФЕКТЫ (ПУТИ К ФАЙЛАМ)
    // =========================
    private static final String SOUND_MOVE = "/audio/move.wav";
    private static final String SOUND_WIN  = "/audio/win.wav";
    private static final String SOUND_LOSE = "/audio/lose.wav";
    private static final String SOUND_DRAW = "/audio/draw.wav";
    private static final String SOUND_MESSAGE = "/audio/message.wav";

    private static void log(String msg) {
        System.out.println("[AUDIO] " + msg);
    }

    // =========================
    // MUSIC
    // =========================

    public static void playMusic(String path) {
        if (path == null || path.isEmpty()) {
            log("❌ MUSIC PATH IS NULL OR EMPTY");
            return;
        }

        var url = AudioManager.class.getResource(path);

        log("PATH = " + path);
        log("URL = " + url);

        if (url == null) {
            log("❌ MUSIC FILE NOT FOUND: " + path);
            return;
        }

        log("✔ AUDIO URL: " + url);

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
        if (soundMuted) {
            log("SOUND MUTED, SKIPPING: " + path);
            return;
        }

        if (path == null || path.isEmpty()) {
            log("❌ SOUND PATH IS NULL OR EMPTY");
            return;
        }

        var url = AudioManager.class.getResource(path);

        if (url == null) {
            log("❌ SOUND FILE NOT FOUND: " + path + " - SOUND WILL BE SKIPPED");
            return;
        }

        try {
            Media media = new Media(url.toExternalForm());
            MediaPlayer player = new MediaPlayer(media);
            player.setVolume(soundVolume);
            player.setOnReady(() -> log("✔ SOUND READY: " + path));
            player.setOnError(() ->
                    log("❌ SOUND ERROR: " + player.getError())
            );
            player.setOnEndOfMedia(() -> {
                player.dispose();
                log("✔ SOUND DISPOSED: " + path);
            });
            player.play();

        } catch (Exception e) {
            log("❌ EXCEPTION WHILE PLAYING SOUND: " + path);
            e.printStackTrace();
        }
    }

    // =========================
    // УДОБНЫЕ МЕТОДЫ ДЛЯ ИГРОВЫХ СОБЫТИЙ С ПРОВЕРКОЙ
    // =========================

    /**
     * Воспроизвести звук хода
     */
    public static void playMoveSound() {
        log("Attempting to play move sound...");
        playSound(SOUND_MOVE);
    }

    /**
     * Воспроизвести звук победы
     */
    public static void playWinSound() {
        log("Attempting to play win sound...");
        playSound(SOUND_WIN);
    }

    /**
     * Воспроизвести звук проигрыша
     */
    public static void playLoseSound() {
        log("Attempting to play lose sound...");
        playSound(SOUND_LOSE);
    }

    /**
     * Воспроизвести звук ничьи
     */
    public static void playDrawSound() {
        log("Attempting to play draw sound...");
        playSound(SOUND_DRAW);
    }

    public static void playMessageSound() {
        log("Attempting to play move sound...");
        playSound(SOUND_MESSAGE);
    }

    public static void setSoundVolume(double v) {
        soundVolume = v;
        log("Sound volume set to: " + v);
    }

    public static double getSoundVolume() {
        return soundVolume;
    }

    public static boolean toggleSound() {
        soundMuted = !soundMuted;
        log("Sound muted: " + soundMuted);
        return soundMuted;
    }

    // =========================
    // ДОПОЛНИТЕЛЬНЫЕ МЕТОДЫ ДЛЯ ОТЛАДКИ
    // =========================

    /**
     * Проверка наличия звуковых файлов
     */
    public static void checkSoundFiles() {
        log("=== CHECKING SOUND FILES ===");
        checkFile(SOUND_MOVE);
        checkFile(SOUND_WIN);
        checkFile(SOUND_LOSE);
        checkFile(SOUND_DRAW);
        log("============================");
    }

    private static void checkFile(String path) {
        var url = AudioManager.class.getResource(path);
        if (url != null) {
            log("✅ FOUND: " + path + " -> " + url);
        } else {
            log("❌ MISSING: " + path);
        }
    }

    // Добавьте в AudioManager
    public static void testSound() {
        System.out.println("[AUDIO] Testing sound system...");
        checkSoundFiles();
        playMoveSound();
    }
}