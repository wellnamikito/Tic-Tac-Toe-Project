package view.panels;

import javafx.scene.control.ScrollPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.geometry.Insets;
import javafx.geometry.Pos;

public class RulesPanel extends StackPane {

    public RulesPanel() {

        setPrefSize(1368, 1032);
        getStyleClass().add("panel-root");

        VBox content = new VBox();
        content.setSpacing(24);
        content.setPadding(new Insets(70, 80, 70, 80));
        content.setAlignment(Pos.TOP_LEFT);

        Text title = new Text("КРЕСТИКИ-НОЛИКИ");
        title.getStyleClass().add("rule-title");

        Text rules = new Text("""
Это пошаговая логическая игра для двух игроков.

━━━━━━━━━━━━━━━━━━━━

🎮 РЕЖИМЫ ИГРЫ

Доступны два режима:
• 3×3 — классический режим
• 9×9 — расширенный режим

Также можно играть:
• против другого игрока
• против бота

━━━━━━━━━━━━━━━━━━━━

🔹 3×3 РЕЖИМ

Цель: собрать 3 символа подряд.

• горизонталь
• вертикаль
• диагональ

Игроки ходят по очереди:
• X ходит первым
• O ходит вторым

Нельзя ставить в занятую клетку.

🏆 Победа:
3 символа подряд.

🤝 Ничья:
если поле заполнено.

━━━━━━━━━━━━━━━━━━━━

🔹 9×9 РЕЖИМ

Цель: 5 символов подряд.

• горизонталь
• вертикаль
• диагональ

Особенности:
• больше поле
• больше стратегия

🏆 Победа:
5 подряд символов.

━━━━━━━━━━━━━━━━━━━━

🤖 БОТ

• делает ход автоматически
• блокирует игрока
• строит линию

━━━━━━━━━━━━━━━━━━━━

💬 ЧАТ

Можно:
• общаться
• искать игроков
• обсуждать матчи

Запрещено:
• оскорбления
• спам
• реклама

━━━━━━━━━━━━━━━━━━━━

🏆 ДОПОЛНИТЕЛЬНО

• профиль игрока
• статистика
• рейтинг
• достижения
• магазин
""");

        rules.setWrappingWidth(900);
        rules.getStyleClass().add("rule-text");

        content.getChildren().addAll(title, rules);

        ScrollPane scroll = new ScrollPane(content);

        scroll.setPrefSize(1000, 950);
        scroll.setLayoutX(180);
        scroll.setLayoutY(40);

        scroll.setFitToWidth(true);
        scroll.setPannable(true);

        scroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);

        scroll.getStyleClass().add("scroll-pane");

        scroll.setClip(new javafx.scene.shape.Rectangle(1000, 950));

        getChildren().add(scroll);

        getStylesheets().add(
                getClass().getResource("/css/panels.css").toExternalForm()
        );
    }
}