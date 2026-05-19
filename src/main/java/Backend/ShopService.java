package Backend;

import java.util.*;

/**
 * ГЛАВНЫЙ КЛАСС МАГАЗИНА.
 * Фронтенд: работайте в основном с методами этого класса.
 */
public class ShopService {
    private final BalanceManager balanceManager;
    private final InventoryManager inventoryManager;
    private final Map<String, ShopItem> catalog = new HashMap<>();

    public ShopService(BalanceManager bm, InventoryManager im) {
        this.balanceManager = bm;
        this.inventoryManager = im;
    }

    // Метод для добавления товара в магазин (вызывать при старте игры)
    public void registerItem(ShopItem item) {
        catalog.put(item.getId(), item);
    }

    // Фронтенд: вызывайте при клике на кнопку "Купить монеты"
    public void buyCoins(String userId, double moneyAmount) throws InsufficientFundsException {
        balanceManager.spend(userId, CurrencyType.REAL_MONEY, moneyAmount);
        balanceManager.addFunds(userId, CurrencyType.COINS, moneyAmount * 100); // 1.0 = 100 монет
    }

    // Фронтенд: основной метод для покупки фона/дизайна/стикера
    public void performPurchase(String userId, String itemId) throws InsufficientFundsException {
        ShopItem item = catalog.get(itemId);

        if (item == null) return;
        if (inventoryManager.hasItem(userId, itemId)) return;

        // Если денег не хватит, метод spend сам выбросит исключение InsufficientFundsException
        balanceManager.spend(userId, CurrencyType.COINS, item.getPrice());

        // Если дошли сюда - покупка успешна
        inventoryManager.addItem(userId, itemId);
        item.onApply(userId);
    }

    // Фронтенд: используйте для отрисовки витрины магазина
    public Collection<ShopItem> getAllItems() {
        return catalog.values();
    }
}
