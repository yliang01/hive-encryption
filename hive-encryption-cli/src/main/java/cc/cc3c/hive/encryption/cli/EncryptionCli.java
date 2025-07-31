package cc.cc3c.hive.encryption.cli;

import java.io.Console;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;

public class EncryptionCli {

    public static void main(String[] args) {
        if (args.length == 0) {
            printHelp();
            return;
        }

        String mode = args[0];
        String inputFile = null;
        String inputHex = null;
        String outputFile = null;
        String iv = null;
        String salt = null;

        for (int i = 1; i < args.length; i++) {
            switch (args[i]) {
                case "--input-file":
                case "-i":
                    inputFile = args[++i];
                    break;
                case "--input-hex":
                case "-x":
                    inputHex = args[++i];
                    break;
                case "--output-file":
                case "-o":
                    outputFile = args[++i];
                    break;
                case "--iv":
                case "-v":
                    iv = args[++i];
                    break;
                case "--salt":
                case "-s":
                    salt = args[++i];
                    break;
                default:
                    System.err.println("Unknown option: " + args[i]);
                    printHelp();
                    return;
            }

        }

        if (inputFile != null && inputHex != null) {
            System.err.println("Error: Specify only one input source.");
            return;
        }

        if (inputFile == null && inputHex == null) {
            System.err.println("Error: Input not specified.");
            return;
        }

        char[] password = readPassword("Enter password: ");

        try {
            byte[] inputData = inputFile != null ? readFile(inputFile) : hexToBytes(inputHex);
            byte[] result;

            if ("encrypt".equalsIgnoreCase(mode)) {
                result = encrypt(inputData, password, iv, salt);
            } else if ("decrypt".equalsIgnoreCase(mode)) {
                result = decrypt(inputData, password, iv, salt);
            } else {
                System.err.println("Unknown command: " + mode);
                printHelp();
                return;
            }

            if (outputFile != null) {
                Files.write(Paths.get(outputFile), result);
            } else {
                System.out.write(result);
            }

        } catch (IOException e) {
            System.err.println("I/O Error: " + e.getMessage());
        }
    }

    private static byte[] readFile(String path) throws IOException {
        return Files.readAllBytes(Paths.get(path));
    }

    private static char[] readPassword(String prompt) {
        Console console = System.console();
        if (console != null) {
            return console.readPassword(prompt);
        } else {
            System.out.print(prompt);
            return new Scanner(System.in).nextLine().toCharArray();
        }
    }

    private static void printHelp() {
        System.out.println("Usage:");
        System.out.println("  encrypt|decrypt [options]");
        System.out.println("Options:");
        System.out.println("  --input-file, -i   <file>       Input from file");
        System.out.println("  --input-hex,  -x   <hexstring>  Input from hex string");
        System.out.println("  --output-file, -o  <file>       Output to file (default: stdout)");
        System.out.println("  --iv,         -v   <hexstring>  Initialization vector (IV)");
        System.out.println("  --salt,       -s   <hexstring>  Salt for key derivation");
    }

    // 由你来实现这两个方法
    private static byte[] encrypt(byte[] data, char[] password, byte[] iv, byte[] salt) {
        throw new UnsupportedOperationException("encrypt() 未实现");
    }

    private static byte[] decrypt(byte[] data, char[] password, byte[] iv, byte[] salt) {
        throw new UnsupportedOperationException("decrypt() 未实现");
    }
}
