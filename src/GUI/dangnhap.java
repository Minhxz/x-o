package GUI;

import Fancy.Theme;
import Logic.Database;
import Logic.logic;
import Music.gameMusic;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class dangnhap extends JFrame {

    public dangnhap() {
        Theme theme = logic.getTheme();
        Color BG = theme.background;
        Color PRIMARY = theme.primary;
        Color PRIMARY_DARK = theme.primaryDark;
        Color TEXT = theme.text;
        Color ACCENT = theme.accent;

        setTitle("Player Login");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);

        GradientPanel root = new GradientPanel(BG, PRIMARY_DARK.darker());
        root.setLayout(new BorderLayout());
        root.setBorder(new EmptyBorder(40, 30, 30, 30));
        setContentPane(root);

        JPanel header = new JPanel();
        header.setOpaque(false);
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));
        JLabel title = new JLabel("WELCOME BACK");
        title.setFont(new Font("SansSerif", Font.BOLD, 40));
        title.setForeground(ACCENT);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        JLabel subtitle = new JLabel("Sign in to continue your game");
        subtitle.setFont(new Font("SansSerif", Font.PLAIN, 18));
        subtitle.setForeground(TEXT);
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        header.add(title);
        header.add(Box.createVerticalStrut(10));
        header.add(subtitle);
        root.add(header, BorderLayout.NORTH);

        JPanel content = new JPanel(new GridBagLayout());
        content.setOpaque(false);
        GridBagConstraints gbcRoot = new GridBagConstraints();
        gbcRoot.gridx = 0;
        gbcRoot.gridy = 0;
        gbcRoot.insets = new Insets(15, 0, 0, 0);
        gbcRoot.fill = GridBagConstraints.NONE;
        gbcRoot.weightx = 1;
        gbcRoot.weighty = 1;

        RoundedPanel formCard = new RoundedPanel(28);
        formCard.setBackground(new Color(0, 0, 0, 150));
        formCard.setBorder(new EmptyBorder(40, 50, 40, 50));
        formCard.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;

        JLabel userLabel = label("Tên người dùng:", TEXT);
        JTextField userField = field("Nhập tên người dùng...");
        JLabel emailLabel = label("Email:", TEXT);
        JTextField emailField = field("Nhập địa chỉ email...");
        JLabel passLabel = label("Mật khẩu:", TEXT);
        JPasswordField passField = passwordField("Nhập mật khẩu...");

        userField.setPreferredSize(new Dimension(300, 45));
        emailField.setPreferredSize(new Dimension(300, 45));
        passField.setPreferredSize(new Dimension(300, 45));

        gbc.insets = new Insets(0, 0, 5, 0);
        formCard.add(userLabel, gbc);
        gbc.gridy++;
        gbc.insets = new Insets(0, 0, 15, 0);
        formCard.add(userField, gbc);

        gbc.gridy++;
        gbc.insets = new Insets(0, 0, 5, 0);
        formCard.add(emailLabel, gbc);
        gbc.gridy++;
        gbc.insets = new Insets(0, 0, 15, 0);
        formCard.add(emailField, gbc);

        gbc.gridy++;
        gbc.insets = new Insets(0, 0, 5, 0);
        formCard.add(passLabel, gbc);
        gbc.gridy++;
        gbc.insets = new Insets(0, 0, 30, 0);
        formCard.add(passField, gbc);

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        actions.setOpaque(false);

        JComponent loginBtn = actionButton("LOGIN", PRIMARY, TEXT, ACCENT, () -> {
            gameMusic.playMenuClick(); 
            
            String userInput = userField.getText().trim();
            String emailInput = emailField.getText().trim();
            String passwordInput = new String(passField.getPassword());
            
            if (userInput.isEmpty() || emailInput.isEmpty() || passwordInput.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ!", "Thông báo", JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (!emailInput.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
                JOptionPane.showMessageDialog(this, "Email không hợp lệ!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String sql = "SELECT * FROM accounts WHERE email = ? AND password = ?";

            try (Connection conn = Database.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {
                
                pstmt.setString(1, emailInput);
                pstmt.setString(2, passwordInput);
                ResultSet rs = pstmt.executeQuery();

                if (rs.next()) {
                    String dbUsername = rs.getString("username");
                    
                    if (dbUsername.equals(userInput)) {
                        // --- CRITICAL UPDATE: PASS EMAIL TO LOGIC ---
                        logic.setCurrentAccountEmail(emailInput); 
                        logic.setPlayerNames(dbUsername, "Player 2"); 
                        
                        logic.setPlayerSymbols("X", "O"); 
                        MainMenu menu = new MainMenu();
                        menu.setVisible(true);
                        dispose();
                    } else {
                        JOptionPane.showMessageDialog(this, "Tên người dùng không khớp!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "Email hoặc Mật khẩu không chính xác!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                }

            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Lỗi kết nối: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        });
        
        JComponent signupBtn = outlineButton("Sign Up", PRIMARY_DARK, TEXT, ACCENT, () -> {
            gameMusic.playMenuClick();
            Signup signup = new Signup();
            signup.setVisible(true);
            dispose();
        });

        actions.add(loginBtn);
        actions.add(signupBtn);

        gbc.gridy++;
        gbc.insets = new Insets(0, 0, 0, 0);
        formCard.add(actions, gbc);

        JPanel formWrap = new JPanel(new BorderLayout());
        formWrap.setOpaque(false);
        formWrap.add(formCard, BorderLayout.CENTER);

        content.add(formWrap, gbcRoot);
        root.add(content, BorderLayout.CENTER);

        setVisible(true);
    }

    // Helper methods (label, field, passwordField, actionButton, outlineButton) 
    // and inner classes (GradientPanel, RoundedPanel, RoundedButton) 
    // remain exactly the same as your provided code...
    
    private static JLabel label(String text, Color fg) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("SansSerif", Font.PLAIN, 16)); 
        label.setForeground(fg);
        return label;
    }

    private static JTextField field(String placeholder) {
        JTextField field = new JTextField() {
            @Override protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (getText().isEmpty() && !isFocusOwner()) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setColor(new Color(130, 130, 130));
                    g2.setFont(getFont().deriveFont(Font.ITALIC));
                    int y = (getHeight() - g.getFontMetrics().getHeight()) / 2 + g.getFontMetrics().getAscent();
                    g2.drawString(placeholder, getInsets().left, y);
                    g2.dispose();
                }
            }
        };
        field.setFont(new Font("SansSerif", Font.PLAIN, 16));
        field.setForeground(new Color(30, 30, 30));
        field.setCaretColor(new Color(30, 30, 30));
        field.setBackground(new Color(255, 255, 240)); 
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(255, 255, 255, 120), 1),
                new EmptyBorder(10, 12, 10, 12))); 
        field.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) { field.repaint(); }
            public void focusLost(java.awt.event.FocusEvent evt) { field.repaint(); }
        });
        return field;
    }

    private static JPasswordField passwordField(String placeholder) {
        JPasswordField field = new JPasswordField() {
            @Override protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (new String(getPassword()).isEmpty() && !isFocusOwner()) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setColor(new Color(130, 130, 130));
                    g2.setFont(getFont().deriveFont(Font.ITALIC));
                    int y = (getHeight() - g.getFontMetrics().getHeight()) / 2 + g.getFontMetrics().getAscent();
                    g2.drawString(placeholder, getInsets().left, y);
                    g2.dispose();
                }
            }
        };
        field.setFont(new Font("SansSerif", Font.PLAIN, 16));
        field.setForeground(new Color(30, 30, 30));
        field.setCaretColor(new Color(30, 30, 30));
        field.setBackground(new Color(255, 255, 240)); 
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(255, 255, 255, 120), 1),
                new EmptyBorder(10, 12, 10, 12))); 
        field.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) { field.repaint(); }
            public void focusLost(java.awt.event.FocusEvent evt) { field.repaint(); }
        });
        return field;
    }

    private static JComponent actionButton(String text, Color base, Color fg, Color glow, Runnable action) {
        JButton btn = new JButton(text);
        btn.setForeground(fg);
        btn.setFont(new Font("SansSerif", Font.BOLD, 16));
        btn.setOpaque(false);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        RoundedButton wrap = new RoundedButton(16, base, glow);
        wrap.setBorder(new EmptyBorder(10, 25, 10, 25));
        wrap.setLayout(new BorderLayout());
        wrap.add(btn, BorderLayout.CENTER);

        btn.addActionListener(e -> action.run());
        btn.addMouseListener(new MouseAdapter() {
            @Override public void mouseEntered(MouseEvent e) { wrap.setHover(true); }
            @Override public void mouseExited(MouseEvent e) { wrap.setHover(false); }
        });
        return wrap;
    }

    private static JComponent outlineButton(String text, Color base, Color fg, Color glow, Runnable action) {
        JButton btn = new JButton(text);
        btn.setForeground(fg);
        btn.setFont(new Font("SansSerif", Font.BOLD, 16));
        btn.setOpaque(false);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        RoundedButton wrap = new RoundedButton(16, new Color(0, 0, 0, 90), glow);
        wrap.setBorder(new EmptyBorder(10, 25, 10, 25));
        wrap.setLayout(new BorderLayout());
        wrap.add(btn, BorderLayout.CENTER);

        btn.addActionListener(e -> action.run());
        btn.addMouseListener(new MouseAdapter() {
            @Override public void mouseEntered(MouseEvent e) { wrap.setHover(true); }
            @Override public void mouseExited(MouseEvent e) { wrap.setHover(false); }
        });
        return wrap;
    }

    static class GradientPanel extends JPanel {
        private final Color top; private final Color bottom;
        GradientPanel(Color top, Color bottom) { this.top = top; this.bottom = bottom; setOpaque(false); }
        @Override protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setPaint(new GradientPaint(0, 0, top, 0, getHeight(), bottom));
            g2.fillRect(0, 0, getWidth(), getHeight());
            g2.dispose(); super.paintComponent(g);
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
            g2.setColor(new Color(0, 0, 0, 90));
            g2.fillRoundRect(4, 5, getWidth() - 8, getHeight() - 8, arc, arc);
            Color fill = hover ? glow : base;
            g2.setColor(fill);
            g2.fillRoundRect(0, 0, getWidth() - 4, getHeight() - 4, arc, arc);
            g2.setColor(new Color(255, 255, 255, hover ? 70 : 40));
            g2.drawRoundRect(1, 1, getWidth() - 6, getHeight() - 6, arc, arc);
            g2.dispose(); super.paintComponent(g);
        }
    }
}