package manager;

import Backend.InsufficientFundsException;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import model.ShopCategory;
import model.ShopItem;
import view.panels.ShopPanel;

public class ShopManager {

    public static ObservableList<ShopItem> getItems(
            ShopCategory category
    ){

        ObservableList<ShopItem> items =
                FXCollections.observableArrayList();

        switch(category){

            case GAME_THEME -> {

                items.add(
                        new ShopItem(
                                "theme_1",
                                "Неоновая тема",
                                10000,
                                null,
                                category
                        )
                );

                items.add(
                        new ShopItem(
                                "theme_2",
                                "Золотые крестики",
                                800,
                                null,
                                category
                        )
                );
            }

            case PROFILE_THEME -> {

                items.add(
                        new ShopItem(
                                "profile_1",
                                "Космос",
                                1000,
                                null,
                                category
                        )
                );
            }

            case STICKER -> {

                items.add(
                        new ShopItem(
                                "sticker_1",
                                "Улыбка",
                                200,
                                null,
                                category
                        )
                );
            }

            case COINS -> {

                items.add(
                        new ShopItem(
                                "coins_1000",
                                "1000 монет",
                                99,
                                "/images/themes/neon.png",
                                category
                        )
                );

                items.add(
                        new ShopItem(
                                "coins_5000",
                                "5000 монет",
                                399,
                                null,
                                category
                        )
                );

                items.add(
                        new ShopItem(
                                "coins_10000",
                                "10000 монет",
                                699,
                                null,
                                category
                        )
                );
            }
        }

        return items;
    }

    public static boolean buyItem(
            ShopItem item
    ){

        try {

            if(item.getCategory()
                    == ShopCategory.COINS){

                switch (item.getId()){

                    case "coins_1000" ->

                            ShopPanel.ShopBackend
                                    .BALANCE
                                    .addFunds(
                                            ShopPanel.ShopBackend.USER_ID,
                                            Backend.CurrencyType.COINS,
                                            1000
                                    );

                    case "coins_5000" ->

                            ShopPanel.ShopBackend
                                    .BALANCE
                                    .addFunds(
                                            ShopPanel.ShopBackend.USER_ID,
                                            Backend.CurrencyType.COINS,
                                            5000
                                    );

                    case "coins_10000" ->

                            ShopPanel.ShopBackend
                                    .BALANCE
                                    .addFunds(
                                            ShopPanel.ShopBackend.USER_ID,
                                            Backend.CurrencyType.COINS,
                                            10000
                                    );
                }

                return true;
            }

            ShopPanel.ShopBackend.SHOP
                    .performPurchase(
                            ShopPanel.ShopBackend.USER_ID,
                            item.getId()
                    );

            return true;

        }
        catch (InsufficientFundsException e){

            System.out.println(
                    e.getMessage()
            );

            return false;
        }
    }

    public static boolean isOwned(
            String itemId
    ){

        if(itemId.startsWith(
                "coins_"
        )){

            return false;
        }

        return ShopPanel
                .ShopBackend
                .INVENTORY
                .hasItem(
                        ShopPanel
                                .ShopBackend
                                .USER_ID,
                        itemId
                );
    }
}