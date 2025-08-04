package cc.cc3c.hive.oss.encryption;

import cc.cc3c.hive.encryption.HiveEncryption;
import cc.cc3c.hive.encryption.HiveEncryptionAutoConfiguration;
import cc.cc3c.hive.encryption.HiveEncryptionConfig;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Hex;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;

@Slf4j
@SpringBootTest(classes = HiveEncryptionAutoConfiguration.class)
public class HiveEncryptionTest extends AbstractTestNGSpringContextTests {

    @Autowired
    HiveEncryptionConfig hiveEncryptionConfig;

    @Test
    public void encryptToHex() throws Exception {
        HiveEncryption hiveEncryption = new HiveEncryption(hiveEncryptionConfig, "password".getBytes(StandardCharsets.UTF_8));
        String encrypted = hiveEncryption.encryptToHex(new ByteArrayInputStream("data".getBytes(StandardCharsets.UTF_8)));
        Assert.assertEquals(encrypted, "558BAAD2F5D01213ADAAC0D5634D4992");
    }

    @Test
    public void decryptToString() throws Exception {
        HiveEncryption hiveEncryption = new HiveEncryption(hiveEncryptionConfig, "password".getBytes(StandardCharsets.UTF_8));
        String decrypted = hiveEncryption.decryptToString(new ByteArrayInputStream(Hex.decodeHex("558BAAD2F5D01213ADAAC0D5634D4992")));
        Assert.assertEquals(decrypted, "data");
    }
}
