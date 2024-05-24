package bar.kv.Leaderboard;

import bar.kv.managers.PageManager;
import com.google.gson.Gson;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import static java.util.Arrays.copyOf;

public class LeaderboardManager {

    private final PageManager pageManager;

    public LeaderboardManager(PageManager pageManager) {
        this.pageManager = pageManager;
    }

    public ScoreString[] getScores(String tableName) {
        Gson gson = new Gson();
        String str;
        try {
            str = pageManager.getCrypto().getTable(tableName);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException |
                 BadPaddingException e) {
            throw new RuntimeException(e);
        }
        ScoreString[] scores;
        scores = gson.fromJson(str, ScoreString[].class);
        if (scores == null) {
            return new ScoreString[0];
        }
        return scores;
    }

    public ScoreString setScore(int bombsNum, int fieldSize, long time, String tableName) {
        ScoreString[] scores = getScores(tableName);

        ScoreString newScore = new ScoreString(bombsNum, fieldSize, time);

        int oldLen = scores.length;

        ScoreString[] newScores = copyOf(scores, oldLen + 1);

        newScores[oldLen] = newScore;

        Gson gson = new Gson();

        try {
            this.pageManager.getCrypto().saveTable(tableName, gson.toJson(newScores));
        } catch (NoSuchPaddingException | NoSuchAlgorithmException | InvalidKeyException | IllegalBlockSizeException |
                 BadPaddingException e) {
            throw new RuntimeException(e);
        }
        return newScore;
    }

    public static class ScoreString implements Comparable<ScoreString> {
        private final int difficulty;
        private final int fieldSize;
        private final long time;

        public ScoreString(int minesNum, int fieldSize, long time) {
            this.difficulty = minesNum;
            this.fieldSize = fieldSize;
            this.time = time;
        }

        public int getFieldSize() {
            return fieldSize;
        }

        public int getDifficulty() {
            return difficulty;
        }

        public long getTime() {
            return time;
        }

        @Override
        public int compareTo(ScoreString o) {
            int res = o.difficulty - this.difficulty;
            if (res != 0) {
                return res;
            }
            int size = this.fieldSize - o.fieldSize;
            if (size != 0) {
                return size;
            }
            return (int) (this.time - o.time);
        }
    }
}
