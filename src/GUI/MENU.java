package GUI;

import Logic.logic;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class MENU extends JFrame {

    private JLabel statusLabel;
    private JLabel player1ScoreLabel;
    private JLabel player2ScoreLabel;
    private JButton[] boardButtons;

    public MENU() {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(1980, 1080);
        setLocationRelativeTo(null);
        setTitle("Tic Tac Toe - X O Game");

        //Màu sắc
        Color BG_RED = new Color(0xB02222);
        Color ORANGE = new Color(0xE26A12);
        Color ORANGE_DARK = new Color(0xC85A0E);
        Color BOARD_BEIGE = new Color(0xD8B59C);
        Color TILE_BROWN = new Color(0x8B3B06);
        Color WHITE = Color.WHITE;
        Color TEXT_BLACK = new Color(0x111111);
        Color ACCENT_GOLD = new Color(0xFFD700);
        
        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(BG_RED);
        setContentPane(root);

        // ---------- Phầm tiêu đề ----------
        JPanel headerPanel = new JPanel(new GridBagLayout());
        headerPanel.setBackground(BG_RED);
        headerPanel.setBorder(new EmptyBorder(20, 20, 10, 20));
        
        JLabel titleLabel = new JLabel("TIC TAC TOE");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 48));
        titleLabel.setForeground(ACCENT_GOLD);
        headerPanel.add(titleLabel);
        root.add(headerPanel, BorderLayout.NORTH);

        // ---------- Nav Trên  ----------
        JPanel top = new JPanel(new GridBagLayout());
        top.setOpaque(true);
        top.setBackground(BG_RED);
        top.setBorder(new EmptyBorder(10, 20, 15, 20));

        RoundedPanel nav = new RoundedPanel(30);
        nav.setBackground(ORANGE_DARK);
        nav.setLayout(new GridLayout(1, 4, 30, 0));
        nav.setBorder(new EmptyBorder(15, 50, 15, 50));
        
        JButton settingBtn = navBtn("[*] Settings", WHITE);
        JButton loginBtn = navBtn("[→] Login", WHITE);
        JButton rulesBtn = navBtn("[i] Rules", WHITE);
        JButton exitBtn = navBtn("[x] Exit", WHITE);
        
        settingBtn.addActionListener(e -> logic.handleSettingsClick());
        loginBtn.addActionListener(e -> new dangnhap());
        rulesBtn.addActionListener(e -> logic.handleRulesClick());
        exitBtn.addActionListener(e -> logic.handleExitClick());
        
        nav.add(settingBtn);
        nav.add(loginBtn);
        nav.add(rulesBtn);
        nav.add(exitBtn);

        top.add(nav);
        root.add(top, BorderLayout.NORTH);



        // ---------- Phần giữa ----------
        JPanel mid = new JPanel(new GridBagLayout());
        mid.setBackground(BG_RED);
        mid.setBorder(new EmptyBorder(30, 40, 30, 40));
        root.add(mid, BorderLayout.CENTER);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridy = 0;
        gbc.insets = new Insets(0, 40, 0, 40);

       // Người chơi 1
        gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.WEST;
        JComponent player1Card = scoreCard("Player 1's", "SCORE", ORANGE, TEXT_BLACK, WHITE, true);
        mid.add(player1Card, gbc);

        // Người chơi 2
        gbc.gridx = 2;
        gbc.anchor = GridBagConstraints.EAST;
        JComponent player2Card = scoreCard("Player 2's", "SCORE", ORANGE, TEXT_BLACK, WHITE, false);
        mid.add(player2Card, gbc);
        
        //Lượt chơi
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.CENTER;
        
        JPanel boardContainer = new JPanel(new BorderLayout());
        boardContainer.setOpaque(false);
        
        statusLabel = new JLabel(">>> Player X's Turn <<<");
        statusLabel.setForeground(ACCENT_GOLD);
        statusLabel.setFont(new Font("Arial", Font.BOLD, 24));
        statusLabel.setHorizontalAlignment(SwingConstants.CENTER);
        boardContainer.add(statusLabel, BorderLayout.NORTH);
        
        JComponent boardComponent = createBoardPanel(BOARD_BEIGE, TILE_BROWN, WHITE);
        boardContainer.add(boardComponent, BorderLayout.CENTER);
        
        mid.add(boardContainer, gbc);

       
        logic.initMenuGame(boardButtons, statusLabel, player1ScoreLabel, player2ScoreLabel);

        // ----------Nút Dưới  ----------
        JPanel bottom = new JPanel(new GridBagLayout());
        bottom.setBackground(BG_RED);
        bottom.setBorder(new EmptyBorder(20, 0, 30, 0));

        GridBagConstraints bottomGbc = new GridBagConstraints();
        bottomGbc.insets = new Insets(0, 20, 0, 20);
        bottomGbc.gridy = 0;

        JButton newGameBtn = actionButton("New Game", ORANGE, WHITE);
        newGameBtn.addActionListener(e -> logic.resetMenuGame());
        bottomGbc.gridx = 0;
        bottom.add(wrapButton(newGameBtn, ORANGE), bottomGbc);

        JButton undoBtn = actionButton("Undo", ORANGE, WHITE);
        undoBtn.addActionListener(e -> logic.handleUndoClick());
        bottomGbc.gridx = 1;
        bottom.add(wrapButton(undoBtn, ORANGE), bottomGbc);

        JButton hintBtn = actionButton("Hint", ORANGE, WHITE);
        hintBtn.addActionListener(e -> logic.handleHintClick());
        bottomGbc.gridx = 2;
        bottom.add(wrapButton(hintBtn, ORANGE), bottomGbc);

        root.add(bottom, BorderLayout.SOUTH);
    }

    private static JButton navBtn(String text, Color fg) {
        JButton b = new JButton(text);
        b.setForeground(fg);
        b.setFont(new Font("Arial", Font.BOLD, 18));
        b.setOpaque(false);
        b.setContentAreaFilled(false);
        b.setBorderPainted(false);
        b.setFocusPainted(false);
        b.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Hiệu ứng hover chuột
        b.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                b.setFont(new Font("Arial", Font.BOLD, 20));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                b.setFont(new Font("Arial", Font.BOLD, 18));
            }
        });
        
        return b;
    }

    private static JButton actionButton(String text, Color bg, Color fg) {
        JButton b = new JButton(text);
        b.setForeground(fg);
        b.setFont(new Font("Arial", Font.BOLD, 18));
        b.setFocusPainted(false);
        b.setBorderPainted(false);
        b.setContentAreaFilled(false);
        b.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return b;
    }

    private static JComponent wrapButton(JButton btn, Color bgColor) {
        RoundedButton wrap = new RoundedButton(18, bgColor);
        wrap.setBorder(new EmptyBorder(12, 35, 12, 35));
        wrap.setLayout(new BorderLayout());
        wrap.add(btn, BorderLayout.CENTER);
        wrap.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return wrap;
    }

    private JComponent scoreCard(
            String player, String scoreWord,
            Color cardColor, Color titleColor, Color scoreColor, boolean isPlayer1
    ) {
        RoundedPanel card = new RoundedPanel(20);
        card.setBackground(cardColor);
        card.setPreferredSize(new Dimension(240, 320));
        card.setLayout(new BorderLayout());
        card.setBorder(new EmptyBorder(25, 20, 25, 20));

        // Title with player symbol
        String symbol = isPlayer1 ? "X" : "O";
        JLabel title = new JLabel(
                "<html><div style='text-align:center;'>[" + symbol + "]<br/>" + player + "<br/>" + scoreWord + "</div></html>",
                SwingConstants.CENTER
        );
        title.setForeground(titleColor);
        title.setFont(new Font("Arial", Font.BOLD, 22));

        // Score display
        JLabel scoreLbl = new JLabel("0", SwingConstants.CENTER);
        scoreLbl.setForeground(scoreColor);
        scoreLbl.setFont(new Font("Arial", Font.BOLD, 90));

        // Store reference for score updates
        if(isPlayer1) {
            player1ScoreLabel = scoreLbl;
        } else {
            player2ScoreLabel = scoreLbl;
        }

        card.add(title, BorderLayout.NORTH);
        card.add(scoreLbl, BorderLayout.CENTER);

        JPanel wrap = new JPanel(new GridBagLayout());
        wrap.setOpaque(false);
        wrap.add(card);
        return wrap;
    }

    private JComponent createBoardPanel(Color boardBg, Color tileBg, Color markColor) {
        RoundedPanel boardWrap = new RoundedPanel(25);
        boardWrap.setBackground(boardBg);
        boardWrap.setBorder(new EmptyBorder(20, 20, 20, 20));
        boardWrap.setLayout(new GridLayout(3, 3, 15, 15));
        boardWrap.setPreferredSize(new Dimension(400, 400));

        Font markFont = new Font("Arial", Font.BOLD, 80);
        boardButtons = new JButton[9];

        for (int i = 0; i < 9; i++) {
            final JButton cell = new JButton("");
            cell.setFont(markFont);
            cell.setForeground(markColor);
            cell.setFocusPainted(false);
            cell.setBorderPainted(false);
            cell.setContentAreaFilled(false);
            cell.setOpaque(false);
            cell.setCursor(new Cursor(Cursor.HAND_CURSOR));
            
            boardButtons[i] = cell;
            int index = i;
            cell.addActionListener(e -> logic.handleBoardClick(boardButtons[index]));

            RoundedButton tile = new RoundedButton(15, tileBg);
            tile.setLayout(new BorderLayout());
            tile.add(cell, BorderLayout.CENTER);

            boardWrap.add(tile);
        }

        JPanel center = new JPanel(new GridBagLayout());
        center.setOpaque(false);
        center.add(boardWrap);
        return center;
    }

    // Rounded background panel
    static class RoundedPanel extends JPanel {
        private final int arc;
        RoundedPanel(int arc) { this.arc = arc; setOpaque(false); }

        @Override protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(getBackground());
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), arc, arc);
            g2.dispose();
            super.paintComponent(g);
        }
    }

    // Nút tròn 
    static class RoundedButton extends JPanel {
        private final int arc;
        private final Color fill;

        RoundedButton(int arc, Color fill) {
            this.arc = arc;
            this.fill = fill;
            setOpaque(false);
        }

        @Override protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            // Enhanced shadow
            g2.setColor(new Color(0, 0, 0, 80));
            g2.fillRoundRect(4, 4, getWidth() - 8, getHeight() - 8, arc, arc);
            
            // Button fill
            g2.setColor(fill);
            g2.fillRoundRect(0, 0, getWidth() - 4, getHeight() - 4, arc, arc);
            
            // Highlight for 3D effect
            g2.setColor(new Color(255, 255, 255, 30));
            g2.fillRoundRect(1, 1, getWidth() - 6, (getHeight() - 4) / 2, arc, arc);

            g2.dispose();
            super.paintComponent(g);
        }
    }
}