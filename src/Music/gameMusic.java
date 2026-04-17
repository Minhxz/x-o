package Music;

import java.io.File;
import javax.sound.sampled.*;
import javax.swing.JOptionPane; 

public class gameMusic {

    private static Clip bgmClip; // Lưu trữ đối tượng nhạc đang phát
    public static boolean isBgmMuted = false;
    public static boolean isSfxMuted = false;

    // Bật/tắt Nhạc nền
    public static void toggleBGM() {
        isBgmMuted = !isBgmMuted; 
        
        if (bgmClip != null) {
            if (isBgmMuted) {
                bgmClip.stop(); // Ngừng phát
            } else {
                bgmClip.start(); 
                bgmClip.loop(Clip.LOOP_CONTINUOUSLY); // Lặp lại vô tận
            }
        } else if (!isBgmMuted) {
            playBackgroundMusic();
        }
    }

    // Bật/tắt SFX (Hiệu ứng khi đánh cờ, ấn nút)
    public static void toggleSFX() {
        isSfxMuted = !isSfxMuted;
    }

    // Tiếng cộc khi đặt cờ
    public static void playClick() {
        if (isSfxMuted) return; 
        try {
            File soundPath = new File("resources/music/tap.wav");
            if (soundPath.exists()) {
                AudioInputStream audioInput = AudioSystem.getAudioInputStream(soundPath);
                Clip clip = AudioSystem.getClip();
                clip.open(audioInput);
                clip.start();
            }
        } catch (Exception e) { System.err.println("Lỗi phát âm thanh đặt cờ: " + e.getMessage()); }
    }

    // Tiếng pop khi bấm nút trên Menu
    public static void playMenuClick() {
        if (isSfxMuted) return; 
        try {
            File soundPath = new File("resources/music/click.wav");
            if (soundPath.exists()) {
                AudioInputStream audioInput = AudioSystem.getAudioInputStream(soundPath);
                Clip clip = AudioSystem.getClip();
                clip.open(audioInput);
                clip.start();
            }
        } catch (Exception e) { System.err.println("Lỗi phát âm thanh Menu: " + e.getMessage()); }
    }

    // Phát nhạc nền Game (Chỉ gọi 1 lần khi vào game)
    public static void playBackgroundMusic() {
        if (isBgmMuted) return; 

        try {
            // Không chạy đè nếu nhạc đang diễn ra
            if (bgmClip != null && bgmClip.isRunning()) return; 

            File bgmPath = new File("resources/music/background.wav");
            if (bgmPath.exists()) {
                AudioInputStream audioInput = AudioSystem.getAudioInputStream(bgmPath);
                bgmClip = AudioSystem.getClip();
                bgmClip.open(audioInput);
                // Giảm volume mặc định của file wav xuống để nghe tiếng lách cách cờ dễ hơn
                FloatControl gainControl = (FloatControl) bgmClip.getControl(FloatControl.Type.MASTER_GAIN);
                gainControl.setValue(-16.47f); 
                bgmClip.loop(Clip.LOOP_CONTINUOUSLY);
                bgmClip.start();
            }
        } catch (Exception e) { System.err.println("Lỗi phát nhạc nền: " + e.getMessage()); }
    }

    // Nhạc khi thắng
    public static void playWinSound() {
        if (isSfxMuted) return; 
        try {
            File soundPath = new File("resources/music/victory.wav");
            if (!soundPath.exists()) {
                JOptionPane.showMessageDialog(null, "LỖI: Không tìm thấy file victory.wav", "Thiếu File Nhạc", JOptionPane.ERROR_MESSAGE);
                return;
            }
            AudioInputStream audioInput = AudioSystem.getAudioInputStream(soundPath);
            Clip clip = AudioSystem.getClip();
            clip.open(audioInput);
            clip.start();
        } catch (UnsupportedAudioFileException e) {
            JOptionPane.showMessageDialog(null, "LỖI: Định dạng victory sai", "Sai Định Dạng", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) { System.err.println("Lỗi nhạc Win: " + e.getMessage()); }
    }

    // Nhạc khi thua (AI thắng)
    public static void playLoseSound() {
        if (isSfxMuted) return; 
        try {
            File soundPath = new File("resources/music/defeat.wav");
            if (!soundPath.exists()) {
                JOptionPane.showMessageDialog(null, "LỖI: Không tìm thấy file defeat", "Thiếu File Nhạc", JOptionPane.ERROR_MESSAGE);
                return;
            }
            AudioInputStream audioInput = AudioSystem.getAudioInputStream(soundPath);
            Clip clip = AudioSystem.getClip();
            clip.open(audioInput);
            clip.start();
        } catch (UnsupportedAudioFileException e) {
            JOptionPane.showMessageDialog(null, "LỖI: Định dạng defeat sai", "Sai Định Dạng", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) { System.err.println("Lỗi nhạc Lose: " + e.getMessage()); }
    }

    // Nhạc khi hòa
    public static void playDrawSound() {
        if (isSfxMuted) return; 
        try {
            File soundPath = new File("resources/music/draw.wav");
            if (!soundPath.exists()) {
                JOptionPane.showMessageDialog(null, "LỖI: Không tìm thấy file draw", "Thiếu File Nhạc", JOptionPane.ERROR_MESSAGE);
                return;
            }
            AudioInputStream audioInput = AudioSystem.getAudioInputStream(soundPath);
            Clip clip = AudioSystem.getClip();
            clip.open(audioInput);
            clip.start();
        } catch (UnsupportedAudioFileException e) {
            JOptionPane.showMessageDialog(null, "LỖI: Định dạng draw sai", "Sai Định Dạng", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) { System.err.println("Lỗi nhạc Draw: " + e.getMessage()); }
    }
}