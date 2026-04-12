import GUI.dangnhap;

import javax.swing.*;
import java.awt.*;

public class Main {
    private static void setGlobalFont(Font font) {
        for (Object key : UIManager.getDefaults().keySet()) {
            Object value = UIManager.get(key);
            if (value instanceof Font) {
                UIManager.put(key, font);
            }
        }
    }

    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ignored) {
            }

            setGlobalFont(new Font("SansSerif", Font.PLAIN, 16));
            new dangnhap();
        });     
    }
}