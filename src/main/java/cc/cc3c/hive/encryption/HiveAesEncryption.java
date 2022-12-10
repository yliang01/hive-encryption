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
public class HiveAesEncryption {

    private Cipher encryptCipher;
    private Cipher decryptCipher;

    public HiveAesEncryption(String pwd, String iv, String salt) throws Exception {
        SecretKeySpec secretKey = new SecretKeySpec(createKey(pwd, salt), "AES");

        encryptCipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        encryptCipher.init(Cipher.ENCRYPT_MODE, secretKey, new IvParameterSpec(createIVParameter(iv, salt)));

        decryptCipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        decryptCipher.init(Cipher.DECRYPT_MODE, secretKey, new IvParameterSpec(createIVParameter(iv, salt)));
    }

    public String encrypt(String data) throws IOException {
        try (CipherInputStream cipherInputStream = new CipherInputStream(new ByteArrayInputStream(data.getBytes(StandardCharsets.UTF_8)), encryptCipher)) {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            IOUtils.copy(cipherInputStream, byteArrayOutputStream);
            return Hex.encodeHexString(byteArrayOutputStream.toByteArray());
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

    private byte[] createKey(String pwd, String salt) {
        return shaWithSaltX65536(DigestUtils.getSha256Digest(), 32, pwd, salt);
    }

    private byte[] createIVParameter(String iv, String salt) {
        return shaWithSaltX65536(DigestUtils.getMd5Digest(), 16, iv, salt);
    }

    private byte[] shaWithSaltX65536(MessageDigest messageDigest, int byteLength, String data, String salt) {
        byte[] buffer = new byte[byteLength * 2];
        byte[] saltBuffer = messageDigest.digest(salt.getBytes(StandardCharsets.UTF_8));
        byte[] shaBuffer = messageDigest.digest(data.getBytes(StandardCharsets.UTF_8));

        int total = 1 << 16;
        for (int i = 0; i < total; i++) {
            System.arraycopy(shaBuffer, 0, buffer, 0, byteLength);
            System.arraycopy(saltBuffer, 0, buffer, byteLength, byteLength);
            shaBuffer = messageDigest.digest(buffer);
        }
        return shaBuffer;
    }
}
