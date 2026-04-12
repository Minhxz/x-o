package Logic;

import AI.AiDifficulty;
import AI.AiEngine;
import AI.AiMove;
import Fancy.Theme;
import Music.gameMusic; // Đã thêm dòng import class âm thanh

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class logic {

    private static JButton[][] btn;
    private static String playerX = "X";
    private static String playerO = "O";
    private static String currentPlayer = playerX;
    private static boolean end = false;
    private static JLabel statusLabel;
    private static JLabel player1ScoreLabel;
    private static JLabel player2ScoreLabel;
    private static int player1Score = 0;
    private static int player2Score = 0;

    private static int boardSize = 3;
    private static int winLength = 3;
    private static Theme theme = Theme.EARTH;

    private static boolean aiEnabled = false;
    private static AiDifficulty aiDifficulty = AiDifficulty.NORMAL;
    private static String aiSymbol = "🤖";

    public static int getBoardSize() {
        return boardSize;
    }

    public static void setBoardSize(int size) {
        if (size != 3 && size != 5) return;
        boardSize = size;
        winLength = size;
        resetMenuGame();
    }

    public static Theme getTheme() {
        return theme;
    }

    public static void setTheme(Theme newTheme) {
        if (newTheme != null) {
            theme = newTheme;
        }
    }

    public static void setPlayerSymbols(String x, String o) {
        if (x == null || x.trim().isEmpty()) x = "X";
        if (o == null || o.trim().isEmpty()) o = "O";
        playerX = x;
        if (aiEnabled) {
            if (aiSymbol == null || aiSymbol.trim().isEmpty()) {
                aiSymbol = "🤖";
            }
            playerO = aiSymbol;
        } else {
            playerO = o;
        }
        currentPlayer = playerX;
        if (statusLabel != null) {
            statusLabel.setText(">>> Player " + currentPlayer + "'s Turn <<<");
        }
    }

    public static String getPlayerX() {
        return playerX;
    }

    public static String getPlayerO() {
        return playerO;
    }

    public static boolean isAiEnabled() {
        return aiEnabled;
    }

    public static void setAiEnabled(boolean enabled) {
        aiEnabled = enabled;
        if (aiEnabled) {
            if (aiSymbol == null || aiSymbol.trim().isEmpty()) {
                aiSymbol = "🤖";
            }
            playerO = aiSymbol;
        }
    }

    public static AiDifficulty getAiDifficulty() {
        return aiDifficulty;
    }

    public static void setAiDifficulty(AiDifficulty difficulty) {
        if (difficulty != null) {
            aiDifficulty = difficulty;
        }
    }

    public static String getAiSymbol() {
        return aiSymbol;
    }

    public static void setAiSymbol(String symbol) {
        if (symbol == null || symbol.trim().isEmpty()) return;
        aiSymbol = symbol;
        if (aiEnabled) {
            playerO = aiSymbol;
        }
    }

    public static void initMenuGame(JButton[] boardButtons, JLabel status, JLabel p1Score, JLabel p2Score) {
        btn = new JButton[boardSize][boardSize];
        for(int i = 0; i < boardSize; i++) {
            for(int j = 0; j < boardSize; j++) {
                int index = i * boardSize + j;
                btn[i][j] = boardButtons[index];
                btn[i][j].setText("");
            }
        }
        currentPlayer = playerX;
        end = false;
        statusLabel = status;
        player1ScoreLabel = p1Score;
        player2ScoreLabel = p2Score;
        status.setText(">>> Player " + currentPlayer + "'s Turn <<<");
    }

    public static void handleBoardClick(JButton clicked) {
        if(end) return;
        if(!clicked.getText().equals("")) return;

        applyMove(clicked);

        if (aiEnabled && !end && currentPlayer.equals(aiSymbol)) {
            performAiMove();
        }

        if (!end && isBoardFull()) {
            end = true;
            if (statusLabel != null) {
                statusLabel.setText("Draw!");
            }
            JOptionPane.showMessageDialog(null, "Draw!", "Draw", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private static void applyMove(JButton clicked) {
        // ĐÃ THÊM: Phát âm thanh click mỗi khi đánh cờ
        gameMusic.playClick();

        clicked.setText(currentPlayer);

        if(checkWin()){ 
            end = true;
            updateScoreForCurrentPlayer();
            if (statusLabel != null) {
                statusLabel.setText("Winner: " + currentPlayer);
            }
            JOptionPane.showMessageDialog(null, currentPlayer + " thắng!");
            return;
        }

        if (isBoardFull()) {
            end = true;
            if (statusLabel != null) {
                statusLabel.setText("Draw!");
            }
            JOptionPane.showMessageDialog(null, "Draw!", "Draw", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        currentPlayer = currentPlayer.equals(playerX) ? playerO : playerX;
        if(statusLabel != null) {
            statusLabel.setText(">>> Player " + currentPlayer + "'s Turn <<<");
        }
    }

    private static void updateScoreForCurrentPlayer() {
        if(currentPlayer.equals(playerX)) {
            player1Score++;
            if(player1ScoreLabel != null) {
                player1ScoreLabel.setText(String.valueOf(player1Score));
                player1ScoreLabel.revalidate();
                player1ScoreLabel.repaint();
            }
        } else {
            player2Score++;
            if(player2ScoreLabel != null) {
                player2ScoreLabel.setText(String.valueOf(player2Score));
                player2ScoreLabel.revalidate();
                player2ScoreLabel.repaint();
            }
        }
    }

    private static void performAiMove() {
        if (btn == null) return;
        String[][] board = new String[boardSize][boardSize];
        for (int r = 0; r < boardSize; r++) {
            for (int c = 0; c < boardSize; c++) {
                board[r][c] = btn[r][c].getText();
            }
        }

        AiMove move = AiEngine.pickMove(board, aiSymbol, playerX, aiDifficulty, winLength);
        if (move == null) return;
        if (move.row < 0 || move.row >= boardSize || move.col < 0 || move.col >= boardSize) return;

        JButton target = btn[move.row][move.col];
        if (target == null || !target.getText().isEmpty()) return;
        
        // Máy đánh cũng sẽ gọi hàm applyMove -> cũng sẽ kêu tiếng click
        applyMove(target);
    }

    private static boolean checkWin(){
        if (btn == null) return false;
        for (int r = 0; r < boardSize; r++) {
            for (int c = 0; c < boardSize; c++) {
                String mark = btn[r][c].getText();
                if (mark.equals("")) continue;
                if (hasLine(r, c, 0, 1, mark)) return true;
                if (hasLine(r, c, 1, 0, mark)) return true;
                if (hasLine(r, c, 1, 1, mark)) return true;
                if (hasLine(r, c, 1, -1, mark)) return true;
            }
        }
        return false;
    }

    private static boolean isBoardFull() {
        if (btn == null) return false;
        for (int r = 0; r < boardSize; r++) {
            for (int c = 0; c < boardSize; c++) {
                if (btn[r][c].getText().equals("")) {
                    return false;
                }
            }
        }
        return true;
    }

    private static boolean hasLine(int r, int c, int dr, int dc, String mark) {
        int endR = r + (winLength - 1) * dr;
        int endC = c + (winLength - 1) * dc;
        if (endR < 0 || endR >= boardSize || endC < 0 || endC >= boardSize) return false;

        for (int i = 0; i < winLength; i++) {
            if (!btn[r + i * dr][c + i * dc].getText().equals(mark)) {
                return false;
            }
        }
        return true;
    }

    public static void resetMenuGame() {
        if(btn == null) return;

        for(int i=0;i<boardSize;i++)
            for(int j=0;j<boardSize;j++)
                btn[i][j].setText("");

        currentPlayer = playerX;
        end = false;
        if(statusLabel != null) {
            statusLabel.setText(">>> Player " + currentPlayer + "'s Turn <<<");
        }
    }

    public static void createStandaloneGame() {
        new GameWindow();
    }

    public static void handleSettingsClick() {
        JOptionPane.showMessageDialog(null,
                "Settings\n\n" +
                        "Game difficulty and preferences can be configured here.",
                "Settings", JOptionPane.INFORMATION_MESSAGE);
    }

    public static void handleScoresClick() {
        JOptionPane.showMessageDialog(null,
                "High Scores\n\n" +
                        "Player 1: 5 wins\n" +
                        "Player 2: 3 wins\n" +
                        "Draws: 2",
                "Scores", JOptionPane.INFORMATION_MESSAGE);
    }

    public static void handleRulesClick() {
        JOptionPane.showMessageDialog(null,
                "Tic Tac Toe Rules\n\n" +
                        "1. Players take turns placing X or O\n" +
                        "2. First player to get 3 in a row wins\n" +
                        "3. If all 9 squares are filled with no winner, it's a draw\n" +
                        "4. Use Hint for a suggested move\n" +
                        "5. Use Undo to take back your last move",
                "Rules", JOptionPane.INFORMATION_MESSAGE);
    }

    public static void handleExitClick() {
        int response = JOptionPane.showConfirmDialog(null,
                "Are you sure you want to exit?",
                "Exit Game",
                JOptionPane.YES_NO_OPTION);
        if(response == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }

    public static void handleUndoClick() {
        JOptionPane.showMessageDialog(null,
                "Undo functionality coming soon",
                "Undo", JOptionPane.INFORMATION_MESSAGE);
    }

    public static void handleHintClick() {
        if(end) {
            JOptionPane.showMessageDialog(null, "Game has ended. Start a new game!", "Game Over", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        for(int i = 0; i < boardSize; i++) {
            for(int j = 0; j < boardSize; j++) {
                if(btn[i][j].getText().equals("")) {
                    JOptionPane.showMessageDialog(null,
                            "Suggested move:\nPosition (" + (i+1) + ", " + (j+1) + ")",
                            "Hint", JOptionPane.INFORMATION_MESSAGE);
                    return;
                }
            }
        }
        JOptionPane.showMessageDialog(null, "Board is full!", "Hint", JOptionPane.INFORMATION_MESSAGE);
    }

    static class GameWindow extends JFrame implements ActionListener {
        private JButton[][] btnLocal = new JButton[boardSize][boardSize];
        private String player = playerX;
        private boolean endLocal = false;
        private JLabel status = new JLabel("Lượt: " + playerX);

        public GameWindow() {
            setTitle("Game XO");
            int sizePx = (boardSize == 3) ? 350 : 650;
            setSize(sizePx, sizePx + 80);
            setLayout(new BorderLayout());

            status.setHorizontalAlignment(SwingConstants.CENTER);
            add(status, BorderLayout.NORTH);

            JPanel board = new JPanel(new GridLayout(boardSize, boardSize));

            int fontSize = (boardSize == 3) ? 40 : 24;
            for(int i=0;i<boardSize;i++){    
                for(int j=0;j<boardSize;j++){    
                    btnLocal[i][j] = new JButton("");
                    btnLocal[i][j].setFont(new Font("Arial", Font.BOLD, fontSize));
                    btnLocal[i][j].addActionListener(this);
                    board.add(btnLocal[i][j]);
                }
            }

            add(board, BorderLayout.CENTER);

            JButton reset = new JButton("Reset");
            reset.addActionListener(e -> resetGame());
            add(reset, BorderLayout.SOUTH);

            setDefaultCloseOperation(EXIT_ON_CLOSE);
            setVisible(true);
        }

        public void actionPerformed(ActionEvent e) {
            if(endLocal) return;

            JButton b = (JButton) e.getSource();
            if(!b.getText().equals("")) return;

            b.setText(player);

            if(checkWinLocal()){ 
                endLocal = true;
                status.setText("Winner: " + player);
                JOptionPane.showMessageDialog(this, player + " thắng!");
                return;
            }

            if (isBoardFullLocal()) {
                endLocal = true;
                status.setText("Draw!");
                JOptionPane.showMessageDialog(this, "Draw!", "Draw", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            player = player.equals(playerX) ? playerO : playerX;
            status.setText("Lượt: " + player);
        }

        private boolean checkWinLocal(){
            for (int r = 0; r < boardSize; r++) {
                for (int c = 0; c < boardSize; c++) {
                    String mark = btnLocal[r][c].getText();
                    if (mark.equals("")) continue;
                    if (hasLineLocal(r, c, 0, 1, mark)) return true;
                    if (hasLineLocal(r, c, 1, 0, mark)) return true;
                    if (hasLineLocal(r, c, 1, 1, mark)) return true;
                    if (hasLineLocal(r, c, 1, -1, mark)) return true;
                }
            }
            return false;
        }

        private boolean isBoardFullLocal(){
            for (int r = 0; r < boardSize; r++) {
                for (int c = 0; c < boardSize; c++) {
                    if (btnLocal[r][c].getText().equals("")) {
                        return false;
                    }
                }
            }
            return true;
        }

        private boolean hasLineLocal(int r, int c, int dr, int dc, String mark) {
            int endR = r + (winLength - 1) * dr;
            int endC = c + (winLength - 1) * dc;
            if (endR < 0 || endR >= boardSize || endC < 0 || endC >= boardSize) return false;

            for (int i = 0; i < winLength; i++) {
                if (!btnLocal[r + i * dr][c + i * dc].getText().equals(mark)) {
                    return false;
                }
            }
            return true;
        }

        private void resetGame(){
            for(int i=0;i<boardSize;i++)
                for(int j=0;j<boardSize;j++)
                    btnLocal[i][j].setText("");

            player = playerX;
            endLocal = false;
            status.setText("Lượt: " + player);
        }
    }
}