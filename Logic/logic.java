package Logic;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

// Game logic that works with external board or standalone
public class logic {

    private static JButton[][] btn;
    private static char player = 'X';
    private static boolean end = false;
    private static JLabel statusLabel;
    private static JLabel player1ScoreLabel;
    private static JLabel player2ScoreLabel;
    private static int player1Score = 0;
    private static int player2Score = 0;

    // ===== INITIALIZE GAME WITH MENU BOARD =====
    public static void initMenuGame(JButton[] boardButtons, JLabel status, JLabel p1Score, JLabel p2Score) {
        btn = new JButton[3][3];
        // Convert flat array to 3x3 grid
        for(int i = 0; i < 3; i++) {
            for(int j = 0; j < 3; j++) {
                int index = i * 3 + j;
                btn[i][j] = boardButtons[index];
                btn[i][j].setText("");
            }
        }
        player = 'X';
        end = false;
        statusLabel = status;
        player1ScoreLabel = p1Score;
        player2ScoreLabel = p2Score;
        status.setText(">>> Player X's Turn <<<");
    }

    // ===== HANDLE BOARD BUTTON CLICK =====
    public static void handleBoardClick(JButton clicked) {
        if(end) return;
        if(!clicked.getText().equals("")) return;

        clicked.setText(player + "");

        if(checkWin()){
            end = true;
            // Update score
            if(player == 'X') {
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
            JOptionPane.showMessageDialog(null, player + " thắng!");
            return;
        }

        player = (player == 'X') ? 'O' : 'X';
        if(statusLabel != null) {
            String playerName = (player == 'X') ? "Player X" : "Player O";
            statusLabel.setText(">>> " + playerName + "'s Turn <<<");
        }
    }

    // ===== KIỂM TRA THẮNG =====
    private static boolean checkWin(){
        for(int i=0;i<3;i++){
            if(equal(btn[i][0],btn[i][1],btn[i][2])) return true;
            if(equal(btn[0][i],btn[1][i],btn[2][i])) return true;
        }

        if(equal(btn[0][0],btn[1][1],btn[2][2])) return true;
        if(equal(btn[0][2],btn[1][1],btn[2][0])) return true;

        return false;
    }

    // so sánh 3 ô
    private static boolean equal(JButton a, JButton b, JButton c){
        return !a.getText().equals("") &&
                a.getText().equals(b.getText()) &&
                b.getText().equals(c.getText());
    }

    // ===== RESET GAME =====
    public static void resetMenuGame() {
        if(btn == null) return;
        
        for(int i=0;i<3;i++)
            for(int j=0;j<3;j++)
                btn[i][j].setText("");

        player = 'X';
        end = false;
        if(statusLabel != null) {
            statusLabel.setText(">>> Player X's Turn <<<");
        }
    }

    // ===== STANDALONE GAME WINDOW =====
    public static void createStandaloneGame() {
        new GameWindow();
    }

    // ===== BUTTON EVENT HANDLERS FOR NAV =====
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
        
        for(int i = 0; i < 3; i++) {
            for(int j = 0; j < 3; j++) {
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

    // ===== STANDALONE GAME WINDOW CLASS =====
    static class GameWindow extends JFrame implements ActionListener {
        private JButton[][] btn = new JButton[3][3];
        private char player = 'X';
        private boolean end = false;
        private JLabel status = new JLabel("Lượt: X");

        public GameWindow() {
            setTitle("Game XO");
            setSize(350, 400);
            setLayout(new BorderLayout());

            status.setHorizontalAlignment(SwingConstants.CENTER);
            add(status, BorderLayout.NORTH);

            JPanel board = new JPanel(new GridLayout(3,3));

            for(int i=0;i<3;i++){
                for(int j=0;j<3;j++){
                    btn[i][j] = new JButton("");
                    btn[i][j].setFont(new Font("Arial", Font.BOLD, 40));
                    btn[i][j].addActionListener(this);
                    board.add(btn[i][j]);
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
            if(end) return;

            JButton b = (JButton) e.getSource();
            if(!b.getText().equals("")) return;

            b.setText(player + "");

            if(checkWinLocal()){
                end = true;
                JOptionPane.showMessageDialog(this, player + " thắng!");
                return;
            }

            player = (player == 'X') ? 'O' : 'X';
            status.setText("Lượt: " + player);
        }

        private boolean checkWinLocal(){
            for(int i=0;i<3;i++){
                if(equalLocal(btn[i][0],btn[i][1],btn[i][2])) return true;
                if(equalLocal(btn[0][i],btn[1][i],btn[2][i])) return true;
            }

            if(equalLocal(btn[0][0],btn[1][1],btn[2][2])) return true;
            if(equalLocal(btn[0][2],btn[1][1],btn[2][0])) return true;

            return false;
        }

        private boolean equalLocal(JButton a, JButton b, JButton c){
            return !a.getText().equals("") &&
                    a.getText().equals(b.getText()) &&
                    b.getText().equals(c.getText());
        }

        private void resetGame(){
            for(int i=0;i<3;i++)
                for(int j=0;j<3;j++)
                    btn[i][j].setText("");

            player = 'X';
            end = false;
            status.setText("Lượt: X");
        }
    }
}