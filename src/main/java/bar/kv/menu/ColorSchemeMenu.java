package bar.kv.menu;

import bar.kv.managers.PageManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import static java.util.Collections.swap;

public class ColorSchemeMenu extends JComponent {
    private final ArrayList<ColorScheme> colorSchemes;

    private final PageManager pageManager;

    public ColorSchemeMenu(PageManager pageManager) {
        enableEvents(AWTEvent.MOUSE_EVENT_MASK);
        this.pageManager = pageManager;
        colorSchemes = new ArrayList<ColorScheme>();
        colorSchemes.add(new ColorScheme(new Color(20, 21, 27), new Color(210, 143, 32), new Color(112, 122, 142)));
        colorSchemes.add(new ColorScheme(new Color(29, 44, 59), new Color(220, 20, 60), new Color(110, 20, 60)));
        colorSchemes.add(new ColorScheme(new Color(20, 10, 48), new Color(223, 13, 33), new Color(254, 146, 55)));
        colorSchemes.add(new ColorScheme(new Color(113, 9, 170), new Color(255, 211, 0), new Color(159, 238, 0)));
        colorSchemes.add(new ColorScheme(new Color(0, 0, 0), new Color(255, 255, 255), new Color(128, 128, 128)));
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
            int cur = 0;
            for (int j = h / 3; j < h; j += h / 3) {
                for (int i = w / 4; i < w / 4 * 4; i += w / 4) {
                    if (cur < colorSchemes.size()) {
                        if ((x > i - 100) && (x < i + 100)) {
                            if ((y > j - 100) && (y < j + 100)) {
                                pageManager.updateColourScheme(colorSchemes.get(cur));
                                pageManager.moving(pageManager.getStartMenu());
                                swap(colorSchemes, cur, 0);
                                for (int a = 1; a < colorSchemes.size() - 1; ++a) {
                                    swap(colorSchemes, a, cur);
                                }
                            }
                        }
                    }
                    ++cur;
                }
            }
        }
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        drawBackground(graphics);
        drawColorSchemes(graphics);
    }

    void drawBackground(Graphics graphics) {
        int w = getWidth();
        int h = getHeight();
        graphics.setColor(Color.BLACK);
        graphics.fillRect(0, 0, w, h);
    }

    void drawColorScheme(Graphics graphics, int x, int y, ColorScheme colorScheme) {
        graphics.setColor(colorScheme.getBrightColor());
        graphics.fillRect(x, y, 200, 200);
        graphics.setColor(colorScheme.getDarkColor());
        graphics.fillPolygon(new int[]{x, x, x + 200}, new int[]{y, y + 200, y + 200}, 3);
        graphics.setColor(colorScheme.getMediumColor());
        graphics.fillPolygon(new int[]{x, x + 100, x + 100}, new int[]{y, y, y + 100}, 3);
    }

    void drawColorSchemes(Graphics graphics) {
        int w = getWidth();
        int h = getHeight();
        int cur = 0;
        for (int j = h / 3; j < h; j += h / 3) {
            for (int i = w / 4; i < w / 4 * 4; i += w / 4) {
                if (cur < colorSchemes.size()) {
                    drawColorScheme(graphics, i - 100, j - 100, colorSchemes.get(cur++));
                }
            }
        }
        repaint();
    }

}
