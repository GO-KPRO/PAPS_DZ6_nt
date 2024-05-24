package bar.kv.managers;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class SoundManager {
    private final PageManager pageManager;
    private final FloatControl clickVolume;
    private final FloatControl flagVolume;
    private final FloatControl explosionVolume;
    private final FloatControl winVolume;
    private final FloatControl loseVolume;
    private final FloatControl musicVolume;
    private final FloatControl damageVolume;
    private Clip click;
    private Clip flag;
    private Clip explosion;
    private Clip win;
    private Clip lose;
    private Clip damage;
    private Clip music;

    public SoundManager(PageManager pageManager) {
        this.pageManager = pageManager;
        try {
            File soundFile = new File("src\\main\\resources\\krSound\\click.wav");
            AudioInputStream stream = AudioSystem.getAudioInputStream(soundFile);
            click = AudioSystem.getClip();
            click.open(stream);
            soundFile = new File("src\\main\\resources\\krSound\\flag.wav");
            stream = AudioSystem.getAudioInputStream(soundFile);
            flag = AudioSystem.getClip();
            flag.open(stream);
            soundFile = new File("src\\main\\resources\\krSound\\explosion.wav");
            stream = AudioSystem.getAudioInputStream(soundFile);
            explosion = AudioSystem.getClip();
            explosion.open(stream);
            soundFile = new File("src\\main\\resources\\krSound\\win.wav");
            stream = AudioSystem.getAudioInputStream(soundFile);
            win = AudioSystem.getClip();
            win.open(stream);
            soundFile = new File("src\\main\\resources\\krSound\\lose.wav");
            stream = AudioSystem.getAudioInputStream(soundFile);
            lose = AudioSystem.getClip();
            lose.open(stream);
            soundFile = new File("src\\main\\resources\\krSound\\damage.wav");
            stream = AudioSystem.getAudioInputStream(soundFile);
            damage = AudioSystem.getClip();
            damage.open(stream);
            soundFile = new File("src\\main\\resources\\krSound\\music.wav");
            stream = AudioSystem.getAudioInputStream(soundFile);
            music = AudioSystem.getClip();
            music.open(stream);
        } catch (UnsupportedAudioFileException | LineUnavailableException | IOException e) {
            System.out.println("Wrong file format\n");
        }
        clickVolume = (FloatControl) click.getControl(FloatControl.Type.MASTER_GAIN);
        flagVolume = (FloatControl) flag.getControl(FloatControl.Type.MASTER_GAIN);
        explosionVolume = (FloatControl) explosion.getControl(FloatControl.Type.MASTER_GAIN);
        winVolume = (FloatControl) win.getControl(FloatControl.Type.MASTER_GAIN);
        loseVolume = (FloatControl) lose.getControl(FloatControl.Type.MASTER_GAIN);
        musicVolume = (FloatControl) music.getControl(FloatControl.Type.MASTER_GAIN);
        damageVolume = (FloatControl) damage.getControl(FloatControl.Type.MASTER_GAIN);
        setMusic(50);
        setVolume(50);
        playMusic();
    }

    public void click() {
        click.setFramePosition(0);
        click.start();
    }

    public void flag() {
        flag.setFramePosition(0);
        flag.start();
    }

    public void explosion() {
        explosion.setFramePosition(0);
        explosion.start();
    }

    public void win() {
        win.setFramePosition(0);
        win.start();
    }

    public void lose() {
        lose.setFramePosition(0);
        lose.start();
    }

    public void damage() {
        damage.setFramePosition(0);
        damage.start();
    }

    public void playMusic() {
        Thread myThready = new Thread(() -> {
            while (true) {
                if (!music.isRunning()) {
                    music.setFramePosition(0);
                    music.start();
                }
            }
        });
        myThready.start();
    }

    public void setVolume(int num) {
        float vol = (float) (clickVolume.getMinimum() + num * (clickVolume.getMaximum() - clickVolume.getMinimum()) / 100.0);
        clickVolume.setValue(vol);
        vol = (float) (flagVolume.getMinimum() + num * (flagVolume.getMaximum() - flagVolume.getMinimum()) / 100.0);
        flagVolume.setValue(vol);
        vol = (float) (explosionVolume.getMinimum() + num * (explosionVolume.getMaximum() - explosionVolume.getMinimum()) / 100.0);
        explosionVolume.setValue(vol);
        vol = (float) (winVolume.getMinimum() + num * (winVolume.getMaximum() - winVolume.getMinimum()) / 100.0);
        winVolume.setValue(vol);
        vol = (float) (loseVolume.getMinimum() + num * (loseVolume.getMaximum() - loseVolume.getMinimum()) / 100.0);
        loseVolume.setValue(vol);
        vol = (float) (damageVolume.getMinimum() + num * (damageVolume.getMaximum() - damageVolume.getMinimum()) / 100.0);
        damageVolume.setValue(vol);
    }

    public void setMusic(int num) {
        float vol = (float) (musicVolume.getMinimum() + num * (musicVolume.getMaximum() - musicVolume.getMinimum()) / 100.0);
        musicVolume.setValue(vol);
    }
}
