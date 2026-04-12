package GUI;

import Fancy.Theme;
import Logic.MenuActions;
import Logic.logic;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MainMenu extends JFrame {

    public MainMenu() {
        Theme theme = logic.getTheme();

        Color BG = theme.background;
        Color PRIMARY = theme.primary;
        Color PRIMARY_DARK = theme.primaryDark;
        Color TEXT = theme.text;
        Color ACCENT = theme.accent;

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(1280, 720);
        setLocationRelativeTo(null);
        setTitle("Tic Tac Toe - Main Menu");

        GradientPanel root = new GradientPanel(BG, PRIMARY_DARK.darker());
        root.setLayout(new BorderLayout());
        root.setBorder(new EmptyBorder(20, 30, 30, 30));
        setContentPane(root);

        JPanel headerPanel = new JPanel(new GridBagLayout());
        headerPanel.setOpaque(false);
        headerPanel.setBorder(new EmptyBorder(20, 10, 10, 10));

        JLabel titleLabel = new JLabel("TIC TAC TOE");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 60));
        titleLabel.setForeground(ACCENT);

        JLabel subTitle = new JLabel("Choose your challenge and play!");
        subTitle.setFont(new Font("SansSerif", Font.PLAIN, 20));
        subTitle.setForeground(TEXT);

        Box titleBox = Box.createVerticalBox();
        titleBox.add(titleLabel);
        titleBox.add(Box.createVerticalStrut(6));
        titleBox.add(subTitle);
        headerPanel.add(titleBox);
        root.add(headerPanel, BorderLayout.NORTH);

        JPanel center = new JPanel(new GridBagLayout());
        center.setOpaque(false);
        center.setBorder(new EmptyBorder(20, 20, 40, 20));

        RoundedPanel menuCard = new RoundedPanel(28);
        menuCard.setBackground(new Color(0, 0, 0, 130));
        menuCard.setBorder(new EmptyBorder(30, 60, 30, 60));
        menuCard.setLayout(new GridLayout(4, 1, 0, 18));

        JComponent playBtn = gameButton("▶ Play", PRIMARY, TEXT, ACCENT, () -> MenuActions.openPlay(this));
        JComponent modeBtn = gameButton("⚙ Mode", PRIMARY, TEXT, ACCENT, () -> MenuActions.showModeDialog(this));
        JComponent colorBtn = gameButton("🎨 Color", PRIMARY, TEXT, ACCENT, () -> MenuActions.showThemeDialog(this));
        JComponent characterBtn = gameButton("★ Character", PRIMARY, TEXT, ACCENT, () -> MenuActions.showCharacterDialog(this));

        menuCard.add(playBtn);
        menuCard.add(modeBtn);
        menuCard.add(colorBtn);
        menuCard.add(characterBtn);

        center.add(menuCard);
        root.add(center, BorderLayout.CENTER);

        JPanel footer = new JPanel(new FlowLayout(FlowLayout.CENTER));
        footer.setOpaque(false);
        JLabel hint = new JLabel("Tip: Use the settings to switch board size and theme!");
        hint.setForeground(TEXT);
        hint.setFont(new Font("SansSerif", Font.PLAIN, 16));
        footer.add(hint);
        root.add(footer, BorderLayout.SOUTH);
    }

    private static JComponent gameButton(String text, Color bg, Color fg, Color glow, Runnable action) {
        JButton btn = new JButton(text);
        btn.setForeground(fg);
        btn.setFont(new Font("SansSerif", Font.BOLD, 24));
        btn.setOpaque(false);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        RoundedButton wrap = new RoundedButton(22, bg, glow);
        wrap.setBorder(new EmptyBorder(12, 20, 12, 20));
        wrap.setLayout(new BorderLayout());
        wrap.add(btn, BorderLayout.CENTER);
        wrap.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btn.addActionListener(e -> action.run());
        btn.addMouseListener(new MouseAdapter() {
            @Override public void mouseEntered(MouseEvent e) { wrap.setHover(true); }
            @Override public void mouseExited(MouseEvent e) { wrap.setHover(false); }
        });

        return wrap;
    }

    static class GradientPanel extends JPanel {
        private final Color top;
        private final Color bottom;

        GradientPanel(Color top, Color bottom) {
            this.top = top;
            this.bottom = bottom;
            setOpaque(false);
        }

        @Override protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setPaint(new GradientPaint(0, 0, top, 0, getHeight(), bottom));
            g2.fillRect(0, 0, getWidth(), getHeight());
            g2.dispose();
            super.paintComponent(g);
        }
    }

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

    static class RoundedButton extends JPanel {
        private final int arc;
        private final Color base;
        private final Color glow;
        private boolean hover;

        RoundedButton(int arc, Color base, Color glow) {
            this.arc = arc;
            this.base = base;
            this.glow = glow;
            setOpaque(false);
        }

        void setHover(boolean hover) {
            this.hover = hover;
            repaint();
        }

        @Override protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            g2.setColor(new Color(0, 0, 0, 90));
            g2.fillRoundRect(4, 5, getWidth() - 8, getHeight() - 8, arc, arc);

            Color fill = hover ? glow : base;
            g2.setColor(fill);
            g2.fillRoundRect(0, 0, getWidth() - 4, getHeight() - 4, arc, arc);

            g2.setColor(new Color(255, 255, 255, hover ? 60 : 30));
            g2.drawRoundRect(1, 1, getWidth() - 6, getHeight() - 6, arc, arc);

            g2.dispose();
            super.paintComponent(g);
        }
    }
}