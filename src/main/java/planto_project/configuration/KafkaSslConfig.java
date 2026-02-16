package planto_project.configuration;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Base64;

@Configuration
@Slf4j
@Profile("!local")
public class KafkaSslConfig {

    @Value("${KAFKA_TRUSTSTORE_BASE64:}")
    private String truststoreBase64;

    @Value("${KAFKA_KEYSTORE_BASE64:}")
    private String keystoreBase64;

    @Bean
    public KafkaCertificates kafkaCertificates() throws IOException {
        log.info("Initializing Kafka SSL certificates...");

        if (truststoreBase64.isEmpty() || keystoreBase64.isEmpty()) {
            log.info("Using local certificate files (development mode)");
            return new KafkaCertificates(
                    "src/main/resources/kafka-certs/client.truststore.jks",
                    "src/main/resources/kafka-certs/client.keystore.p12"
            );
        }

        byte[] truststoreBytes = Base64.getDecoder().decode(truststoreBase64);
        byte[] keystoreBytes = Base64.getDecoder().decode(keystoreBase64);

        Path tempDir = Files.createTempDirectory("kafka-certs");
        log.info("Created temporary directory for certificates: {}", tempDir);

        File truststoreFile = tempDir.resolve("client.truststore.jks").toFile();
        File keystoreFile = tempDir.resolve("client.keystore.p12").toFile();

        try (FileOutputStream fos = new FileOutputStream(truststoreFile)) {
            fos.write(truststoreBytes);
        }
        try (FileOutputStream fos = new FileOutputStream(keystoreFile)) {
            fos.write(keystoreBytes);
        }

        log.info(" Kafka SSL certificates loaded successfully");
        log.info("   Truststore: {}", truststoreFile.getAbsolutePath());
        log.info("   Keystore: {}", keystoreFile.getAbsolutePath());

        return new KafkaCertificates(
                truststoreFile.getAbsolutePath(),
                keystoreFile.getAbsolutePath()
        );
    }


    public static class KafkaCertificates {
        private final String truststorePath;
        private final String keystorePath;

        public KafkaCertificates(String truststorePath, String keystorePath) {
            this.truststorePath = truststorePath;
            this.keystorePath = keystorePath;
        }

        public String getTruststorePath() {
            return truststorePath;
        }

        public String getKeystorePath() {
            return keystorePath;
        }
    }
}