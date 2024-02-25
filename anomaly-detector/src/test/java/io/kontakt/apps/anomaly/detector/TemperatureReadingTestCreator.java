package io.kontakt.apps.anomaly.detector;

import io.kontak.apps.event.TemperatureReading;

import java.time.Instant;

public class TemperatureReadingTestCreator {

    public static TemperatureReading avgTemp20Degree() {
        return ofTemperature(20d);
    }

    public static TemperatureReading ofTemperature(double temperature) {
        return ofTemperatureAtTime(temperature, Instant.now());
    }

    public static TemperatureReading ofTemperatureAtTime(double temperature, Instant instant) {
        return new TemperatureReading(temperature, "room", "thermometer", instant);
    }

}
