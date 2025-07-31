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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

@Getter
public class HiveEncryption {

    private Cipher encryptCipher;
    private Cipher decryptCipher;

    private final HiveEncryptionConfig encryptionConfig;

    public HiveEncryption(HiveEncryptionConfig encryptionConfig, String iv) throws Exception {
        this.encryptionConfig = encryptionConfig;
        initCipher(encryptionConfig.getPassword(), iv);
    }

    private void initCipher(String pwd, String iv) throws Exception {
        SecretKeySpec secretKey = new SecretKeySpec(createKey(pwd), encryptionConfig.getKeyAlgorithm());

        encryptCipher = Cipher.getInstance(encryptionConfig.getCipherAlgorithm());
        encryptCipher.init(Cipher.ENCRYPT_MODE, secretKey, new IvParameterSpec(createIVParameter(iv)));

        decryptCipher = Cipher.getInstance(encryptionConfig.getCipherAlgorithm());
        decryptCipher.init(Cipher.DECRYPT_MODE, secretKey, new IvParameterSpec(createIVParameter(iv)));
    }

    public String encrypt(String data) throws IOException {
        try (CipherInputStream cipherInputStream = new CipherInputStream(new ByteArrayInputStream(data.getBytes(StandardCharsets.UTF_8)), encryptCipher)) {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            IOUtils.copy(cipherInputStream, byteArrayOutputStream);
            return Hex.encodeHexString(byteArrayOutputStream.toByteArray()).toUpperCase();
        }
    }

    public String decrypt(String data) throws Exception {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        CipherOutputStream cipherOutputStream = new CipherOutputStream(byteArrayOutputStream, decryptCipher);
        try (byteArrayOutputStream; cipherOutputStream) {
            cipherOutputStream.write(Hex.decodeHex(data));
        }
        return byteArrayOutputStream.toString(StandardCharsets.UTF_8);
    }

    private byte[] createKey(String pwd) {
        return shaWithSaltX65536(DigestUtils.getSha256Digest(), 32, pwd);
    }

    private byte[] createIVParameter(String iv) {
        return shaWithSaltX65536(DigestUtils.getMd5Digest(), 16, iv);
    }

    private byte[] shaWithSaltX65536(MessageDigest messageDigest, int byteLength, String data) {
        byte[] buffer = new byte[byteLength * 2];
        byte[] salt = messageDigest.digest(encryptionConfig.getSalt().getBytes(StandardCharsets.UTF_8));
        byte[] sha = messageDigest.digest(data.getBytes(StandardCharsets.UTF_8));

        int total = 1 << 16;
        for (int i = 0; i < total; i++) {
            System.arraycopy(sha, 0, buffer, 0, byteLength);
            System.arraycopy(salt, 0, buffer, byteLength, byteLength);
            sha = messageDigest.digest(buffer);
        }
        return sha;
    }
}
