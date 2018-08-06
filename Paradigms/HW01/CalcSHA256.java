import java.io.*;
import java.nio.file.*;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;

public class CalcSHA256 {
    public static void main(String[] args) throws Exception {
        try {
            Scanner scanner = new Scanner(new File(args[0]), "UTF-8");
            try {
                while (scanner.hasNextLine()) {
                    String fileName = scanner.nextLine();
                    byte[] data = Files.readAllBytes(Paths.get(fileName));
                    MessageDigest md5 = MessageDigest.getInstance("SHA-256");
                    System.out.format("%064X%n", new BigInteger(1, md5.digest(data)));
                }
            } finally {
                scanner.close();
            }
        } catch (IOException e) {
            System.err.println("Exception (No such file) " + e.getMessage());
        } catch (NoSuchAlgorithmException e) {
            System.err.println("Exception (No such algorithm) " + e.getMessage());
        }
    }
}