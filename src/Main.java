import GUI.dangnhap;
import java.awt.*;
import javax.swing.*;

public class Main {
    // Hàm tiện ích để đồng bộ font chữ cho tất cả các thành phần UI (Nút, Nhãn, Textfield...)
    private static void setGlobalFont(Font font) {
        for (Object key : UIManager.getDefaults().keySet()) {
            Object value = UIManager.get(key);
            if (value instanceof Font) {
                UIManager.put(key, font);
            }
        }
    }

    public static void main(String[] args) {
        // Chạy giao diện trên Event Dispatch Thread (EDT) để đảm bảo thread-safe cho Swing
        javax.swing.SwingUtilities.invokeLater(() -> {
            try {
                // Áp dụng giao diện (Look and Feel) mặc định của hệ điều hành
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ignored) {
            }

            // Đặt font chữ chung là SansSerif, kích thước 16
            setGlobalFont(new Font("SansSerif", Font.PLAIN, 16));
            
            // Khởi tạo và hiển thị màn hình đăng nhập đầu tiên
            new dangnhap();
        });     
    }
}