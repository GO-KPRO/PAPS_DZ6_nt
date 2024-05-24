package bar.kv.GOL;

import bar.kv.managers.PageManager;
import bar.kv.menu.ColorScheme;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.Random;

import static java.lang.Integer.min;
import static java.lang.Thread.sleep;

public class GameOfLife extends JComponent {
    private final PageManager pageManager;
    private ColorScheme colorScheme = new ColorScheme();
    private boolean[][] field;
    private int[][] countField;
    private int fieldX;
    private int fieldY;
    private int cellSize;
    private int delay = 100;
    private boolean isRunning;

    public GameOfLife(PageManager pageManager) {
        enableEvents(AWTEvent.MOUSE_EVENT_MASK);
        this.pageManager = pageManager;
        field = new boolean[50][50];
        countField = new int[50][50];
        isRunning = false;
        calcCord();
    }

    public void setColorScheme(ColorScheme colorScheme) {
        this.colorScheme = colorScheme;
        repaint();
    }

    @Override
    protected void processMouseEvent(MouseEvent mouseEvent) {
        super.processMouseEvent(mouseEvent);
        if ((mouseEvent.getID() == MouseEvent.MOUSE_PRESSED) && (mouseEvent.getButton() == MouseEvent.BUTTON1)) {
            int x = mouseEvent.getX();
            int y = mouseEvent.getY();
            pageManager.getSoundManager().click();
            if ((x > getWidth() - 100) && (x < getWidth())) {
                if ((y > 150) && (y < 250)) {
                    isRunning = !isRunning;
                    repaint();
                    Thread myThready = new Thread(new Runnable() {
                        public void run() {
                            while (isRunning) {
                                step();
                                try {
                                    sleep(delay);
                                } catch (InterruptedException e) {
                                    throw new RuntimeException(e);
                                }
                            }
                        }
                    });
                    myThready.start();
                }
            }
            if ((x > getWidth() - 100) && (x < getWidth())) {
                if ((y > 0) && (y < 100)) {
                    restart();
                    isRunning = !isRunning;
                    repaint();
                }
            }
            if ((x > 0) && (x < 100)) {
                if ((y > 0) && (y < 100)) {
                    isRunning = false;
                    pageManager.moving(pageManager.getGameSelectionMenu());
                }
            }
            if ((x > 0) && (x < 100)) {
                if ((y > 150) && (y < 250)) {
                    isRunning = false;
                    pageManager.paint(pageManager.getGameOfLifeResizeMenu());
                    pageManager.moving(pageManager.getGameOfLife());
                }
            }
            if ((x > getWidth() - 100) && (x < getWidth())) {
                if ((y > 300) && (y < 400)) {
                    random();
                }
            }
            if ((x > 0) && (x < 100)) {
                if ((y > 300) && (y < 600)) {
                    setDelay(1000 / (((y - 300) * 30) / 300 + 1));
                }
            }
            for (int i = 0; i < field.length; ++i) {
                for (int j = 0; j < field[0].length; ++j) {
                    if ((x > fieldX + i * cellSize) && (x < fieldX + i * cellSize + cellSize)) {
                        if ((y > fieldY + j * cellSize) && (y < fieldY + j * cellSize + cellSize)) {
                            field[i][j] = !field[i][j];
                            recount();
                            repaint();
                        }
                    }
                }
            }
        }
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        drawBackground(graphics);
        drawField(graphics);
        drawBackButton(graphics);
        drawRestartButton(graphics);
        drawStartStopButton(graphics);
        drawRandomButton(graphics);
        drawResizeButton(graphics);
        drawSlider(graphics, 1000 / delay);
    }

    int getFieldSizeX() {
        return field.length;
    }

    int getFieldSizeY() {
        return field[0].length;
    }

    public void resizeField(int x, int y) {
        field = new boolean[x][y];
        countField = new int[x][y];
        recount();
        repaint();
    }

    void drawField(Graphics graphics) {
        calcCord();
        for (int i = 0; i < field.length; ++i) {
            for (int j = 0; j < field[0].length; ++j) {
                if (field[i][j]) {
                    graphics.setColor(colorScheme.getDarkColor());
                } else {
                    graphics.setColor(colorScheme.getBrightColor());
                }
                graphics.fillRect(fieldX + i * cellSize, fieldY + j * cellSize, cellSize, cellSize);
            }
        }
    }

    void restart() {
        for (int i = 0; i < field.length; ++i) {
            for (int j = 0; j < field[0].length; ++j) {
                field[i][j] = false;
                countField[i][j] = 0;
            }
        }
        repaint();
    }

    void random() {
        Random random = new Random();
        for (int i = 0; i < field.length; ++i) {
            for (int j = 0; j < field[0].length; ++j) {
                field[i][j] = random.nextBoolean();
            }
        }
        recount();
        repaint();
    }

    void recount() {
        boolean[][] copyField = new boolean[field.length + 2][field[0].length + 2];
        for (int i = 0; i < field.length + 2; ++i) {
            for (int j = 0; j < field[0].length + 2; ++j) {
                if (((i == 0) || (i == field.length + 1)) || ((j == 0) || (j == field[0].length + 1))) {
                    copyField[i][j] = false;
                } else {
                    copyField[i][j] = field[i - 1][j - 1];
                    countField[i - 1][j - 1] = 0;
                }
            }
        }
        for (int i = 0; i < field.length; ++i) {
            for (int j = 0; j < field[0].length; ++j) {
                for (int a = 0; a < 3; ++a) {
                    for (int b = 0; b < 3; ++b) {
                        if (copyField[i + a][j + b]) {
                            ++countField[i][j];
                        }
                    }
                }
            }
        }
    }

    void step() {
        boolean[][] copyField = new boolean[field.length][field[0].length];
        for (int i = 0; i < field.length; ++i) {
            System.arraycopy(field[i], 0, copyField[i], 0, field[0].length);
        }
        for (int i = 0; i < field.length; ++i) {
            for (int j = 0; j < field[0].length; ++j) {
                if (copyField[i][j]) {
                    if (!((countField[i][j] == 3) || (countField[i][j] == 4))) {
                        field[i][j] = false;
                    }
                } else {
                    if (countField[i][j] == 3) {
                        field[i][j] = true;
                    }
                }
            }
        }
        recount();
        repaint();
    }

    void drawBackground(Graphics graphics) {
        int w = getWidth();
        int h = getHeight();
        graphics.setColor(colorScheme.getMediumColor());
        graphics.fillRect(0, 0, w, h);
    }

    void drawBackButton(Graphics graphics) {
        graphics.drawImage(pageManager.getImageManager().getBackButton(), 0, 0, 100, 100, null);
    }

    void drawResizeButton(Graphics graphics) {
        graphics.drawImage(pageManager.getImageManager().getSettingsButton(), 0, 150, 100, 100, null);
    }

    void drawRestartButton(Graphics graphics) {
        graphics.drawImage(pageManager.getImageManager().getClearButton(), getWidth() - 100, 0, 100, 100, null);
    }

    void drawRandomButton(Graphics graphics) {
        graphics.drawImage(pageManager.getImageManager().getRandomButton(), getWidth() - 100, 300, 100, 100, null);
    }

    void drawStartStopButton(Graphics graphics) {
        if (isRunning) {
            graphics.drawImage(pageManager.getImageManager().getStopGameButton(), getWidth() - 100, 150, 100, 100, null);
        } else {
            graphics.drawImage(pageManager.getImageManager().getStartGameButton(), getWidth() - 100, 150, 100, 100, null);
        }
    }

    public void setDelay(int delay) {
        this.delay = delay;
        repaint();
    }

    void drawSlider(Graphics graphics, int num) {
        graphics.setColor(colorScheme.getDarkColor());
        graphics.fillRect(0, 275, 100, 350);
        graphics.setColor(colorScheme.getMediumColor());
        graphics.fillRect(10, 300 + ((num - 1) * 300) / 30 - 10, 80, 30);
        graphics.setColor(colorScheme.getBrightColor());
        char[] chars = ("Скорость: " + num).toCharArray();
        graphics.setFont(graphics.getFont().deriveFont(Font.PLAIN, 15));
        graphics.drawChars(chars, 0, chars.length, 0, 270);
    }

    void calcCord() {
        int w = (getWidth() - 200) / field.length;
        int h = getHeight() / field[0].length;
        cellSize = min(w, h);
        int fieldHeight = field[0].length * cellSize;
        int fieldWidth = field.length * cellSize;
        int cenX = getWidth() / 2;
        int cenY = getHeight() / 2;
        fieldX = cenX - fieldWidth / 2;
        fieldY = cenY - fieldHeight / 2;
    }
}
