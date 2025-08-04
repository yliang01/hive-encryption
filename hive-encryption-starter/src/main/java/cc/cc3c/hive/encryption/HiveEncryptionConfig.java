package cc.cc3c.hive.encryption;

import lombok.Getter;

@Getter
public class HiveEncryptionConfig {
    private final String keyAlgorithm = "AES";
    private final String cipherAlgorithm = "AES/CBC/PKCS5Padding";
    private final byte[] salt;
    private final byte[] password;

    public HiveEncryptionConfig(byte[] salt, byte[] password) {
        this.salt = salt;
        this.password = password;
    }
}