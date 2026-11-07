package cc.cc3c.hive.encryption;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Data
@Validated
@ConfigurationProperties(prefix = "hive.encryption")
public class HiveEncryptionConfigProperties {
    @NotNull
    private String salt;
    @NotNull
    private String password;
}
