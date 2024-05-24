package bar.kv.menu;

import bar.kv.managers.PageManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;

public class StartMenu extends JComponent {

    private final PageManager pageManager;
    private ColorScheme colorScheme = new ColorScheme();

    public StartMenu(PageManager pageManager) {
        enableEvents(AWTEvent.MOUSE_EVENT_MASK);
        this.pageManager = pageManager;
    }

    @Override
    protected void processMouseEvent(MouseEvent mouseEvent) {
        int w = getWidth();
        int h = getHeight();
        super.processMouseEvent(mouseEvent);
        if ((mouseEvent.getID() == MouseEvent.MOUSE_PRESSED) && (mouseEvent.getButton() == MouseEvent.BUTTON1)) {
            int x = mouseEvent.getX();
            int y = mouseEvent.getY();
            pageManager.getSoundManager().click();
            if ((x > w / 2 - 115) && (x < w / 2 + 115)) {
                if ((y > h * 2 / 3 - 45) && (y < h * 2 / 3 + 45)) {
                    pageManager.moving(pageManager.getGameSelectionMenu());
                }
            }
            if ((x > w - 200) && (x < w - 100)) {
                if ((y > 0) && (y < 100)) {
                    pageManager.moving(pageManager.getColorSchemeMenu());
                }
            }
            if ((x > w - 300) && (x < w - 200)) {
                if ((y > 0) && (y < 100)) {
                    pageManager.moving(pageManager.getInformationPage());
                }
            }
            if ((x > w - 100) && (x < w)) {
                if ((y > 0) && (y < 100)) {
                    pageManager.moving(pageManager.getSettings());
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
        drawStartButton(graphics);
        drawColorSchemeButton(graphics);
        drawSettingsButton(graphics);
        drawInformationButton(graphics);
        drawName(graphics);
    }

    void drawInformationButton(Graphics graphics) {
        int w = getWidth();
        graphics.drawImage(pageManager.getImageManager().getInformationButton(), w - 300, 0, 100, 100, null);
    }

    void drawStartButton(Graphics graphics) {
        int w = getWidth();
        int h = getHeight();
        graphics.drawImage(pageManager.getImageManager().getStartButton(), w / 2 - 115, h * 2 / 3 - 45, 230, 90, null);
    }

    void drawSettingsButton(Graphics graphics) {
        int w = getWidth();
        graphics.drawImage(pageManager.getImageManager().getSettingsButton(), w - 100, 0, 100, 100, null);
    }

    void drawColorSchemeButton(Graphics graphics) {
        int w = getWidth();
        graphics.drawImage(pageManager.getImageManager().getColorSchemeButton(), w - 200, 0, 100, 100, null);
    }

    void drawBackground(Graphics graphics) {
        int w = getWidth();
        int h = getHeight();
        graphics.setColor(colorScheme.getMediumColor());
        graphics.fillRect(0, 0, w, h);
    }

    void drawName(Graphics graphics) {
        int w = getWidth();
        int h = getHeight();
        graphics.drawImage(pageManager.getImageManager().getName(), w / 2 - 380, h * 2 / 5 - 50, 760, 100, null);
    }
}
