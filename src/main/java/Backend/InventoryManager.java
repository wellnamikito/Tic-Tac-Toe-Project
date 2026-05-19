package Backend;

import java.util.*;

/**
 * Класс хранит информацию о том, какие предметы уже куплены.
 */
public class InventoryManager {
    private final Map<String, Set<String>> ownedItems = new HashMap<>();

    public void addItem(String userId, String itemId) {
        ownedItems.computeIfAbsent(userId, k -> new HashSet<>()).add(itemId);
    }

    // Фронтенд: используйте, чтобы скрыть кнопку "Купить" или написать "Уже куплено"
    public boolean hasItem(String userId, String itemId) {
        return ownedItems.getOrDefault(userId, Collections.emptySet()).contains(itemId);
    }
}

