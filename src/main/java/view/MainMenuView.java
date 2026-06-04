package view;

import controller.MainMenuController;
import config.UIConfig;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import manager.AudioManager;
import manager.ScreenManager;
import view.panels.RulesPanel;
import view.panels.SettingsPanel;
import view.panels.ShopPanel;

public class MainMenuView {

    private Group scaleContainer;
    private AnchorPane rightPanelContainer;
    private String openedPanel = "";

    public Scene createScene(Stage stage) {

        MainMenuController controller = new MainMenuController(stage);

        StackPane root = new StackPane();

        ImageView bg = new ImageView(
                new Image(getClass().getResource("/images/background.png").toExternalForm())
        );

        bg.setPreserveRatio(false);
        bg.fitWidthProperty().bind(stage.widthProperty());
        bg.fitHeightProperty().bind(stage.heightProperty());

        AnchorPane ui = new AnchorPane();
        ui.setPrefSize(UIConfig.BASE_WIDTH, UIConfig.BASE_HEIGHT);

        ImageView panel = new ImageView(
                new Image(getClass().getResource("/images/menu_panel.png").toExternalForm())
        );

        panel.setFitWidth(500);

        ImageView logo = new ImageView(
                new Image(getClass().getResource("/images/logo.png").toExternalForm())
        );

        logo.setFitWidth(513);

        Button play = createButton("/buttons/play.png");
        Button shop = createButton("/buttons/shop.png");
        Button rules = createButton("/buttons/rules.png");
        Button settings = createButton("/buttons/settings.png");

        play.setLayoutX(55);
        play.setLayoutY(377);

        shop.setLayoutX(55);
        shop.setLayoutY(508);

        rules.setLayoutX(55);
        rules.setLayoutY(639);

        settings.setLayoutX(55);
        settings.setLayoutY(770);

        addHover(play);
        addHover(shop);
        addHover(rules);
        addHover(settings);

        rightPanelContainer = new AnchorPane();
        rightPanelContainer.setPrefSize(650, 800);

        // ВАЖНО: пока нет панели — мышь НЕ блокируется
        rightPanelContainer.setMouseTransparent(true);

        play.setOnAction(e -> controller.onPlay());

        settings.setOnAction(e ->
                togglePanel(new SettingsPanel(), "settings", settings, rules, shop)
        );

        rules.setOnAction(e ->
                togglePanel(new RulesPanel(), "rules", rules, settings, shop)
        );

        shop.setOnAction(e ->
                togglePanel(new ShopPanel(), "shop", shop, settings, rules)
        );

        ui.getChildren().addAll(
                panel,
                logo,
                play,
                shop,
                rules,
                settings,
                rightPanelContainer
        );

        scaleContainer = new Group(ui);

        root.getChildren().addAll(bg, scaleContainer);

        Scene scene = new Scene(root, UIConfig.BASE_WIDTH, UIConfig.BASE_HEIGHT);

        bindScaling(scene);

        AudioManager.playMusic("/audio/fff.mp3");

        // Восстанавливаем полноэкранный режим из настроек
        ScreenManager.restoreFullscreen();

        return scene;
    }

    private void togglePanel(Node panel, String name, Button active, Button... others) {

        if (openedPanel.equals(name)) {
            hidePanel();
            openedPanel = "";
            active.setStyle("-fx-background-color: transparent;");
            return;
        }

        showPanel(panel);
        openedPanel = name;

        active.setStyle("-fx-background-color: rgba(255,255,255,0.25);");

        for (Button b : others) {
            b.setStyle("-fx-background-color: transparent;");
        }
    }

    private void showPanel(Node panel) {

        rightPanelContainer.setLayoutX(530);
        rightPanelContainer.setLayoutY(24);

        rightPanelContainer.getChildren().setAll(panel);

        rightPanelContainer.setMouseTransparent(false);
    }

    private void hidePanel() {

        rightPanelContainer.getChildren().clear();

        rightPanelContainer.setMouseTransparent(true);
    }

    private Button createButton(String path) {

        ImageView img = new ImageView(
                new Image(getClass().getResource(path).toExternalForm())
        );

        img.setFitWidth(390);
        img.setPreserveRatio(true);

        Button b = new Button("", img);

        b.setFocusTraversable(false);

        b.setStyle("""
        -fx-background-color: transparent;
        -fx-background-insets: 0;
        -fx-padding: 0;
        -fx-focus-color: transparent;
        -fx-faint-focus-color: transparent;
    """);

        return b;
    }

    private void addHover(Button b) {

        b.setOnMouseEntered(e -> {
            b.setScaleX(1.05);
            b.setScaleY(1.05);
        });

        b.setOnMouseExited(e -> {
            b.setScaleX(1);
            b.setScaleY(1);
        });
    }

    private void bindScaling(Scene scene) {

        scene.widthProperty().addListener((obs, o, n) -> updateScale(scene));
        scene.heightProperty().addListener((obs, o, n) -> updateScale(scene));

        updateScale(scene);
    }

    private void updateScale(Scene scene) {

        double scaleX = scene.getWidth() / UIConfig.BASE_WIDTH;
        double scaleY = scene.getHeight() / UIConfig.BASE_HEIGHT;

        double scale = Math.min(scaleX, scaleY);

        scaleContainer.setScaleX(scale);
        scaleContainer.setScaleY(scale);
    }
}