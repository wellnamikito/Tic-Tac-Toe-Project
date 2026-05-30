package Backend;

import java.util.*;

/**
 * InventoryManager — менеджер инвентаря пользователей.
 *
 * <p>📌 Отвечает за хранение информации о купленных предметах.</p>
 *
 * <p>Используется для:
 * <ul>
 *   <li>проверки, куплен ли предмет</li>
 *   <li>добавления новых покупок</li>
 *   <li>контроля UI (скрытие кнопок покупки)</li>
 * </ul>
 *
 * <p>Фронтенд использует этот класс через ShopService для отображения состояния магазина.</p>
 */
public class InventoryManager {

    /**
     * Хранилище купленных предметов.
     *
     * <p>Структура:
     * <ul>
     *   <li>key = userId</li>
     *   <li>value = набор itemId, которые уже куплены</li>
     * </ul>
     */
    private final Map<String, Set<String>> ownedItems = new HashMap<>();

    /**
     * Добавляет предмет в инвентарь пользователя.
     *
     * <p>📌 Вызывается автоматически после успешной покупки через ShopService.</p>
     *
     * @param userId ID пользователя
     * @param itemId ID купленного предмета
     */
    public void addItem(String userId, String itemId) {
        ownedItems.computeIfAbsent(userId, k -> new HashSet<>()).add(itemId);
    }

    /**
     * Проверяет, владеет ли пользователь предметом.
     *
     * <p>📌 Используется фронтендом для:
     * <ul>
     *   <li>скрытия кнопки "Купить"</li>
     *   <li>показа статуса "Уже куплено"</li>
     *   <li>блокировки повторной покупки</li>
     * </ul>
     *
     * @param userId ID пользователя
     * @param itemId ID предмета
     * @return true — если предмет уже куплен, иначе false
     */
    public boolean hasItem(String userId, String itemId) {
        return ownedItems.getOrDefault(userId, Collections.emptySet()).contains(itemId);
    }
}