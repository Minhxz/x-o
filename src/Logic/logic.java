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

    // Trạng thái bàn cờ, lưu trữ dưới dạng mảng 2 chiều các nút (JButton)
    private static JButton[][] btn;
    private static String playerX = "X"; // Ký hiệu của người chơi 1
    private static String playerO = "O"; // Ký hiệu của người chơi 2
    private static String currentPlayer = playerX; // Lưu xem hiện tại đến lượt ai
    private static boolean end = false; // Biến đánh dấu ván đấu đã kết thúc hay chưa
    
    // Tham chiếu đến các Label trên UI để cập nhật thông tin
    private static JLabel statusLabel;
    private static JLabel timerLabel;
    private static JLabel player1ScoreLabel;
    private static JLabel player2ScoreLabel;
    
    // Lưu điểm số của 2 người chơi
    private static int player1Score = 0;
    private static int player2Score = 0;

    // Cấu hình luật chơi
    private static int boardSize = 3; // Kích thước bàn cờ (3x3 hoặc 5x5)
    private static int winLength = 3; // Số lượng quân liên tiếp cần thiết để thắng
    private static Theme theme = Theme.EARTH; // Theme giao diện mặc định

    // Cấu hình AI
    private static boolean aiEnabled = false; // Có chơi với máy không
    private static AiDifficulty aiDifficulty = AiDifficulty.NORMAL; // Độ khó
    private static String aiSymbol = "🤖"; // Icon của AI

    // Thông tin người chơi & thời gian
    private static String p1Name = "Player 1";
    private static String p2Name = "Player 2";
    private static int turnTimeLimit = 0; // Giới hạn thời gian mỗi lượt (0 là không giới hạn)
    private static int timeLeft = 0; // Thời gian đếm ngược còn lại
    private static Timer turnTimer; // Bộ đếm giờ của Swing
    
    // Biến lưu thời điểm trận đấu bắt đầu (để tính thời lượng ván đấu)
    private static long matchStartTime = 0;

    // Biến lưu Email của tài khoản đang đăng nhập hiện tại
    private static String currentAccountEmail = "";

    public static void setCurrentAccountEmail(String email) { currentAccountEmail = email; }
    public static String getCurrentAccountEmail() { return currentAccountEmail; }

    // Hàm tính toán và định dạng thời gian đã trôi qua (phút:giây)
    private static String getElapsedTimeString() {
        long elapsedSec = (System.currentTimeMillis() - matchStartTime) / 1000;
        long mm = elapsedSec / 60;
        long ss = elapsedSec % 60;
        return String.format("%02d:%02d", mm, ss);
    }

    // Hàm ghi lại kết quả ván đấu vào cơ sở dữ liệu (Database)
    public static void saveGameHistory(String p1, String p2, String winner, String matchTime) {
        // Bỏ qua nếu chưa đăng nhập
        if (currentAccountEmail == null || currentAccountEmail.isEmpty()) {
            System.out.println("Cảnh báo: Chưa đăng nhập, ván đấu này sẽ không được lưu vào MySQL!");
            return; 
        }

        // Câu lệnh SQL thêm dữ liệu vào bảng history
        String sql = "INSERT INTO history (account_email, player1_name, player2_name, winner, match_time) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = Database.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, currentAccountEmail);
            pstmt.setString(2, p1);
            pstmt.setString(3, p2);
            pstmt.setString(4, winner);
            pstmt.setString(5, matchTime); 
            pstmt.executeUpdate();
            System.out.println("Đã lưu lịch sử vào MySQL thành công!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // --- Các hàm Getter & Setter ---
    public static String getP1Name() { return p1Name; }
    public static String getP2Name() { return aiEnabled ? "AI" : p2Name; } // Nếu AI bật thì trả về "AI"
    
    public static void setPlayerNames(String n1, String n2) {
        if (n1 != null && !n1.trim().isEmpty()) p1Name = n1.trim();
        if (n2 != null && !n2.trim().isEmpty()) p2Name = n2.trim();
    }

    public static void setTurnTimeLimit(int limit) { turnTimeLimit = limit; }
    public static int getTurnTimeLimit() { return turnTimeLimit; }

    public static int getBoardSize() { return boardSize; }

    public static void setBoardSize(int size) {
        // Chỉ hỗ trợ bàn 3x3 và 5x5
        if (size != 3 && size != 5) return;
        boardSize = size;
        winLength = size;
        
        // Reset mảng nút nếu đổi size
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

    // Khởi tạo các biến và liên kết mảng nút từ giao diện UI truyền sang
    public static void initMenuGame(JButton[] boardButtons, JLabel status, JLabel p1Score, JLabel p2Score, JLabel tLabel) {
        btn = new JButton[boardSize][boardSize];
        // Chuyển từ mảng 1 chiều sang mảng 2 chiều cho dễ tính toán logic tọa độ
        for(int i = 0; i < boardSize; i++) {
            for(int j = 0; j < boardSize; j++) {
                int index = i * boardSize + j;
                btn[i][j] = boardButtons[index];
                btn[i][j].setText("");
            }
        }
        currentPlayer = playerX; // Player 1 đánh trước
        end = false;
        statusLabel = status;
        timerLabel = tLabel;
        player1ScoreLabel = p1Score;
        player2ScoreLabel = p2Score;
        
        matchStartTime = System.currentTimeMillis(); // Bắt đầu tính giờ trận đấu
        
        updateStatusLabel();
        startTimer(); // Chạy đồng hồ đếm ngược lượt
    }

    public static void stopTimer() {
        if (turnTimer != null) turnTimer.stop();
    }

    // Bắt đầu hoặc khởi động lại đồng hồ cho lượt mới
    private static void startTimer() {
        stopTimer();
        if (end) return;
        
        if (turnTimeLimit <= 0) {
            if (timerLabel != null) timerLabel.setText("Timer: Off");
            return;
        }
        
        timeLeft = turnTimeLimit;
        updateTimerLabelText();

        // Swing Timer lặp lại mỗi 1000ms (1 giây)
        turnTimer = new Timer(1000, e -> {
            if (end) {
                stopTimer();
                return;
            }
            timeLeft--;
            updateTimerLabelText();
            // Nếu hết giờ thì gọi hàm xử lý TimeOut
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

    // Hàm xử lý khi một người chơi bị hết thời gian (bị xử thua)
    private static void handleTimeOut() {
        if (end) return;
        end = true; // Kết thúc game
        
        String winnerName = currentPlayer.equals(playerX) ? getP2Name() : getP1Name();
        if (statusLabel != null) {
            statusLabel.setText("Time Out! " + winnerName + " wins!");
        }
        
        // Lưu vào MySQL
        saveGameHistory(getP1Name(), getP2Name(), winnerName + " (Time Out)", getElapsedTimeString());

        // Chơi nhạc phù hợp
        if (isAiEnabled() && winnerName.equals(getAiSymbol())) {
            Music.gameMusic.playLoseSound(); 
        } else {
            Music.gameMusic.playWinSound(); 
        }

        // Cộng điểm
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

    // Hàm được gọi khi người chơi bấm vào một ô trên bàn cờ UI
    public static void handleBoardClick(JButton clicked) {
        if(end) return; // Nếu game đã kết thúc thì không phản hồi
        if(!clicked.getText().equals("")) return; // Ô đã có quân thì không phản hồi

        // Áp dụng nước đi của người chơi
        applyMove(clicked);

        // Nếu chế độ AI đang bật và lượt tiếp theo là của AI thì cho AI đánh
        if (aiEnabled && !end && currentPlayer.equals(aiSymbol)) {
            performAiMove();
        }

        // Nếu bàn cờ kín chỗ mà chưa ai thắng -> Hòa
        if (!end && isBoardFull()) {
            end = true;
            stopTimer();
            if (statusLabel != null) statusLabel.setText("Draw!");
            
            saveGameHistory(getP1Name(), getP2Name(), "Draw", getElapsedTimeString());
            Music.gameMusic.playDrawSound();
            
            JOptionPane.showMessageDialog(null, "Draw!", "Draw", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    // Hàm thực hiện thay đổi ô cờ, kiểm tra kết quả và đổi phiên
    private static void applyMove(JButton clicked) {
        gameMusic.playClick(); // Phát tiếng cộc
        clicked.setText(currentPlayer); // Đặt quân cờ

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

        // Đổi người chơi
        currentPlayer = currentPlayer.equals(playerX) ? playerO : playerX;
        updateStatusLabel();
        startTimer(); // Reset lại timer cho lượt mới
    }

    // Cộng điểm UI
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

    // Kích hoạt AI để chọn nước đi
    private static void performAiMove() {
        if (btn == null) return;
        
        // Trích xuất trạng thái bàn cờ hiện tại thành mảng String 2D cho AI xử lý
        String[][] board = new String[boardSize][boardSize];
        for (int r = 0; r < boardSize; r++) {
            for (int c = 0; c < boardSize; c++) {
                board[r][c] = btn[r][c].getText();
            }
        }

        // Lấy tọa độ nước đi từ AI
        AiMove move = AiEngine.pickMove(board, aiSymbol, playerX, aiDifficulty, winLength);
        if (move == null) return;
        if (move.row < 0 || move.row >= boardSize || move.col < 0 || move.col >= boardSize) return;

        JButton target = btn[move.row][move.col];
        if (target == null || !target.getText().isEmpty()) return;
        
        applyMove(target);
    }

    // Kiểm tra xem đã có ai chiến thắng chưa
    private static boolean checkWin(){
        if (btn == null) return false;
        for (int r = 0; r < boardSize; r++) {
            for (int c = 0; c < boardSize; c++) {
                String mark = btn[r][c].getText();
                if (mark.equals("")) continue;
                // Kiểm tra 4 hướng (ngang, dọc, chéo chính, chéo phụ)
                if (hasLine(r, c, 0, 1, mark)) return true; // Hướng ngang
                if (hasLine(r, c, 1, 0, mark)) return true; // Hướng dọc
                if (hasLine(r, c, 1, 1, mark)) return true; // Hướng chéo góc \
                if (hasLine(r, c, 1, -1, mark)) return true; // Hướng chéo góc /
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

    // Hàm đệ quy/duyệt vòng lặp để kiểm tra có chuỗi n ký tự liên tiếp nhau không
    private static boolean hasLine(int r, int c, int dr, int dc, String mark) {
        int endR = r + (winLength - 1) * dr;
        int endC = c + (winLength - 1) * dc;
        
        // Nếu chuỗi kiểm tra vượt khỏi bàn cờ thì chắn chắn false
        if (endR < 0 || endR >= boardSize || endC < 0 || endC >= boardSize) return false;

        for (int i = 0; i < winLength; i++) {
            if (!btn[r + i * dr][c + i * dc].getText().equals(mark)) return false;
        }
        return true;
    }

    // Reset lại ván đấu (xóa trắng các nút UI)
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
        matchStartTime = System.currentTimeMillis(); // Reset lại thời gian
        
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