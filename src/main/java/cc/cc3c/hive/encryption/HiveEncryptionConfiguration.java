package cc.cc3c.hive.encryption;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("encryption.properties")
@EnableConfigurationProperties
public class HiveEncryptionConfiguration {
    @Bean
    public HiveEncryptionConfig hiveEncryptionConfig() {
        return new HiveEncryptionConfig();
    }
}
