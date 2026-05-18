package Backend;


/**
 • Базовый класс для любого товара в магазине.
 */
public abstract class ShopItem {
    private final String id;      // Уникальный ID товара (например, "BG_SPACE_01")
    private final String name;    // Название для отображения (например, "Космос")
    private final int price;      // Цена в монетах

    public ShopItem(String id, String name, int price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public int getPrice() { return price; }

    /**
     * Метод вызывается автоматически при успешной покупке.
     * Здесь может быть логика активации предмета.
     */
    public abstract void onApply(String userId);
}
