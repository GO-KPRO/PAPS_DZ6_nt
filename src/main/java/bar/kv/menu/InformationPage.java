package bar.kv.menu;

import bar.kv.managers.PageManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;

public class InformationPage extends JComponent {

    private final PageManager pageManager;
    private final String version = "0.5";
    private final String text = "Клеточные игры - сборник простых игр, в которых игровое поле состоит из клеток. Приложение позволяет играть во все проекты в одном месте широкому кругу игроков. Каждая игра имеет свои настройки и темы оформления. Также приложение позволяет сохранять предыдущие рекорды.\n";
    private final String contact = "eabaranov_4@edu.hse.ru";
    private final String name = "Баранов Егор";
    private ColorScheme colorScheme = new ColorScheme();

    public InformationPage(PageManager pageManager) {
        enableEvents(AWTEvent.MOUSE_EVENT_MASK);
        this.pageManager = pageManager;
    }

    @Override
    protected void processMouseEvent(MouseEvent mouseEvent) {
        super.processMouseEvent(mouseEvent);
        if ((mouseEvent.getID() == MouseEvent.MOUSE_PRESSED) && (mouseEvent.getButton() == MouseEvent.BUTTON1)) {
            int x = mouseEvent.getX();
            int y = mouseEvent.getY();
            pageManager.getSoundManager().click();
            if ((x > 0) && (x < 100)) {
                if ((y > 0) && (y < 100)) {
                    pageManager.moving(pageManager.getStartMenu());
                }
            }

        }
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        drawBackground(graphics);
        drawBackButton(graphics);
        drawText(graphics);
        drawVersion(graphics);
        drawName(graphics);
        drawContact(graphics);
    }

    void drawBackground(Graphics graphics) {
        int w = getWidth();
        int h = getHeight();
        graphics.setColor(colorScheme.getDarkColor());
        graphics.fillRect(0, 0, w, h);
    }

    void drawBackButton(Graphics graphics) {
        graphics.setColor(colorScheme.getMediumColor());
        graphics.drawImage(pageManager.getImageManager().getBackButton(), 0, 0, 100, 100, null);
    }

    void drawText(Graphics graphics) {
        int len = 60;
        graphics.setColor(colorScheme.getBrightColor());
        graphics.setFont(graphics.getFont().deriveFont(Font.PLAIN, 30));
        int i;
        for (i = 0; i < text.length() / len; ++i) {
            String str = text.substring(i * len, i * len + len);
            if (str.charAt(0) == ' ') {
                str = str.substring(1);
            }
            if ((str.charAt(str.length() - 1) != ' ') && (text.charAt(i * len + len) != ' ')) {
                str += '-';
            }
            graphics.drawChars(str.toCharArray(), 0, str.length(), 200, 200 + i * 30);
        }
        graphics.drawChars(text.substring(i * len, text.length() - 1).toCharArray(), 0, text.length() - i * len - 1, 200, 200 + i * 30);
    }

    void drawVersion(Graphics graphics) {
        int w = getWidth();
        int h = getHeight();
        graphics.setColor(colorScheme.getBrightColor());
        graphics.setFont(graphics.getFont().deriveFont(Font.PLAIN, 30));
        graphics.drawChars(version.toCharArray(), 0, version.length(), w - 100, h - 50);
    }

    void drawName(Graphics graphics) {
        int h = getHeight();
        graphics.setColor(colorScheme.getBrightColor());
        graphics.setFont(graphics.getFont().deriveFont(Font.PLAIN, 30));
        graphics.drawChars(name.toCharArray(), 0, name.length(), 50, h - 60);
    }

    void drawContact(Graphics graphics) {
        int h = getHeight();
        graphics.setColor(colorScheme.getBrightColor());
        graphics.setFont(graphics.getFont().deriveFont(Font.PLAIN, 30));
        graphics.drawChars(contact.toCharArray(), 0, contact.length(), 50, h - 30);
    }

    public void setColorScheme(ColorScheme colorScheme) {
        this.colorScheme = colorScheme;
    }
}
