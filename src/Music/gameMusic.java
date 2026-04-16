package Music;

import java.io.File;
import javax.sound.sampled.*;

public class gameMusic {

    private static Clip bgmClip; 
    private static boolean isMuted = false;

    // Hàm bật/tắt nhạc
    public static void toggleBackgroundMusic() {
        isMuted = !isMuted; 
        
        if (bgmClip != null) {
            if (isMuted) {
                bgmClip.stop(); 
            } else {
                bgmClip.start(); 
                bgmClip.loop(Clip.LOOP_CONTINUOUSLY);
            }
        } else if (!isMuted) {
            playBackgroundMusic();
        }
    }

    // 1. Tiếng cộc khi đặt cờ
    public static void playClick() {
        try {
            File soundPath = new File("resources/music/tap.wav");
            if (soundPath.exists()) {
                AudioInputStream audioInput = AudioSystem.getAudioInputStream(soundPath);
                Clip clip = AudioSystem.getClip();
                clip.open(audioInput);
                clip.start();
            }
        } catch (Exception e) {
            System.err.println("Lỗi phát âm thanh đặt cờ: " + e.getMessage());
        }
    }

    // 2. Tiếng pop/click khi bấm Menu
    public static void playMenuClick() {
        try {
            File soundPath = new File("resources/music/click.wav");
            if (soundPath.exists()) {
                AudioInputStream audioInput = AudioSystem.getAudioInputStream(soundPath);
                Clip clip = AudioSystem.getClip();
                clip.open(audioInput);
                clip.start();
            }
        } catch (Exception e) {
            System.err.println("Lỗi phát âm thanh Menu: " + e.getMessage());
        }
    }

    // 3. Nhạc nền
    public static void playBackgroundMusic() {
        if (isMuted) return; 

        try {
            if (bgmClip != null && bgmClip.isRunning()) {
                return; 
            }

            File bgmPath = new File("resources/music/background.wav");
            if (bgmPath.exists()) {
                AudioInputStream audioInput = AudioSystem.getAudioInputStream(bgmPath);
                bgmClip = AudioSystem.getClip();
                bgmClip.open(audioInput);
                FloatControl gainControl = (FloatControl) bgmClip.getControl(FloatControl.Type.MASTER_GAIN);
                gainControl.setValue(-16.47f); // ~15% volume
                bgmClip.loop(Clip.LOOP_CONTINUOUSLY);
                bgmClip.start();
            }
        } catch (Exception e) {
            System.err.println("Lỗi phát nhạc nền: " + e.getMessage());
        }
    }

    // 4. Nhạc Thắng (Victory)
    public static void playWinSound() {
        if (isMuted) return;
        try {
            // Đã cập nhật thành victory.wav
            File soundPath = new File("resources/music/victory.wav");
            if (soundPath.exists()) {
                AudioInputStream audioInput = AudioSystem.getAudioInputStream(soundPath);
                Clip clip = AudioSystem.getClip();
                clip.open(audioInput);
                clip.start();
            }
        } catch (Exception e) { System.err.println("Lỗi nhạc Win: " + e.getMessage()); }
    }

    // 5. Nhạc Thua (Defeat)
    public static void playLoseSound() {
        if (isMuted) return;
        try {
            // Đã cập nhật thành defeat.wav
            File soundPath = new File("resources/music/defeat.wav");
            if (soundPath.exists()) {
                AudioInputStream audioInput = AudioSystem.getAudioInputStream(soundPath);
                Clip clip = AudioSystem.getClip();
                clip.open(audioInput);
                clip.start();
            }
        } catch (Exception e) { System.err.println("Lỗi nhạc Lose: " + e.getMessage()); }
    }

    // 6. Nhạc Hòa (Draw)
    public static void playDrawSound() {
        if (isMuted) return;
        try {
            // Sử dụng draw.wav
            File soundPath = new File("resources/music/draw.wav");
            if (soundPath.exists()) {
                AudioInputStream audioInput = AudioSystem.getAudioInputStream(soundPath);
                Clip clip = AudioSystem.getClip();
                clip.open(audioInput);
                clip.start();
            }
        } catch (Exception e) { System.err.println("Lỗi nhạc Draw: " + e.getMessage()); }
    }
}