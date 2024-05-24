package bar.kv.menu;

import bar.kv.managers.PageManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class Settings extends JComponent {


    private final int minSize = 0;
    private final int maxSize = 100;
    private final PageManager pageManager;
    private ColorScheme colorScheme = new ColorScheme();
    private int soundEffects = 50;
    private int music = 50;

    public Settings(PageManager pageManager) {
        enableEvents(AWTEvent.MOUSE_EVENT_MASK);
        this.pageManager = pageManager;
    }

    @Override
    protected void processMouseEvent(MouseEvent mouseEvent) {
        super.processMouseEvent(mouseEvent);
        int w = getWidth();
        int h = getHeight();
        if ((mouseEvent.getID() == MouseEvent.MOUSE_PRESSED) && (mouseEvent.getButton() == MouseEvent.BUTTON1)) {
            int x = mouseEvent.getX();
            int y = mouseEvent.getY();
            pageManager.getSoundManager().click();
            if ((x > 0) && (x < 100)) {
                if ((y > 0) && (y < 100)) {
                    pageManager.moving(pageManager.getStartMenu());
                }
            }

            if ((x > w / 2 - 150) && (x < w / 2 + 150)) {
                int num = ((x - (w / 2 - 150)) * (maxSize - minSize + 1)) / 300 + minSize;
                if ((y > h / 2 - 150) && (y < h / 2 - 50)) {
                    pageManager.getSoundManager().setVolume(num);
                    soundEffects = num;
                    repaint();
                } else if ((y > h / 2) && (y < h / 2 + 100)) {
                    pageManager.getSoundManager().setMusic(num);
                    music = num;
                    repaint();
                }
            }

            if ((x > w / 2 - 110) && (x < w / 2 + 110)) {
                if ((y > h / 2 + 125) && (y < h / 2 + 175)) {
                    reset();
                    pageManager.moving(pageManager.getStartMenu());
                }
            }

        }
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        drawBackground(graphics);
        drawBackButton(graphics);
        drawSliders(graphics, minSize, maxSize, soundEffects, music);
        drawConfirmButton(graphics);
    }

    void drawBackground(Graphics graphics) {
        int w = getWidth();
        int h = getHeight();
        graphics.setColor(colorScheme.getDarkColor());
        graphics.fillRect(0, 0, w, h);
    }

    void drawBackButton(Graphics graphics) {
        graphics.setColor(colorScheme.getMediumColor());
        graphics.drawImage(pageManager.getImageManager().getBackButton(), 0, 0, 100, 100, null);
    }

    public void setColorScheme(ColorScheme colorScheme) {
        this.colorScheme = colorScheme;
    }

    void drawSlider(Graphics graphics, int x, int y, int min, int max, int num, String str) {
        graphics.setColor(colorScheme.getBrightColor());
        graphics.fillRect(x - 25, y, 350, 100);
        graphics.setColor(colorScheme.getMediumColor());
        graphics.fillRect(x - 15 + ((num - min) * 300) / (max - min), y + 10, 30, 80);
        graphics.setColor(colorScheme.getBrightColor());
        char[] chars = (str + num).toCharArray();
        graphics.drawChars(chars, 0, chars.length, x, y - 10);
    }

    void drawSliders(Graphics graphics, int min, int max, int numX, int numY) {
        int w = getWidth();
        int h = getHeight();
        drawSlider(graphics, w / 2 - 150, h / 2 - 150, min, max, numX, "Звуковые эффекты: ");
        drawSlider(graphics, w / 2 - 150, h / 2, min, max, numY, "музыка: ");
    }

    void drawConfirmButton(Graphics graphics) {
        int w = getWidth();
        int h = getHeight();
        graphics.setColor(colorScheme.getBrightColor());
        graphics.fillRect(w / 2 - 110, h / 2 + 125, 220, 50);
        char[] chars = "Очистить результаты".toCharArray();
        graphics.setColor(colorScheme.getDarkColor());
        graphics.setFont(graphics.getFont().deriveFont(Font.PLAIN, 20));
        graphics.drawChars(chars, 0, chars.length, w / 2 - 100, h / 2 + 155);
    }

    void reset() {
        try {
            File table = new File("src\\main\\resources\\krMinesweeperTable\\Table.json");
            Files.deleteIfExists(table.toPath());
            table = new File("src\\main\\resources\\krSudokuTable\\Table.json");
            Files.deleteIfExists(table.toPath());
        } catch (IOException e) {
            System.out.println("Can`t delete file");
        }
        pageManager.getMinesweeperRecordsTable().clear();
        pageManager.getSudokuRecordsTable().clear();
    }
}
