package bar.kv.managers;

import bar.kv.menu.ColorScheme;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ImageManager {

    private final PageManager pageManager;
    private ColorScheme colorScheme = new ColorScheme();

    private BufferedImage startButton;
    private BufferedImage backButton;
    private BufferedImage clearButton;
    private BufferedImage colorSchemeButton;
    private BufferedImage helpButton;
    private BufferedImage informationButton;
    private BufferedImage name;
    private BufferedImage randomButton;
    private BufferedImage settingsButton;
    private BufferedImage startGameButton;
    private BufferedImage stopGameButton;
    private BufferedImage gameOfLife;
    private BufferedImage minesweeper;
    private BufferedImage sudoku;

    public ImageManager(PageManager pageManager) {
        this.pageManager = pageManager;
        setColorScheme(this.colorScheme);
    }

    public void setColorScheme(ColorScheme colorScheme) {
        this.colorScheme = colorScheme;
        startButton = convertImage("src\\main\\resources\\krsimg\\StartButton.png");
        backButton = convertImage("src\\main\\resources\\krsimg\\BackButton.png");
        clearButton = convertImage("src\\main\\resources\\krsimg\\ClearButton.png");
        colorSchemeButton = convertImage("src\\main\\resources\\krsimg\\ColorSchemeButton.png");
        helpButton = convertImage("src\\main\\resources\\krsimg\\HelpButton.png");
        informationButton = convertImage("src\\main\\resources\\krsimg\\InformationButton.png");
        name = convertImage("src\\main\\resources\\krsimg\\Name.png");
        randomButton = convertImage("src\\main\\resources\\krsimg\\RandomButton.png");
        settingsButton = convertImage("src\\main\\resources\\krsimg\\SettingsButton.png");
        startGameButton = convertImage("src\\main\\resources\\krsimg\\StartGameButton.png");
        stopGameButton = convertImage("src\\main\\resources\\krsimg\\StopGameButton.png");
        gameOfLife = convertImage("src\\main\\resources\\krsimg\\life.png");
        minesweeper = convertImage("src\\main\\resources\\krsimg\\minesweeper.png");
        sudoku = convertImage("src\\main\\resources\\krsimg\\sudoku.png");
    }

    private BufferedImage convertImage(String path) {
        BufferedImage img = null;
        try {
            img = ImageIO.read(new File(path));
        } catch (IOException e) {
            System.out.printf("Can`t open image %s\n", path);
        }
        for (int i = 0; i < img.getWidth(); ++i) {
            for (int j = 0; j < img.getHeight(); ++j) {
                int clr = img.getRGB(i, j);
                if ((clr & 0xff000000) >> 24 != 0) {
                    if (clr == -16777216) {
                        img.setRGB(i, j, colorScheme.getDarkColor().getRGB());
                    } else if (clr == -1) {
                        img.setRGB(i, j, colorScheme.getBrightColor().getRGB());
                    }
                }
            }
        }
        return img;
    }

    private BufferedImage openImage(String path) {
        BufferedImage img = null;
        try {
            img = ImageIO.read(new File(path));
        } catch (IOException e) {
            System.out.printf("Can`t open image %s\n", path);
        }
        return img;
    }

    public BufferedImage getSudoku() {
        return sudoku;
    }

    public BufferedImage getGameOfLife() {
        return gameOfLife;
    }

    public BufferedImage getMinesweeper() {
        return minesweeper;
    }

    public BufferedImage getStartButton() {
        return startButton;
    }

    public BufferedImage getBackButton() {
        return backButton;
    }

    public BufferedImage getClearButton() {
        return clearButton;
    }

    public BufferedImage getColorSchemeButton() {
        return colorSchemeButton;
    }

    public BufferedImage getHelpButton() {
        return helpButton;
    }

    public BufferedImage getInformationButton() {
        return informationButton;
    }

    public BufferedImage getName() {
        return name;
    }

    public BufferedImage getRandomButton() {
        return randomButton;
    }

    public BufferedImage getSettingsButton() {
        return settingsButton;
    }

    public BufferedImage getStartGameButton() {
        return startGameButton;
    }

    public BufferedImage getStopGameButton() {
        return stopGameButton;
    }
}
