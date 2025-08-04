package cc.cc3c.hive.encryption;

import lombok.Getter;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.IOUtils;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

@Getter
public class HiveEncryption {

    private Cipher encryptCipher;
    private Cipher decryptCipher;

    private final HiveEncryptionConfig encryptionConfig;

    public HiveEncryption(HiveEncryptionConfig encryptionConfig, byte[] iv) throws Exception {
        this.encryptionConfig = encryptionConfig;
        initCipher(encryptionConfig.getPassword(), iv);
    }

    private void initCipher(byte[] pwd, byte[] iv) throws Exception {
        SecretKeySpec secretKey = new SecretKeySpec(createKey(pwd), encryptionConfig.getKeyAlgorithm());

        encryptCipher = Cipher.getInstance(encryptionConfig.getCipherAlgorithm());
        encryptCipher.init(Cipher.ENCRYPT_MODE, secretKey, new IvParameterSpec(createIVParameter(iv)));

        decryptCipher = Cipher.getInstance(encryptionConfig.getCipherAlgorithm());
        decryptCipher.init(Cipher.DECRYPT_MODE, secretKey, new IvParameterSpec(createIVParameter(iv)));
    }

    public void encryptStream(InputStream inputStream, OutputStream outputStream) throws IOException {
        try (inputStream; CipherOutputStream cipherOutputStream = new CipherOutputStream(outputStream, encryptCipher)) {
            IOUtils.copy(inputStream, cipherOutputStream);
        }
    }

    public String encryptToHex(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        encryptStream(inputStream, byteArrayOutputStream);
        return Hex.encodeHexString(byteArrayOutputStream.toByteArray()).toUpperCase();
    }

    public void decryptStream(InputStream inputStream, OutputStream outputStream) throws Exception {
        try (CipherInputStream cipherInputStream = new CipherInputStream(inputStream, decryptCipher); outputStream) {
            IOUtils.copy(cipherInputStream, outputStream);
        }
    }

    public String decryptToString(InputStream inputStream) throws Exception {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        decryptStream(inputStream, byteArrayOutputStream);
        return byteArrayOutputStream.toString(StandardCharsets.UTF_8);
    }

    private byte[] createKey(byte[] pwd) {
        return shaWithSaltX65536(DigestUtils.getSha256Digest(), 32, pwd);
    }

    private byte[] createIVParameter(byte[] iv) {
        return shaWithSaltX65536(DigestUtils.getMd5Digest(), 16, iv);
    }

    private byte[] shaWithSaltX65536(MessageDigest messageDigest, int byteLength, byte[] data) {
        byte[] buffer = new byte[byteLength * 2];
        byte[] salt = messageDigest.digest(encryptionConfig.getSalt());
        byte[] sha = messageDigest.digest(data);

        int total = 1 << 16;
        for (int i = 0; i < total; i++) {
            System.arraycopy(sha, 0, buffer, 0, byteLength);
            System.arraycopy(salt, 0, buffer, byteLength, byteLength);
            sha = messageDigest.digest(buffer);
        }
        return sha;
    }
}
