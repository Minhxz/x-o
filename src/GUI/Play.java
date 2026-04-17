package GUI;

import Fancy.Theme;
import Logic.logic;
import Logic.MenuActions;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

// Màn hình chính của trò chơi (Bảng cờ caro)
public class Play extends JFrame {

    private JLabel statusLabel; // Hiển thị "Đến lượt ai"
    private JLabel timerLabel; // Hiển thị thời gian đếm ngược
    private JLabel player1ScoreLabel; // Thẻ điểm của P1
    private JLabel player2ScoreLabel; // Thẻ điểm của P2
    private JButton[] boardButtons; // Danh sách các nút làm ô cờ (sẽ truyền cho lớp Logic xử lý)

    public Play() {
        Theme theme = logic.getTheme();

        Color BG = theme.background;
        Color PRIMARY = theme.primary;
        Color PRIMARY_DARK = theme.primaryDark;
        Color BOARD_BG = theme.board;
        Color TILE_BG = theme.tile;
        Color TEXT = theme.text;
        Color TEXT_DARK = theme.textDark;
        Color ACCENT = theme.accent;

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(1280, 720);
        setExtendedState(JFrame.MAXIMIZED_BOTH); 
        setLocationRelativeTo(null);
        setTitle("Tic Tac Toe - X O Game");

        GradientPanel root = new GradientPanel(BG, PRIMARY_DARK.darker());
        root.setLayout(new BorderLayout());
        root.setBorder(new EmptyBorder(20, 30, 25, 30));
        setContentPane(root);

        // HEADER: Thanh trên cùng (Tên Game + Music, History, Exit)
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);
        headerPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        JLabel titleLabel = new JLabel("TIC TAC TOE");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 46));
        titleLabel.setForeground(ACCENT);
        headerPanel.add(titleLabel, BorderLayout.WEST);

        // Navbar chia 3 cột 
        JPanel navBar = new JPanel(new GridLayout(1, 3, 12, 0));
        navBar.setOpaque(false);

        JComponent musicBtn = navButton("🎵 Music", TEXT, ACCENT, () -> MenuActions.showMusicDialog(this));
        JComponent historyBtn = navButton("🕒 History", TEXT, ACCENT, () -> logic.handleHistoryClick());
        JComponent exitBtn = navButton("✖ Exit", TEXT, ACCENT, () -> { logic.stopTimer(); logic.handleExitClick(); });

        navBar.add(musicBtn);
        navBar.add(historyBtn);
        navBar.add(exitBtn);

        headerPanel.add(navBar, BorderLayout.EAST);
        root.add(headerPanel, BorderLayout.NORTH);

        // CENTER: Phần khung chứa Bảng Cờ và Thẻ Điểm
        JPanel mid = new JPanel(new GridBagLayout());
        mid.setOpaque(false);
        mid.setBorder(new EmptyBorder(20, 30, 15, 30));
        root.add(mid, BorderLayout.CENTER);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridy = 0;
        gbc.insets = new Insets(0, 30, 0, 30);

        // Thẻ điểm Người chơi 1 (Bên Trái)
        gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.WEST;
        JComponent player1Card = scoreCard(logic.getP1Name(), "SCORE", PRIMARY, TEXT_DARK, TEXT, true, ACCENT);
        mid.add(player1Card, gbc);

        // Thẻ điểm Người chơi 2 (Bên Phải)
        gbc.gridx = 2;
        gbc.anchor = GridBagConstraints.EAST;
        JComponent player2Card = scoreCard(logic.getP2Name(), "SCORE", PRIMARY, TEXT_DARK, TEXT, false, ACCENT);
        mid.add(player2Card, gbc);

        // Khung Bảng cờ (Ở Giữa)
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.CENTER;

        JPanel boardContainer = new JPanel(new BorderLayout());
        boardContainer.setOpaque(false);

        // Tiêu đề của khung cờ (Trạng thái lượt + Đếm ngược)
        JPanel boardHeader = new JPanel(new GridLayout(2, 1));
        boardHeader.setOpaque(false);
        boardHeader.setBorder(new EmptyBorder(0, 0, 10, 0));

        statusLabel = new JLabel(">>> " + logic.getP1Name() + "'s Turn <<<", SwingConstants.CENTER);
        statusLabel.setForeground(ACCENT);
        statusLabel.setFont(new Font("SansSerif", Font.BOLD, 26));
        boardHeader.add(statusLabel);

        timerLabel = new JLabel("Timer: Off", SwingConstants.CENTER);
        timerLabel.setForeground(TEXT);
        timerLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
        boardHeader.add(timerLabel);

        boardContainer.add(boardHeader, BorderLayout.NORTH);

        // Tạo bàn cờ (GridLayout) dựa theo setting của Logic
        int boardSize = logic.getBoardSize();
        JComponent boardComponent = createBoardPanel(boardSize, BOARD_BG, TILE_BG, TEXT, ACCENT);
        boardContainer.add(boardComponent, BorderLayout.CENTER);

        mid.add(boardContainer, gbc);

        // SAU KHI VẼ XONG UI: Truyền tất cả biến tham chiếu qua cho Lớp logic xử lý
        logic.initMenuGame(boardButtons, statusLabel, player1ScoreLabel, player2ScoreLabel, timerLabel);

        // FOOTER: 2 nút New Game & Về Menu
        JPanel bottom = new JPanel(new GridBagLayout());
        bottom.setOpaque(false);
        bottom.setBorder(new EmptyBorder(10, 0, 5, 0));

        GridBagConstraints bottomGbc = new GridBagConstraints();
        bottomGbc.insets = new Insets(0, 15, 0, 15);
        bottomGbc.gridy = 0;

        JComponent newGameBtn = actionButton("New Game", PRIMARY, TEXT, ACCENT, () -> logic.resetMenuGame());
        bottomGbc.gridx = 0;
        bottom.add(newGameBtn, bottomGbc);

        JComponent backBtn = actionButton("Back to Menu", PRIMARY, TEXT, ACCENT, () -> {
            logic.stopTimer(); // Ngắt giờ trước khi về Menu
            dispose();
            MainMenu menu = new MainMenu();
            menu.setVisible(true);
        });
        bottomGbc.gridx = 1;
        bottom.add(backBtn, bottomGbc);

        root.add(bottom, BorderLayout.SOUTH);

        revalidate();
        repaint();
    }

    // Các thành phần UI Con: Nút trên thanh điều hướng
    private static JComponent navButton(String text, Color fg, Color glow, Runnable action) {
        JButton btn = baseButton(text, fg, 16);
        RoundedButton wrap = new RoundedButton(16, new Color(0, 0, 0, 140), glow);
        wrap.setBorder(new EmptyBorder(10, 16, 10, 16));
        wrap.setLayout(new BorderLayout());
        wrap.add(btn, BorderLayout.CENTER);

        btn.addActionListener(e -> { Music.gameMusic.playMenuClick(); action.run(); });
        btn.addMouseListener(new HoverEffect(wrap));
        return wrap;
    }

    // Nút chức năng to (Footer)
    private static JComponent actionButton(String text, Color bg, Color fg, Color glow, Runnable action) {
        JButton btn = baseButton(text, fg, 18);
        RoundedButton wrap = new RoundedButton(18, bg, glow);
        wrap.setBorder(new EmptyBorder(12, 35, 12, 35));
        wrap.setLayout(new BorderLayout());
        wrap.add(btn, BorderLayout.CENTER);

        btn.addActionListener(e -> { Music.gameMusic.playMenuClick(); action.run(); });
        btn.addMouseListener(new HoverEffect(wrap));
        return wrap;
    }

    // Khởi tạo phôi nút bấm Swing trơn trong suốt
    private static JButton baseButton(String text, Color fg, int fontSize) {
        JButton b = new JButton(text);
        b.setForeground(fg);
        b.setFont(new Font("SansSerif", Font.BOLD, fontSize));
        b.setOpaque(false);
        b.setContentAreaFilled(false);
        b.setBorderPainted(false);
        b.setFocusPainted(false);
        b.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return b;
    }

    // Thiết kế Thẻ hiển thị Điểm của người chơi
    private JComponent scoreCard(String player, String scoreWord, Color cardColor, Color titleColor, Color scoreColor, boolean isPlayer1, Color glow) {
        RoundedPanel card = new RoundedPanel(22);
        card.setBackground(cardColor);
        card.setPreferredSize(new Dimension(260, 320));
        card.setLayout(new BorderLayout());
        card.setBorder(new EmptyBorder(22, 18, 22, 18));

        String symbol = isPlayer1 ? logic.getPlayerX() : logic.getPlayerO();
        JLabel title = new JLabel("<html><div style='text-align:center;'><span style='font-size:18px;'>[" + symbol + "]</span><br/>" + player + "<br/>" + scoreWord + "</div>", SwingConstants.CENTER);
        title.setForeground(titleColor);
        title.setFont(new Font("SansSerif", Font.BOLD, 22));

        JLabel scoreLbl = new JLabel("0", SwingConstants.CENTER);
        scoreLbl.setForeground(scoreColor);
        scoreLbl.setFont(new Font("SansSerif", Font.BOLD, 90));

        // Lưu thẻ JLabel vào biến Global để Lớp logic có thể lấy thẻ và cộng điểm
        if(isPlayer1) player1ScoreLabel = scoreLbl; else player2ScoreLabel = scoreLbl;

        card.add(title, BorderLayout.NORTH);
        card.add(scoreLbl, BorderLayout.CENTER);

        GlowPanel wrap = new GlowPanel(24, glow);
        wrap.setLayout(new GridBagLayout());
        wrap.add(card);
        return wrap;
    }

    // Khởi tạo và vẽ mạng lưới Bàn cờ (Các ô cờ)
    private JComponent createBoardPanel(int size, Color boardBg, Color tileBg, Color markColor, Color glow) {
        // Nếu chơi 3x3 thì khoảng cách (gap) to ra, 5x5 thì gap bé lại
        int gap = (size == 3) ? 14 : 10;
        RoundedPanel boardWrap = new RoundedPanel(26);
        boardWrap.setBackground(boardBg);
        boardWrap.setBorder(new EmptyBorder(22, 22, 22, 22));
        boardWrap.setLayout(new GridLayout(size, size, gap, gap)); // Chia lưới GridLayout

        int boardPx = 440;
        Dimension fixedSize = new Dimension(boardPx, boardPx);
        boardWrap.setPreferredSize(fixedSize);
        boardWrap.setMinimumSize(fixedSize);
        boardWrap.setMaximumSize(fixedSize);

        // Ký tự trong cờ của bàn 5x5 cần nhỏ gọn hơn
        int fontSize = (size == 3) ? 68 : 36;
        Font markFont = new Font("SansSerif", Font.BOLD, fontSize);
        boardButtons = new JButton[size * size];

        // Duyệt tạo tất cả các Nút (ô cờ)
        for (int i = 0; i < size * size; i++) {
            final JButton cell = new JButton("");
            cell.setFont(markFont);
            cell.setForeground(markColor);
            cell.setFocusPainted(false);
            cell.setBorderPainted(false);
            cell.setContentAreaFilled(false);
            cell.setOpaque(false);
            cell.setCursor(new Cursor(Cursor.HAND_CURSOR));

            boardButtons[i] = cell; // Lưu vào mảng
            int index = i;
            // BẮT SỰ KIỆN: Nếu nhấn vào ô nào thì văng qua hàm logic xử lý
            cell.addActionListener(e -> logic.handleBoardClick(boardButtons[index]));

            // Đóng khung thẩm mỹ
            RoundedButton tile = new RoundedButton(16, tileBg, glow);
            if (size > 3) tile.setBorder(BorderFactory.createLineBorder(new Color(255, 255, 255, 80), 1));
            tile.setLayout(new BorderLayout());
            tile.add(cell, BorderLayout.CENTER);
            cell.addMouseListener(new HoverEffect(tile));
            boardWrap.add(tile);
        }

        JPanel center = new JPanel(new GridBagLayout());
        center.setOpaque(false);
        center.add(boardWrap);
        return center;
    }

    // Các class con UI Vẽ giao diện (Tương tự MainMenu)
    static class GradientPanel extends JPanel {
        private final Color top; private final Color bottom;
        GradientPanel(Color top, Color bottom) { this.top = top; this.bottom = bottom; setOpaque(false); }
        @Override protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setPaint(new GradientPaint(0, 0, top, 0, getHeight(), bottom));
            g2.fillRect(0, 0, getWidth(), getHeight()); g2.dispose(); super.paintComponent(g);
        }
    }
    static class RoundedPanel extends JPanel {
        private final int arc; RoundedPanel(int arc) { this.arc = arc; setOpaque(false); }
        @Override protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(getBackground()); g2.fillRoundRect(0, 0, getWidth(), getHeight(), arc, arc);
            g2.dispose(); super.paintComponent(g);
        }
    }
    // Lớp đặc biệt vẽ thêm bóng đổ (Drop Shadow) ra bên ngoài khung viền
    static class GlowPanel extends JPanel {
        private final int arc; private final Color glow;
        GlowPanel(int arc, Color glow) { this.arc = arc; this.glow = glow; setOpaque(false); }
        @Override protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(new Color(glow.getRed(), glow.getGreen(), glow.getBlue(), 60)); // Màu phát sáng nhạt
            g2.fillRoundRect(2, 4, getWidth() - 4, getHeight() - 4, arc, arc);
            g2.dispose(); super.paintComponent(g);
        }
    }
    static class RoundedButton extends JPanel {
        private final int arc; private final Color base; private final Color glow; private boolean hover;
        RoundedButton(int arc, Color base, Color glow) { this.arc = arc; this.base = base; this.glow = glow; setOpaque(false); }
        void setHover(boolean hover) { this.hover = hover; repaint(); }
        @Override protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(new Color(0, 0, 0, 90)); g2.fillRoundRect(4, 5, getWidth() - 8, getHeight() - 8, arc, arc);
            Color fill = hover ? glow : base; g2.setColor(fill); g2.fillRoundRect(0, 0, getWidth() - 4, getHeight() - 4, arc, arc);
            g2.setColor(new Color(255, 255, 255, hover ? 70 : 40)); g2.drawRoundRect(1, 1, getWidth() - 6, getHeight() - 6, arc, arc);
            g2.dispose(); super.paintComponent(g);
        }
    }
    // Cảm biến chuột: Bắt sự kiện rọi chuột qua để nút bấm đổi màu phát sáng
    static class HoverEffect extends MouseAdapter {
        private final RoundedButton target; HoverEffect(RoundedButton target) { this.target = target; }
        @Override public void mouseEntered(MouseEvent e) { target.setHover(true); }
        @Override public void mouseExited(MouseEvent e) { target.setHover(false); }
    }
}