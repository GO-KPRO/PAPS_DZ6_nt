package bar.kv.SK;

import bar.kv.Leaderboard.LeaderboardManager;
import bar.kv.managers.PageManager;
import bar.kv.menu.ColorScheme;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static java.lang.Math.min;

public class SudokuRecordsTable extends JComponent {

    private ArrayList<LeaderboardManager.ScoreString> scores = new ArrayList<>();
    private final PageManager pageManager;
    private ColorScheme colorScheme = new ColorScheme();

    public SudokuRecordsTable(PageManager pageManager) {
        enableEvents(AWTEvent.MOUSE_EVENT_MASK);
        this.pageManager = pageManager;
        scores = new ArrayList<LeaderboardManager.ScoreString>(List.of(this.pageManager.getLeaderboardManager().getScores("sudoku")));
        Collections.sort(scores);
        repaint();
    }

    public void clear() {
        scores.clear();
    }

    public void addScoreString(int difficulty, long time) {
        scores.add(this.pageManager.getLeaderboardManager().setScore(difficulty, 81, time, "sudoku"));
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
                    pageManager.paint(pageManager.getSudokuStartMenu());
                    pageManager.moving(pageManager.getSudoku());
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
        graphics.drawLine(w / 2, h / 2 - 200, w / 2, h / 2 - 200 + 50);
        for (int i = h / 2 - 200; i < h / 2 + 125; i += 50) {
            graphics.drawLine(w / 2 - 200, i, w / 2 + 200, i);
        }
    }

    void drawLabel(Graphics graphics) {
        int w = getWidth();
        int h = getHeight();
        graphics.setColor(colorScheme.getDarkColor());
        graphics.setFont(graphics.getFont().deriveFont(Font.PLAIN, 20));
        graphics.drawString("Сложность", w / 2 - 150, h / 2 - 170);
        graphics.drawString("Время", w / 2 + 50, h / 2 - 170);
    }

    void drawResults(Graphics graphics) {
        int w = getWidth();
        int h = getHeight();
        graphics.setColor(colorScheme.getDarkColor());
        graphics.drawLine(w / 2, h / 2 - 200, w / 2, h / 2 - 200 + 50);
        int curEl = 0;
        for (int i = h / 2 - 120; i < h / 2 - 120 + 50 * min(5, scores.size()); i += 50) {
            graphics.drawString(getDifficulty(scores.get(curEl).getDifficulty()), w / 2 - 150, i);
            graphics.drawString(getTime(scores.get(curEl).getTime()), w / 2 + 50, i);
            ++curEl;
        }
    }

    private String getDifficulty(int num) {
        return switch (num) {
            case 1 -> "Средне";
            case 2 -> "Сложно";
            default -> "Легко";
        };
    }

    private String getTime(long num) {
        long time = num / 1000;
        DecimalFormat df = new DecimalFormat("00");
        String str = df.format(time / 60) + ':' + df.format(time % 60);
        return str;
    }
}
