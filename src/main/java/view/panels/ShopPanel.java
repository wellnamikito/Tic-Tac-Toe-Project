package view.panels;

import Backend.*;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.scene.text.Text;

import manager.ShopManager;
import model.ShopCategory;
import view.shop.ShopItemCard;

public class ShopPanel extends StackPane {

    private static ShopPanel instance;

    public static class ShopBackend {

        public static final String USER_ID = "player1";

        public static final BalanceManager BALANCE =
                new BalanceManager();

        public static final InventoryManager INVENTORY =
                new InventoryManager();

        public static final ShopService SHOP =
                new ShopService(
                        BALANCE,
                        INVENTORY
                );

        static {

            BALANCE.addFunds(
                    USER_ID,
                    CurrencyType.COINS,
                    10000
            );

            SHOP.registerItem(
                    new ItemsShop.BoardDesignItem(
                            "theme_1",
                            "Неоновая тема",
                            10000
                    )
            );

            SHOP.registerItem(
                    new ItemsShop.BoardDesignItem(
                            "theme_2",
                            "Золотые крестики",
                            800
                    )
            );

            SHOP.registerItem(
                    new ItemsShop.BackgroundItem(
                            "profile_1",
                            "Космос",
                            1000
                    )
            );

            SHOP.registerItem(
                    new ItemsShop.StickerItem(
                            "sticker_1",
                            "Улыбка",
                            200
                    )
            );
        }
    }

    private final Text balanceText;

    public ShopPanel() {

        instance = this;

        setPrefSize(1368,1032);

        getStyleClass().add(
                "shop-root"
        );

        BorderPane window =
                new BorderPane();

        window.getStyleClass()
                .add("shop-window");

        VBox leftPanel =
                new VBox(20);

        leftPanel.setPadding(
                new Insets(25)
        );

        leftPanel.setPrefWidth(
                310
        );

        leftPanel.getStyleClass()
                .add("left-panel");

        HBox balanceBox =
                new HBox(15);

        balanceBox.setAlignment(
                Pos.CENTER_LEFT
        );

        balanceText =
                new Text();

        updateBalance();

        balanceText.getStyleClass()
                .add("shop-balance");

        Button plus =
                new Button("+");

        plus.getStyleClass()
                .add("plus-button");

        balanceBox.getChildren()
                .addAll(
                        new Text("💰"),
                        balanceText,
                        plus
                );

        Button bgBtn =
                createCategory(
                        "1) Персонализация фона"
                );

        Button gameBtn =
                createCategory(
                        "2) Персонализация игры"
                );

        Button stickerBtn =
                createCategory(
                        "3) Стикеры"
                );

        leftPanel.getChildren()
                .addAll(
                        balanceBox,
                        bgBtn,
                        gameBtn,
                        stickerBtn
                );

        VBox rightContent =
                new VBox(25);

        rightContent.setPadding(
                new Insets(25)
        );

        Text title =
                new Text("Магазин");

        title.getStyleClass()
                .add("shop-title");

        FlowPane grid =
                new FlowPane();

        grid.setHgap(25);

        grid.setVgap(25);

        grid.setPrefWrapLength(
                800
        );

        bgBtn.setOnAction(e ->
                loadCategory(
                        ShopCategory.PROFILE_THEME,
                        grid
                )
        );

        gameBtn.setOnAction(e ->
                loadCategory(
                        ShopCategory.GAME_THEME,
                        grid
                )
        );

        stickerBtn.setOnAction(e ->
                loadCategory(
                        ShopCategory.STICKER,
                        grid
                )
        );

        plus.setOnAction(e ->
                loadCategory(
                        ShopCategory.COINS,
                        grid
                )
        );

        loadCategory(
                ShopCategory.STICKER,
                grid
        );

        rightContent.getChildren()
                .addAll(
                        title,
                        grid
                );

        window.setLeft(
                leftPanel
        );

        window.setCenter(
                rightContent
        );

        getChildren().add(
                window
        );

        getStylesheets().add(
                getClass()
                        .getResource(
                                "/css/shop.css"
                        )
                        .toExternalForm()
        );
    }

    public static void refreshBalance(){

        if(instance != null){

            instance.updateBalance();
        }
    }

    public void updateBalance(){

        balanceText.setText(
                String.valueOf(
                        (int)
                                ShopBackend.BALANCE
                                        .getBalance(
                                                ShopBackend.USER_ID,
                                                CurrencyType.COINS
                                        )
                )
        );
    }

    private void loadCategory(
            ShopCategory category,
            FlowPane grid
    ){

        grid.getChildren().clear();

        ShopManager.getItems(
                category
        ).forEach(item ->

                grid.getChildren()
                        .add(
                                new ShopItemCard(
                                        item
                                )
                        )
        );
    }

    private Button createCategory(
            String text
    ){

        Button btn =
                new Button(text);

        btn.getStyleClass()
                .add(
                        "category-button"
                );

        btn.setMaxWidth(
                Double.MAX_VALUE
        );

        return btn;
    }
}