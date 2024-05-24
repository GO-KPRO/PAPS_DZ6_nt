package bar.kv.SK;

import bar.kv.managers.PageManager;
import bar.kv.menu.ColorScheme;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

import static java.lang.Integer.min;
import static java.lang.Math.max;
import static java.lang.Thread.sleep;

public class Sudoku extends JComponent {
    public final int EASY = 0;
    public final int MEDIUM = 1;
    public final int HARD = 2;
    private final PageManager pageManager;
    private int helps;
    private ColorScheme colorScheme = new ColorScheme();
    private long startTime;
    private long curTime;
    private boolean isRunning;
    private SudokuCell[][] field;
    private int curX;
    private int curY;
    private int difficulty;
    private int lives;
    private int fieldX;
    private int fieldY;
    private int cellSize;

    public Sudoku(PageManager pageManager) {
        enableEvents(AWTEvent.MOUSE_EVENT_MASK);
        requestFocus();
        this.pageManager = pageManager;
        field = new SudokuCell[9][9];
        fieldCreate();
        difficulty = EASY;
    }

    public void setCurKeyEvent(KeyEvent curKeyEvent) {
        if ((curX >= 0) && (curX < field.length)) {
            if ((curY >= 0) && (curY < field[0].length)) {
                if (!field[curX][curY].isOpen()) {
                    int num = curKeyEvent.getKeyCode() - KeyEvent.VK_0;
                    if (num > 0) {
                        if (num <= 9) {
                            if (num == field[curX][curY].getNum()) {
                                field[curX][curY].setOpen(true);
                                pageManager.getSoundManager().click();
                            } else {
                                --lives;
                                pageManager.getSoundManager().damage();
                            }
                            if (isSolved(field)) {
                                winGame();
                            }
                            if (lives == 0) {
                                loseGame();
                            }
                            repaint();
                        }
                    }
                }
            }
        }
    }

    public void setDifficulty(int difficulty) {
        this.difficulty = difficulty;
    }

    public void setColorScheme(ColorScheme colorScheme) {
        this.colorScheme = colorScheme;
        repaint();
    }

    private void fieldCreate() {
        for (int i = 0; i < field.length; ++i) {
            for (int j = 0; j < field[0].length; ++j) {
                field[i][j] = new SudokuCell();
            }
        }
    }

    public void start() {
        field = new SudokuCell[9][9];
        fieldCreate();
        lives = 3;
        curX = -1;
        curY = -1;
        helps = 0;
        isRunning = true;
        random();
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
        repaint();
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
                    pageManager.paint(pageManager.getSudokuStartMenu());
                    pageManager.moving(pageManager.getSudoku());
                }
            }
            if ((x > 0) && (x < 100)) {
                if ((y > 300) && (y < 400)) {
                    help();
                    ++helps;
                }
            }
            for (int i = 0; i < field.length; ++i) {
                for (int j = 0; j < field[0].length; ++j) {
                    if ((x > fieldX + i * cellSize) && (x < fieldX + i * cellSize + cellSize)) {
                        if ((y > fieldY + j * cellSize) && (y < fieldY + j * cellSize + cellSize)) {
                            curX = i;
                            curY = j;
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
        drawCurCell(graphics);
        drawGrid(graphics);
        drawBackButton(graphics);
        drawHelpButton(graphics);
        drawRestartButton(graphics);
        drawTimer(graphics);
        drawLives(graphics);
    }

    void drawField(Graphics graphics) {
        for (int i = 0; i < field.length; ++i) {
            for (int j = 0; j < field[0].length; ++j) {
                graphics.setColor(colorScheme.getDarkColor());
                graphics.fillRect(fieldX + i * cellSize, fieldY + j * cellSize, cellSize, cellSize);
                if (field[i][j].isOpen()) {
                    drawNum(graphics, fieldX + i * cellSize, fieldY + j * cellSize, cellSize, field[i][j].getNum(), colorScheme.getBrightColor());
                }
            }
        }
    }

    void drawCurCell(Graphics graphics) {
        if ((curX >= 0) && (curX < field.length)) {
            if ((curY >= 0) && (curY < field[0].length)) {
                graphics.setColor(colorScheme.getBrightColor());
                graphics.fillRect(fieldX + curX * cellSize, fieldY + curY * cellSize, cellSize, cellSize);
                if (field[curX][curY].isOpen()) {
                    drawNum(graphics, fieldX + curX * cellSize, fieldY + curY * cellSize, cellSize, field[curX][curY].getNum(), colorScheme.getDarkColor());
                }
            }
        }
    }

    void drawGrid(Graphics graphics) {
        graphics.setColor(colorScheme.getMediumColor());
        for (int i = 1; i < field.length; ++i) {
            graphics.drawLine(fieldX + i * cellSize, fieldY, fieldX + i * cellSize, fieldY + field[0].length * cellSize);
            if (i % 3 == 0) {
                graphics.fillRect(fieldX + i * cellSize - 1, fieldY, 3, field[0].length * cellSize);
            }
        }
        for (int i = 1; i < field[0].length; ++i) {
            graphics.drawLine(fieldX, fieldY + i * cellSize, fieldX + field.length * cellSize, fieldY + i * cellSize);
            if (i % 3 == 0) {
                graphics.fillRect(fieldX, fieldY + i * cellSize - 1, field.length * cellSize, 3);
            }
        }
    }

    void loseGame() {
        isRunning = false;
        pageManager.getSoundManager().lose();
        try {
            sleep(100);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        pageManager.paint(pageManager.getSudokuLoseMenu());
        pageManager.moving(pageManager.getSudoku());
        start();
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
            pageManager.getSudokuRecordsTable().addScoreString(difficulty, curTime - startTime);
        }
        pageManager.paint(pageManager.getSudokuWinMenu());
        pageManager.moving(pageManager.getSudoku());
    }

    void random() {
        for (int i = 0; i < field.length; ++i) {
            for (int j = 0; j < field[0].length; ++j) {
                field[i][j] = new SudokuCell();
                field[i][j].setNum((j * 3 + j / 3 + i) % 9 + 1);
            }
        }

        ArrayList<Integer> shift = new ArrayList<Integer>();
        for (int i = 0; i < 3; ++i) {
            ArrayList<Integer> curShift = new ArrayList<Integer>();
            for (int j = 0; j < 3; ++j) {
                curShift.add(j + i * 3);
            }
            Collections.shuffle(curShift);
            shift.addAll(curShift);
        }
        field = reStandColumns(field, shift);

        shift = new ArrayList<Integer>();
        for (int i = 0; i < 3; ++i) {
            ArrayList<Integer> curShift = new ArrayList<Integer>();
            for (int j = 0; j < 3; ++j) {
                curShift.add(j + i * 3);
            }
            Collections.shuffle(curShift);
            shift.addAll(curShift);
        }
        field = reStandRows(field, shift);

        shift = new ArrayList<Integer>();
        for (int i = 0; i < 3; ++i) {
            shift.add(i);
        }
        Collections.shuffle(shift);
        field = reStandColumnsBlocs(field, shift);

        shift = new ArrayList<Integer>();
        for (int i = 0; i < 3; ++i) {
            shift.add(i);
        }
        Collections.shuffle(shift);
        field = reStandRowsBlocs(field, shift);

        field = transpose(field);

        field = randomMapping(field);

        ArrayList<Integer> list = new ArrayList<>();
        for (int i = 0; i < field.length * field[0].length; ++i) list.add(i);
        Collections.shuffle(list);
        for (int i = 0; i < 25 + (2 - difficulty) * 10; ++i) {
            field[list.get(i) / field[0].length][list.get(i) % field[0].length].setOpen(true);
        }
        if (!hasOneSolve(field)) {
            random();
        }
        repaint();
    }

    private SudokuCell[][] randomMapping(SudokuCell[][] sudokuCells) {
        SudokuCell[][] res = new SudokuCell[sudokuCells.length][sudokuCells[0].length];

        ArrayList<Integer> shift = new ArrayList<Integer>();
        for (int i = 0; i < sudokuCells.length; ++i) {
            shift.add(i);
        }
        Collections.shuffle(shift);

        for (int i = 0; i < sudokuCells.length; ++i) {
            for (int j = 0; j < sudokuCells[0].length; ++j) {
                res[i][j] = new SudokuCell();
                res[i][j].setNum(shift.get(sudokuCells[i][j].getNum() - 1) + 1);
            }
        }
        return res;
    }

    private SudokuCell[][] reStandRows(SudokuCell[][] sudokuCells, ArrayList<Integer> array) {
        SudokuCell[][] res = new SudokuCell[sudokuCells.length][sudokuCells[0].length];
        for (int i = 0; i < sudokuCells.length; ++i) {
            for (int j = 0; j < sudokuCells[0].length; ++j) {
                res[i][j] = sudokuCells[i][array.get(j)];
            }
        }
        return res;
    }

    private SudokuCell[][] reStandRowsBlocs(SudokuCell[][] sudokuCells, ArrayList<Integer> array) {
        SudokuCell[][] res = new SudokuCell[sudokuCells.length][sudokuCells[0].length];
        for (int i = 0; i < sudokuCells.length; ++i) {
            for (int j = 0; j < sudokuCells[0].length; ++j) {
                res[i][j] = sudokuCells[i][array.get(j / 3) * 3 + j % 3];
            }
        }
        return res;
    }

    private SudokuCell[][] reStandColumns(SudokuCell[][] sudokuCells, ArrayList<Integer> array) {
        SudokuCell[][] res = new SudokuCell[sudokuCells.length][sudokuCells[0].length];
        for (int i = 0; i < sudokuCells.length; ++i) {
            System.arraycopy(sudokuCells[array.get(i)], 0, res[i], 0, sudokuCells[0].length);
        }
        return res;
    }

    private SudokuCell[][] reStandColumnsBlocs(SudokuCell[][] sudokuCells, ArrayList<Integer> array) {
        SudokuCell[][] res = new SudokuCell[sudokuCells.length][sudokuCells[0].length];
        for (int i = 0; i < sudokuCells.length; ++i) {
            System.arraycopy(sudokuCells[array.get(i / 3) * 3 + i % 3], 0, res[i], 0, sudokuCells[0].length);
        }
        return res;
    }

    private SudokuCell[][] transpose(SudokuCell[][] sudokuCells) {
        SudokuCell[][] res = new SudokuCell[sudokuCells.length][sudokuCells[0].length];
        for (int i = 0; i < sudokuCells.length; ++i) {
            for (int j = 0; j < sudokuCells[0].length; ++j) {
                res[i][j] = sudokuCells[j][i];
            }
        }
        return res;
    }

    void help() {
        Random random = new Random();
        int x = random.nextInt(0, 9);
        int y = random.nextInt(0, 9);
        if (!field[x][y].isOpen()) {
            field[x][y].setOpen(true);
            curX = x;
            curY = y;
            repaint();
            if (isSolved(field)) {
                winGame();
            }
        } else {
            help();
        }
    }

    boolean isSolved(SudokuCell[][] sudokuCells) {
        for (int i = 0; i < sudokuCells.length; ++i) {
            for (int j = 0; j < sudokuCells[0].length; ++j) {
                if (!sudokuCells[i][j].isOpen()) {
                    return false;
                }
                for (int a = 0; a < sudokuCells.length; ++a) {
                    if ((a != i) && (sudokuCells[i][j].getNum() == sudokuCells[a][j].getNum())) {
                        return false;
                    }
                    if ((a != j) && (sudokuCells[i][j].getNum() == sudokuCells[i][a].getNum())) {
                        return false;
                    }
                }
                for (int a = (i / 3) * 3; a < (i / 3) * 3 + 3; ++a) {
                    for (int b = (j / 3) * 3; b < (j / 3) * 3 + 3; ++b) {
                        if ((a != i) && (b != j) && (sudokuCells[i][j].getNum() == sudokuCells[a][b].getNum())) {
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }

    boolean isSolved(Integer[][] mas) {
        for (int i = 0; i < mas.length; ++i) {
            for (int j = 0; j < mas[0].length; ++j) {
                for (int a = 0; a < mas.length; ++a) {
                    if ((a != i) && (mas[i][j] == mas[a][j])) {
                        return false;
                    }
                    if ((a != j) && (mas[i][j] == mas[i][a])) {
                        return false;
                    }
                }
                for (int a = (i / 3) * 3; a < (i / 3) * 3 + 3; ++a) {
                    for (int b = (j / 3) * 3; b < (j / 3) * 3 + 3; ++b) {
                        if ((a != i) && (b != j) && (mas[i][j] == mas[a][b])) {
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }

    boolean isValid(Integer[][] mas) {
        for (int i = 0; i < mas.length; ++i) {
            for (int j = 0; j < mas[0].length; ++j) {
                if (mas[i][j] != 0) {
                    for (int a = 0; a < mas.length; ++a) {
                        if ((a != i) && (mas[i][j] == mas[a][j])) {
                            return false;
                        }
                        if ((a != j) && (mas[i][j] == mas[i][a])) {
                            return false;
                        }
                    }
                    for (int a = (i / 3) * 3; a < (i / 3) * 3 + 3; ++a) {
                        for (int b = (j / 3) * 3; b < (j / 3) * 3 + 3; ++b) {
                            if ((a != i) && (b != j) && (mas[i][j] == mas[a][b])) {
                                return false;
                            }
                        }
                    }
                }
            }
        }
        return true;
    }

    int hasNull(Integer[][] mas) {
        for (int i = 0; i < mas.length; ++i) {
            for (int j = 0; j < mas[0].length; ++j) {
                if (mas[i][j] == 0) {
                    return (i * 9 + j);
                }
            }
        }
        return -1;
    }

    Integer[][] leftSolve(Integer[][] mas) {
        if (!isValid(mas)) {
            return null;
        }
        int pos = hasNull(mas);
        if (pos == -1) {
            return mas;
        }
        int posX = pos / 9;
        int posY = pos % 9;
        for (int i = 1; i <= 9; ++i) {
            mas[posX][posY] = i;
            Integer[][] res = leftSolve(mas);
            if ((res != null) && (isSolved(res))) {
                return res;
            }
        }
        mas[posX][posY] = 0;
        return null;
    }

    Integer[][] rightSolve(Integer[][] mas) {
        if (!isValid(mas)) {
            return null;
        }
        int pos = hasNull(mas);
        if (pos == -1) {
            return mas;
        }
        int posX = pos / 9;
        int posY = pos % 9;
        for (int i = 9; i > 0; --i) {
            mas[posX][posY] = i;
            Integer[][] res = rightSolve(mas);
            if ((res != null) && (isSolved(res))) {
                return res;
            }
        }
        mas[posX][posY] = 0;
        return null;
    }

    boolean hasOneSolve(SudokuCell[][] sudokuCells) {
        Integer[][] originalMas = new Integer[sudokuCells.length][sudokuCells[0].length];

        for (int i = 0; i < sudokuCells.length; ++i) {
            for (int j = 0; j < sudokuCells[0].length; ++j) {
                originalMas[i][j] = 0;
                if (sudokuCells[i][j].isOpen()) {
                    originalMas[i][j] = sudokuCells[i][j].getNum();
                }
            }
        }

        Integer[][] resL = leftSolve(originalMas);
        Integer[][] resR = rightSolve(originalMas);

        if (resL == null) {
            return false;
        }
        if (resR == null) {
            return false;
        }

        for (int i = 0; i < sudokuCells.length; ++i) {
            for (int j = 0; j < sudokuCells[0].length; ++j) {
                if (resL[i][j] != resR[i][j]) {
                    return false;
                }
            }
        }
        return true;
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

    void drawRestartButton(Graphics graphics) {
        graphics.drawImage(pageManager.getImageManager().getSettingsButton(), 0, 150, 100, 100, null);
    }

    void drawHelpButton(Graphics graphics) {
        graphics.drawImage(pageManager.getImageManager().getHelpButton(), 0, 300, 100, 100, null);
    }

    void drawTimer(Graphics graphics) {
        int w = getWidth();
        int h = getHeight();
        graphics.setColor(colorScheme.getDarkColor());
        graphics.fillRect(w - 113, 15, 100, 50);
        long time = max(0, (curTime - startTime) / 1000);
        DecimalFormat df = new DecimalFormat("00");
        String str = df.format(time / 60) + ':' + df.format(time % 60);
        graphics.setColor(colorScheme.getBrightColor());
        graphics.setFont(graphics.getFont().deriveFont(Font.PLAIN, 30));
        graphics.drawChars(str.toCharArray(), 0, str.length(), w - 100, 50);
    }

    void drawLives(Graphics graphics) {
        int w = getWidth();
        int h = getHeight();
        graphics.setColor(colorScheme.getDarkColor());
        graphics.fillRect(w - 113, 80, 100, 50);
        graphics.setColor(colorScheme.getBrightColor());
        for (int i = 1 + w - 113; i < w - 113 + 100 - (3 - lives) * 33; i += 33) {
            drawHeart(graphics, i, 80, 32, 50);
        }
    }

    public void drawHeart(Graphics graphics, int x, int y, int width, int height) {
        int heartSize = min(width, height);
        int xShift = (width - heartSize) / 2;
        int yShift = (height - heartSize) / 2;
        int pic = max(1, heartSize / 8);
        int[] xArr = {5, 5, 6, 6, 7, 7, 8, 8, 7, 7, 5, 5, 4, 4, 1, 1, 0, 0, 1, 1, 2, 2, 3, 3};
        int[] yArr = {8, 7, 7, 6, 6, 5, 5, 1, 1, 0, 0, 1, 1, 0, 0, 1, 1, 5, 5, 6, 6, 7, 7, 8};
        xArr = Arrays.stream(xArr).map(a -> a * pic + x + xShift).toArray();
        yArr = Arrays.stream(yArr).map(b -> b * pic + y + yShift).toArray();
        graphics.setColor(colorScheme.getBrightColor());
        graphics.fillPolygon(xArr, yArr, xArr.length);
    }

    void drawNum(Graphics graphics, int x, int y, int height, int num, Color color) {
        graphics.setColor(color);
        graphics.setFont(graphics.getFont().deriveFont(0, height));
        graphics.drawChars(Integer.toString(num).toCharArray(), 0, Integer.toString(num).length(), x + cellSize / 4, y + cellSize * 7 / 8);
    }

    void calcCord() {
        int w = (getWidth() - 200) / field.length;
        int h = (getHeight() - 100) / field[0].length;
        cellSize = min(w, h);
        int fieldHeight = field[0].length * cellSize;
        int fieldWidth = field.length * cellSize;
        int cenX = getWidth() / 2;
        int cenY = getHeight() / 2;
        fieldX = cenX - fieldWidth / 2;
        fieldY = cenY - fieldHeight / 2;
    }
}
