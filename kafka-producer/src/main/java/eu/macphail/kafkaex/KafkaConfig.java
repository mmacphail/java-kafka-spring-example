package eu.macphail.kafkaex;

import com.fasterxml.jackson.databind.ObjectMapper;
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

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaConfig {

    @Autowired
    ObjectMapper objectMapper;

    @Value(value = "${kafka.bootstrapAddress}")
    private String bootstrapAddress;

    @Bean
    public ProducerFactory<String, FileInfo> fileInfoProducerFactory() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);

        JsonSerializer<FileInfo> ser = new JsonSerializer<>(objectMapper);
        return new DefaultKafkaProducerFactory<>(configProps, new StringSerializer(), ser);
    }

    @Bean
    public KafkaTemplate<String, FileInfo> fileInfoKafkaTemplate() {
        return new KafkaTemplate<>(fileInfoProducerFactory());
    }




}
