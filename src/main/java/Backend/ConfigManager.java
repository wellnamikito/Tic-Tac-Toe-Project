package Backend;

import java.io.*;
import java.util.Properties;

/**
 • Менеджер конфигурации.
 • Позволяет изменять настройки сервера без пересборки кода.
 */
public class ConfigManager {
    private final Properties properties = new Properties();
    private static final String CONFIG_FILE = "server.properties";

    public ConfigManager() {
        loadConfiguration();
    }

    private void loadConfiguration() {
        File file = new File(CONFIG_FILE);
        if (file.exists()) {
            try (InputStream input = new FileInputStream(file)) {
                properties.load(input);
                System.out.println("[CONFIG] Настройки загружены из внешнего файла.");
            } catch (IOException ex) {
                System.err.println("[CONFIG] Ошибка при чтении файла. Используем дефолты.");
                setDefaults();
            }
        } else {
            System.out.println("[CONFIG] Файл настроек не найден. Созданы параметры по умолчанию.");
            setDefaults();
        }
    }

    private void setDefaults() {
        properties.setProperty("server.port", "12345");
        properties.setProperty("server.max_clients", "10");
        properties.setProperty("game.board_size", "3");
    }

    public int getPort() {
        return Integer.parseInt(properties.getProperty("server.port"));
    }

    public int getMaxClients() {
        return Integer.parseInt(properties.getProperty("server.max_clients"));
    }

    public int getBoardSize() {
        return Integer.parseInt(properties.getProperty("game.board_size"));
    }
}
