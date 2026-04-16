package Music;

import java.io.File;
import javax.sound.sampled.*;
import javax.swing.JOptionPane; 

public class gameMusic {

    private static Clip bgmClip; 
    public static boolean isBgmMuted = false;
    public static boolean isSfxMuted = false;

    // Hàm bật/tắt Nhạc nền (BGM)
    public static void toggleBGM() {
        isBgmMuted = !isBgmMuted; 
        
        if (bgmClip != null) {
            if (isBgmMuted) {
                bgmClip.stop(); 
            } else {
                bgmClip.start(); 
                bgmClip.loop(Clip.LOOP_CONTINUOUSLY);
            }
        } else if (!isBgmMuted) {
            playBackgroundMusic();
        }
    }

    // Hàm bật/tắt Hiệu ứng âm thanh (SFX)
    public static void toggleSFX() {
        isSfxMuted = !isSfxMuted;
    }

    // 1. Tiếng cộc khi đặt cờ
    public static void playClick() {
        if (isSfxMuted) return; // Kiểm tra xem SFX có bị tắt không
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

    // 2. Tiếng pop/click khi bấm Menu
    public static void playMenuClick() {
        if (isSfxMuted) return; // Kiểm tra xem SFX có bị tắt không
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

    // 3. Nhạc nền
    public static void playBackgroundMusic() {
        if (isBgmMuted) return; 

        try {
            if (bgmClip != null && bgmClip.isRunning()) return; 

            File bgmPath = new File("resources/music/background.wav");
            if (bgmPath.exists()) {
                AudioInputStream audioInput = AudioSystem.getAudioInputStream(bgmPath);
                bgmClip = AudioSystem.getClip();
                bgmClip.open(audioInput);
                FloatControl gainControl = (FloatControl) bgmClip.getControl(FloatControl.Type.MASTER_GAIN);
                gainControl.setValue(-16.47f); 
                bgmClip.loop(Clip.LOOP_CONTINUOUSLY);
                bgmClip.start();
            }
        } catch (Exception e) { System.err.println("Lỗi phát nhạc nền: " + e.getMessage()); }
    }

    // 4. Nhạc Thắng (Victory)
    public static void playWinSound() {
        if (isSfxMuted) return; // Âm thanh kết quả thuộc về SFX
        try {
            File soundPath = new File("resources/music/victory.wav");
            if (!soundPath.exists()) {
                JOptionPane.showMessageDialog(null, "LỖI: Không tìm thấy file âm thanh tại đường dẫn:\n" + soundPath.getAbsolutePath(), "Thiếu File Nhạc", JOptionPane.ERROR_MESSAGE);
                return;
            }
            AudioInputStream audioInput = AudioSystem.getAudioInputStream(soundPath);
            Clip clip = AudioSystem.getClip();
            clip.open(audioInput);
            clip.start();
        } catch (UnsupportedAudioFileException e) {
            JOptionPane.showMessageDialog(null, "LỖI: File victory.wav là file giả mạo.\nHãy convert từ MP3 sang WAV chuẩn!", "Sai Định Dạng", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) { System.err.println("Lỗi nhạc Win: " + e.getMessage()); }
    }

    // 5. Nhạc Thua (Defeat)
    public static void playLoseSound() {
        if (isSfxMuted) return; 
        try {
            File soundPath = new File("resources/music/defeat.wav");
            if (!soundPath.exists()) {
                JOptionPane.showMessageDialog(null, "LỖI: Không tìm thấy file âm thanh tại đường dẫn:\n" + soundPath.getAbsolutePath(), "Thiếu File Nhạc", JOptionPane.ERROR_MESSAGE);
                return;
            }
            AudioInputStream audioInput = AudioSystem.getAudioInputStream(soundPath);
            Clip clip = AudioSystem.getClip();
            clip.open(audioInput);
            clip.start();
        } catch (UnsupportedAudioFileException e) {
            JOptionPane.showMessageDialog(null, "LỖI: File defeat.wav là file giả mạo.\nHãy convert từ MP3 sang WAV chuẩn!", "Sai Định Dạng", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) { System.err.println("Lỗi nhạc Lose: " + e.getMessage()); }
    }

    // 6. Nhạc Hòa (Draw)
    public static void playDrawSound() {
        if (isSfxMuted) return; 
        try {
            File soundPath = new File("resources/music/draw.wav");
            if (!soundPath.exists()) {
                JOptionPane.showMessageDialog(null, "LỖI: Không tìm thấy file âm thanh tại đường dẫn:\n" + soundPath.getAbsolutePath(), "Thiếu File Nhạc", JOptionPane.ERROR_MESSAGE);
                return;
            }
            AudioInputStream audioInput = AudioSystem.getAudioInputStream(soundPath);
            Clip clip = AudioSystem.getClip();
            clip.open(audioInput);
            clip.start();
        } catch (UnsupportedAudioFileException e) {
            JOptionPane.showMessageDialog(null, "LỖI: File draw.wav là file giả mạo.\nHãy convert từ MP3 sang WAV chuẩn!", "Sai Định Dạng", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) { System.err.println("Lỗi nhạc Draw: " + e.getMessage()); }
    }
}