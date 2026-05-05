package Backend;

public class MinimaxSolver {

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

