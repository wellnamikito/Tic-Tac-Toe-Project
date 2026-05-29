package Backend;

/**
 * MinimaxSolver — реализация алгоритма Minimax для принятия решений в игре.
 *
 * <p>📌 Используется для:
 * <ul>
 *   <li>просчёта оптимального хода ИИ</li>
 *   <li>оценки игровых состояний</li>
 *   <li>симуляции возможных вариантов развития игры</li>
 * </ul>
 *
 * <p>Алгоритм работает путём перебора возможных ходов на заданную глубину
 * и выбора наиболее выгодного результата.</p>
 *
 * <p>Фронтенд напрямую этот класс не использует — он работает через backend-логику игры,
 * но результат влияет на поведение ИИ в реальном времени.</p>
 */
public class MinimaxSolver {

    /**
     * Выполняет рекурсивный расчёт оптимального значения состояния игры
     * с использованием алгоритма Minimax.
     *
     * <p>📌 Логика:
     * <ul>
     *   <li>если игра завершена или достигнута глубина — возвращается оценка состояния</li>
     *   <li>если ход максимизирующего игрока — выбирается лучший результат</li>
     *   <li>если ход минимизирующего игрока — выбирается худший для противника результат</li>
     * </ul>
     *
     * @param state текущее состояние игры
     * @param depth глубина просчёта (чем больше — тем “умнее” ИИ, но медленнее)
     * @return числовая оценка состояния (чем выше — тем лучше для AI)
     */
    public int solve(GameState state, int depth) {

        // Базовый случай: игра окончена или достигнута макс. глубина
        if (state.isGameOver() || depth == 0) {
            return state.evaluateScore();
        }

        if (state.isMaxPlayerTurn()) {
            int maxEval = Integer.MIN_VALUE;

            for (GameState move : state.getPossibleMoves()) {
                int eval = solve(move, depth - 1);
                maxEval = Math.max(maxEval, eval);
            }

            return maxEval;
        } else {
            int minEval = Integer.MAX_VALUE;

            for (GameState move : state.getPossibleMoves()) {
                int eval = solve(move, depth - 1);
                minEval = Math.min(minEval, eval);
            }

            return minEval;
        }
    }
}