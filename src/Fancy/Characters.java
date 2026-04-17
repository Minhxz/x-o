package Fancy;

// Lớp chứa các ký hiệu cờ (emoji/text) để thay thế X/O
public class Characters {
    private static final String[] CHARACTERS = {
            "X", "O", "★", "♥", "☀", "☁", "▲", "●", "◆", "♣", "♠", "☺"
    };

    public static String[] names() {
        return CHARACTERS;
    }
}