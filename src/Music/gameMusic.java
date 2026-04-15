package Music;

import java.io.File;
import javax.sound.sampled.*;

public class gameMusic {

    private static Clip bgmClip; 

    // 1. Tiếng cộc khi đặt cờ
    public static void playClick() {
        try {
            File soundPath = new File("resources/music/click.wav");
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
            File soundPath = new File("resources/music/tap.wav");
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
        try {
            File bgmPath = new File("resources/music/background.wav");
            if (bgmPath.exists()) {
                AudioInputStream audioInput = AudioSystem.getAudioInputStream(bgmPath);
                bgmClip = AudioSystem.getClip();
                bgmClip.open(audioInput);
                FloatControl gainControl = (FloatControl) bgmClip.getControl(FloatControl.Type.MASTER_GAIN);
                gainControl.setValue(-20.0f);
                bgmClip.loop(Clip.LOOP_CONTINUOUSLY);
                bgmClip.start();
            }
        } catch (Exception e) {
            System.err.println("Lỗi phát nhạc nền: " + e.getMessage());
        }
    }
}