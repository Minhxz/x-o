package Fancy;

import java.awt.Color;

// Lớp định nghĩa cấu hình bảng màu (Color Palette) cho game
public class Theme {
    public final String name;
    public final Color background;
    public final Color primary;
    public final Color primaryDark;
    public final Color board;
    public final Color tile;
    public final Color text;
    public final Color textDark;
    public final Color accent;

    public Theme(String name, Color background, Color primary, Color primaryDark, Color board, Color tile, Color text, Color textDark, Color accent) {
        this.name = name;
        this.background = background;
        this.primary = primary;
        this.primaryDark = primaryDark;
        this.board = board;
        this.tile = tile;
        this.text = text;
        this.textDark = textDark;
        this.accent = accent;
    }

    // THEME 1: Màu Nâu/Cam Đất
    public static final Theme EARTH = new Theme(
            "Sunset Orange",
            new Color(0x2D0F02), // Background: Nâu cam cực đậm
            new Color(0xD9480F), // Primary: Cam cháy 
            new Color(0x7B2005), // PrimaryDark: Cam đất tối
            new Color(0xFFD8A8), // Board: Màu kem cam nhạt 
            new Color(0xF76707), // Tile: Cam tươi 
            Color.WHITE,         // Text: Trắng
            new Color(0x431407), // TextDark: Nâu đen
            new Color(0xFFD43B)  // Accent: Vàng rực
    );

    // THEME 2: XANH LỤC BẢO
    public static final Theme FIRE = new Theme(
            "Emerald Forest",
            new Color(0x022C22), 
            new Color(0x10B981), 
            new Color(0x064E3B), 
            new Color(0xD1FAE5), 
            new Color(0x059669), 
            Color.WHITE,         
            new Color(0x022C22), 
            new Color(0x34D399)  
    );

    // THEME 3: XANH NAVY
    public static final Theme WATER = new Theme(
            "Deep Ocean",
            new Color(0x0F172A), 
            new Color(0x2563EB), 
            new Color(0x1E3A8A), 
            new Color(0xDBEAFE), 
            new Color(0x3B82F6), 
            Color.WHITE,         
            new Color(0x1E293B), 
            new Color(0x7DD3FC)  
    );

    public static Theme[] presets() {
        return new Theme[]{EARTH, FIRE, WATER};
    }

    public static Theme byName(String name) {
        for (Theme theme : presets()) {
            if (theme.name.equalsIgnoreCase(name)) {
                return theme;
            }
        }
        return null;
    }
}