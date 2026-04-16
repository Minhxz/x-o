package Logic;

import AI.AiDifficulty;
import AI.AiEngine;
import AI.AiMove;
import Fancy.Theme;
import Music.gameMusic; 

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class logic {

    private static JButton[][] btn;
    private static String playerX = "X";
    private static String playerO = "O";
    private static String currentPlayer = playerX;
    private static boolean end = false;
    
    private static JLabel statusLabel;
    private static JLabel timerLabel;
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

    private static String p1Name = "Player 1";
    private static String p2Name = "Player 2";
    private static int turnTimeLimit = 0; 
    private static int timeLeft = 0;
    private static Timer turnTimer;
    
    // Tracks the exact start time of the match
    private static long matchStartTime = 0;

    // --- BIẾN LƯU EMAIL TÀI KHOẢN ĐANG CHƠI ---
    private static String currentAccountEmail = "";

    public static void setCurrentAccountEmail(String email) { currentAccountEmail = email; }
    public static String getCurrentAccountEmail() { return currentAccountEmail; }

    // Tính toán thời gian đã trôi qua
    private static String getElapsedTimeString() {
        long elapsedSec = (System.currentTimeMillis() - matchStartTime) / 1000;
        long mm = elapsedSec / 60;
        long ss = elapsedSec % 60;
        return String.format("%02d:%02d", mm, ss);
    }

    // --- HÀM LƯU LỊCH SỬ VÀO DATABASE ---
    public static void saveGameHistory(String p1, String p2, String winner, String matchTime) {
        if (currentAccountEmail == null || currentAccountEmail.isEmpty()) {
            System.out.println("Cảnh báo: Chưa đăng nhập, ván đấu này sẽ không được lưu vào MySQL!");
            return; 
        }

        String sql = "INSERT INTO history (account_email, player1_name, player2_name, winner, match_time) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = Database.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, currentAccountEmail);
            pstmt.setString(2, p1);
            pstmt.setString(3, p2);
            pstmt.setString(4, winner);
            pstmt.setString(5, matchTime); // Added match_time parameter
            pstmt.executeUpdate();
            System.out.println("Đã lưu lịch sử vào MySQL thành công!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static String getP1Name() { return p1Name; }
    public static String getP2Name() { return aiEnabled ? "AI" : p2Name; }
    
    public static void setPlayerNames(String n1, String n2) {
        if (n1 != null && !n1.trim().isEmpty()) p1Name = n1.trim();
        if (n2 != null && !n2.trim().isEmpty()) p2Name = n2.trim();
    }

    public static void setTurnTimeLimit(int limit) { turnTimeLimit = limit; }
    public static int getTurnTimeLimit() { return turnTimeLimit; }

    public static int getBoardSize() { return boardSize; }

    public static void setBoardSize(int size) {
        if (size != 3 && size != 5) return;
        boardSize = size;
        winLength = size;
        
        if (btn != null && btn.length != size) {
            btn = null; 
        } else {
            resetMenuGame();
        }
    }

    public static Theme getTheme() { return theme; }
    public static void setTheme(Theme newTheme) { if (newTheme != null) theme = newTheme; }

    public static void setPlayerSymbols(String x, String o) {
        if (x == null || x.trim().isEmpty()) x = "X";
        if (o == null || o.trim().isEmpty()) o = "O";
        playerX = x;
        if (aiEnabled) {
            if (aiSymbol == null || aiSymbol.trim().isEmpty()) aiSymbol = "🤖";
            playerO = aiSymbol;
        } else {
            playerO = o;
        }
        currentPlayer = playerX;
        updateStatusLabel();
    }

    public static String getPlayerX() { return playerX; }
    public static String getPlayerO() { return playerO; }

    public static boolean isAiEnabled() { return aiEnabled; }

    public static void setAiEnabled(boolean enabled) {
        aiEnabled = enabled;
        if (aiEnabled) {
            if (aiSymbol == null || aiSymbol.trim().isEmpty()) aiSymbol = "🤖";
            playerO = aiSymbol;
        }
    }

    public static AiDifficulty getAiDifficulty() { return aiDifficulty; }
    public static void setAiDifficulty(AiDifficulty difficulty) { if (difficulty != null) aiDifficulty = difficulty; }

    public static String getAiSymbol() { return aiSymbol; }
    public static void setAiSymbol(String symbol) {
        if (symbol == null || symbol.trim().isEmpty()) return;
        aiSymbol = symbol;
        if (aiEnabled) playerO = aiSymbol;
    }

    public static void initMenuGame(JButton[] boardButtons, JLabel status, JLabel p1Score, JLabel p2Score, JLabel tLabel) {
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
        timerLabel = tLabel;
        player1ScoreLabel = p1Score;
        player2ScoreLabel = p2Score;
        
        matchStartTime = System.currentTimeMillis(); // Start match timer
        
        updateStatusLabel();
        startTimer(); 
    }

    public static void stopTimer() {
        if (turnTimer != null) turnTimer.stop();
    }

    private static void startTimer() {
        stopTimer();
        if (end) return;
        
        if (turnTimeLimit <= 0) {
            if (timerLabel != null) timerLabel.setText("Timer: Off");
            return;
        }
        
        timeLeft = turnTimeLimit;
        updateTimerLabelText();

        turnTimer = new Timer(1000, e -> {
            if (end) {
                stopTimer();
                return;
            }
            timeLeft--;
            updateTimerLabelText();
            if (timeLeft <= 0) {
                stopTimer();
                handleTimeOut();
            }
        });
        turnTimer.start();
    }

    private static void updateTimerLabelText() {
        if (timerLabel != null) {
            timerLabel.setText("⏳ Time Left: " + timeLeft + "s");
        }
    }

    private static void handleTimeOut() {
        if (end) return;
        end = true;
        
        String winnerName = currentPlayer.equals(playerX) ? getP2Name() : getP1Name();
        if (statusLabel != null) {
            statusLabel.setText("Time Out! " + winnerName + " wins!");
        }
        
        saveGameHistory(getP1Name(), getP2Name(), winnerName + " (Time Out)", getElapsedTimeString());

        if (isAiEnabled() && winnerName.equals(getAiSymbol())) {
            Music.gameMusic.playLoseSound(); 
        } else {
            Music.gameMusic.playWinSound(); 
        }

        if (currentPlayer.equals(playerX)) {
            player2Score++;
            if (player2ScoreLabel != null) player2ScoreLabel.setText(String.valueOf(player2Score));
        } else {
            player1Score++;
            if (player1ScoreLabel != null) player1ScoreLabel.setText(String.valueOf(player1Score));
        }
        
        JOptionPane.showMessageDialog(null, "Hết giờ! " + winnerName + " thắng!", "Time Out", JOptionPane.WARNING_MESSAGE);
    }

    private static void updateStatusLabel() {
        if (statusLabel != null) {
            String name = currentPlayer.equals(playerX) ? getP1Name() : getP2Name();
            statusLabel.setText(">>> " + name + "'s Turn <<<");
        }
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
            stopTimer();
            if (statusLabel != null) statusLabel.setText("Draw!");
            
            saveGameHistory(getP1Name(), getP2Name(), "Draw", getElapsedTimeString());
            Music.gameMusic.playDrawSound();
            
            JOptionPane.showMessageDialog(null, "Draw!", "Draw", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private static void applyMove(JButton clicked) {
        gameMusic.playClick();
        clicked.setText(currentPlayer);

        if(checkWin()){ 
            end = true;
            stopTimer();
            updateScoreForCurrentPlayer();
            String winnerName = currentPlayer.equals(playerX) ? getP1Name() : getP2Name();
            if (statusLabel != null) statusLabel.setText("Winner: " + winnerName);
            
            saveGameHistory(getP1Name(), getP2Name(), winnerName, getElapsedTimeString());

            if (isAiEnabled() && currentPlayer.equals(aiSymbol)) {
                Music.gameMusic.playLoseSound(); 
            } else {
                Music.gameMusic.playWinSound(); 
            }
            
            JOptionPane.showMessageDialog(null, winnerName + " thắng!");
            return;
        }

        if (isBoardFull()) {
            end = true;
            stopTimer();
            if (statusLabel != null) statusLabel.setText("Draw!");
            
            saveGameHistory(getP1Name(), getP2Name(), "Draw", getElapsedTimeString());
            Music.gameMusic.playDrawSound();
            
            JOptionPane.showMessageDialog(null, "Draw!", "Draw", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        currentPlayer = currentPlayer.equals(playerX) ? playerO : playerX;
        updateStatusLabel();
        startTimer(); 
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
                if (btn[r][c].getText().equals("")) return false;
            }
        }
        return true;
    }

    private static boolean hasLine(int r, int c, int dr, int dc, String mark) {
        int endR = r + (winLength - 1) * dr;
        int endC = c + (winLength - 1) * dc;
        if (endR < 0 || endR >= boardSize || endC < 0 || endC >= boardSize) return false;

        for (int i = 0; i < winLength; i++) {
            if (!btn[r + i * dr][c + i * dc].getText().equals(mark)) return false;
        }
        return true;
    }

    public static void resetMenuGame() {
        if(btn == null) return;
        
        if(btn.length != boardSize) {
            btn = null;
            return;
        }

        for(int i=0;i<boardSize;i++)
            for(int j=0;j<boardSize;j++)
                if(btn[i][j] != null) btn[i][j].setText("");

        currentPlayer = playerX;
        end = false;
        matchStartTime = System.currentTimeMillis(); // Reset match timer for new game
        
        updateStatusLabel();
        startTimer(); 
    }
    
    public static void handleHistoryClick() {
        MenuActions.showHistory(null);
    }
    
    public static void handleExitClick() {
        int response = JOptionPane.showConfirmDialog(null, "Are you sure you want to exit?", "Exit Game", JOptionPane.YES_NO_OPTION);
        if(response == JOptionPane.YES_OPTION) System.exit(0);
    }
}