package Backend;

import java.util.Random;

public class MinimaxSolver {

    private final Random random = new Random();

    public int getDepth(Difficulty difficulty) {

        return switch (difficulty) {
            case EASY -> 1;
            case MEDIUM -> 3;
            case HARD -> 9;
        };
    }

    public int solve(GameState state, int depth) {

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