package bar.kv.managers;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class DrawManager {
    private final JFrame window;
    private final PageManager pageManager;

    public DrawManager(PageManager pageManager) {
        this.pageManager = pageManager;
        window = new JFrame("Cell Games");
        Image img = new ImageIcon("src\\main\\resources\\krsimg\\Дерево.jpg ").getImage();
        window.setIconImage(img);
        window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        window.setLayout(new BorderLayout());
        window.setExtendedState(JFrame.MAXIMIZED_BOTH);
        window.setResizable(false);
        window.setVisible(true);
        window.setFocusable(true);
        window.setFocusTraversalKeysEnabled(false);
        window.addKeyListener(new MyKey());
        window.setFocusable(true);
    }

    public void addComponent(JComponent jComponent) {
        window.add(jComponent);
    }

    public void removeComponent(JComponent jComponent) {
        window.remove(jComponent);
    }

    public void repaint() {
        window.revalidate();
        window.repaint();
    }

    public class MyKey extends KeyAdapter implements ActionListener {

        @Override
        public void keyPressed(KeyEvent keyEvent) {
            pageManager.setCurKeyEvent(keyEvent);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            repaint();
        }
    }
}
