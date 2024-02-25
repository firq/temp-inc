package io.kontakt.apps.temerature.analytics.api;

import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

@SpringBootTest(classes = TemperatureAnalyticsApplication.class)
@Testcontainers
@AutoConfigureMockMvc
public class AbstractIntegrationTest {

    public final static KafkaContainer kafkaContainer;
    public final static PostgreSQLContainer<?> postgres;

    static {
        kafkaContainer = new KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:7.4.0"));
        kafkaContainer.start();
        Runtime.getRuntime().addShutdownHook(new Thread(kafkaContainer::stop));
        postgres = new PostgreSQLContainer<>(DockerImageName.parse("postgres:12.7"));
        postgres.start();
        Runtime.getRuntime().addShutdownHook(new Thread(postgres::stop));
    }

    @DynamicPropertySource
    static void datasourceConfig(DynamicPropertyRegistry registry) {
        registry.add("spring.cloud.stream.binders.kafka.environment.spring.cloud.stream.kafka.streams.binder.brokers", kafkaContainer::getBootstrapServers);
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

}
