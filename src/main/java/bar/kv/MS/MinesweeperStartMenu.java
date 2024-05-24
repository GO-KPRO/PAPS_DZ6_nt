package bar.kv.MS;

import bar.kv.managers.PageManager;
import bar.kv.menu.ColorScheme;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;

import static java.lang.Math.max;
import static java.lang.Math.min;

public class MinesweeperStartMenu extends JComponent {

    private final int maxSize = 100;
    private final int minSize = 3;
    private final PageManager pageManager;
    private final Minesweeper minesweeper;
    private ColorScheme colorScheme = new ColorScheme();
    private int fieldWidth;
    private int fieldHeight;
    private int bombsNum;

    public MinesweeperStartMenu(PageManager pageManager, Minesweeper minesweeper) {
        enableEvents(AWTEvent.MOUSE_EVENT_MASK);
        this.pageManager = pageManager;
        this.minesweeper = minesweeper;
        fieldWidth = minesweeper.getFieldSizeX();
        fieldHeight = minesweeper.getFieldSizeY();
        bombsNum = minesweeper.getBombsNum();
        repaint();
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

            if ((x > w / 2 - 100) && (x < w / 2 + 100)) {
                if ((y > h / 2 + 225) && (y < h / 2 + 275)) {
                    pageManager.remove(this);
                    minesweeper.start(fieldWidth, fieldHeight);
                }
            }

            if ((x > w / 2 - 150) && (x < w / 2 + 150)) {
                int num = ((x - (w / 2 - 150)) * (maxSize - minSize + 1)) / 300 + minSize;
                if ((y > h / 2 - 200) && (y < h / 2 - 100)) {
                    fieldWidth = num;
                    bombsNum = min(bombsNum, fieldWidth * fieldHeight - 1);
                    minesweeper.setBombsNum(bombsNum);
                    repaint();
                } else if ((y > h / 2 - 50) && (y < h / 2 + 50)) {
                    fieldHeight = num;
                    bombsNum = min(bombsNum, fieldWidth * fieldHeight - 1);
                    minesweeper.setBombsNum(bombsNum);
                    repaint();
                } else if ((y > h / 2 + 100) && (y < h / 2 + 200)) {
                    bombsNum = max(1, ((x - (w / 2 - 150)) * (fieldWidth * fieldHeight)) / 300);
                    minesweeper.setBombsNum(bombsNum);
                    repaint();
                }
            }
        }
    }

    public void setColorScheme(ColorScheme colorScheme) {
        this.colorScheme = colorScheme;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        drawBackground(graphics);
        drawWindow(graphics);
        drawSliders(graphics, minSize, maxSize, fieldWidth, fieldHeight, bombsNum);
        drawStartButton(graphics);
    }

    void drawBackground(Graphics graphics) {
        int w = getWidth();
        int h = getHeight();
        graphics.setColor(new Color((float) 0.0, (float) 0.0, (float) 0.0, (float) 0.75));
        graphics.fillRect(0, 0, w, h);
    }

    void drawWindow(Graphics graphics) {
        int w = getWidth();
        int h = getHeight();
        graphics.setColor(colorScheme.getBrightColor());
        graphics.fillRect(w / 2 - 300, h / 2 - 250, 600, 550);
    }

    void drawSlider(Graphics graphics, int x, int y, int min, int max, int num, String str) {
        graphics.setColor(colorScheme.getDarkColor());
        graphics.fillRect(x - 25, y, 350, 100);
        graphics.setColor(colorScheme.getMediumColor());
        graphics.fillRect(x - 15 + ((num - min) * 300) / (max - min), y + 10, 30, 80);
        graphics.setColor(colorScheme.getDarkColor());
        char[] chars = (str + num).toCharArray();
        graphics.drawChars(chars, 0, chars.length, x, y - 10);
    }

    void drawSliders(Graphics graphics, int min, int max, int numX, int numY, int numZ) {
        int w = getWidth();
        int h = getHeight();
        drawSlider(graphics, w / 2 - 150, h / 2 - 200, min, max, numX, "Ширина: ");
        drawSlider(graphics, w / 2 - 150, h / 2 - 50, min, max, numY, "Высота: ");
        drawSlider(graphics, w / 2 - 150, h / 2 + 100, 1, numX * numY - 1, numZ, "Бомбы: ");
    }

    void drawStartButton(Graphics graphics) {
        int w = getWidth();
        int h = getHeight();
        graphics.setColor(colorScheme.getDarkColor());
        graphics.fillRect(w / 2 - 100, h / 2 + 225, 200, 50);
        char[] chars = "Старт".toCharArray();
        graphics.setColor(colorScheme.getBrightColor());
        graphics.setFont(graphics.getFont().deriveFont(Font.PLAIN, 20));
        graphics.drawChars(chars, 0, chars.length, w / 2 - 30, h / 2 + 255);
    }
}
