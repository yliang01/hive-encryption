package cc.cc3c.hive.encryption;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

@Data
@Validated
@Component
@ConfigurationProperties(prefix = "hive.encryption")
public class HiveEncryptionConfig {
    private String keyAlgorithm;
    private String cipherAlgorithm;
    private String salt;
    private String password;
}
