package view;

import controller.MainMenuController;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.transform.Scale;
import javafx.stage.Stage;

public class MainMenuView {

    private static final double BASE_WIDTH = 1920;
    private static final double BASE_HEIGHT = 1080;

    public Scene createScene(Stage stage) {

        MainMenuController controller = new MainMenuController(stage);

        StackPane root = new StackPane();

        // =========================
        // BACKGROUND
        // =========================
        ImageView bg = new ImageView(
                new Image(getClass().getResource("/images/background.png").toExternalForm())
        );

        bg.setPreserveRatio(false);
        bg.fitWidthProperty().bind(stage.widthProperty());
        bg.fitHeightProperty().bind(stage.heightProperty());

        // =========================
        // UI ROOT
        // =========================
        AnchorPane ui = new AnchorPane();
        ui.setPrefSize(BASE_WIDTH, BASE_HEIGHT);

        // =========================
        // SCALE (ВАЖНО: MIN, НЕ MAX)
        // =========================
        Scale scale = new Scale();
        ui.getTransforms().add(scale);

        // обновление масштаба
        stage.widthProperty().addListener((obs, o, n) -> update(stage, ui, scale));
        stage.heightProperty().addListener((obs, o, n) -> update(stage, ui, scale));

        // =========================
        // PANEL (ПРИКРЕПЛЕНА СЛЕВА/СВЕРХУ)
        // =========================
        ImageView panel = new ImageView(
                new Image(getClass().getResource("/images/menu_panel.png").toExternalForm())
        );
        panel.setFitWidth(500);
        panel.setPreserveRatio(true);

        panel.setLayoutX(0);
        panel.setLayoutY(0);

        // =========================
        // LOGO
        // =========================
        ImageView logo = new ImageView(
                new Image(getClass().getResource("/images/logo.png").toExternalForm())
        );
        logo.setFitWidth(513);
        logo.setPreserveRatio(true);

        logo.setLayoutX(-7);
        logo.setLayoutY(0);

        // =========================
        // BUTTONS
        // =========================
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

        play.setOnAction(e -> controller.onPlay());
        shop.setOnAction(e -> controller.onShop());
        rules.setOnAction(e -> controller.onRules());
        settings.setOnAction(e -> controller.onSettings());

        addHover(play);
        addHover(shop);
        addHover(rules);
        addHover(settings);

        // =========================
        // ADD UI
        // =========================
        ui.getChildren().addAll(panel, logo, play, shop, rules, settings);

        root.getChildren().addAll(bg, ui);

        Scene scene = new Scene(root, BASE_WIDTH, BASE_HEIGHT);

        update(stage, ui, scale);

        return scene;
    }

    // =========================
    // SCALE SYSTEM (ПРАВИЛЬНЫЙ)
    // =========================
    private void update(Stage stage, AnchorPane ui, Scale scale) {

        double scaleX = stage.getWidth() / BASE_WIDTH;
        double scaleY = stage.getHeight() / BASE_HEIGHT;

        // 🔥 ВАЖНО: MIN, как в играх (не вылезает за экран)
        double s = Math.min(scaleX, scaleY);

        scale.setX(s);
        scale.setY(s);

        double realW = BASE_WIDTH * s;
        double realH = BASE_HEIGHT * s;

        // центрируем ВСЕГДА
        ui.setLayoutX((stage.getWidth() - realW) / 2);
        ui.setLayoutY((stage.getHeight() - realH) / 2);
    }

    // =========================
    // BUTTON
    // =========================
    private Button createButton(String path) {

        ImageView img = new ImageView(
                new Image(getClass().getResource(path).toExternalForm())
        );

        img.setFitWidth(390);
        img.setPreserveRatio(true);

        Button b = new Button("", img);
        b.setStyle("-fx-background-color: transparent;");
        return b;
    }

    // =========================
    // HOVER
    // =========================
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
}