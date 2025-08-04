package cc.cc3c.hive.encryption;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.nio.charset.StandardCharsets;

@Configuration
@EnableConfigurationProperties(HiveEncryptionConfigProperties.class)
public class HiveEncryptionAutoConfiguration {

    @Bean
    public HiveEncryptionConfig hiveEncryptionConfig(HiveEncryptionConfigProperties configProperties) {
        return new HiveEncryptionConfig(
                configProperties.getSalt().getBytes(StandardCharsets.UTF_8),
                configProperties.getPassword().getBytes(StandardCharsets.UTF_8));
    }
}
