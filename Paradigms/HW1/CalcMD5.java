import java.io.*;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;

public class CalcMD5 {
    public static void main(String[] args) throws Exception {
        try {
            Scanner scanner = new Scanner(new FileInputStream(args[0]), "UTF-8");
            while (scanner.hasNextLine()) {
                String fileName = scanner.nextLine();
                String string = new String(Files.readAllBytes(Paths.get(fileName)), ("UTF-8"));

                MessageDigest md5 = MessageDigest.getInstance("MD5");
                md5.update(StandardCharsets.UTF_8.encode(string));
                String out = String.format("%032x", new BigInteger(1, md5.digest()));
                System.out.println(out.toUpperCase());
            }
        } catch (IOException ioException) {
            System.err.println("Exception (No such file) " + ioException.getMessage());
        } catch (NoSuchAlgorithmException noSuchAlgorithmException) {
            System.err.println("Exception (No such algorithm) " + noSuchAlgorithmException.getMessage());
        } catch (SecurityException securityException) {
            System.err.println("Exception (You haven't permition to this file) " + securityException.getMessage());
        } catch (InvalidPathException invalidPathExciption) {
            System.err.println("Exception (Invalid path to file) " + invalidPathExciption.getMessage());
        } catch (OutOfMemoryError outOfMemoryExeption) {
            System.err.println("Exception (JVM can't allocate an object) " + outOfMemoryExeption.getMessage());
        }
    }
}