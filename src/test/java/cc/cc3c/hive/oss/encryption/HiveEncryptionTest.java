package cc.cc3c.hive.oss.encryption;

import cc.cc3c.hive.encryption.HiveEncryption;
import cc.cc3c.hive.encryption.HiveEncryptionConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.Test;

@Slf4j
@SpringBootTest
public class HiveEncryptionTest extends AbstractTestNGSpringContextTests {

    @Autowired
    HiveEncryptionConfig hiveEncryptionConfig;

    @Test
    public void encrypt() throws Exception {
        HiveEncryption hiveEncryption = new HiveEncryption(hiveEncryptionConfig, "password");
        String encrypted = hiveEncryption.encrypt("data");
        Assert.assertEquals(encrypted, "558BAAD2F5D01213ADAAC0D5634D4992");
    }

    @Test
    public void decrypt() throws Exception {
        HiveEncryption hiveEncryption = new HiveEncryption(hiveEncryptionConfig, "password");
        String decrypted = hiveEncryption.decrypt("558BAAD2F5D01213ADAAC0D5634D4992");
        Assert.assertEquals(decrypted, "data");
    }
}
