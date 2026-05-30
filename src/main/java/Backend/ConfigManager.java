package Backend;

import java.io.*;
import java.util.Properties;

/**
 * ConfigManager — менеджер конфигурации сервера.
 *
 * <p>📌 Отвечает за загрузку и хранение настроек приложения.</p>
 *
 * <p>Позволяет изменять параметры сервера без пересборки кода,
 * используя внешний файл <b>server.properties</b>.</p>
 *
 * <p>📌 Используется для:
 * <ul>
 *   <li>настройки порта сервера</li>
 *   <li>ограничения количества игроков</li>
 *   <li>настройки размера игрового поля</li>
 * </ul>
 *
 * <p>Если файл конфигурации отсутствует — используются значения по умолчанию.</p>
 */
public class ConfigManager {

    /**
     * Хранилище конфигурационных параметров.
     */
    private final Properties properties = new Properties();

    /**
     * Имя файла конфигурации.
     */
    private static final String CONFIG_FILE = "server.properties";

    /**
     * Создаёт менеджер конфигурации и автоматически загружает настройки.
     */
    public ConfigManager() {
        loadConfiguration();
    }

    /**
     * Загружает конфигурацию из внешнего файла.
     *
     * <p>📌 Логика:
     * <ul>
     *   <li>если файл существует — загружается</li>
     *   <li>если файл отсутствует — создаются дефолтные значения</li>
     *   <li>если ошибка чтения — также используются дефолты</li>
     * </ul>
     */
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

    /**
     * Устанавливает значения конфигурации по умолчанию.
     *
     * <p>📌 Используется если файл конфигурации отсутствует или повреждён.</p>
     */
    private void setDefaults() {
        properties.setProperty("server.port", "12345");
        properties.setProperty("server.max_clients", "10");
        properties.setProperty("game.board_size", "3");
    }

    /**
     * @return порт сервера (TCP)
     */
    public int getPort() {
        return Integer.parseInt(properties.getProperty("server.port"));
    }

    /**
     * @return максимальное количество подключённых клиентов
     */
    public int getMaxClients() {
        return Integer.parseInt(properties.getProperty("server.max_clients"));
    }

    /**
     * @return размер игрового поля (3 или 9)
     */
    public int getBoardSize() {
        return Integer.parseInt(properties.getProperty("game.board_size"));
    }
}