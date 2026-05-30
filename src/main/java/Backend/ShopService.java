package Backend;

import java.util.*;

/**
 * ShopService — главный сервис магазина приложения.
 *
 * <p>⚡ Основная точка взаимодействия фронтенда с системой магазина.</p>
 *
 * <p>Отвечает за:
 * <ul>
 *   <li>покупку внутриигровой валюты</li>
 *   <li>покупку предметов (фоны, скины, стикеры и т.д.)</li>
 *   <li>хранение каталога товаров</li>
 *   <li>проверку баланса и инвентаря пользователя</li>
 * </ul>
 *
 * <p>Фронтенд должен использовать этот класс как единственный слой доступа к магазину.</p>
 */
public class ShopService {

    /**
     * Управление балансом пользователей (валюта: монеты, реальные деньги и т.д.).
     */
    private final BalanceManager balanceManager;

    /**
     * Управление инвентарём пользователей (купленные предметы).
     */
    private final InventoryManager inventoryManager;

    /**
     * Каталог всех доступных товаров в магазине.
     * key = itemId
     */
    private final Map<String, ShopItem> catalog = new HashMap<>();

    /**
     * Конструктор сервиса магазина.
     *
     * @param bm менеджер баланса пользователей
     * @param im менеджер инвентаря пользователей
     */
    public ShopService(BalanceManager bm, InventoryManager im) {
        this.balanceManager = bm;
        this.inventoryManager = im;
    }

    /**
     * Добавляет товар в каталог магазина.
     *
     * <p>Используется при инициализации сервера или загрузке контента.</p>
     *
     * @param item товар магазина
     */
    public void registerItem(ShopItem item) {
        catalog.put(item.getId(), item);
    }

    /**
     * Покупка внутриигровой валюты за реальные деньги.
     *
     * <p>📌 Используется фронтендом при нажатии "Купить монеты".</p>
     *
     * <p>Конвертация:
     * 1.0 REAL_MONEY = 100 COINS</p>
     *
     * @param userId ID пользователя
     * @param moneyAmount сумма реальных денег
     * @throws InsufficientFundsException если недостаточно средств
     */
    public void buyCoins(String userId, double moneyAmount) throws InsufficientFundsException {
        balanceManager.spend(userId, CurrencyType.REAL_MONEY, moneyAmount);
        balanceManager.addFunds(userId, CurrencyType.COINS, moneyAmount * 100);
    }

    /**
     * Основной метод покупки предмета в магазине.
     *
     * <p>📌 Используется фронтендом для покупки:
     * <ul>
     *   <li>фонов</li>
     *   <li>скинов</li>
     *   <li>стикеров</li>
     *   <li>других внутриигровых предметов</li>
     * </ul>
     *
     * <p>Логика:
     * <ul>
     *   <li>проверяет существование товара</li>
     *   <li>проверяет, не куплен ли он уже</li>
     *   <li>списывает монеты</li>
     *   <li>добавляет в инвентарь</li>
     *   <li>применяет эффект предмета</li>
     * </ul>
     *
     * @param userId ID пользователя
     * @param itemId ID товара
     * @throws InsufficientFundsException если недостаточно монет
     */
    public void performPurchase(String userId, String itemId) throws InsufficientFundsException {
        ShopItem item = catalog.get(itemId);

        if (item == null) return;
        if (inventoryManager.hasItem(userId, itemId)) return;

        balanceManager.spend(userId, CurrencyType.COINS, item.getPrice());

        inventoryManager.addItem(userId, itemId);
        item.onApply(userId);
    }

    /**
     * Получение полного списка товаров магазина.
     *
     * <p>📌 Используется фронтендом для отображения витрины магазина.</p>
     *
     * @return коллекция всех доступных товаров
     */
    public Collection<ShopItem> getAllItems() {
        return catalog.values();
    }
}