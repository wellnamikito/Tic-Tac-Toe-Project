package Backend;

/**
 * ShopItem — базовый абстрактный класс для всех товаров магазина.
 *
 * <p>📌 Используется как основа для любых покупаемых предметов:
 * <ul>
 *   <li>фоны (backgrounds)</li>
 *   <li>скины</li>
 *   <li>стикеры</li>
 *   <li>другие косметические или игровые предметы</li>
 * </ul>
 *
 * <p>Фронтенд НЕ создает этот класс напрямую — только работает с его наследниками.</p>
 *
 * <p>После успешной покупки автоматически вызывается метод {@link #onApply(String)},
 * который активирует эффект предмета.</p>
 */
public abstract class ShopItem {

    /**
     * Уникальный идентификатор товара.
     * <p>Пример: "BG_SPACE_01"</p>
     */
    private final String id;

    /**
     * Название товара для отображения во фронтенде.
     * <p>Пример: "Космос"</p>
     */
    private final String name;

    /**
     * Цена товара в игровой валюте (монеты).
     */
    private final int price;

    /**
     * Создаёт новый товар магазина.
     *
     * @param id уникальный идентификатор товара
     * @param name отображаемое название товара
     * @param price цена в монетах
     */
    public ShopItem(String id, String name, int price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    /**
     * @return уникальный ID товара
     */
    public String getId() { return id; }

    /**
     * @return название товара для UI
     */
    public String getName() { return name; }

    /**
     * @return цена товара в монетах
     */
    public int getPrice() { return price; }

    /**
     * Вызывается автоматически после успешной покупки товара.
     *
     * <p>📌 Используется для активации эффекта предмета:
     * <ul>
     *   <li>установка фона</li>
     *   <li>применение скина</li>
     *   <li>активация косметики</li>
     * </ul>
     *
     * @param userId ID пользователя, который купил предмет
     */
    public abstract void onApply(String userId);
}