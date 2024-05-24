package bar.kv.MS;

import bar.kv.managers.PageManager;
import bar.kv.menu.ColorScheme;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;

import static java.lang.Integer.min;
import static java.lang.Math.max;
import static java.lang.Thread.sleep;

public class Minesweeper extends JComponent {

    private final PageManager pageManager;
    private ColorScheme colorScheme = new ColorScheme();
    private long startTime;
    private long curTime;
    private boolean isRunning;
    private boolean isMistake;
    private MinesweeperCell[][] field;
    private int fieldX;
    private int fieldY;
    private int bombsNum;
    private int cellSize;
    private int helps;

    public Minesweeper(PageManager pageManager) {
        enableEvents(AWTEvent.MOUSE_EVENT_MASK);
        this.pageManager = pageManager;
        field = new MinesweeperCell[50][50];
        fieldCreate();
        bombsNum = 50;
        isMistake = false;
        helps = 0;
    }

    public void setColorScheme(ColorScheme colorScheme) {
        this.colorScheme = colorScheme;
        repaint();
    }

    private void fieldCreate() {
        for (int i = 0; i < field.length; ++i) {
            for (int j = 0; j < field[0].length; ++j) {
                field[i][j] = new MinesweeperCell();
            }
        }
    }

    public void start(int x, int y) {
        field = new MinesweeperCell[x][y];
        fieldCreate();
        isRunning = true;
        isMistake = false;
        helps = 0;
        random();
        recount();
        startTime = System.currentTimeMillis();
        Thread myThready = new Thread(() -> {
            while (isRunning) {
                curTime = System.currentTimeMillis();
                repaint();
                try {
                    sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        myThready.start();
        calcCord();
    }

    public int getBombsNum() {
        return bombsNum;
    }

    public void setBombsNum(int bombsNum) {
        this.bombsNum = bombsNum;
    }

    @Override
    protected void processMouseEvent(MouseEvent mouseEvent) {
        super.processMouseEvent(mouseEvent);
        int x = mouseEvent.getX();
        int y = mouseEvent.getY();
        if ((mouseEvent.getID() == MouseEvent.MOUSE_PRESSED) && (mouseEvent.getButton() == MouseEvent.BUTTON1)) {
            pageManager.getSoundManager().click();
            if ((x > 0) && (x < 100)) {
                if ((y > 0) && (y < 100)) {
                    isRunning = false;
                    startTime = System.currentTimeMillis();
                    pageManager.moving(pageManager.getGameSelectionMenu());
                }
            }
            if ((x > 0) && (x < 100)) {
                if ((y > 150) && (y < 250)) {
                    pageManager.paint(pageManager.getMinesweeperStartMenu());
                    pageManager.moving(pageManager.getMinesweeper());
                }
            }
            if ((x > 0) && (x < 100)) {
                if ((y > 300) && (y < 400)) {
                    isMistake = !help();
                    if (!isMistake) {
                        ++helps;
                    }
                    repaint();
                }
            }
            for (int i = 0; i < field.length; ++i) {
                for (int j = 0; j < field[0].length; ++j) {
                    if ((x > fieldX + i * cellSize) && (x < fieldX + i * cellSize + cellSize)) {
                        if ((y > fieldY + j * cellSize) && (y < fieldY + j * cellSize + cellSize)) {
                            isMistake = false;
                            if (!field[i][j].isFlag()) {
                                open(i, j);
                            }
                            repaint();
                            if (isWin()) {
                                winGame();
                            }
                        }
                    }
                }
            }
        }
        if ((mouseEvent.getID() == MouseEvent.MOUSE_PRESSED) && (mouseEvent.getButton() == MouseEvent.BUTTON3)) {
            pageManager.getSoundManager().flag();
            for (int i = 0; i < field.length; ++i) {
                for (int j = 0; j < field[0].length; ++j) {
                    if ((x > fieldX + i * cellSize) && (x < fieldX + i * cellSize + cellSize)) {
                        if ((y > fieldY + j * cellSize) && (y < fieldY + j * cellSize + cellSize)) {
                            field[i][j].setFlag(!field[i][j].isFlag());
                            repaint();
                            if (isWin()) {
                                winGame();
                            }
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
        drawGrid(graphics);
        drawBackButton(graphics);
        drawResizeButton(graphics);
        drawHelpButton(graphics);
        drawTimer(graphics);
        drawMistake(graphics);
    }

    int getFieldSizeX() {
        return field.length;
    }

    int getFieldSizeY() {
        return field[0].length;
    }

    void drawField(Graphics graphics) {
        for (int i = 0; i < field.length; ++i) {
            for (int j = 0; j < field[0].length; ++j) {
                if (field[i][j].isOpen()) {
                    graphics.setColor(colorScheme.getBrightColor());
                    graphics.fillRect(fieldX + i * cellSize, fieldY + j * cellSize, cellSize, cellSize);
                    graphics.setColor(colorScheme.getDarkColor());
                    if (field[i][j].isBomb()) {
                        drawBomb(graphics, fieldX + i * cellSize, fieldY + j * cellSize, cellSize, cellSize);
                        loseGame();
                    } else {
                        drawNum(graphics, fieldX + i * cellSize, fieldY + j * cellSize, cellSize, field[i][j].getNearBombs());
                    }
                } else {
                    graphics.setColor(colorScheme.getDarkColor());
                    graphics.fillRect(fieldX + i * cellSize, fieldY + j * cellSize, cellSize, cellSize);
                    if (field[i][j].isFlag()) {
                        drawFlag(graphics, fieldX + i * cellSize, fieldY + j * cellSize, cellSize, cellSize);
                    }
                }
            }
        }
    }

    void drawGrid(Graphics graphics) {
        graphics.setColor(colorScheme.getMediumColor());
        for (int i = 1; i < field.length; ++i) {
            graphics.drawLine(fieldX + i * cellSize, fieldY, fieldX + i * cellSize, fieldY + field[0].length * cellSize);
        }
        for (int i = 1; i < field[0].length; ++i) {
            graphics.drawLine(fieldX, fieldY + i * cellSize, fieldX + field.length * cellSize, fieldY + i * cellSize);
        }
    }

    void loseGame() {
        isRunning = false;
        pageManager.getSoundManager().explosion();
        try {
            sleep(100);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        pageManager.paint(pageManager.getMinesweeperLoseMenu());
        pageManager.moving(pageManager.getMinesweeper());
        start(field.length, field[0].length);
        isRunning = false;
    }

    void winGame() {
        isRunning = false;
        pageManager.getSoundManager().win();
        try {
            sleep(100);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        if (helps == 0) {
            pageManager.getMinesweeperRecordsTable().addScoreString(bombsNum, field.length * field[0].length, curTime - startTime);
        }
        pageManager.paint(pageManager.getMinesweeperWinMenu());
        pageManager.moving(pageManager.getMinesweeper());
    }

    void random() {
        ArrayList<Integer> list = new ArrayList<>();
        for (int i = 0; i < field.length * field[0].length; ++i) list.add(i);
        Collections.shuffle(list);
        for (int i = 0; i < bombsNum; ++i) {
            field[list.get(i) / field[0].length][list.get(i) % field[0].length].setBomb(true);
        }
        recount();
        repaint();
    }

    boolean help() {
        for (int i = 0; i < field.length; ++i) {
            for (int j = 0; j < field[0].length; ++j) {
                if (field[i][j].isBomb()) {
                    if (!field[i][j].isFlag()) {
                        boolean flag = false;
                        for (int a = -1; a <= 1; ++a) {
                            for (int b = -1; b <= 1; ++b) {
                                if ((i + a >= 0) && (i + a < field.length)) {
                                    if ((j + b >= 0) && (j + b < field[0].length)) {
                                        if (field[i + a][j + b].isOpen()) {
                                            flag = true;
                                        }
                                    }
                                }
                            }
                        }
                        if (flag) {
                            field[i][j].setFlag(true);
                            repaint();
                            if (isWin()) {
                                winGame();
                            }
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    void recount() {
        for (int i = 0; i < field.length; ++i) {
            for (int j = 0; j < field[0].length; ++j) {
                int cur = 0;
                for (int a = -1; a <= 1; ++a) {
                    for (int b = -1; b <= 1; ++b) {
                        if ((i + a >= 0) && (i + a < field.length)) {
                            if ((j + b >= 0) && (j + b < field[0].length)) {
                                if (field[i + a][j + b].isBomb()) {
                                    ++cur;
                                }
                            }
                        }
                    }
                }
                field[i][j].setNearBombs(cur);
            }
        }
    }

    boolean isWin() {
        for (MinesweeperCell[] minesweeperCells : field) {
            for (int j = 0; j < field[0].length; ++j) {
                if ((!minesweeperCells[j].isBomb()) && (!minesweeperCells[j].isOpen())) {
                    return false;
                }
                if ((minesweeperCells[j].isBomb()) && (!minesweeperCells[j].isFlag())) {
                    return false;
                }
            }
        }
        return true;
    }

    void open(int i, int j) {
        if ((i >= 0) && (i < field.length)) {
            if ((j >= 0) && (j < field[0].length)) {
                if (!field[i][j].isOpen()) {
                    field[i][j].setOpen(true);
                    if (field[i][j].getNearBombs() == 0) {
                        open(i + 1, j);
                        open(i - 1, j);
                        open(i, j + 1);
                        open(i, j - 1);
                        open(i + 1, j + 1);
                        open(i - 1, j + 1);
                        open(i + 1, j - 1);
                        open(i - 1, j - 1);
                    }
                }

            }
        }

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

    void drawHelpButton(Graphics graphics) {
        graphics.drawImage(pageManager.getImageManager().getHelpButton(), 0, 300, 100, 100, null);
    }

    void drawMistake(Graphics graphics) {
        graphics.setColor(colorScheme.getDarkColor());
        if (isMistake) {
            graphics.drawLine(0, 300, 100, 400);
            graphics.drawLine(0, 400, 100, 300);
        }
    }

    void drawTimer(Graphics graphics) {
        int w = getWidth();
        graphics.setColor(colorScheme.getDarkColor());
        graphics.fillRect(w - 113, 15, 100, 50);
        long time = max(0, (curTime - startTime) / 1000);
        DecimalFormat df = new DecimalFormat("00");
        String str = df.format(time / 60) + ':' + df.format(time % 60);
        graphics.setColor(colorScheme.getBrightColor());
        graphics.setFont(graphics.getFont().deriveFont(Font.PLAIN, 30));
        graphics.drawChars(str.toCharArray(), 0, str.length(), w - 100, 50);
    }

    void drawBomb(Graphics graphics, int x, int y, int width, int height) {
        graphics.setColor(colorScheme.getDarkColor());
        graphics.fillOval(x, y, width, height);
        graphics.drawLine(x, y, x + width, y + height);
        graphics.drawLine(x, y + height, x + width, y);
    }

    void drawFlag(Graphics graphics, int x, int y, int width, int height) {
        graphics.setColor(colorScheme.getBrightColor());
        graphics.fillPolygon(new int[]{x + width / 2, x + width / 2, x + width}, new int[]{y, y + height / 2, y + height / 4}, 3);
        graphics.setColor(colorScheme.getMediumColor());
        graphics.drawLine(x + width / 2, y, x + width / 2, y + height);
    }

    void drawNum(Graphics graphics, int x, int y, int height, int num) {
        if (num != 0) {
            graphics.setColor(colorScheme.getDarkColor());
            graphics.setFont(graphics.getFont().deriveFont(Font.PLAIN, height));
            graphics.drawChars(Integer.toString(num).toCharArray(), 0, Integer.toString(num).length(), x + cellSize / 4, y + cellSize - 1);
        }
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
