package Fancy;

import java.awt.Color;

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

    // THEME CAM (Đã được chỉnh thành EARTH để mặc định khi mở game)
    public static final Theme EARTH = new Theme(
            "Sunset Orange",
            new Color(0x2D0F02), // Background: Nâu cam cực đậm (tạo độ sâu)
            new Color(0xD9480F), // Primary: Cam cháy (màu chủ đạo)
            new Color(0x7B2005), // PrimaryDark: Cam đất tối
            new Color(0xFFD8A8), // Board: Màu kem cam nhạt (dễ nhìn quân cờ)
            new Color(0xF76707), // Tile: Cam tươi cho các ô cờ
            Color.WHITE,         // Text: Trắng
            new Color(0x431407), // TextDark: Nâu đen
            new Color(0xFFD43B)  // Accent: Vàng rực cho các điểm nhấn
    );

    // THEME XANH LỤC BẢO
    public static final Theme FIRE = new Theme(
            "Emerald Forest",
            new Color(0x022C22), // Background: Xanh đen lục bảo
            new Color(0x10B981), // Primary: Xanh lục sáng
            new Color(0x064E3B), // PrimaryDark: Xanh rừng sâu
            new Color(0xD1FAE5), // Board: Xanh bạc hà rất nhạt
            new Color(0x059669), // Tile: Xanh lá đậm
            Color.WHITE,         // Text: Trắng
            new Color(0x022C22), // TextDark: Xanh đen
            new Color(0x34D399)  // Accent: Xanh ngọc
    );

    // THEME XANH NAVY
    public static final Theme WATER = new Theme(
            "Deep Ocean",
            new Color(0x0F172A), // Background: Xanh Navy tối (Slate)
            new Color(0x2563EB), // Primary: Xanh dương chuẩn
            new Color(0x1E3A8A), // PrimaryDark: Xanh biển sâu
            new Color(0xDBEAFE), // Board: Xanh lam nhạt
            new Color(0x3B82F6), // Tile: Xanh dương sáng
            Color.WHITE,         // Text: Trắng
            new Color(0x1E293B), // TextDark: Xám xanh đậm
            new Color(0x7DD3FC)  // Accent: Xanh lơ (Cyan)
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