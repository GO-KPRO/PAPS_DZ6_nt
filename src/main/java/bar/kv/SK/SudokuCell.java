package bar.kv.SK;

public class SudokuCell {
    private boolean isOpen;
    private int num;

    public SudokuCell() {
        isOpen = false;
    }

    public boolean isOpen() {
        return isOpen;
    }

    public void setOpen(boolean open) {
        isOpen = open;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }
}
