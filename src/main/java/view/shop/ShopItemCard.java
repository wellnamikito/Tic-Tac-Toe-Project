package view.shop;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import manager.ShopManager;
import model.ShopItem;
import view.panels.ShopPanel;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class ShopItemCard extends VBox {

    public ShopItemCard(
            ShopItem item
    ) {

        getStyleClass().add(
                "shop-item"
        );

        setAlignment(
                Pos.CENTER
        );

        setSpacing(
                12
        );

        StackPane preview =
                new StackPane();

        preview.getStyleClass()
                .add(
                        "preview"
                );

        preview.setPrefSize(
                120,
                120
        );

        // =========================
        // IMAGE OR FALLBACK
        // =========================

        if(item.getImage() != null){

            ImageView imageView;

            try {

                Image img =
                        new Image(
                                getClass()
                                        .getResourceAsStream(
                                                item.getImage()
                                        )
                        );

                imageView =
                        new ImageView(img);

                imageView.setFitWidth(100);
                imageView.setFitHeight(100);
                imageView.setPreserveRatio(true);

                preview.getChildren()
                        .add(imageView);

            } catch (Exception e) {

                Text fallback =
                        new Text("⭐");

                preview.getChildren()
                        .add(fallback);
            }

        } else {

            Text fallback =
                    new Text("⭐");

            preview.getChildren()
                    .add(fallback);
        }

        // =========================
        // TITLE
        // =========================

        Text title =
                new Text(
                        item.getName()
                );

        title.getStyleClass()
                .add(
                        "item-title"
                );

        // =========================
        // PRICE
        // =========================

        Text price =
                new Text(
                        "💰 " +
                                item.getPrice()
                );

        price.getStyleClass()
                .add(
                        "shop-price"
                );

        // =========================
        // OWNED CHECK
        // =========================

        boolean owned =
                ShopManager.isOwned(
                        item.getId()
                );

        Button buy =
                new Button(
                        owned
                                ? "Куплено"
                                : "Купить"
                );

        buy.setDisable(
                owned
        );

        buy.getStyleClass()
                .add(
                        "buy-button"
                );

        buy.setOnAction(e -> {

            boolean success =
                    ShopManager.buyItem(
                            item
                    );

            if(success){

                buy.setText(
                        "Куплено"
                );

                buy.setDisable(
                        true
                );

                ShopPanel.refreshBalance();
            }
        });

        // =========================
        // BUILD UI
        // =========================

        getChildren().addAll(
                preview,
                title,
                price,
                buy
        );
    }
}