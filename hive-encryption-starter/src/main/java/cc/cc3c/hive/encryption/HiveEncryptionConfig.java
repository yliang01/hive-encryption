package cc.cc3c.hive.encryption;

import lombok.Getter;

@Getter
public class HiveEncryptionConfig {
    private final String keyAlgorithm = "AES";
    private final String cipherAlgorithm = "AES/CBC/PKCS5Padding";
    private final String salt;
    private final String password;

    public HiveEncryptionConfig(String salt, String password) {
        this.salt = salt;
        this.password = password;
    }
}