package bar.kv.MS;

public class MinesweeperCell {
    private boolean isBomb;
    private boolean isOpen;
    private int nearBombs;
    private boolean isFlag;

    public MinesweeperCell() {
        isBomb = false;
        isOpen = false;
        isFlag = false;
        nearBombs = 0;
    }

    public boolean isFlag() {
        return isFlag;
    }

    public void setFlag(boolean flag) {
        isFlag = flag;
    }

    public boolean isBomb() {
        return isBomb;
    }

    public void setBomb(boolean bomb) {
        isBomb = bomb;
    }

    public boolean isOpen() {
        return isOpen;
    }

    public void setOpen(boolean open) {
        isOpen = open;
    }

    public int getNearBombs() {
        return nearBombs;
    }

    public void setNearBombs(int nearBombs) {
        this.nearBombs = nearBombs;
    }
}
