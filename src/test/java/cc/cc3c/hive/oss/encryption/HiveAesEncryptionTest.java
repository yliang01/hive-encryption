package cc.cc3c.hive.oss.encryption;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

@Slf4j
public class HiveAesEncryptionTest {

    @Test
    public void encrypt() throws Exception {
        HiveAesEncryption hiveAesEncryption = new HiveAesEncryption("password", "password", "password");
        String encrypted = hiveAesEncryption.encrypt("data");
        log.info("encrypted data: {}", encrypted.toUpperCase());
        log.info("encrypted data length: {}", encrypted.length());
    }

    @Test
    public void decrypt() throws Exception {
        HiveAesEncryption hiveAesEncryption = new HiveAesEncryption("password", "password", "password");
        String decrypted = hiveAesEncryption.decrypt("558BAAD2F5D01213ADAAC0D5634D4992");
        log.info("decrypted data: {}", decrypted);
    }
}
