package bar.kv.MS;

import bar.kv.managers.PageManager;
import bar.kv.menu.ColorScheme;
import com.google.gson.Gson;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;

import static java.lang.Math.min;

public class MinesweeperRecordsTable extends JComponent {
    private final ArrayList<ScoreString> scores = new ArrayList<>();
    private final PageManager pageManager;
    private ColorScheme colorScheme = new ColorScheme();

    public MinesweeperRecordsTable(PageManager pageManager) {
        enableEvents(AWTEvent.MOUSE_EVENT_MASK);
        this.pageManager = pageManager;
        File table = new File("src\\main\\resources\\krMinesweeperTable\\Table.json");
        if (!table.exists()) {
            try {
                Files.createFile(table.toPath());
            } catch (IOException e) {
                System.out.println("Can`t create file\n");
            }
        }
        Gson gson = new Gson();
        try {
            java.util.List<String> strings = Files.readAllLines(table.toPath());
            for (String str : strings) {
                scores.add(gson.fromJson(str, ScoreString.class));
            }
        } catch (IOException e) {
            System.out.println("Can`t open table\n");
        }
        Collections.sort(scores);
        repaint();
    }

    public void clear() {
        scores.clear();
    }

    public void addScoreString(int bombsNum, int fieldSize, long time) {
        scores.add(new ScoreString(bombsNum, fieldSize, time));
        File table = new File("src\\main\\resources\\krMinesweeperTable\\Table.json");
        if (!table.exists()) {
            try {
                Files.createFile(table.toPath());
            } catch (IOException e) {
                System.out.println("Can`t create file\n");
            }
        }
        Gson gson = new Gson();
        try {
            Files.writeString(table.toPath(), gson.toJson(new ScoreString(bombsNum, fieldSize, time)) + "\n", StandardOpenOption.APPEND);
        } catch (IOException e) {
            System.out.println("Can`t write in file\n");
        }
        Collections.sort(scores);
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
                if ((y > h / 2 + 125) && (y < h / 2 + 175)) {
                    pageManager.remove(this);
                    pageManager.paint(pageManager.getMinesweeperStartMenu());
                    pageManager.moving(pageManager.getMinesweeper());
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
        drawTable(graphics);
        drawBackButton(graphics);
        drawResults(graphics);
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
        graphics.fillRect(w / 2 - 200, h / 2 - 200, 400, 400);
    }

    void drawBackButton(Graphics graphics) {
        int w = getWidth();
        int h = getHeight();
        graphics.setColor(colorScheme.getDarkColor());
        graphics.fillRect(w / 2 - 100, h / 2 + 125, 200, 50);
        char[] chars = "Назад".toCharArray();
        graphics.setColor(colorScheme.getBrightColor());
        graphics.setFont(graphics.getFont().deriveFont(Font.PLAIN, 20));
        graphics.drawChars(chars, 0, chars.length, w / 2 - 30, h / 2 + 155);
    }

    void drawTable(Graphics graphics) {
        drawGrid(graphics);
        drawLabel(graphics);
    }

    void drawGrid(Graphics graphics) {
        int w = getWidth();
        int h = getHeight();
        graphics.setColor(colorScheme.getDarkColor());
        graphics.drawLine(w / 2 - 67, h / 2 - 200, w / 2 - 67, h / 2 - 200 + 50);
        graphics.drawLine(w / 2 + 67, h / 2 - 200, w / 2 + 67, h / 2 - 200 + 50);
        for (int i = h / 2 - 200; i < h / 2 + 125; i += 50) {
            graphics.drawLine(w / 2 - 200, i, w / 2 + 200, i);
        }
    }

    void drawLabel(Graphics graphics) {
        int w = getWidth();
        int h = getHeight();
        graphics.setColor(colorScheme.getDarkColor());
        graphics.setFont(graphics.getFont().deriveFont(Font.PLAIN, 20));
        graphics.drawString("Мины", w / 2 - 175, h / 2 - 170);
        graphics.drawString("Поле", w / 2 - 40, h / 2 - 170);
        graphics.drawString("Время", w / 2 + 90, h / 2 - 170);
    }

    void drawResults(Graphics graphics) {
        int w = getWidth();
        int h = getHeight();
        graphics.setColor(colorScheme.getDarkColor());
        int curEl = 0;
        for (int i = h / 2 - 120; i < h / 2 - 120 + 50 * min(5, scores.size()); i += 50) {
            graphics.drawString(Integer.toString(scores.get(curEl).getMinesNum()), w / 2 - 175, i);
            graphics.drawString(Integer.toString(scores.get(curEl).getFieldSize()), w / 2 - 40, i);
            graphics.drawString(getTime(scores.get(curEl).getTime()), w / 2 + 90, i);
            ++curEl;
        }
    }

    private String getTime(long num) {
        long time = num / 1000;
        DecimalFormat df = new DecimalFormat("00");
        return df.format(time / 60) + ':' + df.format(time % 60);
    }

    private class ScoreString implements Comparable<ScoreString> {
        private final int minesNum;
        private final int fieldSize;
        private final long time;

        public ScoreString(int minesNum, int fieldSize, long time) {
            this.minesNum = minesNum;
            this.fieldSize = fieldSize;
            this.time = time;
        }

        public int getFieldSize() {
            return fieldSize;
        }

        public int getMinesNum() {
            return minesNum;
        }

        public long getTime() {
            return time;
        }

        @Override
        public int compareTo(ScoreString o) {
            int res = o.minesNum - this.minesNum;
            if (res != 0) {
                return res;
            }
            int size = this.fieldSize - o.fieldSize;
            if (size != 0) {
                return size;
            }
            return (int) (this.time - o.time);
        }
    }
}
