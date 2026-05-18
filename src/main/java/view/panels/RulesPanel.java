package view.panels;

import javafx.scene.control.ScrollPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.geometry.Pos;
import javafx.geometry.Insets;

public class RulesPanel extends StackPane {

    public RulesPanel() {
        setPrefSize(1368, 1032);
        getStyleClass().add("rules-panel");

        VBox content = new VBox();
        content.setSpacing(20);
        content.setAlignment(Pos.TOP_CENTER);
        content.setPadding(new Insets(60, 80, 60, 80));

        Text title = new Text("КРЕСТИКИ-НОЛИКИ");
        title.getStyleClass().add("rules-title");

        content.getChildren().addAll(
                title,
                separatorLine(),
                textBlock("Это пошаговая логическая игра для двух игроков."),
                separatorLine(),
                textBlock("""
🎮 РЕЖИМЫ ИГРЫ

Доступны два режима:

• 3×3 — классический режим
• 9×9 — расширенный режим

Также можно играть:
• против другого игрока
• против бота"""),
                separatorLine(),
                textBlock("""
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
Если поле заполнено и победителя нет."""),
                separatorLine(),
                textBlock("""
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
если поле заполнено и линии нет."""),
                separatorLine(),
                textBlock("""
🤖 ИГРА ПРОТИВ БОТА

Бот:

• делает ход автоматически
• пытается собрать линию
• блокирует игрока"""),
                separatorLine(),
                textBlock("""
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
• токсичное поведение"""),
                separatorLine(),
                textBlock("""
🏆 ДОПОЛНИТЕЛЬНО

В игре есть:

• профиль игрока
• статистика побед
• рейтинг
• достижения
• магазин оформления""")
        );

        ScrollPane scroll = new ScrollPane(content);
        scroll.setFitToWidth(true);
        scroll.setPannable(true);
        scroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scroll.getStyleClass().add("rules-scroll");

        getChildren().add(scroll);

        getStylesheets().add(
                getClass().getResource("/css/panels.css").toExternalForm()
        );
    }

    // Текстовый блок — центрированный
    private Text textBlock(String text) {
        Text t = new Text(text);
        t.getStyleClass().add("rules-text");
        t.setWrappingWidth(1100);
        return t;
    }

    // Линия из символов — центрируется автоматически через выравнивание
    private Text separatorLine() {
        Text line = new Text("─".repeat(100));
        line.getStyleClass().add("rules-separator-text");
        line.setWrappingWidth(1100);
        return line;
    }
}