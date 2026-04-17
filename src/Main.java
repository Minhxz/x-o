import GUI.dangnhap;
import java.awt.*;
import javax.swing.*;

public class Main {
    // Hàm này duyệt qua tất cả các thành phần UI mặc định của Java Swing
    // và ép chúng sử dụng chung một font chữ do mình cài đặt
    private static void setGlobalFont(Font font) {
        for (Object key : UIManager.getDefaults().keySet()) {
            Object value = UIManager.get(key);
            if (value instanceof Font) {
                UIManager.put(key, font);
            }
        }
    }

    public static void main(String[] args) {
        // invokeLater giúp chạy giao diện trên Event Dispatch Thread (luồng riêng của Swing),
        // tránh các lỗi giật lag hoặc xung đột khi render UI
        javax.swing.SwingUtilities.invokeLater(() -> {
            try {
                // Sử dụng giao diện (Look & Feel) mặc định của hệ điều hành đang chạy (Windows/Mac)
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ignored) {
            }

            // Đặt font chữ mặc định cho toàn bộ game là SansSerif, size 16
            setGlobalFont(new Font("SansSerif", Font.PLAIN, 16));
            
            // Khởi tạo màn hình đăng nhập đầu tiên khi chạy game
            new dangnhap();
        });     
    }
}


