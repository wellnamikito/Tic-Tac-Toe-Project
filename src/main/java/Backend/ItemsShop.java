package Backend;

public class ItemsShop {

    // 1. ПРЕДМЕТ: ФОН ПРОФИЛЯ
    public static class BackgroundItem extends ShopItem {
        public BackgroundItem(String id, String name, int price) {
            super(id, name, price);
        }

        @Override
        public void onApply(String userId) {
            // Фронтенд: здесь будет код для смены картинки в профиле
            System.out.println("[Система] Фон профиля " + getName() + " применен для " + userId);
        }
    }

    // 2. ПРЕДМЕТ: ДИЗАЙН ИГРОВОГО ПОЛЯ
    public static class BoardDesignItem extends ShopItem {
        public BoardDesignItem(String id, String name, int price) {
            super(id, name, price);
        }

        @Override
        public void onApply(String userId) {
            // Фронтенд: здесь будет код для смены скина крестиков и ноликов
            System.out.println("[Система] Дизайн поля " + getName() + " установлен для " + userId);
        }
    }

    // 3. ПРЕДМЕТ: СТИКЕРЫ ДЛЯ ЧАТА
    public static class StickerItem extends ShopItem {
        public StickerItem(String id, String name, int price) {
            super(id, name, price);
        }

        @Override
        public void onApply(String userId) {
            // Фронтенд: здесь будет код для разблокировки стикера в меню чата
            System.out.println("[Система] Стикер " + getName() + " добавлен в инвентарь чата " + userId);
        }
    }
}
