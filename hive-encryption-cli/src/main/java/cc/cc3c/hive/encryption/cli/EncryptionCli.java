package cc.cc3c.hive.encryption.cli;

import cc.cc3c.hive.encryption.HiveEncryption;
import cc.cc3c.hive.encryption.HiveEncryptionConfig;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.io.FileUtils;

import java.io.ByteArrayInputStream;
import java.io.Console;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Scanner;

public class EncryptionCli {

    public static void main(String[] args) {
        if (args.length == 0) {
            printHelp();
            return;
        }

        String mode = args[0];
        boolean encryptMode = "encrypt".equalsIgnoreCase(mode);
        boolean decryptMode = "decrypt".equalsIgnoreCase(mode);
        if (!encryptMode && !decryptMode) {
            System.out.println("Error: Unknown mode: " + mode);
            printHelp();
            return;
        }

        String inputFile = null;
        String inputText = null;
        String outputFile = null;
        byte[] password = null;
        byte[] iv = null;
        byte[] salt = null;

        try {
            for (int i = 1; i < args.length; i++) {
                switch (args[i]) {
                    case "--input-file":
                    case "-i":
                        inputFile = args[++i];
                        break;
                    case "--input":
                    case "-x":
                        inputText = args[++i];
                        break;
                    case "--output-file":
                    case "-o":
                        outputFile = args[++i];
                        break;
                    case "--iv":
                    case "-v":
                        iv = getBytes(args[++i]);
                        break;
                    case "--salt":
                    case "-s":
                        salt = getBytes(args[++i]);
                        break;
                    default:
                        System.out.println("Error: Unknown option: " + args[i]);
                        printHelp();
                        return;
                }
            }
        } catch (IndexOutOfBoundsException e) {
            System.out.println("Error: Missing value for the last option.");
            printHelp();
            return;
        }

        try (InputStream inputStream = checkInput(encryptMode, decryptMode, inputFile, inputText)) {
            if (inputFile != null && outputFile == null) {
                throw new IllegalArgumentException("Error: When using --input-file, --output-file must also be specified.");
            }
            password = readPassword("Enter password: ");
            if (iv == null) iv = password;
            if (salt == null) salt = password;

            HiveEncryption hiveEncryption = new HiveEncryption(new HiveEncryptionConfig(salt, password), iv);

            if (encryptMode) {
                if (outputFile != null) {
                    try (OutputStream outputStream = FileUtils.openOutputStream(Paths.get(outputFile).toFile())) {
                        hiveEncryption.encryptStream(inputStream, outputStream);
                    }
                } else {
                    String hex = hiveEncryption.encryptToHex(inputStream);
                    System.out.println(hex);
                }
            }
            if (decryptMode) {
                if (outputFile != null) {
                    try (OutputStream outputStream = FileUtils.openOutputStream(Paths.get(outputFile).toFile())) {
                        hiveEncryption.decryptStream(inputStream, outputStream);
                    }
                } else {
                    String hex = hiveEncryption.decryptToString(inputStream);
                    System.out.println(hex);
                }
            }
        } catch (Exception e) {
            System.out.println("failed: " + e.getMessage());
            printHelp();
        } finally {
            if (password != null) {
                Arrays.fill(password, (byte) '\0');
            }
            if (iv != null) {
                Arrays.fill(iv, (byte) '\0');
            }
            if (salt != null) {
                Arrays.fill(salt, (byte) '\0');
            }
        }
    }

    private static InputStream checkInput(boolean encryptMode, boolean decryptMode, String inputFile, String inputText) throws Exception {
        if (inputFile == null && inputText == null) {
            throw new IllegalArgumentException("Error: Specify one of --input or --input-file.");
        }
        if (inputFile != null && inputText != null) {
            throw new IllegalArgumentException("Error: Specify only one of --input or --input-file.");
        }
        if (encryptMode) {
            if (inputFile != null) {
                return FileUtils.openInputStream(Paths.get(inputFile).toFile());
            } else {
                return new ByteArrayInputStream(inputText.getBytes(StandardCharsets.UTF_8));
            }
        }
        if (decryptMode) {
            if (inputFile != null) {
                return FileUtils.openInputStream(Paths.get(inputFile).toFile());
            } else {
                if (!inputText.matches("(?i)^[0-9a-f]*$") || (inputText.length() % 2 != 0)) {
                    throw new IllegalArgumentException("Error: In decrypt mode, --input must be a valid hex string.");
                }
                return new ByteArrayInputStream(Hex.decodeHex(inputText));
            }
        }
        throw new IllegalArgumentException("invalid input");
    }

    private static byte[] getBytes(String data) {
        return data.getBytes(StandardCharsets.UTF_8);
    }

    private static byte[] readPassword(String prompt) {
        Console console = System.console();
        if (console != null) {
            char[] chars = console.readPassword(prompt);
            ByteBuffer byteBuffer = StandardCharsets.UTF_8.encode(CharBuffer.wrap(chars));
            byte[] bytes = Arrays.copyOfRange(byteBuffer.array(), byteBuffer.position(), byteBuffer.limit());
            Arrays.fill(chars, '\0');
            return bytes;
        } else {
            System.out.print(prompt);
            return new Scanner(System.in).nextLine().getBytes(StandardCharsets.UTF_8);
        }
    }

    private static void printHelp() {
        System.out.println("Usage:");
        System.out.println("  encrypt|decrypt [options]");
        System.out.println("Options:");
        System.out.println("  --input-file,  -i  <file>       Input file path");
        System.out.println("  --input,       -x  <string>     Plain string (encrypt) or hex string (decrypt)");
        System.out.println("  --output-file, -o  <file>       Output file (required if input is file and mode is encrypt)");
        System.out.println("  --iv,          -v  <string>     Initialization vector (optional, default: password)");
        System.out.println("  --salt,        -s  <string>     Salt for key derivation (optional, default: password)");
    }
}
