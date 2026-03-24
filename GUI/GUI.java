package GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class GUI extends JFrame {
    private JPanel boardPanel;
    private JButton[][] buttons;
    private JLabel lblScoreX, lblScoreO, lblTurn;
    private JButton btnReset;

    public GUI() {
        setTitle("Tic-Tac-Toe");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 500);
        setLocationRelativeTo(null);

        // Score panel
        JPanel scorePanel = new JPanel();
        scorePanel.setLayout(new GridLayout(1, 3, 10, 10));
        lblScoreX = new JLabel("X: 0");
        lblScoreO = new JLabel("O: 0");
        lblTurn = new JLabel("Turn: X");
        lblScoreX.setHorizontalAlignment(SwingConstants.CENTER);
        lblScoreO.setHorizontalAlignment(SwingConstants.CENTER);
        lblTurn.setHorizontalAlignment(SwingConstants.CENTER);
        scorePanel.add(lblScoreX);
        scorePanel.add(lblTurn);
        scorePanel.add(lblScoreO);

        // Board panel (3x3)
        boardPanel = new JPanel();
        boardPanel.setLayout(new GridLayout(3, 3, 3, 3));
        buttons = new JButton[3][3];
        Font btnFont = new Font("Arial", Font.BOLD, 40);
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                final int row = i, col = j;
                buttons[i][j] = new JButton("");
                buttons[i][j].setFont(btnFont);
                buttons[i][j].setFocusable(false);
                buttons[i][j].addActionListener(e -> {
                    // Tạm thời chỉ cho bấm, chưa nối GameLogic
                    if (buttons[row][col].getText().equals("")) {
                        buttons[row][col].setText(lblTurn.getText().endsWith("X") ? "X" : "O");
                        // Đổi lượt tạm thời
                        lblTurn.setText("Turn: " + (lblTurn.getText().endsWith("X") ? "O" : "X"));
                    }
                });
                boardPanel.add(buttons[i][j]);
            }
        }

        // Reset button
        btnReset = new JButton("Reset Game");
        btnReset.addActionListener(e -> resetBoard());

        // Layout
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout(10, 10));
        mainPanel.add(scorePanel, BorderLayout.NORTH);
        mainPanel.add(boardPanel, BorderLayout.CENTER);
        mainPanel.add(btnReset, BorderLayout.SOUTH);

        setContentPane(mainPanel);
        setVisible(true);
    }

    // Hàm reset bàn cờ
    private void resetBoard() {
        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 3; j++)
                buttons[i][j].setText("");  
        lblTurn.setText("Turn: X");
    }
}