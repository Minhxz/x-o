package Music;

import javax.sound.sampled.*;
import java.io.File;

public class gameMusic {
    public static void playClick() {
        try {
            // Đường dẫn đến file âm thanh bạn đã tải lên (đã đổi tên thành place.wav)
            File soundPath = new File("resources/music/place.wav");
            
            if (soundPath.exists()) {
                AudioInputStream audioInput = AudioSystem.getAudioInputStream(soundPath);
                Clip clip = AudioSystem.getClip();
                clip.open(audioInput);
                clip.start();
            } else {
                System.out.println("Không tìm thấy file âm thanh tại: " + soundPath.getAbsolutePath());
            }
        } catch (Exception e) {
            System.err.println("Lỗi phát âm thanh: " + e.getMessage());
        }
    }
}