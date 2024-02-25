package io.kontak.apps.temperature.generator;

import io.kontak.apps.event.TemperatureReading;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;
import java.util.Random;
import java.util.UUID;

@Component
public class SimpleTemperatureGenerator implements TemperatureGenerator {

    int counter = 0;

    private final Random random = new Random();
    private final static String CONSTANT_TEMPERATURE_ROOM_WITH_SINGLE_THERMOMETER = "constantTemperatureRoomWithSingleThermometer";
    private final static String CONSTANT_TEMPERATURE_ROOM_WITH_SINGLE_THERMOMETER_ID = UUID.randomUUID().toString();
    private final static String ANOMALY_PRONE_ROOM = "anomalyProneRarelyReportingRoom";
    private final static String ANOMALY_PRONE_ROOM_FIXED_THERMOMETER = UUID.randomUUID().toString();
    private final static String ANOMALY_PRONE_ROOM_RARELY_REPORTING_FAULTY_THERMOMETER = UUID.randomUUID().toString();
    private final static String SIBERIA_ROOM = "siberia";
    private final static String SIBERIA_ROOM_THERMOMETER_ID = UUID.randomUUID().toString();

    @Override
    public List<TemperatureReading> generate() {
        return List.of(generateSingleReading());
    }

    private TemperatureReading generateSingleReading() {
        counter++;
        String room = ANOMALY_PRONE_ROOM;
        String thermometer = ANOMALY_PRONE_ROOM_FIXED_THERMOMETER;
        double temperature = random.nextDouble(19d, 21d);

        if (counter % 2 == 0) {
            room = CONSTANT_TEMPERATURE_ROOM_WITH_SINGLE_THERMOMETER;
            thermometer = CONSTANT_TEMPERATURE_ROOM_WITH_SINGLE_THERMOMETER_ID;
        }
        if (counter % 25 == 0) {
            room = SIBERIA_ROOM;
            thermometer = SIBERIA_ROOM_THERMOMETER_ID;
            temperature = 5;
        } else if (counter % 5 == 0) {
            room = ANOMALY_PRONE_ROOM;
            thermometer = ANOMALY_PRONE_ROOM_RARELY_REPORTING_FAULTY_THERMOMETER;
            temperature = 30;
        }

        return new TemperatureReading(
                temperature,
                room,
                thermometer,
                Instant.now());
    }

}
