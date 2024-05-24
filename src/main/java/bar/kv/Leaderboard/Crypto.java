package bar.kv.Leaderboard;

import com.google.gson.Gson;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

public class Crypto {

    public void saveTable(String tableName, String content) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        Cipher cipher = Cipher.getInstance("AES");
        SecretKeySpec key = new SecretKeySpec("ABOBABEBRANICECO".getBytes(), "AES");
        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte[] bytes = cipher.doFinal(content.getBytes());
        File table = switch (tableName) {
            case "minesweeper" -> new File("src\\main\\resources\\krMinesweeperTable\\Table");
            case "sudoku" -> new File("src\\main\\resources\\krSudokuTable\\Table");
            default -> new File("");
        };
        if (!table.exists()) {
            try {
                Files.createFile(table.toPath());
            } catch (IOException e) {
                System.out.println("Can`t create file\n");
            }
        }
        Gson gson = new Gson();
        try {
            Files.writeString(table.toPath(), gson.toJson(bytes), StandardOpenOption.WRITE);
        } catch (IOException e) {
            System.out.println("Can`t write in file\n");
        }
    }

    public String getTable(String tableName) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        File table = switch (tableName) {
            case "minesweeper" -> new File("src\\main\\resources\\krMinesweeperTable\\Table");
            case "sudoku" -> new File("src\\main\\resources\\krSudokuTable\\Table");
            default -> new File("");
        };
        if (!table.exists()) {
            System.out.println("No results");
            return "";
        }

        List<String> strings;
        Gson gson = new Gson();

        try {
            strings = Files.readAllLines(table.toPath());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        byte[] bytes = gson.fromJson(strings.get(0), byte[].class);

        Cipher cipher = Cipher.getInstance("AES");
        SecretKeySpec key = new SecretKeySpec("ABOBABEBRANICECO".getBytes(), "AES");
        cipher.init(Cipher.DECRYPT_MODE, key);
        byte[] decryptedBytes = cipher.doFinal(bytes);
        String content = new String(decryptedBytes);
        return content;
    }
}
