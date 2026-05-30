package Backend;

/**
 * ItemsShop — набор реализаций товаров магазина.
 *
 * <p>📌 Содержит конкретные типы ShopItem, которые могут быть куплены пользователем.</p>
 *
 * <p>Каждый предмет автоматически выполняет действие после покупки через метод {@link ShopItem#onApply(String)}.</p>
 *
 * <p>Фронтенд не должен напрямую управлять этими классами — только отображать и вызывать покупку через ShopService.</p>
 */
public class ItemsShop {

    /**
     * BackgroundItem — товар типа "фон профиля".
     *
     * <p>📌 После покупки изменяет внешний вид профиля пользователя.</p>
     */
    public static class BackgroundItem extends ShopItem {

        /**
         * Создаёт фон профиля.
         *
         * @param id уникальный ID фона
         * @param name название фона для UI
         * @param price цена в монетах
         */
        public BackgroundItem(String id, String name, int price) {
            super(id, name, price);
        }

        /**
         * Применяет фон профиля пользователю.
         *
         * <p>📌 Здесь должна быть интеграция с фронтендом:
         * смена изображения профиля / темы UI.</p>
         *
         * @param userId ID пользователя
         */
        @Override
        public void onApply(String userId) {
            System.out.println("[Система] Фон профиля " + getName() + " применен для " + userId);
        }
    }

    /**
     * BoardDesignItem — товар типа "дизайн игрового поля".
     *
     * <p>📌 Изменяет внешний вид игрового поля (крестики/нолики, тема и т.д.).</p>
     */
    public static class BoardDesignItem extends ShopItem {

        /**
         * Создаёт дизайн игрового поля.
         *
         * @param id уникальный ID дизайна
         * @param name название дизайна
         * @param price цена в монетах
         */
        public BoardDesignItem(String id, String name, int price) {
            super(id, name, price);
        }

        /**
         * Применяет дизайн игрового поля для пользователя.
         *
         * <p>📌 Должен быть обработан фронтендом (смена UI поля игры).</p>
         *
         * @param userId ID пользователя
         */
        @Override
        public void onApply(String userId) {
            System.out.println("[Система] Дизайн поля " + getName() + " установлен для " + userId);
        }
    }

    /**
     * StickerItem — товар типа "стикер".
     *
     * <p>📌 Разблокирует стикеры для чата или внутриигрового общения.</p>
     */
    public static class StickerItem extends ShopItem {

        /**
         * Создаёт стикер.
         *
         * @param id уникальный ID стикера
         * @param name название стикера
         * @param price цена в монетах
         */
        public StickerItem(String id, String name, int price) {
            super(id, name, price);
        }

        /**
         * Добавляет стикер в инвентарь пользователя.
         *
         * <p>📌 После покупки должен появиться в UI чата/инвентаря.</p>
         *
         * @param userId ID пользователя
         */
        @Override
        public void onApply(String userId) {
            System.out.println("[Система] Стикер " + getName() + " добавлен в инвентарь чата " + userId);
        }
    }
}