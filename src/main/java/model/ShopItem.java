package model;

public class ShopItem {

    private final String id;
    private final String name;
    private final int price;
    private final String image;
    private final ShopCategory category;

    public ShopItem(
            String id,
            String name,
            int price,
            String image,
            ShopCategory category
    ) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.image = image;
        this.category = category;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public String getImage() {
        return image;
    }

    public ShopCategory getCategory() {
        return category;
    }
}