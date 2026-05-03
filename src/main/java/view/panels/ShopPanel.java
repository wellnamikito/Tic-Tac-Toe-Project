package view.panels;

import javafx.geometry.Pos;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class ShopPanel extends VBox {

    public ShopPanel() {

        setSpacing(20);
        setAlignment(Pos.TOP_CENTER);

        setPrefWidth(600);
        setPrefHeight(700);

        setStyle("-fx-background-color: rgba(0,0,0,0.6); -fx-padding: 30;");

        Text title = new Text("SHOP");

        getChildren().add(title);
    }
}