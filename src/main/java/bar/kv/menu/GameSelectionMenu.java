package bar.kv.menu;

import bar.kv.managers.PageManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;

public class GameSelectionMenu extends JComponent {
    private final int size = 3;
    private final PageManager pageManager;
    private ColorScheme colorScheme = new ColorScheme();

    public GameSelectionMenu(PageManager pageManager) {
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
            int curSize = 0;
            for (int j = h / 4; j < h; j += h / 4) {
                for (int i = w / 3; i < w; i += w / 3) {
                    if (curSize < size) {
                        if ((x > i - 150) && (x < i + 150)) {
                            if ((y > j - 75) && (y < j + 75)) {
                                startGame(curSize);
                            }
                        }
                        ++curSize;
                    }
                }
            }
        }
    }

    void startGame(int num) {
        switch (num) {
            case (0) -> {
                pageManager.paint(pageManager.getMinesweeperStartMenu());
                pageManager.moving(pageManager.getMinesweeper());
            }
            case (1) -> {
                pageManager.paint(pageManager.getSudokuStartMenu());
                pageManager.moving(pageManager.getSudoku());
            }
            case (2) -> pageManager.moving(pageManager.getGameOfLife());
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
        drawGamesCards(graphics);
        drawBackButton(graphics);
    }

    void drawMinesweeperCard(Graphics graphics, int x, int y) {
        graphics.drawImage(pageManager.getImageManager().getMinesweeper(), x - 150, y - 75, 300, 150, null);
        graphics.setColor(colorScheme.getMediumColor());
        graphics.fillRect(x - 145, y - 70, 70, 30);
        graphics.setColor(colorScheme.getDarkColor());
        graphics.setFont(graphics.getFont().deriveFont(Font.PLAIN, 20));
        graphics.drawString("Сапёр", x - 140, y - 50);
    }

    void drawSudokuCard(Graphics graphics, int x, int y) {
        graphics.drawImage(pageManager.getImageManager().getSudoku(), x - 150, y - 75, 300, 150, null);
        graphics.setColor(colorScheme.getMediumColor());
        graphics.fillRect(x - 145, y - 70, 80, 30);
        graphics.setColor(colorScheme.getDarkColor());
        graphics.setFont(graphics.getFont().deriveFont(Font.PLAIN, 20));
        graphics.drawString("Судоку", x - 140, y - 50);
    }

    void drawLifeCard(Graphics graphics, int x, int y) {
        graphics.drawImage(pageManager.getImageManager().getGameOfLife(), x - 150, y - 75, 300, 150, null);
        graphics.setColor(colorScheme.getMediumColor());
        graphics.fillRect(x - 145, y - 70, 120, 30);
        graphics.setColor(colorScheme.getDarkColor());
        graphics.setFont(graphics.getFont().deriveFont(Font.PLAIN, 20));
        graphics.drawString("Игра Жизнь", x - 140, y - 50);
    }

    void drawCard(Graphics graphics, int num, int x, int y) {
        switch (num) {
            case (0) -> drawMinesweeperCard(graphics, x, y);
            case (1) -> drawSudokuCard(graphics, x, y);
            case (2) -> drawLifeCard(graphics, x, y);
        }
    }

    void drawGamesCards(Graphics graphics) {
        int w = getWidth();
        int h = getHeight();
        int curSize = 0;
        graphics.setColor(colorScheme.getBrightColor());
        for (int j = h / 4; j < h; j += h / 4) {
            for (int i = w / 3; i < w; i += w / 3) {
                if (curSize < size) {
                    drawCard(graphics, curSize, i, j);
                    ++curSize;
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
        graphics.setColor(colorScheme.getDarkColor());
        graphics.drawImage(pageManager.getImageManager().getBackButton(), 0, 0, 100, 100, null);
    }

}
