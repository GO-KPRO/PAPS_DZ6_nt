package bar.kv.MS;

import bar.kv.managers.PageManager;
import bar.kv.menu.ColorScheme;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;

public class MinesweeperWinMenu extends JComponent {
    private final PageManager pageManager;
    private ColorScheme colorScheme = new ColorScheme();

    public MinesweeperWinMenu(PageManager pageManager) {
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
                    pageManager.paint(pageManager.getMinesweeperStartMenu());
                    pageManager.moving(pageManager.getMinesweeper());
                }
                if ((y > h / 2 + 25) && (y < h / 2 + 75)) {
                    pageManager.remove(this);
                    pageManager.paint(pageManager.getMinesweeperRecordsTable());
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
        drawWin(graphics);
        drawRestartButton(graphics);
        drawRecordsButton(graphics);
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
        char[] chars = "Заново".toCharArray();
        graphics.setColor(colorScheme.getBrightColor());
        graphics.setFont(graphics.getFont().deriveFont(Font.PLAIN, 20));
        graphics.drawChars(chars, 0, chars.length, w / 2 - 35, h / 2 + 155);
    }

    void drawRecordsButton(Graphics graphics) {
        int w = getWidth();
        int h = getHeight();
        graphics.setColor(colorScheme.getDarkColor());
        graphics.fillRect(w / 2 - 100, h / 2 + 25, 200, 50);
        char[] chars = "Рекорды".toCharArray();
        graphics.setColor(colorScheme.getBrightColor());
        graphics.setFont(graphics.getFont().deriveFont(Font.PLAIN, 20));
        graphics.drawChars(chars, 0, chars.length, w / 2 - 40, h / 2 + 55);
    }

    void drawWin(Graphics graphics) {
        int w = getWidth();
        int h = getHeight();
        graphics.setColor(colorScheme.getDarkColor());
        graphics.setFont(graphics.getFont().deriveFont(Font.PLAIN, 50));
        graphics.drawChars("ПОБЕДА".toCharArray(), 0, "ПОБЕДА".length(), w / 2 - 105, h / 2 - 100);
    }
}
