package GUI;

import Fancy.Theme;
import Logic.logic;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Signup extends JFrame {

    public Signup() {
        Theme theme = logic.getTheme();
        Color BG = theme.background;
        Color PRIMARY = theme.primary;
        Color PRIMARY_DARK = theme.primaryDark;
        Color TEXT = theme.text;
        Color ACCENT = theme.accent;

        setTitle("Create Account");
        setSize(760, 560);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        GradientPanel root = new GradientPanel(BG, PRIMARY_DARK.darker());
        root.setLayout(new BorderLayout());
        root.setBorder(new EmptyBorder(24, 30, 30, 30));
        setContentPane(root);

        JPanel header = new JPanel();
        header.setOpaque(false);
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));
        JLabel title = new JLabel("CREATE ACCOUNT");
        title.setFont(new Font("SansSerif", Font.BOLD, 30));
        title.setForeground(ACCENT);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        JLabel subtitle = new JLabel("Join the Tic Tac Toe community");
        subtitle.setFont(new Font("SansSerif", Font.PLAIN, 15));
        subtitle.setForeground(TEXT);
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        header.add(title);
        header.add(Box.createVerticalStrut(6));
        header.add(subtitle);
        root.add(header, BorderLayout.NORTH);

        JPanel content = new JPanel(new GridBagLayout());
        content.setOpaque(false);
        GridBagConstraints gbcRoot = new GridBagConstraints();
        gbcRoot.gridx = 0;
        gbcRoot.gridy = 0;
        gbcRoot.insets = new Insets(10, 0, 0, 0);
        gbcRoot.fill = GridBagConstraints.BOTH;
        gbcRoot.weightx = 1;
        gbcRoot.weighty = 1;

        RoundedPanel leftInfo = new RoundedPanel(22);
        leftInfo.setBackground(new Color(0, 0, 0, 110));
        leftInfo.setBorder(new EmptyBorder(20, 20, 20, 20));
        leftInfo.setLayout(new BoxLayout(leftInfo, BoxLayout.Y_AXIS));

        JLabel infoTitle = new JLabel("Already have an account?");
        infoTitle.setFont(new Font("SansSerif", Font.BOLD, 18));
        infoTitle.setForeground(ACCENT);
        infoTitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel infoText = new JLabel("Sign in to continue playing.");
        infoText.setFont(new Font("SansSerif", Font.PLAIN, 13));
        infoText.setForeground(TEXT);
        infoText.setAlignmentX(Component.LEFT_ALIGNMENT);

        JComponent loginBtn = outlineButton("Back to Login", PRIMARY, TEXT, ACCENT, () -> {
            dangnhap login = new dangnhap();
            login.setVisible(true);
            dispose();
        });
        loginBtn.setAlignmentX(Component.LEFT_ALIGNMENT);

        leftInfo.add(infoTitle);
        leftInfo.add(Box.createVerticalStrut(6));
        leftInfo.add(infoText);
        leftInfo.add(Box.createVerticalStrut(14));
        leftInfo.add(loginBtn);
        leftInfo.add(Box.createVerticalGlue());

        RoundedPanel formCard = new RoundedPanel(22);
        formCard.setBackground(new Color(0, 0, 0, 150));
        formCard.setBorder(new EmptyBorder(20, 20, 20, 20));
        formCard.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(6, 0, 6, 0);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;

        JLabel userLabel = label("Username", TEXT);
        JTextField userField = field();
        JLabel emailLabel = label("Email", TEXT);
        JTextField emailField = field();
        JLabel phoneLabel = label("Phone", TEXT);
        JTextField phoneField = field();
        JLabel passLabel = label("Password", TEXT);
        JPasswordField passField = passwordField();
        JLabel confirmLabel = label("Confirm Password", TEXT);
        JPasswordField confirmField = passwordField();

        JPanel formGrid = new JPanel(new GridBagLayout());
        formGrid.setOpaque(false);
        GridBagConstraints form = new GridBagConstraints();
        form.gridx = 0;
        form.gridy = 0;
        form.weightx = 1;
        form.insets = new Insets(6, 0, 6, 12);
        form.fill = GridBagConstraints.HORIZONTAL;

        formGrid.add(userLabel, form);
        form.gridy++;
        formGrid.add(userField, form);
        form.gridy++;
        formGrid.add(emailLabel, form);
        form.gridy++;
        formGrid.add(emailField, form);
        form.gridy++;
        formGrid.add(phoneLabel, form);
        form.gridy++;
        formGrid.add(phoneField, form);

        form.gridx = 1;
        form.gridy = 0;
        form.insets = new Insets(6, 0, 6, 0);

        formGrid.add(passLabel, form);
        form.gridy++;
        formGrid.add(passField, form);
        form.gridy++;
        formGrid.add(confirmLabel, form);
        form.gridy++;
        formGrid.add(confirmField, form);
        form.gridy++;
        formGrid.add(new JLabel(), form);
        form.gridy++;
        formGrid.add(new JLabel(), form);

        gbc.gridwidth = 2;
        formCard.add(formGrid, gbc);

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 6));
        actions.setOpaque(false);

        JComponent createBtn = actionButton("Create Account", PRIMARY, TEXT, ACCENT, () -> {
            String pass = new String(passField.getPassword());
            String confirm = new String(confirmField.getPassword());
            if (!pass.equals(confirm)) {
                JOptionPane.showMessageDialog(this, "Passwords do not match.", "Signup", JOptionPane.WARNING_MESSAGE);
                return;
            }
            MainMenu menu = new MainMenu();
            menu.setVisible(true);
            dispose();
        });
        JComponent cancelBtn = outlineButton("Exit", PRIMARY_DARK, TEXT, ACCENT, () -> System.exit(0));

        actions.add(createBtn);
        actions.add(cancelBtn);

        JPanel formWrap = new JPanel(new BorderLayout());
        formWrap.setOpaque(false);
        formWrap.add(formCard, BorderLayout.CENTER);
        formWrap.add(actions, BorderLayout.SOUTH);

        JPanel columns = new JPanel(new GridBagLayout());
        columns.setOpaque(false);
        GridBagConstraints col = new GridBagConstraints();
        col.gridy = 0;
        col.fill = GridBagConstraints.BOTH;
        col.weighty = 1;

        col.gridx = 0;
        col.weightx = 0.38;
        col.insets = new Insets(0, 0, 0, 14);
        columns.add(leftInfo, col);

        col.gridx = 1;
        col.weightx = 0.62;
        col.insets = new Insets(0, 0, 0, 0);
        columns.add(formWrap, col);

        content.add(columns, gbcRoot);
        root.add(content, BorderLayout.CENTER);

        setVisible(true);
    }

    private static JLabel label(String text, Color fg) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("SansSerif", Font.BOLD, 15));
        label.setForeground(fg);
        return label;
    }

    private static JTextField field() {
        JTextField field = new JTextField();
        field.setFont(new Font("SansSerif", Font.PLAIN, 15));
        field.setForeground(new Color(30, 30, 30));
        field.setCaretColor(new Color(30, 30, 30));
        field.setBackground(new Color(255, 255, 255, 245));
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(255, 255, 255, 120), 1),
                new EmptyBorder(10, 12, 10, 12)));
        return field;
    }

    private static JPasswordField passwordField() {
        JPasswordField field = new JPasswordField();
        field.setFont(new Font("SansSerif", Font.PLAIN, 15));
        field.setForeground(new Color(30, 30, 30));
        field.setCaretColor(new Color(30, 30, 30));
        field.setBackground(new Color(255, 255, 255, 245));
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(255, 255, 255, 120), 1),
                new EmptyBorder(10, 12, 10, 12)));
        return field;
    }

    private static JComponent actionButton(String text, Color base, Color fg, Color glow, Runnable action) {
        JButton btn = new JButton(text);
        btn.setForeground(fg);
        btn.setFont(new Font("SansSerif", Font.BOLD, 15));
        btn.setOpaque(false);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        RoundedButton wrap = new RoundedButton(16, base, glow);
        wrap.setBorder(new EmptyBorder(8, 22, 8, 22));
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
        btn.setFont(new Font("SansSerif", Font.BOLD, 15));
        btn.setOpaque(false);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        RoundedButton wrap = new RoundedButton(16, new Color(0, 0, 0, 90), glow);
        wrap.setBorder(new EmptyBorder(8, 20, 8, 20));
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

            g2.setColor(new Color(255, 255, 255, hover ? 70 : 40));
            g2.drawRoundRect(1, 1, getWidth() - 6, getHeight() - 6, arc, arc);

            g2.dispose();
            super.paintComponent(g);
        }
    }