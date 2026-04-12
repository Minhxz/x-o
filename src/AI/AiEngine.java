package AI;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class AiEngine {
    private static final Random RANDOM = new Random();

    public static AiMove pickMove(String[][] board, String aiSymbol, String humanSymbol, AiDifficulty difficulty, int winLength) {
        if (board == null || board.length == 0) return null;
        int size = board.length;
        switch (difficulty) {
            case HARD:
                if (size == 3 && winLength == 3) {
                    AiMove hardMove = minimaxMove(board, aiSymbol, humanSymbol, winLength);
                    if (hardMove != null) return hardMove;
                }
                return normalMove(board, aiSymbol, humanSymbol, winLength);
            case NORMAL:
                return normalMove(board, aiSymbol, humanSymbol, winLength);
            case EASY:
            default:
                return randomMove(board);
        }
    }

    private static AiMove normalMove(String[][] board, String aiSymbol, String humanSymbol, int winLength) {
        AiMove win = findWinningMove(board, aiSymbol, winLength);
        if (win != null) return win;

        AiMove block = findWinningMove(board, humanSymbol, winLength);
        if (block != null) return block;

        int size = board.length;
        int center = size / 2;
        if (isEmpty(board, center, center)) {
            return new AiMove(center, center);
        }

        return randomMove(board);
    }

    private static AiMove randomMove(String[][] board) {
        List<AiMove> empty = emptyCells(board);
        if (empty.isEmpty()) return null;
        return empty.get(RANDOM.nextInt(empty.size()));
    }

    private static AiMove findWinningMove(String[][] board, String symbol, int winLength) {
        int size = board.length;
        for (int r = 0; r < size; r++) {
            for (int c = 0; c < size; c++) {
                if (!isEmpty(board, r, c)) continue;
                board[r][c] = symbol;
                boolean win = checkWin(board, winLength);
                board[r][c] = "";
                if (win) return new AiMove(r, c);
            }
        }
        return null;
    }

    private static AiMove minimaxMove(String[][] board, String aiSymbol, String humanSymbol, int winLength) {
        int bestScore = Integer.MIN_VALUE;
        AiMove bestMove = null;
        int size = board.length;

        for (int r = 0; r < size; r++) {
            for (int c = 0; c < size; c++) {
                if (!isEmpty(board, r, c)) continue;
                board[r][c] = aiSymbol;
                int score = minimax(board, false, aiSymbol, humanSymbol, winLength, 0);
                board[r][c] = "";
                if (score > bestScore) {
                    bestScore = score;
                    bestMove = new AiMove(r, c);
                }
            }
        }
        return bestMove;
    }

    private static int minimax(String[][] board, boolean aiTurn, String aiSymbol, String humanSymbol, int winLength, int depth) {
        if (checkWinner(board, aiSymbol, winLength)) return 10 - depth;
        if (checkWinner(board, humanSymbol, winLength)) return depth - 10;
        if (emptyCells(board).isEmpty()) return 0;

        int size = board.length;
        if (aiTurn) {
            int best = Integer.MIN_VALUE;
            for (int r = 0; r < size; r++) {
                for (int c = 0; c < size; c++) {
                    if (!isEmpty(board, r, c)) continue;
                    board[r][c] = aiSymbol;
                    best = Math.max(best, minimax(board, false, aiSymbol, humanSymbol, winLength, depth + 1));
                    board[r][c] = "";
                }
            }
            return best;
        } else {
            int best = Integer.MAX_VALUE;
            for (int r = 0; r < size; r++) {
                for (int c = 0; c < size; c++) {
                    if (!isEmpty(board, r, c)) continue;
                    board[r][c] = humanSymbol;
                    best = Math.min(best, minimax(board, true, aiSymbol, humanSymbol, winLength, depth + 1));
                    board[r][c] = "";
                }
            }
            return best;
        }
    }

    private static List<AiMove> emptyCells(String[][] board) {
        List<AiMove> cells = new ArrayList<>();
        int size = board.length;
        for (int r = 0; r < size; r++) {
            for (int c = 0; c < size; c++) {
                if (isEmpty(board, r, c)) {
                    cells.add(new AiMove(r, c));
                }
            }
        }
        return cells;
    }

    private static boolean isEmpty(String[][] board, int r, int c) {
        String value = board[r][c];
        return value == null || value.isEmpty();
    }

    private static boolean checkWin(String[][] board, int winLength) {
        int size = board.length;
        for (int r = 0; r < size; r++) {
            for (int c = 0; c < size; c++) {
                String mark = board[r][c];
                if (mark == null || mark.isEmpty()) continue;
                if (hasLine(board, r, c, 0, 1, mark, winLength)) return true;
                if (hasLine(board, r, c, 1, 0, mark, winLength)) return true;
                if (hasLine(board, r, c, 1, 1, mark, winLength)) return true;
                if (hasLine(board, r, c, 1, -1, mark, winLength)) return true;
            }
        }
        return false;
    }

    private static boolean checkWinner(String[][] board, String symbol, int winLength) {
        int size = board.length;
        for (int r = 0; r < size; r++) {
            for (int c = 0; c < size; c++) {
                String mark = board[r][c];
                if (!symbol.equals(mark)) continue;
                if (hasLine(board, r, c, 0, 1, symbol, winLength)) return true;
                if (hasLine(board, r, c, 1, 0, symbol, winLength)) return true;
                if (hasLine(board, r, c, 1, 1, symbol, winLength)) return true;
                if (hasLine(board, r, c, 1, -1, symbol, winLength)) return true;
            }
        }
        return false;
    }

    private static boolean hasLine(String[][] board, int r, int c, int dr, int dc, String mark, int winLength) {
        int size = board.length;
        int endR = r + (winLength - 1) * dr;
        int endC = c + (winLength - 1) * dc;
        if (endR < 0 || endR >= size || endC < 0 || endC >= size) return false;

        for (int i = 0; i < winLength; i++) {
            if (!mark.equals(board[r + i * dr][c + i * dc])) {
                return false;
            }
        }
        return true;
    }
}