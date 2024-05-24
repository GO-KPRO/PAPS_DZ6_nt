package bar.kv.SK;

import bar.kv.managers.PageManager;
import bar.kv.menu.ColorScheme;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;

public class SudokuStartMenu extends JComponent {

    private final PageManager pageManager;
    private final Sudoku sudoku;
    private ColorScheme colorScheme = new ColorScheme();

    public SudokuStartMenu(PageManager pageManager, Sudoku sudoku) {
        enableEvents(AWTEvent.MOUSE_EVENT_MASK);
        this.pageManager = pageManager;
        this.sudoku = sudoku;
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
                if ((y > h / 2 - 100) && (y < h / 2 - 50)) {
                    pageManager.remove(this);
                    sudoku.setDifficulty(sudoku.EASY);
                    sudoku.start();
                }
                if ((y > h / 2) && (y < h / 2 + 50)) {
                    pageManager.remove(this);
                    sudoku.setDifficulty(sudoku.MEDIUM);
                    sudoku.start();
                }
                if ((y > h / 2 + 100) && (y < h / 2 + 150)) {
                    pageManager.remove(this);
                    sudoku.setDifficulty(sudoku.HARD);
                    sudoku.start();
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
        drawStartEasyButton(graphics);
        drawStartMediumButton(graphics);
        drawStartHardButton(graphics);
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
        graphics.fillRect(w / 2 - 150, h / 2 - 150, 300, 350);
    }

    void drawStartEasyButton(Graphics graphics) {
        int w = getWidth();
        int h = getHeight();
        graphics.setColor(colorScheme.getDarkColor());
        graphics.fillRect(w / 2 - 100, h / 2 - 100, 200, 50);
        char[] chars = "Легко".toCharArray();
        graphics.setColor(colorScheme.getBrightColor());
        graphics.setFont(graphics.getFont().deriveFont(Font.PLAIN, 20));
        graphics.drawChars(chars, 0, chars.length, w / 2 - 30, h / 2 - 65);
    }

    void drawStartMediumButton(Graphics graphics) {
        int w = getWidth();
        int h = getHeight();
        graphics.setColor(colorScheme.getDarkColor());
        graphics.fillRect(w / 2 - 100, h / 2, 200, 50);
        char[] chars = "Средне".toCharArray();
        graphics.setColor(colorScheme.getBrightColor());
        graphics.setFont(graphics.getFont().deriveFont(Font.PLAIN, 20));
        graphics.drawChars(chars, 0, chars.length, w / 2 - 33, h / 2 + 30);
    }

    void drawStartHardButton(Graphics graphics) {
        int w = getWidth();
        int h = getHeight();
        graphics.setColor(colorScheme.getDarkColor());
        graphics.fillRect(w / 2 - 100, h / 2 + 100, 200, 50);
        char[] chars = "Сложно".toCharArray();
        graphics.setColor(colorScheme.getBrightColor());
        graphics.setFont(graphics.getFont().deriveFont(Font.PLAIN, 20));
        graphics.drawChars(chars, 0, chars.length, w / 2 - 33, h / 2 + 130);
    }
}
