package bar.kv.managers;

import bar.kv.GOL.GameOfLife;
import bar.kv.GOL.GameOfLifeResizeMenu;
import bar.kv.Leaderboard.Crypto;
import bar.kv.Leaderboard.LeaderboardManager;
import bar.kv.MS.*;
import bar.kv.SK.*;
import bar.kv.menu.*;

import javax.swing.*;
import java.awt.event.KeyEvent;

public class PageManager {
    private final DrawManager drawManager;
    private final SoundManager soundManager;
    private final LeaderboardManager leaderboardManager;
    private final Crypto crypto;
    private final GameSelectionMenu gameSelectionMenu;
    private final StartMenu startMenu;
    private final GameOfLife gameOfLife;
    private final GameOfLifeResizeMenu gameOfLifeResizeMenu;
    private final ColorSchemeMenu colorSchemeMenu;
    private final InformationPage informationPage;
    private final Settings settings;
    private final Minesweeper minesweeper;
    private final MinesweeperStartMenu minesweeperStartMenu;
    private final MinesweeperLoseMenu minesweeperLoseMenu;
    private final MinesweeperWinMenu minesweeperWinMenu;
    private final MinesweeperRecordsTable minesweeperRecordsTable;
    private final Sudoku sudoku;
    private final SudokuStartMenu sudokuStartMenu;
    private final SudokuLoseMenu sudokuLoseMenu;
    private final SudokuWinMenu sudokuWinMenu;
    private final SudokuRecordsTable sudokuRecordsTable;
    private final ImageManager imageManager;
    private JComponent curPage;


    public PageManager() {
        imageManager = new ImageManager(this);
        drawManager = new DrawManager(this);
        leaderboardManager = new LeaderboardManager(this);
        crypto = new Crypto();
        gameSelectionMenu = new GameSelectionMenu(this);
        gameOfLife = new GameOfLife(this);
        gameOfLifeResizeMenu = new GameOfLifeResizeMenu(this, gameOfLife);
        colorSchemeMenu = new ColorSchemeMenu(this);
        informationPage = new InformationPage(this);
        settings = new Settings(this);
        minesweeper = new Minesweeper(this);
        minesweeperStartMenu = new MinesweeperStartMenu(this, minesweeper);
        minesweeperLoseMenu = new MinesweeperLoseMenu(this);
        minesweeperWinMenu = new MinesweeperWinMenu(this);
        minesweeperRecordsTable = new MinesweeperRecordsTable(this);
        sudoku = new Sudoku(this);
        sudokuStartMenu = new SudokuStartMenu(this, sudoku);
        sudokuLoseMenu = new SudokuLoseMenu(this);
        sudokuWinMenu = new SudokuWinMenu(this);
        sudokuRecordsTable = new SudokuRecordsTable(this);
        startMenu = new StartMenu(this);

        curPage = startMenu;
        drawManager.addComponent(curPage);
        drawManager.repaint();
        soundManager = new SoundManager(this);
    }

    public GameOfLife getGameOfLife() {
        return gameOfLife;
    }

    public MinesweeperLoseMenu getMinesweeperLoseMenu() {
        return minesweeperLoseMenu;
    }

    public ColorSchemeMenu getColorSchemeMenu() {
        return colorSchemeMenu;
    }

    public GameOfLifeResizeMenu getGameOfLifeResizeMenu() {
        return gameOfLifeResizeMenu;
    }

    public InformationPage getInformationPage() {
        return informationPage;
    }

    public SudokuRecordsTable getSudokuRecordsTable() {
        return sudokuRecordsTable;
    }

    public MinesweeperWinMenu getMinesweeperWinMenu() {
        return minesweeperWinMenu;
    }

    public MinesweeperRecordsTable getMinesweeperRecordsTable() {
        return minesweeperRecordsTable;
    }

    public SoundManager getSoundManager() {
        return soundManager;
    }

    public Settings getSettings() {
        return settings;
    }

    public Minesweeper getMinesweeper() {
        return minesweeper;
    }

    public MinesweeperStartMenu getMinesweeperStartMenu() {
        return minesweeperStartMenu;
    }

    public Sudoku getSudoku() {
        return sudoku;
    }

    public SudokuStartMenu getSudokuStartMenu() {
        return sudokuStartMenu;
    }

    public SudokuLoseMenu getSudokuLoseMenu() {
        return sudokuLoseMenu;
    }

    public SudokuWinMenu getSudokuWinMenu() {
        return sudokuWinMenu;
    }

    public void setCurKeyEvent(KeyEvent keyEvent) {
        sudoku.setCurKeyEvent(keyEvent);
    }

    public LeaderboardManager getLeaderboardManager() {
        return leaderboardManager;
    }

    public Crypto getCrypto() {
        return crypto;
    }

    public void updateColourScheme(ColorScheme colorScheme) {
        imageManager.setColorScheme(colorScheme);
        gameOfLife.setColorScheme(colorScheme);
        gameSelectionMenu.setColorScheme(colorScheme);
        gameOfLifeResizeMenu.setColorScheme(colorScheme);
        startMenu.setColorScheme(colorScheme);
        informationPage.setColorScheme(colorScheme);
        settings.setColorScheme(colorScheme);
        minesweeperStartMenu.setColorScheme(colorScheme);
        minesweeper.setColorScheme(colorScheme);
        minesweeperWinMenu.setColorScheme(colorScheme);
        minesweeperLoseMenu.setColorScheme(colorScheme);
        minesweeperRecordsTable.setColorScheme(colorScheme);
        sudokuStartMenu.setColorScheme(colorScheme);
        sudoku.setColorScheme(colorScheme);
        sudokuWinMenu.setColorScheme(colorScheme);
        sudokuLoseMenu.setColorScheme(colorScheme);
        sudokuRecordsTable.setColorScheme(colorScheme);
    }

    public ImageManager getImageManager() {
        return imageManager;
    }

    public GameSelectionMenu getGameSelectionMenu() {
        return gameSelectionMenu;
    }

    public StartMenu getStartMenu() {
        return startMenu;
    }

    public void moving(JComponent to) {
        drawManager.removeComponent(curPage);
        drawManager.addComponent(to);
        drawManager.repaint();
        curPage = to;
    }

    public void paint(JComponent jComponent) {
        drawManager.addComponent(jComponent);
        drawManager.repaint();
    }

    public void remove(JComponent jComponent) {
        drawManager.removeComponent(jComponent);
        drawManager.repaint();
    }
}
