package GUI;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class MENU extends JFrame {

    public MENU() {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(1980, 1080);
        setLocationRelativeTo(null);

        //Màu sắc
        Color BG_RED = new Color(0xB02222);
        Color ORANGE = new Color(0xE26A12);
        Color ORANGE_DARK = new Color(0xC85A0E);
        Color BOARD_BEIGE = new Color(0xD8B59C);
        Color TILE_BROWN = new Color(0x8B3B06);
        Color WHITE = Color.WHITE;
        Color TEXT_BLACK = new Color(0x111111);
        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(BG_RED);
        setContentPane(root);



        // ---------- Nav Trên  ----------
        JPanel top = new JPanel(new GridBagLayout());
        top.setOpaque(true);
        top.setBackground(BG_RED);
        top.setBorder(new EmptyBorder(25, 20, 10, 20));

        RoundedPanel nav = new RoundedPanel(30);
        nav.setBackground(ORANGE_DARK);
        nav.setLayout(new GridLayout(1, 3, 30, 0));
        nav.setBorder(new EmptyBorder(20, 60, 20, 60));
        nav.add(navBtn("Setting", WHITE));
        nav.add(navBtn("Login", WHITE));
        nav.add(navBtn("About Us", WHITE));

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

        //Điểm của ng chơi 1
        gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.WEST;
        mid.add(scoreCard("Player 1", "SCORE", "0", ORANGE, TEXT_BLACK, WHITE), gbc);
        
        //Bàn cờ
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.CENTER;
        mid.add(boardPanel(BOARD_BEIGE, TILE_BROWN, WHITE), gbc);

        //Điểm của ng chơi 2
        gbc.gridx = 2;
        gbc.anchor = GridBagConstraints.EAST;
        mid.add(scoreCard("Player 2", "SCORE", "0", ORANGE, TEXT_BLACK, WHITE), gbc);

        // ----------Nút Dưới  ----------
        JPanel bottom = new JPanel(new GridBagLayout());
        bottom.setBackground(BG_RED);
        bottom.setBorder(new EmptyBorder(0, 0, 40, 0));

        JButton newGame = new JButton("New Game");
        newGame.setForeground(WHITE);
        newGame.setFont(new Font("SansSerif", Font.BOLD, 26));
        newGame.setFocusPainted(false);
        newGame.setBorderPainted(false);
        newGame.setContentAreaFilled(false);

        RoundedButton newGameWrap = new RoundedButton(28, ORANGE);
        newGameWrap.setBorder(new EmptyBorder(14, 44, 14, 44));
        newGameWrap.setLayout(new BorderLayout());
        newGameWrap.add(newGame, BorderLayout.CENTER);

        bottom.add(newGameWrap);
        root.add(bottom, BorderLayout.SOUTH);
    }

    private static JButton navBtn(String text, Color fg) {
        JButton b = new JButton(text);
        b.setForeground(fg);
        b.setFont(new Font("SansSerif", Font.BOLD, 28));
        b.setOpaque(false);
        b.setContentAreaFilled(false);
        b.setBorderPainted(false);
        b.setFocusPainted(false);
        return b;
    }

    private static JComponent scoreCard(
            String player, String scoreWord, String score,
            Color cardColor, Color titleColor, Color scoreColor
    ) {
        RoundedPanel card = new RoundedPanel(30);
        card.setBackground(cardColor);
        card.setPreferredSize(new Dimension(220, 300));
        card.setLayout(new BorderLayout());
        card.setBorder(new EmptyBorder(30, 20, 30, 20));

        JLabel title = new JLabel(
                "<html><div style='text-align:center;'>" + player + "<br/>" + scoreWord + "</div></html>",
                SwingConstants.CENTER
        );
        title.setForeground(titleColor);
        title.setFont(new Font("SansSerif", Font.BOLD, 26));

        JLabel scoreLbl = new JLabel(score, SwingConstants.CENTER);
        scoreLbl.setForeground(scoreColor);
        scoreLbl.setFont(new Font("SansSerif", Font.BOLD, 86));

        card.add(title, BorderLayout.NORTH);
        card.add(scoreLbl, BorderLayout.CENTER);

        JPanel wrap = new JPanel(new GridBagLayout());
        wrap.setOpaque(false);
        wrap.add(card);
        return wrap;
    }

    private static JComponent boardPanel(Color boardBg, Color tileBg, Color markColor) {
        RoundedPanel boardWrap = new RoundedPanel(35);
        boardWrap.setBackground(boardBg);
        boardWrap.setBorder(new EmptyBorder(22, 22, 22, 22));
        boardWrap.setLayout(new GridLayout(3, 3, 18, 18));

        Font markFont = new Font("SansSerif", Font.BOLD, 72);

        String[] marks = {
                "X", "O", "O",
                "X", "O", "O",
                "X", "X", "X"
        };

        for (int i = 0; i < 9; i++) {
            JButton cell = new JButton(marks[i]);
            cell.setFont(markFont);
            cell.setForeground(markColor);
            cell.setFocusPainted(false);
            cell.setBorderPainted(false);
            cell.setContentAreaFilled(false);
            cell.setOpaque(false);
            cell.setEnabled(false); // static

            RoundedButton tile = new RoundedButton(28, tileBg);
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

            g2.setColor(new Color(0, 0, 0, 55));
            g2.fillRoundRect(6, 6, getWidth() - 6, getHeight() - 6, arc, arc);

            g2.setColor(fill);
            g2.fillRoundRect(0, 0, getWidth() - 6, getHeight() - 6, arc, arc);

            g2.dispose();
            super.paintComponent(g);
        }
    }
}