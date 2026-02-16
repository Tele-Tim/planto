package planto_project.configuration;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;
import planto_project.dto.events.OrderCreatedEvents;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaProducerConfigCustom {

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    @Value("${spring.kafka.ssl.trust-store-password}")
    private String truststorePassword;

    @Value("${spring.kafka.ssl.key-store-password}")
    private String keystorePassword;

    @Autowired(required = false)
    private KafkaSslConfig.KafkaCertificates kafkaCertificates;

    @Bean
    public ProducerFactory<String, OrderCreatedEvents> producerFactory() {
        Map<String, Object> config = new HashMap<>();

        config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);

               if (kafkaCertificates != null) {
            config.put("security.protocol", "SSL");
            config.put("ssl.truststore.location", kafkaCertificates.getTruststorePath());
            config.put("ssl.truststore.password", truststorePassword);
            config.put("ssl.keystore.location", kafkaCertificates.getKeystorePath());
            config.put("ssl.keystore.password", keystorePassword);
            config.put("ssl.keystore.type", "PKCS12");
        }

        return new DefaultKafkaProducerFactory<>(config);
    }

    @Bean
    public KafkaTemplate<String, OrderCreatedEvents> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }
}