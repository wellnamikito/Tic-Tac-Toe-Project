package Backend;
import java.util.*;

public interface GameState {
    boolean isGameOver();
    int evaluateScore(); // Оценка позиции: +10 (победа), -10 (проигрыш), 0 (ничья)
    List<GameState> getPossibleMoves();
    boolean isMaxPlayerTurn();
}