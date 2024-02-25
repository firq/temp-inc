package io.kontakt.apps.temerature.analytics.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.kontak.apps.event.Anomaly;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class AnomalyAnalyticsFullFlowIntegrationTest extends AbstractIntegrationTest {

    @Autowired
    MockMvc mvc;
    @Autowired
    ObjectMapper objectMapper;
    @Value("${spring.cloud.stream.bindings.anomalyDetectedProcessor-in-0.destination}")
    private String topic;

    @Test
    void anomaliesByThermometer() throws Exception {
        // given: 2 anomalies from single thermometer
        String thermometer1Id = UUID.randomUUID().toString();
        String roomId = UUID.randomUUID().toString();
        Anomaly firstAnomaly = new Anomaly(20, roomId, thermometer1Id, Instant.now());
        Anomaly secondAnomaly = new Anomaly(20, roomId, thermometer1Id, Instant.now());
        // and: one more from different one
        Anomaly anomalyOnDifferentThermometer = new Anomaly(20, roomId, UUID.randomUUID().toString(), Instant.now());
        // then: anomalies are produced on kafka topic
        produceAnomalies(firstAnomaly, secondAnomaly, anomalyOnDifferentThermometer);
        // when: endpoint is queried for anomalies from first thermometer
        String responseString = mvc.perform(MockMvcRequestBuilders.get("/public/v1/anomalies/thermometer/" + thermometer1Id).accept(MediaType.APPLICATION_JSON))
                // then: application responds with ok status and list of anomalies
                .andExpect(status().isOk()).andReturn()
                .getResponse().getContentAsString();
        List<Anomaly> returnedAnomalies = Stream.of(objectMapper.readValue(responseString, Anomaly[].class)).toList();

        // 2 anomalies are returned
        assertEquals(2, returnedAnomalies.size());
        // both from first thermometer
        assertTrue(returnedAnomalies.stream().map(Anomaly::thermometerId).allMatch(thermometer1Id::equals));
    }

    @Test
    void anomaliesByRoom() throws Exception {
        // given: 2 anomalies from single room
        String thermometer1Id = UUID.randomUUID().toString();
        String roomId = UUID.randomUUID().toString();
        Anomaly firstAnomaly = new Anomaly(20, roomId, thermometer1Id, Instant.now());
        Anomaly secondAnomaly = new Anomaly(20, roomId, thermometer1Id, Instant.now());
        // and: one more from different one
        Anomaly anomalyFromDifferentRoom = new Anomaly(20, UUID.randomUUID().toString(), thermometer1Id, Instant.now());
        // then: anomalies are produced on kafka topic
        produceAnomalies(firstAnomaly, secondAnomaly, anomalyFromDifferentRoom);
        // when: endpoint is queried for anomalies from first room
        String responseString = mvc.perform(MockMvcRequestBuilders.get("/public/v1/anomalies/room/" + roomId).accept(MediaType.APPLICATION_JSON))
                // then: application responds with ok status and list of anomalies
                .andExpect(status().isOk()).andReturn()
                .getResponse().getContentAsString();
        List<Anomaly> returnedAnomalies = Stream.of(objectMapper.readValue(responseString, Anomaly[].class)).toList();

        // 2 anomalies are returned
        assertEquals(2, returnedAnomalies.size());
        // both from first room
        assertTrue(returnedAnomalies.stream().map(Anomaly::roomId).allMatch(roomId::equals));
    }

    @Test
    void thermometersWithAnomaliesAboveThreshold() throws Exception {
        // given: 3 anomalies from single thermometer
        String room1 = UUID.randomUUID().toString();
        String thermometer1 = UUID.randomUUID().toString();
        Anomaly firstAnomaly = new Anomaly(20, room1, thermometer1, Instant.now());
        Anomaly secondAnomaly = new Anomaly(20, room1, thermometer1, Instant.now());
        Anomaly thirddAnomaly = new Anomaly(20, room1, thermometer1, Instant.now());
        // and: two more from different one
        String room2 = UUID.randomUUID().toString();
        String thermometer2 = UUID.randomUUID().toString();
        Anomaly anomalyOnDifferentThermometer = new Anomaly(20, room2, thermometer2, Instant.now());
        Anomaly secondAnomalyOnDifferentThermometer = new Anomaly(20, room2, thermometer2, Instant.now());
        // then: anomalies are produced on kafka topic
        produceAnomalies(firstAnomaly, secondAnomaly, thirddAnomaly, anomalyOnDifferentThermometer, secondAnomalyOnDifferentThermometer);
        // when: endpoint is queried for anomalies from first room
        String responseString = mvc.perform(MockMvcRequestBuilders.get("/public/v1/thermometers/anomalies/above-threshold/2").accept(MediaType.APPLICATION_JSON))
                // then: application responds with ok status and list of thermometers with over 2 anomalies
                .andExpect(status().isOk()).andReturn()
                .getResponse().getContentAsString();
        List<String> returnedThermometersIds = Stream.of(objectMapper.readValue(responseString, String[].class)).toList();

        // 1 thermometer id returned
        assertEquals(1, returnedThermometersIds.size());
        // points to thermometer which raised 3 anomalies
        assertEquals(thermometer1, returnedThermometersIds.get(0));
    }

    private void produceAnomalies(Anomaly... anomalies) {
        try (TestKafkaProducer<Anomaly> producer = new TestKafkaProducer<>(
                kafkaContainer.getBootstrapServers(),
                topic)
        ) {
            Arrays.stream(anomalies)
                    .forEach(anomaly -> producer.produce(anomaly.thermometerId(), anomaly));
        }
    }

}