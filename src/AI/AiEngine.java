package AI;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

// Thuật toán của AI (Trí tuệ nhân tạo)
public class AiEngine {
    private static final Random RANDOM = new Random();

    // Hàm chọn nước đi, sẽ phân luồng thuật toán dựa trên Độ khó đã chọn
    public static AiMove pickMove(String[][] board, String aiSymbol, String humanSymbol, AiDifficulty difficulty, int winLength) {
        if (board == null || board.length == 0) return null;
        int size = board.length;
        switch (difficulty) {
            case HARD:
                // Minimax chỉ áp dụng cho 3x3 do độ phức tạp quá cao (có thể đứng máy nếu tính cho 5x5)
                if (size == 3 && winLength == 3) {
                    AiMove hardMove = minimaxMove(board, aiSymbol, humanSymbol, winLength);
                    if (hardMove != null) return hardMove;
                }
                // Rơi xuống Normal nếu điều kiện không thỏa
                return normalMove(board, aiSymbol, humanSymbol, winLength);
            case NORMAL:
                return normalMove(board, aiSymbol, humanSymbol, winLength);
            case EASY:
            default:
                // Easy: Chỉ đánh ngẫu nhiên vào ô trống
                return randomMove(board);
        }
    }

    // AI độ khó trung bình (Dựa trên Logic thủ công/Luật If-Else đơn giản)
    private static AiMove normalMove(String[][] board, String aiSymbol, String humanSymbol, int winLength) {
        // Cấp bách 1: Máy có thể đi 1 nước để THẮNG LUÔN -> Đi ngay
        AiMove win = findWinningMove(board, aiSymbol, winLength);
        if (win != null) return win;

        // Cấp bách 2: Người chơi chuẩn bị thắng -> Máy chặn ngay lập tức
        AiMove block = findWinningMove(board, humanSymbol, winLength);
        if (block != null) return block;

        // Ưu tiên 3: Chiếm ô chính giữa (vì nó bao quát nhiều hướng chéo nhất)
        int size = board.length;
        int center = size / 2;
        if (isEmpty(board, center, center)) {
            return new AiMove(center, center);
        }

        // Còn không thì random
        return randomMove(board);
    }

    // AI đánh Random: Quét toàn bộ bàn cờ tìm ô trống và random bốc 1 ô
    private static AiMove randomMove(String[][] board) {
        List<AiMove> empty = emptyCells(board);
        if (empty.isEmpty()) return null;
        return empty.get(RANDOM.nextInt(empty.size()));
    }

    // Thử đặt quân vào toàn bộ các ô trống, nếu ô nào đặt xong hàm checkWin() báo thắng -> Lấy ô đó
    private static AiMove findWinningMove(String[][] board, String symbol, int winLength) {
        int size = board.length;
        for (int r = 0; r < size; r++) {
            for (int c = 0; c < size; c++) {
                if (!isEmpty(board, r, c)) continue;
                board[r][c] = symbol;
                boolean win = checkWin(board, winLength);
                board[r][c] = ""; // Khôi phục lại cờ như cũ
                if (win) return new AiMove(r, c);
            }
        }
        return null;
    }

    // --- Thuật toán Minimax: Phân tích trước TẤT CẢ TƯƠNG LAI ---
    private static AiMove minimaxMove(String[][] board, String aiSymbol, String humanSymbol, int winLength) {
        int bestScore = Integer.MIN_VALUE;
        AiMove bestMove = null;
        int size = board.length;

        // Duyệt từng ô trống
        for (int r = 0; r < size; r++) {
            for (int c = 0; c < size; c++) {
                if (!isEmpty(board, r, c)) continue;
                board[r][c] = aiSymbol;
                // Nhảy vào đệ quy minimax để lấy điểm số nếu đánh nước này
                int score = minimax(board, false, aiSymbol, humanSymbol, winLength, 0);
                board[r][c] = ""; // Backtracking: Rút quân cờ lại
                // Đánh giá nhánh tốt nhất
                if (score > bestScore) {
                    bestScore = score;
                    bestMove = new AiMove(r, c);
                }
            }
        }
        return bestMove;
    }

    // Hàm đệ quy lõi Minimax
    private static int minimax(String[][] board, boolean aiTurn, String aiSymbol, String humanSymbol, int winLength, int depth) {
        // Hệ thống điểm số:
        // Thắng: Dương điểm. Thua: Âm điểm.
        // `depth` (độ sâu) dùng để phạt những đường thắng chậm: Muốn thắng thì phải thắng trong thời gian ngắn nhất (10 - depth).
        if (checkWinner(board, aiSymbol, winLength)) return 10 - depth;
        if (checkWinner(board, humanSymbol, winLength)) return depth - 10;
        if (emptyCells(board).isEmpty()) return 0; // Kịch bản ván hòa = 0 điểm

        int size = board.length;
        if (aiTurn) {
            // Lượt của Máy: Cố gắng MAXIMIZE điểm số (Lấy điểm cao nhất)
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
            // Lượt của Người: Cố gắng MINIMIZE điểm số (Hạ máy xuống điểm thấp nhất vì người cũng muốn thắng)
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