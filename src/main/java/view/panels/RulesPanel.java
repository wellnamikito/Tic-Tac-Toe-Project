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
        content.setPadding(new Insets(30, 80, 70, 80));
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

🔹 РЕЖИМ 3×3

Цель игры — собрать 3 символа подряд:

• по горизонтали

• по вертикали

• по диагонали

Игроки ходят по очереди:

• X ходит первым

• O ходит вторым

Нельзя ставить символ в занятую клетку.

🏆 Победа:

Первый, кто собрал линию из 3 символов.

🤝 Ничья:

Если поле заполнено и победителя нет.

━━━━━━━━━━━━━━━━━━━━

🔹 РЕЖИМ 9×9

Цель — собрать 5 символов подряд:

• горизонталь

• вертикаль

• диагональ

Особенности:

• больше поле

• больше стратегия

• больше времени на игру

🏆 Победа:

5 символов подряд.

🤝 Ничья:

если поле заполнено и линии нет.

━━━━━━━━━━━━━━━━━━━━

🤖 ИГРА ПРОТИВ БОТА

Бот:

• делает ход автоматически

• пытается собрать линию

• блокирует игрока

━━━━━━━━━━━━━━━━━━━━

💬 ИГРОВОЙ ЧАТ

Можно:

• общаться

• искать соперников

• обсуждать стратегии

• договариваться о матчах

Запрещено:

• оскорбления

• спам

• реклама

• токсичное поведение

━━━━━━━━━━━━━━━━━━━━

🏆 ДОПОЛНИТЕЛЬНО

В игре есть:

• профиль игрока

• статистика побед

• рейтинг

• достижения

• магазин оформления

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