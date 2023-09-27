package cc.cc3c.hive.oss.encryption;

import lombok.extern.slf4j.Slf4j;
import org.testng.Assert;
import org.testng.annotations.Test;

@Slf4j
public class HiveAesEncryptionTest {

    @Test
    public void encrypt() throws Exception {
        HiveAesEncryption hiveAesEncryption = new HiveAesEncryption("password", "password", "password");
        String encrypted = hiveAesEncryption.encrypt("data");
        Assert.assertEquals(encrypted, "558BAAD2F5D01213ADAAC0D5634D4992");
    }

    @Test
    public void decrypt() throws Exception {
        HiveAesEncryption hiveAesEncryption = new HiveAesEncryption("password", "password", "password");
        String decrypted = hiveAesEncryption.decrypt("558BAAD2F5D01213ADAAC0D5634D4992");
        Assert.assertEquals(decrypted, "data");
    }
}
