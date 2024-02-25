package io.kontakt.apps.anomaly.detector;

import io.kontak.apps.event.Anomaly;
import io.kontak.apps.event.TemperatureReading;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;

import java.time.Duration;

import static io.kontakt.apps.anomaly.detector.TemperatureReadingTestCreator.avgTemp20Degree;
import static io.kontakt.apps.anomaly.detector.TemperatureReadingTestCreator.ofTemperature;

public class TemperatureMeasurementsListenerTest extends AbstractIntegrationTest {

    @Value("${spring.cloud.stream.bindings.anomalyDetectorProcessor-in-0.destination}")
    private String inputTopic;

    @Value("${spring.cloud.stream.bindings.anomalyDetectorProcessor-out-0.destination}")
    private String outputTopic;

    @Test
    void testInOutFlow() {
        try (TestKafkaConsumer<Anomaly> consumer = new TestKafkaConsumer<>(
                kafkaContainer.getBootstrapServers(),
                outputTopic,
                Anomaly.class
        );
             TestKafkaProducer<TemperatureReading> producer = new TestKafkaProducer<>(
                     kafkaContainer.getBootstrapServers(),
                     inputTopic
             )) {
            TemperatureReading temperatureReading = avgTemp20Degree();
            producer.produce(temperatureReading.thermometerId(), temperatureReading);
            TemperatureReading temperatureReadingAnomaly = ofTemperature(30d);
            producer.produce(temperatureReadingAnomaly.thermometerId(), temperatureReadingAnomaly);
            consumer.drain(
                    consumerRecords -> consumerRecords.stream().anyMatch(r -> r.value().thermometerId().equals(temperatureReading.thermometerId())),
                    Duration.ofSeconds(5)
            );
        }
    }
}
