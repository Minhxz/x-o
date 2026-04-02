import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;

public class dangnhap {
    public static void main(String[] args) {

        // ===== COLOR =====
        Color BG1 = new Color(0x7A1E1E);
        Color BG2 = new Color(0xC0392B);
        Color CARD = new Color(255,255,255,240);
        Color PRIMARY = new Color(0xF08A24);
        Color PRIMARY_HOVER = new Color(0xD9731A);
        Color TEXT = new Color(0x2C2C2C);
        Color BORDER = new Color(220,220,220);

        JFrame frame = new JFrame("Đăng nhập");
        frame.setSize(1000,700);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        // ===== BACKGROUND =====
        JPanel background = new JPanel() {
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);

                GradientPaint gp = new GradientPaint(
                        0, 0, BG1,
                        getWidth(), getHeight(), BG2, true
                );
                g2.setPaint(gp);
                g2.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        background.setLayout(new GridBagLayout());

        // ===== CARD =====
        RoundedPanel card = new RoundedPanel(30);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(new Color(0xD8B59C));
        card.setBorder(new EmptyBorder(40,50,40,50));

        // ===== TITLE =====
        JLabel title = new JLabel("Đăng nhập");
        title.setFont(new Font("Segoe UI", Font.BOLD, 28));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        // ===== USER =====
        JLabel userLabel = new JLabel("Tên đăng nhập");
        JTextField user = new JTextField();
        styleInput(user, BORDER);

        // ===== PASS =====
        JLabel passLabel = new JLabel("Mật khẩu");
        JPasswordField pass = new JPasswordField();
        styleInput(pass, BORDER);

        // ===== BUTTON =====
        JButton loginBtn = new JButton("Đăng nhập") {
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);

                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                super.paintComponent(g);
            }
        };

        loginBtn.setContentAreaFilled(false);
        loginBtn.setMaximumSize(new Dimension(300,45));
        loginBtn.setBackground(PRIMARY);
        loginBtn.setForeground(Color.WHITE);
        loginBtn.setFont(new Font("Segoe UI", Font.BOLD, 16));
        loginBtn.setBorder(new EmptyBorder(10,20,10,20));
        loginBtn.setAlignmentX(Component.CENTER_ALIGNMENT);

        // hover
        loginBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                loginBtn.setBackground(PRIMARY_HOVER);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                loginBtn.setBackground(PRIMARY);
            }
        });

        // ===== ADD =====
        card.add(title);
        card.add(Box.createVerticalStrut(25));

        card.add(userLabel);
        card.add(Box.createVerticalStrut(5));
        card.add(user);
        card.add(Box.createVerticalStrut(15));

        card.add(passLabel);
        card.add(Box.createVerticalStrut(5));
        card.add(pass);
        card.add(Box.createVerticalStrut(25));

        card.add(loginBtn);

        background.add(card);
        frame.add(background);

        // ===== EVENT LOGIN (ĐÃ FIX) =====
        loginBtn.addActionListener(e -> {

            // kiểm tra đơn giản
            if(user.getText().isEmpty() || pass.getPassword().length == 0){
                JOptionPane.showMessageDialog(frame, "Nhập đủ thông tin!");
                return;
            }

            // ===== DIALOG =====
            JDialog dialog = new JDialog(frame, "Bạn là ai?", true);
            dialog.setSize(400,300);
            dialog.setLayout(new BorderLayout());

            JLabel t = new JLabel("Bạn là ai?");
            t.setFont(new Font("Segoe UI", Font.BOLD, 22));
            t.setBorder(new EmptyBorder(10,20,10,10));
            dialog.add(t, BorderLayout.NORTH);

            JPanel center = new JPanel();
            center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS));

            JTextField nickname = new JTextField();
            styleInput(nickname, BORDER);
            nickname.setMaximumSize(new Dimension(300,45));
            nickname.setAlignmentX(Component.CENTER_ALIGNMENT);

            JLabel sub = new JLabel("Lựa chọn tuyệt vời! 👍");
            sub.setAlignmentX(Component.CENTER_ALIGNMENT);

            center.add(Box.createVerticalStrut(20));
            center.add(nickname);
            center.add(Box.createVerticalStrut(20));
            center.add(sub);

            dialog.add(center, BorderLayout.CENTER);

            JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            JButton next = new JButton("Tiếp tục");
            next.setBackground(PRIMARY);
            next.setForeground(Color.WHITE);

            bottom.add(next);
            dialog.add(bottom, BorderLayout.SOUTH);

            next.addActionListener(e2 -> {
                String name = nickname.getText();
                if(name.isEmpty()){
                    JOptionPane.showMessageDialog(dialog, "Nhập tên đi!");
                } else {
                    dialog.dispose();
                    JOptionPane.showMessageDialog(frame, "Xin chào " + name);
                }
            });

            dialog.setLocationRelativeTo(frame);
            dialog.setVisible(true);
        });

        frame.setVisible(true);
    }

    // ===== STYLE INPUT =====
    private static void styleInput(JTextField field, Color borderColor){
        field.setMaximumSize(new Dimension(300,40));
        field.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        field.setBorder(new CompoundBorder(
                new LineBorder(borderColor,1,true),
                new EmptyBorder(10,15,10,15)
        ));
    }
}

// ===== PANEL BO GÓC =====
class RoundedPanel extends JPanel {
    private int radius;

    public RoundedPanel(int radius) {
        this.radius = radius;
        setOpaque(false);
    }

    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        g2.setColor(getBackground());
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);

        g2.dispose();
        super.paintComponent(g);
    }
}