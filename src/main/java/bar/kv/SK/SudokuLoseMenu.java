package bar.kv.SK;

import bar.kv.managers.PageManager;
import bar.kv.menu.ColorScheme;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;

public class SudokuLoseMenu extends JComponent {
    private final PageManager pageManager;
    private ColorScheme colorScheme = new ColorScheme();

    public SudokuLoseMenu(PageManager pageManager) {
        enableEvents(AWTEvent.MOUSE_EVENT_MASK);
        this.pageManager = pageManager;
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
        drawLose(graphics);
        drawRestartButton(graphics);
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


    void drawRestartButton(Graphics graphics) {
        int w = getWidth();
        int h = getHeight();
        graphics.setColor(colorScheme.getDarkColor());
        graphics.fillRect(w / 2 - 100, h / 2 + 125, 200, 50);
        char[] chars = "Продолжить".toCharArray();
        graphics.setColor(colorScheme.getBrightColor());
        graphics.setFont(graphics.getFont().deriveFont(Font.PLAIN, 20));
        graphics.drawChars(chars, 0, chars.length, w / 2 - 60, h / 2 + 155);
    }

    void drawLose(Graphics graphics) {
        int w = getWidth();
        int h = getHeight();
        graphics.setColor(colorScheme.getDarkColor());
        graphics.setFont(graphics.getFont().deriveFont(Font.PLAIN, 50));
        graphics.drawChars("ПРОИГРАЛ".toCharArray(), 0, "ПРОИГРАЛ".length(), w / 2 - 130, h / 2);
    }
}

